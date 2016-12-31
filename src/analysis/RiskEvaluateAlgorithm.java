package analysis;

import model.fileGraph.CodeFile;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface RiskEvaluateAlgorithm {

    double getFileRisk(CodeFile cf);


}
