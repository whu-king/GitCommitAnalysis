package model.fileGraph;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class CodeFile {

    private final String filePath;
    private List<Date> updateDate = new LinkedList<Date>();
    private int age;
    private int commitNum;
    private int rowNum;
    private int bugFixInvolvedNum;

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
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
}
