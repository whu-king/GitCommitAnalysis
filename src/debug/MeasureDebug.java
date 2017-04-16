package debug;

import actions.DefaultConfigure;
import analysis.SingleCommitAnalysis;
import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import utils.FormatConversionUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/3.
 */
public class MeasureDebug {

    static FileGraph fg;
    static List<GitCommit> gitCommits;
    static String statPath = "C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json";
    static String commitPath = "C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json";

    static{
//        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
//        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
//        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
//        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");
//        gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(), dmfcf.getGitCommitMessage());
        gitCommits = DataMergeFromAllCommit.MergeFrom(statPath,commitPath);
        fg = FileGraph.valueOf(gitCommits);
    }

    public static void main(String[] args){
//        cleanup();
//        defect();
//        feature();
//        improvement();
        zero();
    }

    public static void getCommitRelatedGraph(String sha){
        String fileSets[] = new String[10000];
        Date date = null;
        for(GitCommit gitCommit : gitCommits){
            if(gitCommit.getCommitSHA().equalsIgnoreCase(sha)){
                List<FileChange> diffs = gitCommit.getFileDiff().getDiffs();
                int size  = diffs.size();
                fileSets = new String[size];
                for(int i = 0 ; i < size; i++){
                    fileSets[i] = diffs.get(i).getPath();
                }
                date = FormatConversionUtil.getDateFromString(gitCommit.getDate());
                break;
            }
        }
        FileGraph siftedFg = SingleCommitAnalysis.listCoupledFile(date,Arrays.asList(fileSets), fg, DefaultConfigure.DEFAULT_DOWN_THRESHOLD);
    }

    public static void zero(){
        getCommitRelatedGraph("bab9af2b52b8f4880e4c3dc316751865cf77c2bb");
    }

    public static void cleanup(){
        getCommitRelatedGraph("b5d715343a24be61be13a1546908ff9dbd3a743c");
    }

    public static void defect(){
        getCommitRelatedGraph("6b819fb993f382381ab11fb2768ce405332e08ce");
    }

    public static void feature(){
        getCommitRelatedGraph("92cbe6f98041ce435d91d692510eb6b934a97ee0");
    }

    public static void improvement(){
        getCommitRelatedGraph("50fafdc3d32a67817050f011a39dd3e1bfbaac40");
    }
}
