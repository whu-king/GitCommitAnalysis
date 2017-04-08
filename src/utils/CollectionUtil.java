package utils;

import model.fileGraph.Edge;
import model.fileGraph.Node;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/1.
 */
public class CollectionUtil {

    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Map.Entry<String, String> entry1,
                                           Map.Entry<String, String> entry2) {
                            int value1 = 0, value2 = 0;
                            try {
                                value1 = getInt(entry1.getValue());
                                value2 = getInt(entry2.getValue());
                            } catch (NumberFormatException e) {
                                value1 = 0;
                                value2 = 0;
                            }
                            return value2 - value1;
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


    private static int getInt(String str) {
        int i = 0;
        try {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(str);
            if (m.find()) {
                i = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static Map<String, Node> sortMapByValue(Map<String,Node> oriMap,
                                                   Comparator<Map.Entry<String,Node>> comp) {
        Map<String, Node> sortedMap = new LinkedHashMap<String,Node>();
        //todo different comparator leads different order
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, Node>> entryList = new ArrayList<Map.Entry<String, Node>>(oriMap.entrySet());
            Collections.sort(entryList,comp);
            Iterator<Map.Entry<String, Node>> iter = entryList.iterator();
            Map.Entry<String, Node> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }



}
