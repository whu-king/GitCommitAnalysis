package model.fileGraph;

import model.gitLog.GitCommit;
import utils.FormatConversionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class CodeFile {

    private final String filePath;
    //todo when analysis gitStat construct dirs
//    private List<String> dirs;
    private int commitNum;
//    private int rowNum;
    private double risk;
//    private int bugFixInvolvedNum;
//    private int age;
    private List<Date> updateDateOfBug = new ArrayList<Date>();
    private List<Integer> changeExtentsInBugFix = new ArrayList<Integer>();
    private List<String> codeWorkers = new LinkedList<String>();
    private List<GitCommit> changeLog = new ArrayList<GitCommit>();

    public CodeFile(String path){
        this.filePath = path;
    }

    public void addUpdateDate(Date date){
        if(updateDateOfBug != null){
            updateDateOfBug.add(date);
        }
    }

    public void addChangeCommit(GitCommit gitCommit){
        if(changeLog != null){
            changeLog.add(gitCommit);
        }
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o.getClass() == this.getClass()){
            CodeFile cf = (CodeFile) o;
            if(this.filePath.equalsIgnoreCase(cf.filePath)){
                return true;
            }
        }
        return false;
    }

    public int hashCode(){
        return filePath.hashCode();
    }

    public int getBugFixInvolvedNum() {
        return changeExtentsInBugFix.size();
    }

    public String getFilePath() {
        return filePath;
    }

    public List<Date> getUpdateDateOfBug() {
        return updateDateOfBug;
    }

    public void setUpdateDateOfBug(List<Date> updateDateOfBug) {
        this.updateDateOfBug = updateDateOfBug;
    }

    public int getAge() {
        Date nowDate = new Date(System.currentTimeMillis());
        // the commit is order by time Dec
        return FormatConversionUtil.getInternalDaysOfTwo(updateDateOfBug.get(updateDateOfBug.size() - 1),nowDate);
    }

    public int getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(int commitNum) {
        this.commitNum = commitNum;
    }

    public List<String> getCodeWorkers() {
        return codeWorkers;
    }

    public void setCodeWorkers(List<String> codeWorkers) {
        this.codeWorkers = codeWorkers;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public List<Integer> getChangeExtentsInBugFix() {
        return changeExtentsInBugFix;
    }

    public void setChangeExtentsInBugFix(List<Integer> changeExtentsInBugFix) {
        this.changeExtentsInBugFix = changeExtentsInBugFix;
    }

    public void addCommitExtent(int i){
        changeExtentsInBugFix.add(i);
    }

    public List<GitCommit> getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(List<GitCommit> changeLog) {
        this.changeLog = changeLog;
    }
}
