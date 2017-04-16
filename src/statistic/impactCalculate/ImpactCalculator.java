package statistic.impactCalculate;


import dataAccess.DataMergeFromAllCommit;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import statistic.ExcelHelper;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/3/14.
 */
public class ImpactCalculator {


    public static int TASK_SIZE = 40;
    public static int THREAD_MAX = 10;

    public static ConcurrentHashMap<String,GitCommit> updatedGCs = new ConcurrentHashMap<String, GitCommit>();
    public static ExecutorService es = Executors.newCachedThreadPool(new HandleThreadFactory());
    public static String excelPath = "E:\\老师项目\\GitHub\\Impact统计\\ImpactData.xlsx";

    public static CountDownLatch c;

    static FileGraph fg = null;
    static List<GitCommit> gitCommits = null;
    static XSSFWorkbook excel = null;

    static {
        String statPath = "C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json";
        String commitPath = "C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json";
        gitCommits = DataMergeFromAllCommit.MergeFrom(statPath, commitPath);
        fg = FileGraph.valueOf(gitCommits);
        try {
            excel = ExcelHelper.creatExcelFrame("D:\\temp.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        int gitNum = gitCommits.size();

//        Thread.setDefaultUncaughtExceptionHandler(new MyThreadFaultHandler());
        int count = gitNum / TASK_SIZE;
        //todo why this is not right
        if(count * TASK_SIZE < gitNum) count = count + 1;
        c = new CountDownLatch(count);

        long before = System.currentTimeMillis();
        for(int i = 1; i < gitNum; i += TASK_SIZE){

            int end = i + TASK_SIZE;
            if(end > gitNum) end = gitNum - 1;
            es.execute(new StatThread(i, end, updatedGCs, gitCommits, c,fg));
        }
        long after = System.currentTimeMillis();
        c.await();

        int i = 1;
        for(GitCommit gitCommit : updatedGCs.values()){
            writeDataIntoExcel(i++,gitCommit);
        }
        System.out.println(after - before);
        String targetFile = "D:\\NettyLineImpact_v2.xlsx";
        File f = new File(targetFile);
        f.createNewFile();
        OutputStream out = new FileOutputStream(f);
        excel.write(out);
        out.close();
        excel.close();

        System.out.println("Write Data -ExcelFile : " + targetFile);
    }

    static class HandleThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new MyThreadFaultHandler());
            return t;
        }
    }

    private static void writeDataIntoExcel(int rowNum, GitCommit gitCommit) throws Exception {

        XSSFWorkbook workbook = excel;

        XSSFSheet inputsheet = workbook.getSheetAt(0);
        XSSFRow row = inputsheet.createRow(rowNum);
        row.createCell(0).setCellValue(gitCommit.getImpact());
        row.createCell(1).setCellValue(gitCommit.getCommitSHA());
        row.createCell(2).setCellValue(gitCommit.getDate());
        row.createCell(3).setCellValue(gitCommit.getAuthor());
        row.createCell(4).setCellValue(gitCommit.getMessage());
        row.createCell(5).setCellValue(gitCommit.getFileDiff().getDiffs().size());

    }
}
