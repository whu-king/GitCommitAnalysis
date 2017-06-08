package statistic.ImpactCommit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/15.
 */
public class Commit {

    private String sha;
    private String message;
    private String author;
    private String type = "";
    private String date = "";
    private double fileNum;
    private double impact,risk,effort;
    private double insertions;
    private double deletions;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public double getEffort() {
        return effort;
    }

    public void setEffort(double effort) {
        this.effort = effort;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getFileNum() {
        return fileNum;
    }

    public void setFileNum(double fileNum) {
        this.fileNum = fileNum;
    }

    public double getInsertions() {
        return insertions;
    }

    public void setInsertions(double insertions) {
        this.insertions = insertions;
    }

    public double getDeletions() {
        return deletions;
    }

    public void setDeletions(double deletions) {
        this.deletions = deletions;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message.replace("-"," ");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    public boolean isBugCommit() {

        String regex = ".*\\b(bug|fix|error|fail|leak|resolve|miss|correct|ensure|should|#\\d*)(es|ed|s|ly)?\\s*.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public boolean isFeatureCommit(){
        String regex = ".*\\b(support|feature|implement|allow|extend|add)(es|ed|s|ly)?\\b.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);

        String nonRegex = ".*add.*(fix|javadoc|test|example|maven).*";
        Pattern NonBug = Pattern.compile(nonRegex, Pattern.CASE_INSENSITIVE);
        Matcher mm = NonBug.matcher(message);
        if(mm.find()) return false;

        if(m.find()) {
            return true;
        }
        return false;
    }

    public boolean isCleanupCommit(){

        String regex = ".*\\b(remove|unuse|cleanup|deprecat|unnecessary)(es|ed|s|ly)?\\b.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public boolean isImprovement(){
        String regex = ".*\\b(improve|performance|reduce|optimize|memory|buffer|cache)(es|ed|s|ly)?\\b.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        if(isFeatureCommit()) return false;
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public boolean isJavaDoc(){
        String regex = ".*\\b(javadoc|document|license|copyright|txt|readme)(es|ed|s|ly)?\\b.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public boolean isConfig(){
        String regex = ".*\\b(maven|release.*plugin|pom|ant|build|compile)(es|ed|s|ly)?\\b*.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public boolean isTest(){
        String regex = ".*\\b(unittest|test|testcase|testsuite)(es|ed|s|ly)?\\b.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = bug.matcher(message);
        if(m.find()) return true;
        return false;
    }

    public String findType(){
        if(isBugCommit()) type = "Defect";
        else if(isFeatureCommit()) type ="Feature";
        else if(isImprovement()) type = "Improvement";
        else if(isCleanupCommit()) type = "Cleanup";
        else if(isConfig()) type = "Config";
        if(isJavaDoc()) type += " JavaDoc";
        if(isTest()) type += " Test";
        return type;
    }

    public static void main(String[] args){
        Commit c = new Commit();
        c.setMessage("Fixed-ClosedChannelException-in-various-integration-tests");
        System.out.println(c.isBugCommit());
    }

}
