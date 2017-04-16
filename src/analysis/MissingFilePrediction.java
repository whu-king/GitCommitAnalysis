package analysis;

import model.fileGraph.CodeFile;
import model.fileGraph.Node;
import model.gitLog.GitCommit;
import utils.FormatConversionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */
public class MissingFilePrediction {

    public static double  getProbality(Date date,Node conditionNode, Node predictedNode ){
        double p = 0.0;
        CodeFile conditionCF = conditionNode.getFile();
        List<GitCommit> baseCommits = new ArrayList<GitCommit>();
        List<GitCommit> reviewCommits = new ArrayList<GitCommit>();
        List<GitCommit> gitCommits = conditionCF.getChangeLog();
        for(GitCommit gitCommit : gitCommits){
            Date commitDate = FormatConversionUtil.getDateFromString(gitCommit.getDate());
            if(commitDate.after(date)) continue;
            baseCommits.add(gitCommit);
        }
        gitCommits = predictedNode.getFile().getChangeLog();
        for(GitCommit gitCommit : gitCommits){
            Date commitDate = FormatConversionUtil.getDateFromString(gitCommit.getDate());
            if(commitDate.after(date)) continue;
            reviewCommits.add(gitCommit);
        }
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
