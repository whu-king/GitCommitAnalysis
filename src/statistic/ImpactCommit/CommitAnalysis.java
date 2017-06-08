package statistic.ImpactCommit;

import utils.ExcelReader;
import utils.FileOperation;
import utils.FormatConversionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.LogConfigure;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/3/14.
 */
public class CommitAnalysis {

    public  String projectName = "";
    public  String sourceExcel = "";
    public  String commitTypeExcelPath = "";
    public  String finalExcel = "";
    public  String basicPath = "";
    public String csvDir = "";
    public String timeTrendDir = "";
    public  List<Commit> allCommits = new ArrayList<Commit>();

    private static int RangeGroup = 10;

    private static int selectStep(String sheetName) {
        if (sheetName.equalsIgnoreCase("IMP")) return 1;
        if (sheetName.equalsIgnoreCase("FNUM")) return 1;
        if(sheetName.equalsIgnoreCase("RISK")) return 1;
        if(sheetName.equalsIgnoreCase("EFFORT")) return 1;
        else return 5;
    }

    public static void main(String[] args)  throws Exception{

//        excelDefectAndFeatureData2Csv_orderByTime();
//        Analysis.analysisAuthorType();
//        Analysis.excel2csv();
//        typeDistribution();
//        excelDefectAndFeatureData2Csv_orderByTime();
    }


    public  void setUP(String name){
        projectName = name;
        basicPath = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + projectName;
        sourceExcel = basicPath + "\\Impact.xlsx";
        commitTypeExcelPath = basicPath + "\\commitType\\commitType.xlsx";
        finalExcel = basicPath + "\\commitType\\summary.xlsx";
        csvDir = basicPath + "\\commitType\\heatMap";
        timeTrendDir = basicPath + "\\timeTrend";
        try {
            checkAndCreateDirectory(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAndCreateDirectory(String projectName) throws IOException {
        File commitTypeExcel = new File(commitTypeExcelPath);
        if(!commitTypeExcel.exists()) FileOperation.createFileRecursively(commitTypeExcel);
        File summaryExcel = new File(finalExcel);
        if(!summaryExcel.exists()) FileOperation.createFileRecursively(summaryExcel);
        File csv = new File(csvDir);
        if(!csv.exists()) FileOperation.createFileRecursively(csv);
        File timeTrend = new File(timeTrendDir);
        if(!timeTrend.exists()) FileOperation.createFileRecursively(timeTrend);
    }

    public  void typeDistribution() throws Exception{
        LogConfigure.log.info("type Distribution start...");
        Workbook wb = new XSSFWorkbook(new FileInputStream(sourceExcel));
        OutputStream out = new FileOutputStream(new File(commitTypeExcelPath));
        Sheet originalSheet = wb.getSheet("ImpactSheet");
        int rowNums = originalSheet.getPhysicalNumberOfRows();
        long before = System.currentTimeMillis();

        for (int i = 1; i < rowNums; i++) {
            String[] cells = ExcelReader.readline(originalSheet, i);
            Commit commit = new Commit();
            commit.setImpact(Double.valueOf(cells[0]));
            commit.setMessage(cells[4]);
            commit.setSha(cells[1]);
            commit.setFileNum(Double.valueOf(cells[5]));
            commit.setInsertions(Double.valueOf(cells[6]));
            commit.setDeletions(Double.valueOf(cells[7]));
            commit.setDate(cells[2]);
            commit.setAuthor(cells[3]);
            commit.setRisk(Double.valueOf(cells[8]));
            commit.setEffort(Double.valueOf(cells[9]));
            commit.findType();
            allCommits.add(commit);
        }
        fillTypeSheet(wb, "ratio");
        wb.write(out);
        summaryAll();
//        heatMapData2Csv();
        out.close();
        long after = System.currentTimeMillis();
        LogConfigure.log.info("type distribution finished\n take time" + (after - before) + "ms");

    }

    public void timeTrend4All() throws Exception{

        LogConfigure.log.info("time Trend for all start...");
        Workbook wb = new XSSFWorkbook(new FileInputStream(sourceExcel));

            Sheet sheet = wb.getSheet("impactSheet");
            int rowNum = sheet.getPhysicalNumberOfRows();
            String csv = "Group,IMP,FNUM,INS,DEL,RISK,EFFORT\n";
            String endDate = "";
            int groupCount = 0;
            String dateGroup = "";
            for (int i = 1; i < rowNum; i++) {
                Row row = sheet.getRow(i);
                if (endDate.equalsIgnoreCase("")) {
                    String date = getStringFromCell(row.getCell(2));
                    dateGroup = FormatConversionUtil.getYMD(date);
                    endDate = FormatConversionUtil.getNextDay(date,180);
                } else {
                    String currentDate = getStringFromCell(row.getCell(2));
                    Date end = FormatConversionUtil.getDateFromString(endDate);
                    Date current = FormatConversionUtil.getDateFromString(currentDate);
                    if (current.after(end)) {
                        if(groupCount > 200){
                            groupCount = 0;
                            dateGroup = FormatConversionUtil.getYMD(endDate);
                            endDate = FormatConversionUtil.getNextDay(currentDate,180);
                        }else{
                            endDate = FormatConversionUtil.getNextDay(currentDate,30);
                        }
                    }
                }
                csv += dateGroup + ",";
                String imp = getStringFromCell(row.getCell(0));
                csv += imp + ",";
                for (int k = 0; k < 5; k++) {
                    String value = getStringFromCell(row.getCell( 5 + k));
                    csv += value;
                    if (k != 4) csv += ",";
                    else csv += "\n";
                }
                groupCount ++;
            }
            String filePath = timeTrendDir + "\\" + "all-orderByTime" + ".csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(csv);
            bw.close();
        LogConfigure.log.info("Time Trend finished");

    }

    public void timeTrend4DiffType() throws Exception {

            Workbook wb = new XSSFWorkbook(new FileInputStream(commitTypeExcelPath));
            String sheetNames[] = new String[]{"Defect", "Feature"};
            for (String sheetName : sheetNames) {
                Sheet sheet = wb.getSheet(sheetName);
                int rowNum = sheet.getPhysicalNumberOfRows();
                String csv = "Group,IMP,FNUM,INS,DEL,RISK,EFFORT\n";
                int groupIndex = 0;
                String endDate = "";
                int groupCount = 0;
                String dateGroup = "";
                for (int i = 1; i < rowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (endDate.equalsIgnoreCase("")) {
                        String date = getStringFromCell(row.getCell(9));
                        dateGroup = FormatConversionUtil.getYMD(date);
                        endDate = FormatConversionUtil.getNextDay(date,180);
                    } else {
                        String currentDate = getStringFromCell(row.getCell(9));
                        Date end = FormatConversionUtil.getDateFromString(endDate);
                        Date current = FormatConversionUtil.getDateFromString(currentDate);
                        if (current.after(end)) {
                            if(groupCount > 100){
                                groupIndex++;
                                groupCount = 0;
                                dateGroup = FormatConversionUtil.getYMD(endDate);
                                endDate = FormatConversionUtil.getNextDay(currentDate,180);
                            }else{
                                endDate = FormatConversionUtil.getNextDay(currentDate,30);
                            }
                        }
                    }
                    csv += dateGroup + ",";
                    for (int k = 0; k < 6; k++) {
                        String imp = getStringFromCell(row.getCell(3 + k));
                        csv += imp;
                        if (k != 5) csv += ",";
                        else csv += "\n";
                    }
                    groupCount ++;
                }
                String filePath = timeTrendDir + "\\" + sheetName + "-orderByTime" + ".csv";
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
                bw.write(csv);
                bw.close();
            }

    }

    private void summaryAll() throws Exception {
        Workbook wb = new XSSFWorkbook(new FileInputStream(commitTypeExcelPath));
        OutputStream out = new FileOutputStream(new File(finalExcel));
        String[] sheetNames = new String[]{"IMP", "FNUM", "INSERT", "DELETE","RISK","EFFORT"};
        String[] types = new String[]{"Defect", "Feature", "Cleanup", "Improvement", "Config", "JavaDoc", "Test"};
        FormulaEvaluator fe = new XSSFFormulaEvaluator((XSSFWorkbook) wb);

        int sheetCount = 0;
        for (String sheetName : sheetNames) {
            Sheet sheet = wb.createSheet(sheetName);
            String titles[] = new String[]{"Range", "Defect", "Feature", "Cleanup", "Improvement", "Config", "JavaDoc", "Test"};
            ExcelReader.writeLine(sheet, 0,0, titles);
            int typeCount = 0;
            for (String type : types) {
                Sheet dataSheet = wb.getSheet(type);
//                System.out.println(" ____________" + type);
                for (int i = 1; i < RangeGroup + 1; i++) {
                    Cell cell = dataSheet.getRow(i).getCell(11 + 3 * sheetCount);
                    double value = fe.evaluate(cell).getNumberValue();
                    ExcelReader.writeCells(sheet, i, 1 + typeCount, new double[]{value});
                }
                typeCount++;
            }
            sheetCount++;
        }


        wb.write(out);
        out.close();

    }

    private  void fillTypeSheet(Workbook wb, String numberCared) {

        Collection<Commit> commits = allCommits;
        int fieldNums = 10;

        String[] types = new String[]{"Defect", "Feature", "Cleanup", "Improvement", "Config", "JavaDoc", "Test"};
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            Sheet sheet = wb.createSheet(types[i]);
            int count = 0;
            String titles[] = new String[]{"SHA", "Log", "Type", "IMP", "FileNum", "INSERT", "DELETE","RISK","EFFORT", "Date"};
            ExcelReader.writeLine(sheet, count,0, titles);
            count++;
            for (Commit commit : commits) {
                if (commit.getType().contains(type)) {
                    String[] cells = new String[3];
                    cells[0] = commit.getSha();
                    cells[1] = commit.getMessage();
                    cells[2] = commit.getType();
                    ExcelReader.writeLine(sheet, count, 0, cells);
                    double []doubles = new double[6];
                    doubles[0] = commit.getImpact();
                    doubles[1] = commit.getFileNum();
                    doubles[2] = commit.getInsertions();
                    doubles[3] = commit.getDeletions();
                    doubles[4] = commit.getRisk();
                    doubles[5] = commit.getEffort();
                    ExcelReader.writeCells(sheet, count, 3, 1,doubles);
                    cells = new String[1];
                    cells[0] = commit.getDate();
                    ExcelReader.writeLine(sheet,count,9,cells);
                    count++;
                }
            }
            int compareBase;
            if (numberCared.equalsIgnoreCase("ratio")) compareBase = count;
            else compareBase = 1;


            int impactRange = 0;
            int insertionRange = 0;
            int deletionRange = 0;
            int fileRange = 0;
            int riskRange = 0;
            int effortRange = 0;

            int impactStep = selectStep("IMP");
            int fileStep = (int)selectStep("FNUM");
            int insertionStep = (int)selectStep("INSERT");
            int deletionStep = (int)selectStep("DELETE");
            int riskStep = (int)selectStep("RISK");
            int effortStep = (int)selectStep("EFFORT");

            int maxLine = 15000;
            for (int j = 1; j < RangeGroup; j++) {
                impactRange += impactStep;
                fileRange += fileStep;
                insertionRange += insertionStep;
                deletionRange += deletionStep;
                riskRange += riskStep;
                effortRange += effortStep;
                //减小间距
                int nums[] = new int[]{impactRange, fileRange, deletionRange, insertionRange,riskRange,effortRange};
                String formula1 = "(COUNTIF(D2:D" + maxLine + ",\"< " + impactRange + "\") - COUNTIF(D2:D" + maxLine + ",\"< " + (impactRange - impactStep) + " \"))/" + compareBase;
                String formula2 = "(COUNTIF(E2:E" + maxLine + ",\"< " + fileRange + "\") - COUNTIF(E2:E" + maxLine + ",\"< " + (fileRange - fileStep) + " \"))/" + compareBase;
                String formula3 = "(COUNTIF(F2:F" + maxLine + ",\"< " + insertionRange + "\") - COUNTIF(F2:F" + maxLine + ",\"< " + (insertionRange - insertionStep) + " \"))/" + compareBase;
                String formula4 = "(COUNTIF(G2:G" + maxLine + ",\"< " + deletionRange + "\") - COUNTIF(G2:G" + maxLine + ",\"< " + (deletionRange - deletionStep) + " \"))/" + compareBase;
                String formula5 = "(COUNTIF(H2:H" + maxLine + ",\"< " + riskRange + "\") - COUNTIF(H2:H" + maxLine + ",\"< " + (riskRange - riskStep) + " \"))/" + compareBase;
                String formula6 = "(COUNTIF(I2:I" + maxLine + ",\"< " + effortRange + "\") - COUNTIF(I2:I" + maxLine + ",\"< " + (effortRange - effortStep) + " \"))/" + compareBase;

                ExcelReader.writeFormulaIntoCells(sheet, j, fieldNums + 1, new String[]{formula1, formula2, formula3, formula4,formula5,formula6});
                ExcelReader.writeCells(sheet, j, fieldNums, nums);
                if (j == RangeGroup -1) {
                    formula1 = "(COUNTIF(D2:D" + maxLine + ",\"> " + impactRange + "\"))/" + compareBase;
                    formula2 = "(COUNTIF(E2:E" + maxLine + ",\"> " + fileRange + "\"))/" + compareBase;
                    formula3 = "(COUNTIF(F2:F" + maxLine + ",\"> " + insertionRange + "\"))/" + compareBase;
                    formula4 = "(COUNTIF(G2:G" + maxLine + ",\"> " + deletionRange + "\"))/" + compareBase;
                    formula5 = "(COUNTIF(H2:H" + maxLine + ",\"> " + riskRange + "\"))/" + compareBase;
                    formula6 = "(COUNTIF(I2:I" + maxLine + ",\"> " + effortRange + "\"))/" + compareBase;
                    ExcelReader.writeFormulaIntoCells(sheet, j + 1, fieldNums + 1, new String[]{formula1, formula2, formula3, formula4,formula5,formula6});
                    ExcelReader.writeCells(sheet, j + 1, fieldNums, nums);
                }
            }
        }
    }

    private  void heatMapData2Csv() throws Exception {
        Workbook wb = new XSSFWorkbook(new FileInputStream(finalExcel));
        String[] sheetNames = new String[]{"IMP", "FNUM", "INSERT", "DELETE","RISK","EFFORT"};
        for (String sheetName : sheetNames) {
            Sheet sheet = wb.getSheet(sheetName);
            int rowNum = sheet.getPhysicalNumberOfRows();
            String csv = "";
            int step = selectStep(sheetName);
            for (int i = 0; i < rowNum; i++) {
                if (i != 0 && i != RangeGroup) {
                    csv = csv + step * i + ",";
                } else if (i == 0) {
                    csv = csv + "Range,";
                } else if (i == RangeGroup) {
                    csv = csv + step * (i-1) + "+,";
                }


                Row row = sheet.getRow(i);
                for (int k = 1; k < 8; k++) {
                    csv = csv + getStringFromCell(row.getCell(k));
                    if (k == 7) csv += "\n";
                    else csv += ",";
                }
            }
            String filepath = csvDir + "\\" + sheetName + ".csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            bw.write(csv);
            bw.close();
        }
    }


    private  void excelDefectAndFeatureData2Csv_orderBySeq() throws Exception {
        Workbook wb = new XSSFWorkbook(new FileInputStream(commitTypeExcelPath));
        String sheetNames[] = new String[]{"Defect", "Feature"};
        int groupGap = 200;
        for (String sheetName : sheetNames) {
            Sheet sheet = wb.getSheet(sheetName);
            int rowNum = sheet.getPhysicalNumberOfRows();
            if (rowNum < 1000) groupGap = 100;
            int groupNum = rowNum / groupGap;
            String csv = "Group,IMP,FNUM,INS,DEL\n";
            for (int i = 0; i < groupNum; i++) {
                for (int index = groupGap * i; index < groupGap * (i + 1); index++) {
                    if (index == 0) continue;
                    csv += i + ",";
                    Row row = sheet.getRow(index);
                    for (int k = 0; k < 4; k++) {
                        String imp = getStringFromCell(row.getCell(3 + k));
                        csv += imp;
                        if (k != 3) csv += ",";
                        else csv += "\n";
                    }
                }
            }
            String impPath = basicPath + "\\" + sheetName + "-variance" + ".csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(impPath));
            bw.write(csv);
            bw.close();

        }
    }


    public void everySingleType2csv() throws Exception {

        sourceExcel = basicPath + "\\impact(ori).xlsx";
        commitTypeExcelPath = basicPath + "\\commitType\\commitType(ori).xlsx";
        finalExcel = basicPath + "\\commitType\\summary(ori).xlsx";
        typeDistribution();

        Workbook wb = new XSSFWorkbook(new FileInputStream(commitTypeExcelPath));
        String[] types = new String[]{"Defect", "Feature", "Cleanup", "Improvement", "Config", "JavaDoc", "Test"};
        String title = "";
        String titles[] = new String[]{"SHA", "Log", "Type", "IMP", "FileNum", "INSERT", "DELETE","RISK","EFFORT", "Date"};

        for(int m = 0; m < titles.length; m++){
            title += titles[m];
            if(m != titles.length - 1) title += ",";
            else title += "\n";
        }

        for (int i = 0; i < types.length; i++) {
            StringBuffer sb = new StringBuffer();
            String type = types[i];
            Sheet sheet = wb.getSheet(types[i]);
            int count = 0;
            sb.append(title);
            int rows = sheet.getPhysicalNumberOfRows();
            for(int j = 12 ; j < rows; j++){
                String cells[] = ExcelReader.readline(sheet,j);
                String delimeter;
                for(int k = 0; k < cells.length; k++){
                    if(k == cells.length - 1) delimeter = "\n";
                    else delimeter = ",";
                    sb.append(cells[k] + delimeter);
                }
            }
            String path = basicPath + "\\commitType\\" + type + ".csv";
            FileOperation.createFileRecursively(new File(path));
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(sb.toString());
            bw.close();
    }

    }

    public void groupDataMix2csv()throws Exception{
        commitTypeExcelPath = basicPath + "\\commitType\\commitType.xlsx";
        Workbook wb = new XSSFWorkbook(new FileInputStream(commitTypeExcelPath));
        String[] types = new String[]{"Defect", "Feature", "Cleanup", "Improvement", "Config", "JavaDoc", "Test"};
        String title = "";
        String titles[] = new String[]{"Type", "IMP", "RISK","EFFORT"};

        for(int m = 0; m < titles.length; m++){
            title += titles[m];
            if(m != titles.length - 1) title += ",";
            else title += "\n";
        }
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < types.length; i++) {

            String type = types[i];
            Sheet sheet = wb.getSheet(types[i]);
            int count = 0;
            sb.append(title);
            int rows = sheet.getPhysicalNumberOfRows();
            for(int j = 12 ; j < rows; j++){

                String cells[] = ExcelReader.readline(sheet,j);
                String delimeter;
                cells = new String[]{cells[3],cells[7],cells[8]};
                sb.append(type + ",");
                for(int k = 0; k < cells.length; k++){
                    if(k == cells.length - 1) delimeter = "\n";
                    else delimeter = ",";
                    sb.append(cells[k] + delimeter);
                }
            }
        }
        String path = basicPath + "\\commitType\\groupMix.csv";
        FileOperation.createFileRecursively(new File(path));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(sb.toString());
        bw.close();
    }


    private static String getStringFromCell(Cell cell) {
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            FormulaEvaluator fe = new XSSFFormulaEvaluator((XSSFWorkbook) cell.getSheet().getWorkbook());
            double value = fe.evaluate(cell).getNumberValue();
            return String.valueOf(value);
        }
        return cell.getStringCellValue();
    }



}
