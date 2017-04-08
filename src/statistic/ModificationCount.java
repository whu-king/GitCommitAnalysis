package statistic;

import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.FileGraph;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import utils.CollectionUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1.
 */
public class ModificationCount {

    Map<String,Integer> modificationTimesStat = new HashMap<String,Integer>();

    public void whoIsMostModified(List<GitCommit> gitCommits){

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

        for(Map.Entry<String,String> entry : CollectionUtil.sortMapByValue(mapForSort).entrySet() ){
            String name = entry.getKey();
            String times = String.valueOf(entry.getValue());
            System.out.println("FileName : " + name + " --- " + times);
        }
    }

    private void countChangeOnFileFrom(FileChange change){
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

    public static void main(String[] args){
        ModificationCount mc = new ModificationCount();
        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");

        List<GitCommit> gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(), dmfcf.getGitCommitMessage());
        mc.whoIsMostModified(gitCommits);
    }
}
