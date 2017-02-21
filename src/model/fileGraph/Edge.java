package model.fileGraph;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Edge {

    private long id;
    private Node firstNode;
    private Node secondNode;
    private int weight;

    public Edge(){}
    public Edge(Node node1, Node node2, int weight){
        this.firstNode = node1;
        this.secondNode = node2;
        this.weight = weight;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o.getClass() == this.getClass()){
            Edge e = (Edge)o;
            if(this.firstNode.equals(e.firstNode) &&
                    this.secondNode.equals(e.secondNode)){
                return true;
            }
        }

        return false;
    }

    public int hashCode(){
        return firstNode.hashCode() + secondNode.hashCode();
    }


}
