package model.fileGraph;

import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import utils.FormatConversionUtil;
import utils.GsonUtil;

import java.util.*;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Graph {

    protected long id;
    //use hashcode of Edge firstFile and secondFile to identify Edge;
    protected Map<Integer,Edge> edges;
    // use hashcode of file path to identify node
    protected Map<Integer,Node> nodes;

    public Graph(){
        id = -1;
        edges = new HashMap<Integer,Edge>();
        nodes = new HashMap<Integer,Node>();
    }


    public Node findNodeByPath(String path){
        if(this.nodes.containsKey(path.hashCode())){
            return this.nodes.get(path.hashCode());
        };
        return null;
    }

    public Edge findEdgeByEdge(Edge edge){
        int hashcode = edge.hashCode();
        if(this.edges.containsKey(hashcode)){
            return this.edges.get(hashcode);
        }
        return null;
    }

    public void print(){

        System.out.println("Node : " + "\n");
        for(Map.Entry<Integer,Node> entry : nodes.entrySet()){
            Node n = entry.getValue();
            System.out.println(GsonUtil.getJsonForm(Node.class, n) + "\n");
        }
        for(Map.Entry<Integer,Edge> entry : edges.entrySet()){
            Edge e = entry.getValue();
            System.out.println(GsonUtil.getJsonForm(Edge.class,e) + "\n");
        }
    }

    public void addNode(Node n){
        nodes.put(n.getFile().getFilePath().hashCode(), n);
    }

    public void addEdge(Edge e){
        int hashcode = e.hashCode();
        edges.put(hashcode,e);
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
