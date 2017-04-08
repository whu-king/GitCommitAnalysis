package dataAccess;

import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;
import utils.FileOperation;
import utils.GsonUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class DataMergeFromAllCommit {

    public static List<GitCommit> MergeFrom(String gitStatPath,String gitCommitPath){
        String gitStatJson = FileOperation.readStringFrom(gitStatPath);
        String gitCommitJson = FileOperation.readStringFrom(gitCommitPath);
        List<GitCommit> gitCommits = GsonUtil.jsonToList(gitCommitJson.trim(),  GitCommit[].class);
        List<GitStat> gitStats = GsonUtil.jsonToList(gitStatJson.trim(), GitStat[].class);

        GitCommit BuggitCommit = null;
        //gitStat 和 gitCommit是一一对应的
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
        System.out.println("GitCommit Compose Finished");

        gitCommits = DataCleaner.clean(gitCommits);

        return gitCommits;
    }


    public static GitStat findByIndexFrom(List<GitStat> gitStats, int index){
        return gitStats.get(index);
    }

}
