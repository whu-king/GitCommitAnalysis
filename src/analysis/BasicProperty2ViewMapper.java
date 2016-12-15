package analysis;

import model.fileGraph.Edge;
import model.fileGraph.Node;

/**
 * Created by Administrator on 2016/12/15.
 */
public class BasicProperty2ViewMapper implements Property2ViewMapper {

    EvaluateAlgorithm evaluateAlgorithm = new XieEvaluateAlgorithm();

    @Override
    public double getRadiusFrom(Node node) {
        //todo more dynamic factors instead of solid 3
        if(node.getFile().getCommitNum() > 240 ) return 60;
        return (node.getFile().getCommitNum() / 4) + 1;
    }

    @Override
    public double getColorValueFrom(Node node) {
        double risk = evaluateAlgorithm.getFileRisk(node.getFile());
        if( -50 < risk && risk < 0) return 5;
        if(-100 < risk && risk < -50) return 4;
        if(-200 < risk && risk < -100) return 3;
        if(-400 < risk && risk < -200) return 2;
        else return 1;
    }

    @Override
    public double getLengthFrom(Edge edge) {
        //todo more dynamic
        return 50 - edge.getWeight()/3;
    }
}
