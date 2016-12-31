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
public class DataMerge {

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
            gitStat = checkGitStat(gitStat);
            if(gitStat == null) {
                System.out.println("Missing Stat Information For Commit : " + sha );
            }else {
                gitCommit.setFileDiff(gitStat);
            }
        }

        return gitCommits;
    }

    private static GitStat checkGitStat(GitStat gitStat) {
        List<FileChange> changes = gitStat.getDiffs();
        changes = checkDuplicateCommit(changes);
        changes = checkEmptyOperation(changes);
        gitStat.setDiffs(changes);
        return gitStat;
    }


    private static List<FileChange> checkDuplicateCommit(List<FileChange> diffs) {

        List<FileChange> newDiff = new LinkedList<FileChange>();
        for(FileChange fc : diffs){
            if(newDiff.contains(fc)) continue;
            newDiff.add(fc);
        }
        return newDiff;
    }

    private static List<FileChange> checkEmptyOperation(List<FileChange> diffs) {
        List<FileChange> newDiff = new LinkedList<FileChange>();
        for (FileChange fc : diffs) {
            if (fc.getInsertions().equalsIgnoreCase("0") &&
                    fc.getDeletions().equalsIgnoreCase("0")) continue;
            if (fc.getDeletions().equalsIgnoreCase("-") &&
                    fc.getInsertions().equalsIgnoreCase("-")) continue;
            newDiff.add(fc);
        }
        return newDiff;
    }

    public static GitStat findByIndexFrom(List<GitStat> gitStats, int index){
        return gitStats.get(index);
    }

    public static GitStat findBySHAFrom(List<GitStat> gitStats,String targetSHA){
        for(GitStat gitStat : gitStats){
            String sha = gitStat.getCommitSHA();
            if(targetSHA.equalsIgnoreCase(sha)){

                List<FileChange> changes = gitStat.getDiffs();

//                if(sha.equalsIgnoreCase("c457c3e6d6251035b9541d593782cbdc65cd68bf")){
//                    for(FileChange change : changes){
//                        System.out.println(GsonUtil.getJsonFrom(FileChange.class,change));
//                    }
//                }
                gitStat.setDiffs(changes);
                return gitStat;
            }
        }
        return null;
    }
}
