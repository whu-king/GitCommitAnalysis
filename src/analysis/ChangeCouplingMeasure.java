package analysis;

import model.fileGraph.FileGraph;
import model.fileGraph.Graph;
import model.fileGraph.Node;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/28.
 */
public interface ChangeCouplingMeasure {

    public int getNumberOfNCoupledClass(Date nowDate,Node node, int n,FileGraph graph );
    public int getSumOfNCoupledLinks(Date nowDate, Node node,int n,FileGraph graph);
    public double getTimeExpSumOfNCoupledLinks(Date nowDate, Node node, int n,FileGraph graph);
    public double getTimeLinearSumOfNCoupledLinks(Date nowDate,Node node, int n, FileGraph graph);
}
