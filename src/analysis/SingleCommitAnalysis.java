package analysis;

import actions.DefaultConfigure;
import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.CodeFile;
import model.fileGraph.Edge;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import org.apache.commons.collections.map.HashedMap;
import utils.FormatConversionUtil;

import java.util.*;

/**
 * Created by Administrator on 2017/1/1.
 */
public class SingleCommitAnalysis {

    public static FileGraph listCoupledFile(GitCommit gitCommit, FileGraph fg, int minThreshold) {
        List<FileChange> changes = gitCommit.getFileDiff().getDiffs();
        String[] files = new String[changes.size()];
        int k = 0;
        for (FileChange change : changes) {
            files[k++] = change.getPath();
        }
        Date nowDate = FormatConversionUtil.getDateFromString(gitCommit.getDate());
        return listCoupledFile(nowDate, Arrays.asList(files), fg, minThreshold);
    }

    //算法主要实现
    public static FileGraph listCoupledFile(Date nowDate, List<String> commitFiles, FileGraph fg, int minThreshold) {

        //MINTHRESHOLD 没有用，实际上只有有一次耦合就会就算在内

        //耦合出现的概率
        Map<String, Double> probalityMatrix = new HashedMap();
        //获取第一层耦合的文件，并将这些文件和相关的边放入graph中
        FileGraph certainGraph = getCoupledGraph(nowDate, commitFiles, fg, minThreshold, probalityMatrix);
        //certainGraph中包含了第一层耦合的文件和commit本身的文件以及相关的边
        //现在从中提取出第一层耦合的文件，方便区分显示
        List<String> firstCoupledFiles = new ArrayList<String>();
        for (String nodeName : certainGraph.getNodes().keySet()) {
            if (commitFiles.contains(nodeName)) continue;
            firstCoupledFiles.add(nodeName);
        }

        //第二层耦合文件
        List<String> secondCoupledFiles = new ArrayList<String>();
        if(firstCoupledFiles.size() != 0){
            //获取第二层耦合文件，这个时候图中包含第一层耦合文件和第二层耦合文件以及相关的边
            FileGraph secondRelatedGraph = getCoupledGraph(nowDate, firstCoupledFiles, fg, minThreshold, probalityMatrix);
            for (Node node : secondRelatedGraph.getNodes().values()) {
                String nodeName = node.getFile().getFilePath();
                if (certainGraph.findNodeByPath(nodeName) != null) continue;
                //将第二层耦合的文件加入到之前的certainGraph中
                certainGraph.addNode(node);
                secondCoupledFiles.add(nodeName);
            }

            //将第二层耦合文件与第一层耦合文件的边加入到certainGraph中
            for (Map.Entry<String, Edge> entry : secondRelatedGraph.getEdges().entrySet()) {
                if (certainGraph.getEdges().containsKey(entry.getKey())) continue;
                certainGraph.addEdge(entry.getValue());
            }
        }
        //certainGraph构造完成

        int commitSize = commitFiles.size();
        //将当前Commit中本身File结点之间的边加入图中
        for (int i = 0; i < commitSize; i++) {
            Node firstNode = fg.findNodeByPath(commitFiles.get(i));
            if (firstNode == null) continue;
            for (int j = i + 1; j < commitSize; j++) {
                Node secondNode = fg.findNodeByPath(commitFiles.get(j));
                if (secondNode == null) continue;
                //若没有边，给定一个很弱的边，避免前面图形显示的时候结点分离太远
                certainGraph.addEdge(new Edge(firstNode, secondNode, 1));
            }
        }

        //计算这个图的IMP
        double impact = 0;

        for (String commitFileName : commitFiles) {
            Node node = fg.findNodeByPath(commitFileName);
            if (node == null) continue;
            //Commit中自身文件的IMP直接加起来
            impact += getImpactForSingleNode(nowDate, node, fg);
            for (String firstCoupledFileName : firstCoupledFiles) {

                Node firstCoupledNode = fg.findNodeByPath(firstCoupledFileName);
                double p = 0;
                if (probalityMatrix.get(commitFileName + firstCoupledFileName) != null) {
                    //CommitFile与当前第一层耦合文件的耦合概率
                    p = probalityMatrix.get(commitFileName + firstCoupledFileName);
                    // 将第一层耦合文件的IMP按照概率加权
                    impact += getImpactForSingleNode(nowDate, firstCoupledNode, fg) * p;

                    for (String secondCoupledFileName : secondCoupledFiles) {
                        Node secondCoupledNode = fg.findNodeByPath(secondCoupledFileName);
                        double p2 = 0;
                        if (probalityMatrix.get(firstCoupledFileName + secondCoupledFileName) != null) {
                            //第一层耦合文件与第二层耦合文件的耦合概率
                            p2 = probalityMatrix.get(firstCoupledFileName + secondCoupledFileName);
                            //将第二层耦合文件的IMP按照p，p2加权
                            impact += getImpactForSingleNode(nowDate, secondCoupledNode, fg) * p * p2;
                        }
                    }

                }
            }
        }

        System.out.println("Impact : " + impact);
        certainGraph.setImpact(impact);

        //区分第一层文件和第二层文件
        for (Node node : certainGraph.getNodes().values()) {
            String filepath = node.getFile().getFilePath();
            if (commitFiles.contains(filepath)) node.setRank(1);
            else if (firstCoupledFiles.contains(filepath)) node.setRank(2);
        }
        return certainGraph;
    }

    private static FileGraph getCoupledGraph(Date nowDate, List<String> files, FileGraph fg, int minThreshold, Map<String, Double> probality) {

        FileGraph certainGraph = new FileGraph();
        //遍历当前的基础文件
        for (String name : files) {
            Node node = fg.findNodeByPath(name);
            if (node == null) continue;
            String nodeName = node.getFile().getFilePath();
            CodeFile codeFile = node.getFile();
            //获取commit提交历史
            List<GitCommit> gitCommits = codeFile.getChangeLog();

            for (GitCommit gitCommit : gitCommits) {
                Date commitDate = FormatConversionUtil.getDateFromString(gitCommit.getDate());
                //只截取时间是nowDate之前的commit历史信息，其他忽略
                if (commitDate.after(nowDate)) continue;
                List<FileChange> fileChanges = gitCommit.getFileDiff().getDiffs();
                for (FileChange fileChange : fileChanges) {
                    //当前FIle的Commit历史里出现过的文件名
                    String className = fileChange.getPath();
                    Edge edge = fg.findEdgeByString(nodeName, className);
                    if (edge == null) continue;

                    Node anode;
                    //该文件是否已经加入到certainGraph或者是否是当前要分析的Commit中的文件
                    if ((anode = certainGraph.findNodeByPath(className)) != null) continue;
                    if (files.contains(className)) continue;
                    else {
                        anode = fg.findNodeByPath(className);
                    }
                    certainGraph.addEdge(edge);
                    certainGraph.addNode(anode);
                    //计算耦合概率
                    probality.put(node.getFile().getFilePath() + anode.getFile().getFilePath(),
                            MissingFilePrediction.getProbality(nowDate,node, anode));


                }
            }
            certainGraph.addNode(node);
        }

        return certainGraph;
    }

    private static boolean siftNodeByRisk(Node anode) {
        RiskEvaluateAlgorithm rea = new XieEvaluateAlgorithm();
        CodeFile targetCF = anode.getFile();
//        if(targetCF.getCommitNum() < DefaultConfigure.DEFAULT_DOWN_THRESHOLD) return false;
//        if(rea.getFileRisk(targetCF) == 0) return false;
        return true;
    }

    public static double getImpactForSingleNode(Date date, Node node, FileGraph fg) {
        ChangeCouplingMeasure changeCouplingMeasure = new BasicChangeCouplingMeasure();

        //这部分单个文件与系统的耦合度则是参考之前的文章 On the Relationship Between Change Coupling
        double timeExp = changeCouplingMeasure.getTimeLinearSumOfNCoupledLinks(date, node, DefaultConfigure.DEFAULT_DOWN_THRESHOLD, fg);
        return timeExp;
    }

}
