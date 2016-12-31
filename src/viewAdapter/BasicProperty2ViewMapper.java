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
        if(node.getFile().getCommitNum() > 240 ) return 60;
        return (node.getFile().getCommitNum() / 4) + 1;
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
        return 50 - edge.getWeight()/3;
    }
}
