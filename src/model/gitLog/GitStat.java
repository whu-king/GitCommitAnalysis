package model.gitLog;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class GitStat {
    private String commitSHA;
    private List<FileChange> diffs;


    public String getCommitSHA() {
        return commitSHA;
    }

    public void setCommitSHA(String commitSHA) {
        this.commitSHA = commitSHA;
    }

    public List<FileChange> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<FileChange> diffs) {
        this.diffs = diffs;
    }
}
