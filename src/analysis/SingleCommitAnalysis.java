package analysis;

import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.CodeFile;
import model.fileGraph.Edge;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/1.
 */
public class SingleCommitAnalysis {

    public static FileGraph listCoupledFile(List<String> commitFiles, FileGraph fg, int minThreshold) {


        Map<String, Double> probalityMatrix = new HashedMap();
        FileGraph certainGraph = getCoupledGraph(commitFiles, fg, minThreshold, probalityMatrix);
        List<String> firstCoupledFiles = new ArrayList<String>();
        for (String nodeName : certainGraph.getNodes().keySet()) {
            if (commitFiles.contains(nodeName)) continue;
            firstCoupledFiles.add(nodeName);
        }

        List<String> secondCoupledFiles = new ArrayList<String>();
        FileGraph secondRelatedGraph = getCoupledGraph(firstCoupledFiles, fg, minThreshold, probalityMatrix);
        for (Node node : secondRelatedGraph.getNodes().values()) {
            String nodeName = node.getFile().getFilePath();
            if (certainGraph.findNodeByPath(nodeName) != null) continue;
            certainGraph.addNode(node);
            secondCoupledFiles.add(nodeName);
        }

        for (Map.Entry<String, Edge> entry : secondRelatedGraph.getEdges().entrySet()) {
            if (certainGraph.getEdges().containsKey(entry.getKey())) continue;
            certainGraph.addEdge(entry.getValue());
        }

        int commitSize = commitFiles.size();
        for (int i = 0; i < commitSize; i++) {
            Node firstNode = fg.findNodeByPath(commitFiles.get(i));
            for (int j = i + 1; j < commitSize; j++) {
                Node secondNode = fg.findNodeByPath(commitFiles.get(j));
                certainGraph.addEdge(new Edge(firstNode, secondNode, 100));
            }
        }

        double impact = 0;

        for (String commitFileName : commitFiles) {
            Node node = fg.findNodeByPath(commitFileName);
            impact += getImpactForSingleNode(node, fg);
            for (String firstCoupledFileName : firstCoupledFiles) {

                Node firstCoupledNode = fg.findNodeByPath(firstCoupledFileName);
                double p = 0;
                if (probalityMatrix.get(commitFileName + firstCoupledFileName) != null) {
                    p = probalityMatrix.get(commitFileName + firstCoupledFileName);
                    impact += getImpactForSingleNode(firstCoupledNode, fg) * p;

                    for (String secondCoupledFileName : secondCoupledFiles) {
                        Node secondCoupledNode = fg.findNodeByPath(secondCoupledFileName);
                        double p2 = 0;
                        if (probalityMatrix.get(firstCoupledFileName + secondCoupledFileName) != null) {
                            p = probalityMatrix.get(firstCoupledFileName + secondCoupledFileName);
                            impact += getImpactForSingleNode(secondCoupledNode, fg) * p * p2;
                        }
                    }

                }
            }
        }

        System.out.println("Impact : " + impact);

        for(Node node : certainGraph.getNodes().values()){
            String filepath = node.getFile().getFilePath();
            if(commitFiles.contains(filepath)) node.setRank(1);
            else if(firstCoupledFiles.contains(filepath)) node.setRank(2);
        }
        return certainGraph;
    }

    private static FileGraph getCoupledGraph(List<String> files, FileGraph fg, int minThreshold,Map<String,Double> probality) {

        FileGraph certainGraph = new FileGraph();
        for(String name : files){
            Node node = fg.findNodeByPath(name);
            String nodeName = node.getFile().getFilePath();
            CodeFile codeFile = node.getFile();
            List<GitCommit> gitCommits = codeFile.getChangeLog();

            for(GitCommit gitCommit : gitCommits){
                List<FileChange> fileChanges = gitCommit.getFileDiff().getDiffs();
                for(FileChange fileChange : fileChanges ){
                    String className = fileChange.getPath();
                    Edge edge = fg.findEdgeByString(nodeName,className);
                    if(edge == null) continue;
                    if(edge.getWeight() >= minThreshold) {
                        Node anode;
                        if((anode = certainGraph.findNodeByPath(className)) != null) continue;
                        else{
                            anode = fg.findNodeByPath(className);
                        }

                        if(siftFirstNodeByRisk(anode)) {
                            certainGraph.addEdge(edge);
                            certainGraph.addNode(anode);
                            probality.put(node.getFile().getFilePath()+anode.getFile().getFilePath(),
                                    MissingFilePrediction.getProbality(node,anode));
                        }
                    }
                }
            }
            certainGraph.addNode(node);
        }

        return certainGraph;
    }

    private static boolean siftFirstNodeByRisk( Node anode) {
        RiskEvaluateAlgorithm rea = new XieEvaluateAlgorithm();
        CodeFile targetCF = anode.getFile();
        if(targetCF.getCommitNum() < 10) return false;
        if(rea.getFileRisk(targetCF) == 0) return false;
        return true;
    }

    public static double getImpactForSingleNode(Node node, FileGraph fg){
        ChangeCouplingMeasure changeCouplingMeasure = new BasicChangeCouplingMeasure();
        double timeExp = changeCouplingMeasure.getTimeExpSumOfNCoupledLinks(node,5,fg);
        return timeExp;
    }

    public static void main(String[] args){
        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");
        List<GitCommit> gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(),
                dmfcf.getGitCommitMessage());
        FileGraph fg = dmfcf.build(gitCommits);
        List<String> testFiles = new ArrayList<String>();
        testFiles.add("all/pom.xml");
        FileGraph siftedFg = SingleCommitAnalysis.listCoupledFile(testFiles, fg, 5);
        System.out.println("done");
    }
}
