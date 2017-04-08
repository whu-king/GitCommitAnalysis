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
    private double impact;

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

        String regex = ".*((bug|fix|err|fail|leak)|#[0-9]*).*";
        Pattern bug = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o.getClass() == this.getClass()){
            GitCommit gitCommit = (GitCommit)o;
            if(this.commitSHA.equalsIgnoreCase(gitCommit.commitSHA)){
                return true;
            }
        }

        return false;
    }

    public int hashCode(){
        return commitSHA.hashCode();
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }
}
