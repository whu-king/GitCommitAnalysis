package statistic.ImpactAuthor;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import statistic.ImpactCommit.Commit;
import utils.ExcelReader;
import utils.FileOperation;
import utils.FormatConversionUtil;
import utils.LogConfigure;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/5/6.
 */
public class AuthorAnalysis {

    public  String authorTypeExcel = "";
    public String sourceExcel = "";
    public String basicPath = "";
    public static String innerAuthorStatTxt = innerAuthorStatTxt = "E:\\老师项目\\GitHub\\Impact统计\\result\\OrganizationInnerStat.txt";;
    public static List<String> innerAuthorStatLine = new LinkedList<String>(
            Arrays.asList(new String[]{"Project,OrganzationScale,InnerHit,InnerCommit\n"}));
    public Author authors;
    public String resultExcelName = "authorType.xlsx";

    public double innerCommitRatio = 0;

    public void setUP(String projectName) throws Exception {
        LogConfigure.log.info("in project " + projectName);
        innerAuthorStatLine.add(projectName + ",");
        authors = new Author();
        authors.setUP(projectName);
        basicPath = "E:\\老师项目\\GitHub\\Impact统计\\project\\" + projectName;
        sourceExcel = basicPath + "\\Impact.xlsx";

        authorTypeExcel = basicPath + "\\authorType\\" + resultExcelName;
        checkDirAndCreat();
    }

    public void checkDirAndCreat(){
        File authorTypeFIle = new File(authorTypeExcel);
        if(!authorTypeFIle.exists()) FileOperation.createFileRecursively(authorTypeFIle);
    }

    public void excel2csv() throws Exception{
        Workbook wb = new XSSFWorkbook(new FileInputStream(authorTypeExcel));
        Sheet originalSheet = wb.getSheet("authorType");

        int rowNums = originalSheet.getPhysicalNumberOfRows();
        StringBuffer sb = new StringBuffer();
        int innerCount = 0;
        int outerCount = 0;
        int max = 100000;
        StringBuffer in = new StringBuffer();
        StringBuffer out = new StringBuffer();
        String titles[] = ExcelReader.readline(originalSheet, 0);
        sb.append("Group,");

        for(int j = 0; j < titles.length; j++){
            if(j != titles.length - 1) {
                sb.append(titles[j] + ",");
                in.append(titles[j] + ",");
                out.append(titles[j] + ",");
            }
            else {
                sb.append(titles[j] + "\n");
                in.append(titles[j] + "\n");
                out.append(titles[j] + "\n");
            }
        }
        String firstCommit[] = ExcelReader.readline(originalSheet,1);
        Date startDate = FormatConversionUtil.getDateFromString(FormatConversionUtil.getNextDay(firstCommit[3], 365));
        for(int i = 2; i < rowNums; i++){

            String flag = "";
            String[] cells = ExcelReader.readline(originalSheet,i);
            Date date = FormatConversionUtil.getDateFromString(cells[3]);
//            if(date.before(startDate)) continue;
            if(cells[2].equalsIgnoreCase("true")) {
                if(innerCount == max) continue;
                flag = "Inner";
                innerCount ++;

            }
            if(cells[2].equalsIgnoreCase("false")) {
                if(outerCount == max) continue;
                flag = "Outer";
                outerCount ++;
            }
            int length = cells.length;
            sb.append(flag + ",");
            for(int j = 0; j < length; j++){
                if(j != length - 1) {
                    if(flag.equalsIgnoreCase("Inner")) in.append(cells[j] + ",");
                    else out.append(cells[j] + ",");
                    sb.append(cells[j] + ",");
                }
                else {
                    if(flag.equalsIgnoreCase("Inner")) in.append(cells[j] + "\n");
                    else out.append(cells[j] + "\n");
                    sb.append(cells[j] + "\n");
                }
            }
            if(innerCount == max && outerCount == max) break;
        }

        String allPath = basicPath + "\\authorType\\author-all.csv";
        String inPath = basicPath + "\\authorType\\author-in.csv";
        String outPath = basicPath + "\\authorType\\author-out.csv";
        String fileToStore[] = new String[]{allPath,inPath,outPath};
        for(int i = 0; i < fileToStore.length; i++){
            FileOperation.createFileRecursively(new File(fileToStore[i]));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileToStore[i]));
            if(fileToStore[i].contains("author-all")) bw.write(sb.toString());
            else if(fileToStore[i].contains("author-in")) bw.write(in.toString());
            else if(fileToStore[i].contains("author-out")) bw.write(out.toString());
            bw.close();
        }

    }

    public void distribution() throws Exception {
        int group = 10;
        double riskStep = 1;
        double impStep = 0.5;
        double effortStep = 1;
        //0 IMP , 1 RISK, 2 EFFORT
        int inDis[][] = new int[group][3];
        int outDis[][] = new int[group][3];
        Workbook wb = new XSSFWorkbook(new FileInputStream(authorTypeExcel));
        Sheet sheet = wb.getSheet("authorType");
        int innerCount = 0;
        int outCount = 0;
        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            String[] cells = ExcelReader.readline(sheet,i);
            String innerFlag = cells[2];
            double imp = Double.valueOf(cells[4]);
            double risk = Double.valueOf(cells[8]);
            double effort = Double.valueOf(cells[9]);
            if(innerFlag.equalsIgnoreCase("true")){
                innerCount ++;
                inDis[getRangeIndex(imp, impStep, group)][0] ++;
                inDis[getRangeIndex(risk,riskStep,group)][1]++;
                inDis[getRangeIndex(effort,effortStep,group)][2]++;
            }else if(innerFlag.equalsIgnoreCase("false")){
                outCount ++;
                outDis[getRangeIndex(imp,impStep,group)][0] ++;
                outDis[getRangeIndex(risk,riskStep,group)][1]++;
                outDis[getRangeIndex(effort,effortStep,group)][2]++;
            }
        }
        String IMPString = "";
        IMPString += "Range,Inner,Outer\n";
        for(int i = 0; i < group; i++){
            String range;
            if(i != group - 1) range = impStep*(i + 1) + "";
            else range = impStep * (i) + "+";
            IMPString += range + "," + inDis[i][0]*1.0/innerCount + "," + outDis[i][0]*1.0/outCount + "\n" ;
        }

        String RISKString = "";
        RISKString += "Range,Inner,Outer\n";
        int ariskStep = (int)riskStep;
        for(int i = 0; i < group; i++){
            String range;
            if(i != group - 1) range = ariskStep * (i + 1) + "";
            else range = ariskStep * i + "+";
            RISKString += range + "," + inDis[i][1]*1.0/innerCount + "," + outDis[i][1]*1.0/outCount + "\n";
        }

        String EFFORTString = "";
        EFFORTString += "Range,Inner,Outer\n";
        int aeffortStep = (int)effortStep;
        for(int i = 0; i < group; i++){
            String range;
            if(i != group -1 ) range = aeffortStep * ( i + 1) + "";
            else range = aeffortStep * i + "+";
            EFFORTString += range + "," + inDis[i][2] * 1.0 / innerCount + "," + outDis[i][2] * 1.0/outCount + "\n";
        }

        String allPath = basicPath + "\\authorType\\author-IMP.csv";
        String inPath = basicPath + "\\authorType\\author-RISK.csv";
        String outPath = basicPath + "\\authorType\\author-EFFORT.csv";
        String fileToStore[] = new String[]{allPath,inPath,outPath};
        for(int i = 0; i < fileToStore.length; i++){
            FileOperation.createFileRecursively(new File(fileToStore[i]));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileToStore[i]));
            if(fileToStore[i].contains("author-IMP")) bw.write(IMPString);
            else if(fileToStore[i].contains("author-RISK")) bw.write(RISKString);
            else if(fileToStore[i].contains("author-EFFORT")) bw.write(EFFORTString);
            bw.close();
        }

    }

    private int getRangeIndex(double value,double step,int group){
        if(Math.abs(step - 1) < 1) value = Math.log(value + 1);
        int index = (int) Math.floor(value / step);
        if(index > group - 1) index = group - 1;
        return index;
    }

    public  void analysisAuthorType() throws Exception{
        LogConfigure.log.info("Author Type analysis start...");
        //Impact	SHA	Date Author		Log	FileNums	Insertions	Deletions	Risk	Effort
        List<Commit> allCommits = new ArrayList<Commit>();
        Workbook wb = new XSSFWorkbook(new FileInputStream(sourceExcel));
        OutputStream out = new FileOutputStream(new File(authorTypeExcel));
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
            allCommits.add(commit);
        }

        String titles[] = new String[]{
                "SHA","Log","IsInner","Date","IMP","FNUM","INS","DEL","RISK","EFFORT"};
//
//        String csv = "Inner,SHA,Log,IMP,FNUM,INS,DEL,RISK,EFFORT\n";
        Sheet authorType = wb.createSheet("authorType");
        ExcelReader.writeLine(authorType, 0,0,titles);
        int rowIndex = 1;
        int innerCount = 0;
        for(Commit commit : allCommits){
            String[] cells = new String[4];
            cells[0] = commit.getSha();
            cells[1] = commit.getMessage();
            cells[2] = String.valueOf(authors.isInner(commit.getAuthor()));
            if(cells[2].equalsIgnoreCase("true")) innerCount++;
            cells[3] = commit.getDate();
            ExcelReader.writeLine(authorType, rowIndex, 0, cells);
            double[] doubles= new double[6];
            doubles[0] = commit.getImpact();
            doubles[1] = commit.getFileNum();
            doubles[2] = commit.getInsertions();
            doubles[3] = commit.getDeletions();
            doubles[4] = commit.getRisk() ;
            doubles[5] = commit.getEffort();
            ExcelReader.writeCells(authorType, rowIndex,4,1, doubles);
            rowIndex++;
        }

        int organizationMember = authors.getOrganizationMembers();
        int hit = authors.getInnerHit();
        String innerStatLine = organizationMember + "," + hit + "," + innerCount + "\n";
        innerAuthorStatLine.add(innerStatLine);

        wb.write(out);
        out.close();
        excel2csv();
        distribution();
        LogConfigure.log.info("Author Type finished");
    }

    public static void writeOrgInnerStat() throws IOException {
        String res = "";
        for(String s : innerAuthorStatLine){
            res += s;
        }
        FileOperation.createFileRecursively(new File(innerAuthorStatTxt));
        BufferedWriter bw = new BufferedWriter(new FileWriter(innerAuthorStatTxt));
        bw.write(res);
        bw.close();
    }

}
