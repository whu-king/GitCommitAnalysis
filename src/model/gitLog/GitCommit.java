package model.gitLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/1.
 */
public class GitCommit {

    private String commitSHA;
    private String author;
    private String date;
    private String message;
    private GitStat fileDiff;

    public GitStat getFileDiff() {
        return fileDiff;
    }

    public void setFileDiff(GitStat fileDiff) {
        this.fileDiff = fileDiff;
    }

    public String getCommitSHA() {
        return commitSHA;
    }

    public void setCommitSHA(String commitSHA) {
        this.commitSHA = commitSHA;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBugCommit() {

        String regex = ".*(bug|fix|err|fail|leak).*";
        Pattern bug = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }
}
