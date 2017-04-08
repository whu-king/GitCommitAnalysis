package statistic;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/3/7.
 */
public class ExcelHelper {

    public static XSSFWorkbook creatExcelFrame(String filepath) throws Exception{

//        File file = new File(filepath);
//        if(file.exists()) file.delete();
//        file.createNewFile();
        OutputStream out = new FileOutputStream(filepath);
        XSSFWorkbook workbook = new XSSFWorkbook();
        System.out.println("create Excel Frame...");
        XSSFSheet inputsheet = workbook.createSheet("ImpactSheet");
        XSSFRow row = inputsheet.createRow(0);
        row.createCell(0).setCellValue("Impact");
        row.createCell(1).setCellValue("SHA");
        row.createCell(2).setCellValue("Author");
        row.createCell(3).setCellValue("Date");
        row.createCell(4).setCellValue("Log");
        row.createCell(5).setCellValue("FileNums");
        System.out.println("create finished");

        workbook.write(out);
        out.close();

        return workbook;
    }
}
