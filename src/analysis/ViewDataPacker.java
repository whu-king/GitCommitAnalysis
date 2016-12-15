package analysis;

import model.fileGraph.Edge;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ViewDataPacker {

    private FileGraph fg;
    Property2ViewMapper p2v = new BasicProperty2ViewMapper();

    public ViewDataPacker(FileGraph fg){
        this.fg = fg;
    }

    public void GetJsonForD3WithRange( int minWeight,int maxWeight) throws IOException {

        Map<String,Node> nodes = fg.getNodes();
        Map<String,Edge> edges = fg.getEdges();

        Collection<Node> siftedNodes = new LinkedList<Node>();
        Collection<Edge> siftedEdges = new LinkedList<Edge>();
        for(Node node : nodes.values()){
            if(node.getFile().getCommitNum() >= minWeight){
                siftedNodes.add(node);
            }
        }

        for(Edge edge : edges.values()){
            if(edge.getWeight() >= minWeight &&
                    edge.getWeight() <= maxWeight){
                siftedEdges.add(edge);
            }
        }
        System.out.println("SiftedGraph : (" + minWeight +" <= commitNum <= " + maxWeight + ")");
        System.out.println("Node num : " + siftedNodes.size());
        System.out.println("Edge num : " + siftedEdges.size());

        String path = "C:\\Users\\Administrator\\Documents\\GitCommitAnalysis\\web\\GraphJson.json";
        try {
            writeGraphJsonForD3IntoFile(path,siftedNodes,siftedEdges);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void writeGraphJsonForD3IntoFile(String filePath, Collection<Node> nodes, Collection<Edge> edges) throws Exception {

        File f = new File(filePath);
        if(f.exists()) f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

        int count = 0;
        int size = nodes.size();
        bw.write("{ \"nodes\":[ \n");
        for(Node node : nodes){
            count++;
            bw.write("{\"id\" : \"" +node.getFile().getFilePath() +  "\",\"radius\":" + p2v.getRadiusFrom(node) +
                    ", \"color\":" + p2v.getColorValueFrom(node) + "}");
            if(count != size) bw.write(",\n");
        }
        bw.write("], \n \"links\" : [\n");
        count = 0;

        size = edges.size();

        for(Edge edge : edges){
            count++;
            bw.write("{\"source\" : \"" + edge.getFirstNode().getFile().getFilePath() +
                    "\",\"target\": \"" + edge.getSecondNode().getFile().getFilePath() + "\"," +
                    " \"length\" : " + p2v.getLengthFrom(edge) + "}");
            if(count != size) bw.write(",\n");
        }
        bw.write("]}");
        bw.close();
    }
}
