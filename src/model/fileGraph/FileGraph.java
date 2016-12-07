package model.fileGraph;

import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import utils.FormatConversionUtil;
import utils.GsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */
public class FileGraph extends Graph {

    public static FileGraph valueOf(List<GitCommit> gitCommits){
        FileGraph g = new FileGraph();

        for(GitCommit gitCommit : gitCommits){
            String dateString = gitCommit.getDate();
            Date date = FormatConversionUtil.getDateFromString(dateString);
            List<Node> appearTogether = new ArrayList<Node>();

            for(FileChange change : gitCommit.getFileDiff().getDiffs()){
                Node node = newOrUpdateNode(g, date, change);
                if (node == null) continue;
                appearTogether.add(node);
                g.addNode(node);
            }

            //the codeFile is listed By ABC oder, or else edge weight is wrong
            for(int i = 0; i < appearTogether.size(); i++){
                for(int j = i+1; j < appearTogether.size(); j++){
                    Edge tEdge = new Edge();
                    tEdge.setFirstNode(appearTogether.get(i));
                    tEdge.setSecondNode(appearTogether.get(j));
                    Edge edge = g.findEdgeByEdge(tEdge);
                    if(edge == null){
                        tEdge.setWeight(1);
                        edge = tEdge;
                    }else{
                        edge.setWeight(edge.getWeight()+1);
                    }
                    g.addEdge(edge);
                }
            }
        }
        return g;
    }

    private static Node newOrUpdateNode(FileGraph g, Date date, FileChange change) {
        if(change.getInsertions().equalsIgnoreCase("0") &&
                change.getDeletions().equalsIgnoreCase("0")) return null;
        String path = change.getPath();
        Node node = g.findNodeByPath(path);
        if(node != null){
            CodeFile codeFile = node.getFile();
            codeFile.addUpdateDate(date);
            codeFile.setCommitNum(codeFile.getCommitNum()+1);
        }else{
            node = new Node();
            CodeFile codeFile = new CodeFile(path);
            codeFile.setCommitNum(1);
            codeFile.addUpdateDate(date);
            node.setFile(codeFile);
        }
        return node;
    }


}
