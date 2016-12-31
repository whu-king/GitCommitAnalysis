package statistic;

import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;
import utils.CollectionUtil;
import utils.FormatConversionUtil;

import java.util.*;

/**
 * Created by Administrator on 2016/12/31.
 */
public class CommitSizeAnalyzer {

    private List<GitCommit> history;
    public CommitSizeAnalyzer(List<GitCommit> history){
        this.history = history;
    }

    public String relateSizeAndTime(){

        HashMap<String,String> timeSize = new HashMap<String, String>();
        for(GitCommit gitCommit : history){
            GitStat gitStat = gitCommit.getFileDiff();
            Date date = FormatConversionUtil.getDateFromString(gitCommit.getDate());
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(date);
            int year = aCalendar.get(Calendar.YEAR);
            int day = aCalendar.get(Calendar.DAY_OF_YEAR);
            String id = year + "," + day;
            int size = 0;
            for(FileChange fileChange : gitStat.getDiffs()){
                size += Integer.valueOf(fileChange.getDeletions()) + Integer.valueOf(fileChange.getInsertions());
            }

            if(timeSize.containsKey(id)){
                int totalSize = size + Integer.valueOf(timeSize.get(id));
                timeSize.put(id,String.valueOf(totalSize));
            }else{
                timeSize.put(id, String.valueOf(size));
            }
        }

        StringBuffer stringBuffer = getJsonString(sortMapByKey(timeSize));
        return stringBuffer.toString();
    }

    private StringBuffer getJsonString(Map<String, String> timeSize) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n");
        Set<Map.Entry<String,String>> entrySet = timeSize.entrySet();
        int count = 0;
        for(Map.Entry<String,String> entry : entrySet){
            count ++;
            String[] words = entry.getKey().split(",");
            String year = words[0];
            String day = words[1];
            String size = entry.getValue();
            stringBuffer.append("{ size: " + size + ", year :" + year + ", day : " + day + "}");
            if(count != entrySet.size()) stringBuffer.append(",\n");
        }
        stringBuffer.append("]");
        System.out.println(stringBuffer.toString());
        return stringBuffer;
    }

    public  Map<String, String> sortMapByKey(Map<String, String> oriMap) {
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Map.Entry<String, String> entry1,
                                           Map.Entry<String, String> entry2) {
                            int value1 = 0, value2 = 0;
                            try {
                                String words[] = entry1.getKey().split(",");
                                String words2[] = entry2.getKey().split(",");
                                int year1 = Integer.valueOf(words[0]);
                                int year2 = Integer.valueOf(words2[0]);
                                int day1 = Integer.valueOf(words[1]);
                                int day2 = Integer.valueOf(words2[1]);
                                if( year1 != year2) return year1 - year2;
                                return day1 - day2;
                            } catch (NumberFormatException e) {
                                value1 = 0;
                                value2 = 0;
                            }
                            return 0;
                        }
                    });
            Iterator<Map.Entry<String, String>> iter = entryList.iterator();
            Map.Entry<String, String> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }
}
