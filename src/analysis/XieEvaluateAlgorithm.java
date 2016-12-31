package analysis;

import model.fileGraph.CodeFile;
import utils.FormatConversionUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class XieEvaluateAlgorithm implements RiskEvaluateAlgorithm {

    public double getFileRisk(CodeFile codeFile){
        Date nowDate = new Date(System.currentTimeMillis());
        List<Integer> changeExtents = codeFile.getChangeExtentsInBugFix();
        List<Date> updateDates = codeFile.getUpdateDateOfBug();
        double risk = 0;
        for(Date updateDate : updateDates ){
            int internalDays = FormatConversionUtil.getInternalDaysOfTwo(updateDate,nowDate);
            int commitChange = changeExtents.get(updateDates.indexOf(updateDate));
            if(internalDays < 0) continue;
            risk = risk +  commitChange * 120.0 / (internalDays + 1);
        }
        return risk ;
    }
}
