package model.fileGraph;

import utils.GsonUtil;

import java.util.*;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Graph {

    protected long id;
    //use hashcode of Edge firstFile and secondFile to identify Edge;
    protected Map<String,Edge> edges;
    // use hashcode of file path to identify node
    protected Map<String,Node> nodes;

    public Graph(){
        id = -1;
        edges = new HashMap<String,Edge>();
        nodes = new HashMap<String,Node>();
    }

    public void print(){

        System.out.println("Node : " + "\n");
        for(Map.Entry<String,Node> entry : nodes.entrySet()){
            Node n = entry.getValue();
            System.out.println(GsonUtil.getJsonFrom(Node.class, n) + "\n");
        }
        for(Map.Entry<String,Edge> entry : edges.entrySet()){
            Edge e = entry.getValue();
            System.out.println(GsonUtil.getJsonFrom(Edge.class, e) + "\n");
        }
    }

    public void addNode(Node n){
        nodes.put(n.getFile().getFilePath(), n);
    }

    public void addEdge(Edge e){
        String path2 = e.getFirstNode().getFile().getFilePath() + e.getSecondNode().getFile().getFilePath();
        edges.put(path2,e);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Edge> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, Edge> edges) {
        this.edges = edges;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Node> nodes) {
        this.nodes = nodes;
    }
}
