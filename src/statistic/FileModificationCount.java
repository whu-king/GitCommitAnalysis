package statistic;

import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.FileGraph;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import utils.CollectionUtil;
import utils.LogConfigure;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/1.
 */
public class FileModificationCount {

     Map<String,Integer> modificationTimesStat = new HashMap<String,Integer>();

    public  List<String> whoIsMostModified(List<GitCommit> gitCommits){

        LogConfigure.log.info("Who is most modified...");
        for(GitCommit gitCommit :  gitCommits){
            List<FileChange> changes = gitCommit.getFileDiff().getDiffs();
            for(FileChange change : changes){
                countChangeOnFileFrom(change);
            }
        }

        Map<String,String> mapForSort = new LinkedHashMap<String, String>();
        for(Map.Entry<String,Integer> entry : modificationTimesStat.entrySet() ){
            String name = entry.getKey();
            String times = String.valueOf(entry.getValue());
            mapForSort.put(name,times);
        }

        List<String> filenames = new ArrayList<String>();
        int count = 0;
        String regex = ".*\\.java.*";
        Pattern bug = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        for(Map.Entry<String,String> entry : CollectionUtil.sortMapByValue(mapForSort).entrySet() ){
            String name = entry.getKey();
            Matcher m = bug.matcher(name);
            if(!m.find()) continue;
            String times = String.valueOf(entry.getValue());
            LogConfigure.log.info("FileName : " + name + " --- " + times);
            filenames.add(name);
            count++;
            if(count == 10) break;
        }
        return filenames;
    }

    private  void countChangeOnFileFrom(FileChange change){
        if(change.getDeletions().equalsIgnoreCase("0") &&
                change.getInsertions().equalsIgnoreCase("0")) return;
        String name = change.getPath();
        if(modificationTimesStat.get(name) == null){
            modificationTimesStat.put(name,1);
        }else{
            int oldTimes = modificationTimesStat.get(name);
            oldTimes ++;
            modificationTimesStat.put(name,oldTimes);
        }
    }

}
