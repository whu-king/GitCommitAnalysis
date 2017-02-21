package model.fileGraph;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Node {

    private long id;
    private long rank = 0;
    private CodeFile file;

    public Node(){}
    public Node(CodeFile cf){
        this.file = cf;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CodeFile getFile() {
        return file;
    }

    public void setFile(CodeFile file) {
        this.file = file;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }
}
