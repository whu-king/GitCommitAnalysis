package debug;

import model.gitLog.GitCommit;

import java.util.List;

/**
 * Created by Administrator on 2016/12/31.
 */
public class GitCommitFinder {

    public static GitCommit findBySha(List<GitCommit> gitCommitList, String sha){
        for(GitCommit gitCommit : gitCommitList){
            if(gitCommit.getCommitSHA().equalsIgnoreCase(sha))
                return gitCommit;
        }
        return null;
    }
}
