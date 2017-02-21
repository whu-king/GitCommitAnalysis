package viewAdapter;

import analysis.RiskEvaluateAlgorithm;
import analysis.XieEvaluateAlgorithm;
import model.fileGraph.Edge;
import model.fileGraph.Node;

/**
 * Created by Administrator on 2016/12/15.
 */
public class BasicProperty2ViewMapper implements Property2ViewMapper {

    RiskEvaluateAlgorithm evaluateAlgorithm = new XieEvaluateAlgorithm();

    @Override
    public double getRadiusFrom(Node node) {
        //todo more dynamic factors instead of solid 3
        if(node.getFile().getCommitNum() > 240 ) return 40;
        return (node.getFile().getCommitNum() / 6) + 3;
    }

    @Override
    public double getColorValueFrom(Node node) {
        double risk = evaluateAlgorithm.getFileRisk(node.getFile());
        if(risk > 1000 ) return 1000;
        else return risk;
    }

    @Override
    public double getLengthFrom(Edge edge) {
        //todo more dynamic
        return 60 - edge.getWeight()/3;
    }

    @Override
    public String getStrokeWidth(Node node) {
        switch ((int)node.getRank()){
            case 0 : return "1.5";
            case 1 : return "10";
            case 2 : return "5";
            default: return "1.5";
        }
    }

    @Override
    public String getStrokeColor(Node node) {
        switch ((int)node.getRank()){
            case 0 : return "ffd";//white
            case 1 : return "000";//black
            case 2 : return "ccff99";//green
            default: return "#FFA500";
        }
    }
}
