package statistic;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ExcelReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/28.
 */
public class ProjectSummary {

    public String dess[] = new String[]{
            "jackson-databind","commons-lang","graphhopper","mondrian","nutch"};
    public String pops[] = new String[]{
            "spring-framework","buck","hadoop","druid","realm-java"
    };

    public String summary4popAuthor = "E:\\老师项目\\GitHub\\Impact统计\\project\\summary\\pop\\authorType\\authorType.xlsx";
    public String summary4desAuthor = "E:\\老师项目\\GitHub\\Impact统计\\project\\summary\\des\\authorType\\authorType.xlsx";
    public String summary4popCommit = "E:\\老师项目\\GitHub\\Impact统计\\project\\summary\\pop\\impact.xlsx";
    public String summary4desCommit = "E:\\老师项目\\GitHub\\Impact统计\\project\\summary\\des\\impact.xlsx";
    public String summary4popCommitLn = "E:\\老师项目\\GitHub\\Impact统计\\project\\summary\\pop\\impactLn.xlsx";
    public String summary4desCommitLn = "E:\\老师项目\\GitHub\\Impact统计\\project\\summary\\des\\impactLn.xlsx";

    public void mergeExcel() throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet target = wb.createSheet("authorType");
        String titles[] = new String[]{"SHA","Log","IsInner","Date","IMP","FNUM","INS","DEL","RISK","EFFORT"};
        ExcelReader.writeLine(target,0,0,titles);
        int count = 1;
        for(String des : dess){
            String authorType = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + des + "\\authorType\\authorType.xlsx";
            Workbook temp = new XSSFWorkbook(new FileInputStream(authorType));
            Sheet sheet = temp.getSheet("authorType");
            int rowNums = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNums; i++){
                String[] cells = ExcelReader.readline(sheet, i);
                ExcelReader.writeLine(target,count,0,cells);
                count ++;
            }
        }
        FileOutputStream fileOut = new FileOutputStream(summary4desAuthor);
        wb.write(fileOut);

        wb = new XSSFWorkbook();
        target = wb.createSheet("authorType");
        ExcelReader.writeLine(target,0,0,titles);
        count = 1;
        for(String pop : pops){
            String authorType = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + pop + "\\authorType\\authorType.xlsx";
            Workbook temp = new XSSFWorkbook(new FileInputStream(authorType));
            Sheet sheet = temp.getSheet("authorType");
            int rowNums = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNums; i++){
                String[] cells = ExcelReader.readline(sheet, i);
                ExcelReader.writeLine(target,count,0,cells);
                count ++;
            }
        }
        fileOut = new FileOutputStream(summary4popAuthor);
        wb.write(fileOut);
    }

    public void mergeImpact() throws IOException {
        Workbook wb = new XSSFWorkbook();
        String impactTitles[]  = new String[]{"Impact","SHA","Author","Date","Log","FileNums","Insertions","Deletions","Risk","Effort"};
        wb = new XSSFWorkbook();
        Sheet target = wb.createSheet("ImpactSheet");
        ExcelReader.writeLine(target,0,0,impactTitles);
        int count = 1;
        for(String pop : pops){
            String authorType = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + pop + "\\impact.xlsx";
            Workbook temp = new XSSFWorkbook(new FileInputStream(authorType));
            Sheet sheet = temp.getSheet("ImpactSheet");
            int rowNums = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNums; i++){
                String[] cells = ExcelReader.readline(sheet, i);
                ExcelReader.writeLine(target,count,0,cells);
                count ++;
            }
        }
        FileOutputStream fileOut = new FileOutputStream(summary4popCommit);
        wb.write(fileOut);

        wb = new XSSFWorkbook();
        target = wb.createSheet("ImpactSheet");
        ExcelReader.writeLine(target,0,0,impactTitles);
        count = 1;
        for(String des : dess){
            String authorType = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + des + "\\impact.xlsx";
            Workbook temp = new XSSFWorkbook(new FileInputStream(authorType));
            Sheet sheet = temp.getSheet("ImpactSheet");
            int rowNums = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNums; i++){
                String[] cells = ExcelReader.readline(sheet, i);
                ExcelReader.writeLine(target,count,0,cells);
                count ++;
            }
        }
        fileOut = new FileOutputStream(summary4desCommit);
        wb.write(fileOut);
    }

    public void mergeImpactLn() throws IOException {
        Workbook wb = new XSSFWorkbook();
        String impactTitles[]  = new String[]{"Impact","SHA","Author","Date","Log","FileNums","Insertions","Deletions","Risk","Effort"};
        wb = new XSSFWorkbook();
        Sheet target = wb.createSheet("ImpactSheet");
        ExcelReader.writeLine(target,0,0,impactTitles);
        int count = 1;
        for(String pop : pops){
            String authorType = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + pop + "\\impact.xlsx";
            Workbook temp = new XSSFWorkbook(new FileInputStream(authorType));
            Sheet sheet = temp.getSheet("ImpactSheet");
            int rowNums = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNums; i++){
                String[] cells = ExcelReader.readline(sheet, i);
                double imp = Double.valueOf(cells[0]);
                double risk = Double.valueOf(cells[8]);
                double effort = Double.valueOf(cells[9]);

                cells[0] = String.valueOf(Math.log(imp + 1));
                cells[8] = String.valueOf(Math.log(risk + 1));
                cells[9] = String.valueOf(Math.log(effort + 1));

                ExcelReader.writeLine(target,count,0,cells);
                count ++;
            }
        }
        FileOutputStream fileOut = new FileOutputStream(summary4popCommitLn);
        wb.write(fileOut);

        wb = new XSSFWorkbook();
        target = wb.createSheet("ImpactSheet");
        ExcelReader.writeLine(target,0,0,impactTitles);
        count = 1;
        for(String des : dess){
            String authorType = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + des + "\\impact.xlsx";
            Workbook temp = new XSSFWorkbook(new FileInputStream(authorType));
            Sheet sheet = temp.getSheet("ImpactSheet");
            int rowNums = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNums; i++){
                String[] cells = ExcelReader.readline(sheet, i);
                double imp = Double.valueOf(cells[0]);
                double risk = Double.valueOf(cells[8]);
                double effort = Double.valueOf(cells[9]);

                cells[0] = String.valueOf(Math.log(imp + 1));
                cells[8] = String.valueOf(Math.log(risk + 1));
                cells[9] = String.valueOf(Math.log(effort + 1));
                ExcelReader.writeLine(target,count,0,cells);
                count ++;
            }
        }
        fileOut = new FileOutputStream(summary4desCommitLn);
        wb.write(fileOut);
    }


    public static void main(String[] args) throws Exception {
        new ProjectSummary().mergeImpactLn();
    }
}
