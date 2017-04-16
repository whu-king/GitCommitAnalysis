package statistic;

import actions.DefaultConfigure;
import analysis.SingleCommitAnalysis;
import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.FileGraph;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public class ImpactStat {

    FileGraph fg = null;
    List<GitCommit> gitCommits = null;
    XSSFWorkbook excel = null;

    public ImpactStat(){
        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");
        gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(),
                dmfcf.getGitCommitMessage());
        fg = FileGraph.valueOf(gitCommits);
        try {
            excel = ExcelHelper.creatExcelFrame("D:\\ImpactExcel_v3.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rankImpact() throws Exception {
        int rowNum = 1;
        for(GitCommit gitCommit : gitCommits){

            List<FileChange> changes = gitCommit.getFileDiff().getDiffs();
            String[] files = new String[changes.size()];
            int i = 0;
            for(FileChange change : changes){
                files[i ++] = change.getPath();
            }
            double impact = SingleCommitAnalysis.listCoupledFile(new Date(System.currentTimeMillis()),Arrays.asList(files),fg, DefaultConfigure.DEFAULT_DOWN_THRESHOLD).getImpact();
            writeDataIntoExcel(impact,rowNum,gitCommit);
            rowNum++;
        }

        String targetFile = "D:\\Impact_v7.xlsx";
        File f = new File(targetFile);
        f.createNewFile();
        OutputStream out = new FileOutputStream(f);
//        XSSFWorkbook workbook = new XSSFWorkbook(excel);

        excel.write(out);
        out.close();
        excel.close();

        System.out.println("Write Data -ExcelFile : " + targetFile);



    }

    private void writeDataIntoExcel(double impact, int rowNum,GitCommit gitCommit) throws Exception {

        XSSFWorkbook workbook = excel;

        XSSFSheet inputsheet = workbook.getSheetAt(0);
        XSSFRow row = inputsheet.createRow(rowNum);
        row.createCell(0).setCellValue(impact);
        row.createCell(1).setCellValue(gitCommit.getCommitSHA());
        row.createCell(2).setCellValue(gitCommit.getDate());
        row.createCell(3).setCellValue(gitCommit.getAuthor());
        row.createCell(4).setCellValue(gitCommit.getMessage());
        row.createCell(5).setCellValue(gitCommit.getFileDiff().getDiffs().size());

    }

    public static void main(String[] args) throws Exception {
            new ImpactStat().rankImpact();
    }
}
