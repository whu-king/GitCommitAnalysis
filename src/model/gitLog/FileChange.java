package model.gitLog;

/**
 * Created by Administrator on 2016/12/1.
 */
public  class FileChange {

    private String insertions;
    private String deletions;
    private String path;

    public String getInsertions() {
        return insertions;
    }

    public void setInsertions(String insertions) {
        this.insertions = insertions;
    }

    public String getDeletions() {
        return deletions;
    }

    public void setDeletions(String deletions) {
        this.deletions = deletions;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean equals(Object o){
        if(o == this) return true;
        if(o.getClass() == FileChange.class){
            FileChange tfc = (FileChange) o;
            if(tfc.getPath().equalsIgnoreCase(this.path))
                return true;
        }
        return false;
    }


    public int hashCode(){
        return path.hashCode();
    }


}
