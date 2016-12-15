package dataAccess;

import analysis.ModificationCount;
import analysis.ViewDataPacker;
import model.fileGraph.FileGraph;
import model.fileGraph.Graph;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;
import utils.FileOperation;
import utils.GsonUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class DataMerge {

    public static List<GitCommit> MergeFrom(String gitStatPath,String gitCommitPath){
        String gitStatJson = FileOperation.readStringFrom(gitStatPath);
        String gitCommitJson = FileOperation.readStringFrom(gitCommitPath);
        List<GitCommit> gitCommits = GsonUtil.jsonToList(gitCommitJson.trim(),  GitCommit[].class);
        List<GitStat> gitStats = GsonUtil.jsonToList(gitStatJson.trim(), GitStat[].class);

        for(GitCommit gitCommit : gitCommits){
            String sha = gitCommit.getCommitSHA();
//            GitStat gitStat = findBySHAFrom(gitStats,sha);
            int index = gitCommits.indexOf(gitCommit);
            GitStat gitStat = findByIndexFrom(gitStats,index);
            if(gitStat == null) {
                System.out.println("Missing Stat Information For Commit : " + sha );
            }else {
                gitCommit.setFileDiff(gitStat);
            }
        }
        return gitCommits;
    }

    public static GitStat findBySHAFrom(List<GitStat> gitStats,String targetSHA){
        for(GitStat gitStat : gitStats){
            String sha = gitStat.getCommitSHA();
            if(targetSHA.equalsIgnoreCase(sha)) return gitStat;
        }
        return null;
    }

    public static GitStat findByIndexFrom(List<GitStat> gitStats, int index){
        return gitStats.get(index);
    }

}
