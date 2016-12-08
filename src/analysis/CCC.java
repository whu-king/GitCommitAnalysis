package analysis;

import model.fileGraph.Node;

import java.util.*;

/**
 * Created by Administrator on 2016/12/7.
 */
public class CCC {



    public static Map<String, Node> sortMapByValue(Map<String,Node> oriMap,
                                                   Comparator<Map.Entry<String,Node>> comp) {
        Map<String, Node> sortedMap = new LinkedHashMap<String,Node>();
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
