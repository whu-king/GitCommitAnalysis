package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by Administrator on 2017/3/14.
 */
public class ExcelReader {

    private static final String DELIMITER = "##";

    public static String[] readline(Sheet sheet,int rowNum) {


        Row row = sheet.getRow(rowNum);
        int cellNum = row.getPhysicalNumberOfCells();
        String[] cells = new String[cellNum];
        for(int i = 0; i < cellNum; i++){
            cells[i] = getString(row.getCell(i)).trim();
        }
        return cells;

    }

    public static void writeFormulaIntoCells(Sheet sheet, int rowNum, int cellStart,String[] formulas){
        Row row = sheet.getRow(rowNum);
        if(row == null) row = sheet.createRow(rowNum);
        int cellNum = formulas.length;
        int index = cellStart;
        for(int i = 0; i < cellNum; i++){
            Cell cell = row.getCell(index);
            if(cell == null) cell = row.createCell(index);
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            cell.setCellFormula(formulas[i]);
            index = index + 3;
        }
    }

    public static void writeCells(Sheet sheet, int rowNum, int cellStart,int[] cells){
        Row row = sheet.getRow(rowNum);
        if(row == null) row = sheet.createRow(rowNum);
        int cellNum = cells.length;
        int index = cellStart;
        for(int i = 0; i < cellNum; i++){
            Cell cell = row.getCell(index);
            if(cell == null) cell = row.createCell(index);
            cell.setCellValue(cells[i]);
            index = index + 3;
        }
    }

    public static void writeCells(Sheet sheet, int rowNum, int cellStart,double[] cells){
        Row row = sheet.getRow(rowNum);
        if(row == null) row = sheet.createRow(rowNum);
        int cellNum = cells.length;
        int index = cellStart;
        for(int i = 0; i < cellNum; i++){
            Cell cell = row.getCell(index);
            if(cell == null) cell = row.createCell(index);
            cell.setCellValue(cells[i]);
            index = index + 3;
        }
    }

    public static void writeCells(Sheet sheet, int rowNum, int cellStart, int step, double[] cells){
        Row row = sheet.getRow(rowNum);
        if(row == null) row = sheet.createRow(rowNum);
        int cellNum = cells.length;
        int index = cellStart;
        for(int i = 0; i < cellNum; i++){
            Cell cell = row.getCell(index);
            if(cell == null) cell = row.createCell(index);
            cell.setCellValue(cells[i]);
            index = index + step;
        }
    }

    public static void writeLine(Sheet sheet,int rowNum, int start,String[] cells){
        Row row = sheet.getRow(rowNum);
        if(row == null) row = sheet.createRow(rowNum);
        int cellNum = cells.length;
        for(int i = 0; i < cellNum; i++){
            Cell cell = row.getCell(i + start);
            if(cell == null) cell = row.createCell(i + start);
            cell.setCellValue(cells[i]);
        }
    }

    private static String getString(Cell cell){
        if(cell == null) return "";
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
