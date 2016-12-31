package analysis;

import model.fileGraph.FileGraph;
import model.fileGraph.Graph;
import model.fileGraph.Node;

/**
 * Created by Administrator on 2016/12/28.
 */
public interface ChangeCouplingMeasure {

    public int getNumberOfNCoupledClass(Node node, int n,FileGraph graph );
    public int getSumOfNCoupledLinks(Node node,int n,FileGraph graph);
    public double getTimeExpSumOfNCoupledLinks(Node node, int n,FileGraph graph);
    public double getTimeLinearSumOfNCoupledLinks(Node node, int n, FileGraph graph);
}
