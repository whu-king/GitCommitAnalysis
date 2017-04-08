package analysis;

import model.fileGraph.CodeFile;
import model.fileGraph.Node;
import model.gitLog.GitCommit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */
public class MissingFilePrediction {

    public static double  getProbality(Node conditionNode, Node predictedNode ){
        double p = 0.0;
        CodeFile conditionCF = conditionNode.getFile();
        List<GitCommit> baseCommits = conditionCF.getChangeLog();
        List<GitCommit> reviewCommits = predictedNode.getFile().getChangeLog();
        List<GitCommit> commonCommits = new ArrayList<GitCommit>();
        for(GitCommit gitCommit : reviewCommits){
            if(baseCommits.contains(gitCommit)) commonCommits.add(gitCommit);
        }
        p = commonCommits.size() * 1.0 / baseCommits.size();
//        System.out.println(conditionNode.getFile().getFilePath() + " ---> " +
//        predictedNode.getFile().getFilePath() + "\n prob: " + p);
        return p;

    }
}
