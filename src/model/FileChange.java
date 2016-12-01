package model;

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
}
