package statistic;

import analysis.RiskEvaluateAlgorithm;
import analysis.XieEvaluateAlgorithm;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;
import utils.CollectionUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/21.
 */
public class RiskRank {

    private FileGraph g;
    public RiskRank(FileGraph g){
        this.g = g;
    }

    public  void doRank(){
        Map<String,String> mapForSort = new LinkedHashMap<String, String>();
        RiskEvaluateAlgorithm ea = new XieEvaluateAlgorithm();

        for(Map.Entry<String,Node> entry : g.getNodes().entrySet() ){
            String name = entry.getKey();
            double realRisk = ea.getFileRisk(entry.getValue().getFile());
            if((realRisk - 0) < 0.000000001) {
                mapForSort.put(name,"0");
                continue;
            }
            mapForSort.put(name,String.valueOf(realRisk));
        }

        for(Map.Entry<String,String> entry : CollectionUtil.sortMapByValue(mapForSort).entrySet() ){
            String name = entry.getKey();
            String risk = String.valueOf(entry.getValue());
            if(risk.equalsIgnoreCase("0")) continue;
            System.out.println("FileName : " + name + " --- " + risk);
        }
    }
}
