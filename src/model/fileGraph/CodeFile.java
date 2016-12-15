package model.fileGraph;

import sun.awt.image.ImageWatched;
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
    private List<Date> updateDate = new ArrayList<Date>();
    private List<Integer> changeExtents = new ArrayList<Integer>();
//    private int age;
    private int commitNum;
    private int rowNum;
    private int bugFixInvolvedNum;
    private List<String> codeWorkers = new LinkedList<String>();
    private double risk;

    public CodeFile(String path){
        this.filePath = path;
    }

    public void addUpdateDate(Date date){
        if(updateDate != null){
            updateDate.add(date);
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
        return bugFixInvolvedNum;
    }

    public void setBugFixInvolvedNum(int bugFixInvolvedNum) {
        this.bugFixInvolvedNum = bugFixInvolvedNum;
    }

    public String getFilePath() {
        return filePath;
    }



    public List<Date> getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(List<Date> updateDate) {
        this.updateDate = updateDate;
    }

    public int getAge() {
        Date nowDate = new Date(System.currentTimeMillis());
        // the commit is order by time Dec
        return FormatConversionUtil.getInternalDaysOfTwo(updateDate.get(updateDate.size() - 1),nowDate);
    }

    public int getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(int commitNum) {
        this.commitNum = commitNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
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

    public List<Integer> getChangeExtents() {
        return changeExtents;
    }

    public void setChangeExtents(List<Integer> changeExtents) {
        this.changeExtents = changeExtents;
    }

    public void addCommitExtent(int i){
        changeExtents.add(i);
    }
}
