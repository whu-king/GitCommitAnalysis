package analysis;

import model.fileGraph.CodeFile;
import utils.FormatConversionUtil;

import java.util.Date;
import java.util.EventListener;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class XieEvaluateAlgorithm implements EvaluateAlgorithm {

    public double getFileRisk(CodeFile codeFile){
        Date nowDate = new Date(System.currentTimeMillis());
        List<Integer> changeExtents = codeFile.getChangeExtents();
        List<Date> updateDates = codeFile.getUpdateDate();
        double risk = 0;
        for(Date updateDate : updateDates ){
            int internalDays = FormatConversionUtil.getInternalDaysOfTwo(updateDate,nowDate);
            int commitChange = changeExtents.get(updateDates.indexOf(updateDate));
            risk = risk + (-1) * internalDays * commitChange;
        }
        return risk/10000;
    }
}
