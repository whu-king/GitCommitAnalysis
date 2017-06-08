package statistic;

import statistic.ImpactAuthor.AuthorAnalysis;
import statistic.ImpactCommit.CommitAnalysis;
import statistic.impactCalculate.ImpactCalculator;
import utils.LogConfigure;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
public class Main {
    public static void main(String[] args)  {

        try{
            File dir = new File("e:\\repo");
//            String finishProjects[] = new String[]{"Activiti","airavata","atmosphere","buck","checkstyle",
//                    "jena","maven","netty","pinpoint","realm-java","deeplearning4j","guava",
//                    "hibernate-orm","RxJava","mondrian","imglib2","cloud-odata-java","BIMserver",
//                    "Terasology","clojure","summary","druid","Hystrix","mockito","smarthome",
//                    "spring-framework","titan","apex-core","hadoop","commons-compress","commons-lang",
//                    "flink","mahout","graphhopper","liquibase","jackson-databind"
//            };
            String finishProjects[] = new String[10];
            List<String> finishs = Arrays.asList(finishProjects);
            File[] projectDirs = dir.listFiles();
            for(File projectDir : projectDirs){
                String projectName = projectDir.getName();
                if(finishs.contains(projectName)) continue;

                LogConfigure.log.info("test my log");
                ImpactCalculator ic =  new ImpactCalculator();
                ic.setUP(projectName);
                ic.getTOPChangeLog();
//                ic.countIMPRISKEFFORT();
//                ic.decay();
//
//                CommitAnalysis ica = new CommitAnalysis();
//                ica.setUP(projectName);
//                ica.typeDistribution();
//                ica.timeTrend4All();

//                AuthorAnalysis aa = new AuthorAnalysis();
//                aa.setUP(projectName);
//                aa.analysisAuthorType();
            }

//           new ProjectSummary().mergeExcel();
//
//            AuthorAnalysis aa = new AuthorAnalysis();
//            aa.setUP("summary\\pop");
//            aa.excel2csv();
//            aa.distribution();
//
//            aa.setUP("summary\\des");
//            aa.excel2csv();
//            aa.distribution();
//
//            CommitAnalysis ca = new CommitAnalysis();
//            ca.setUP("summary\\pop");
////            ca.typeDistribution();
////            ca.everySingleType2csv();
//            ca.groupDataMix2csv();
//
//            ca.setUP("summary\\des");
////            ca.typeDistribution();
////            ca.everySingleType2csv();
//            ca.groupDataMix2csv();

        }catch (Exception e){
            e.printStackTrace();
            LogConfigure.log.error(e.getMessage() + "\n");
            LogConfigure.log.error(ImpactCalculator.error2String(e.getStackTrace()) + "\n");
        }

    }
}
