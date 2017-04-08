package statistic;

import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.Edge;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/17.
 */
public class CouplingCount {


    public static void whoIsMostCoupling(){

        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");

        List<GitCommit> gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(), dmfcf.getGitCommitMessage());
        FileGraph fg = dmfcf.build(gitCommits);
        Map<String,Edge> sortedMap = fg.sortEdgeByValue();
        for(Map.Entry<String,Edge> edge : sortedMap.entrySet()){
            if(edge.getValue().getWeight() < 10) continue;
            Edge e = edge.getValue();
            System.out.println(e.getFirstNode().getFile().getFilePath() + "   " +
            e.getSecondNode().getFile().getFilePath() + "      " + e.getWeight());
        }
    }

    public static void main(String[] args){
        whoIsMostCoupling();
    }
}
