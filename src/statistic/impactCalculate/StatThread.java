package statistic.impactCalculate;

import actions.DefaultConfigure;
import analysis.SingleCommitAnalysis;
import model.fileGraph.FileGraph;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/3/14.
 */
public class StatThread implements Runnable {

    private int startRowNum = 0;
    private int endRowNum = 0;
    private CountDownLatch c;
    private List<GitCommit> gitCommits;
    private FileGraph fg;
    private ConcurrentHashMap<String,GitCommit> allGCS;

    public StatThread (int start, int end, ConcurrentHashMap<String,GitCommit> allGCS, List<GitCommit> gitCommits, CountDownLatch c, FileGraph fg){
        startRowNum = start;
        endRowNum = end;
        this.allGCS = allGCS;
        this.gitCommits = gitCommits;
        this.c = c;
        this.fg = fg;
    }
    public void run(){

        for(int i = startRowNum; i < endRowNum; i++){
            GitCommit gitCommit = gitCommits.get(i);
            List<FileChange> changes = gitCommit.getFileDiff().getDiffs();
            String[] files = new String[changes.size()];
            int k = 0;
            for(FileChange change : changes){
                files[k ++] = change.getPath();
            }
            double impact = SingleCommitAnalysis.listCoupledFile(Arrays.asList(files), fg, DefaultConfigure.DEFAULT_DOWN_THRESHOLD).getImpact();
            gitCommit.setImpact(impact);
            allGCS.putIfAbsent(gitCommit.getCommitSHA(),gitCommit);
        }
        c.countDown();
    }



}
