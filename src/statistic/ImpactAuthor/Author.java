package statistic.ImpactAuthor;

import utils.FormatConversionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Administrator on 2017/3/14.
 */
public class Author {

    private double impact_sum = 0;
    private String name = "";
    private List<Date> commitDates = new ArrayList<Date>();
    private boolean isInner;
    private boolean isActive;
    private Map<String,Integer> inners = new HashMap<String, Integer>();

    public int getOrganizationMembers(){
        return (int)Math.floor(inners.size()*1.0/2);
    }

    public int getInnerHit(){
        int hit = 0;
        for(int isHit : inners.values()){
            hit += isHit;
        }
        return hit;
    }

    public boolean equals(Object object){
        if(object == this) return true;
        if(object.getClass() == this.getClass()){
            Author newAuthor = (Author)object;
            if(newAuthor.getName().equalsIgnoreCase(this.name)) return true;
        }
        return false;
    }

    public static void main(String args[]) throws Exception {
        Author a = new Author();
        a.setUP("jena");

    }

    public void setUP(String projectName) throws Exception {

        if(projectName.contains("summary")) return;
        inners = new HashMap<String, Integer>();
        String org = project2orgnization(projectName);
        String memberfile = org + "-members.txt";
        BufferedReader bw = new BufferedReader(new FileReader(memberfile));
        String line = "";
        while( (line = bw.readLine()) != null){
            if(!line.equalsIgnoreCase("")){
                inners.put(line,0);
            }
        }
    }

    public static String project2orgnization(String projectName){
        if(projectName.equalsIgnoreCase("atmosphere")) return "atmosphere";
        if(projectName.equalsIgnoreCase("Activiti")) return "Activiti";
        if(projectName.equalsIgnoreCase("Checkstyle")) return "Checkstyle";
        if(projectName.equalsIgnoreCase("deeplearning4j")) return "deeplearning4j";
        if(projectName.equalsIgnoreCase("Hibernate-orm")) return "Hibernate";
        if(projectName.equalsIgnoreCase("Jena")) return "apache";
        if(projectName.equalsIgnoreCase("Guava")) return "google";
        if(projectName.equalsIgnoreCase("netty")) return "netty";
        if(projectName.equalsIgnoreCase("RxJava")) return "ReactiveX";
        if(projectName.equalsIgnoreCase("Maven")) return "apache";
        if(projectName.equalsIgnoreCase("Pinpoint")) return "naver";
        if(projectName.equalsIgnoreCase("realm-java")) return "realm";
        if(projectName.equalsIgnoreCase("hystrix")) return "netflix";
        if(projectName.equalsIgnoreCase("Buck")) return "facebook";
        if(projectName.equalsIgnoreCase("Terasology")) return "MovingBlocks";
        if(projectName.equalsIgnoreCase("Airavata")) return "apache";
        if(projectName.equalsIgnoreCase("Smarthome")) return "eclipse";
        if(projectName.equalsIgnoreCase("BIMServer")) return "opensourceBIM";
        if(projectName.equalsIgnoreCase("cloud-odata-java")) return "SAP";
        if(projectName.equalsIgnoreCase("Mondrian")) return "pentaho";
        if(projectName.equalsIgnoreCase("imglib2")) return "imglib";
        if(projectName.equalsIgnoreCase("mockito")) return "mockito";
        if(projectName.equalsIgnoreCase("Titan")) return "thinkaurelius";
        if(projectName.equalsIgnoreCase("clojure")) return "clojure";
        if(projectName.equalsIgnoreCase("druid")) return "alibaba";
        if(projectName.equalsIgnoreCase("spring-framework")) return "spring-projects";
        if(projectName.equalsIgnoreCase("hadoop")) return "apache";
        if(projectName.equalsIgnoreCase("apex-core")) return "apache";
        if(projectName.equalsIgnoreCase("flink")) return "apache";
        if(projectName.equalsIgnoreCase("commons-compress")) return "apache";
        if(projectName.equalsIgnoreCase("commons-lang")) return "apache";
        if(projectName.equalsIgnoreCase("mahout")) return "apache";
        if(projectName.equalsIgnoreCase("syncany")) return "syncany";
        if(projectName.equalsIgnoreCase("jackson-databind")) return "FasterXML";
        if(projectName.equalsIgnoreCase("graphhopper")) return "graphhopper";
        if(projectName.equalsIgnoreCase("liquibase")) return "liquibase";
        if(projectName.equalsIgnoreCase("nutch")) return "apache";
        return "";
    }

    public int hashCode(){
        return name.hashCode();
    }

    public Author clone(){
        Author newAuthor = new Author();
        newAuthor.setName(this.name);
        newAuthor.setImpact_sum(this.impact_sum);
        List<Date> newList = new ArrayList<Date>();
        newList.addAll(commitDates);
        newAuthor.setCommitDates(newList);
        return newAuthor;
    }

    public boolean isInner(String name){
        String[] names = name.split("<");
        String singleName = names[0].trim();
        if(inners.get(singleName) == null) return false;
        else {
            inners.put(singleName,1);
        }
        return true;
    }

    public void setActive(){
        Date today = new Date(System.currentTimeMillis());
        int effectiveCount = 0;
        for(Date date : commitDates){
            if(FormatConversionUtil.getInternalDaysOfTwo(date,today) < 30 * 6) effectiveCount ++;
        }
        if(effectiveCount >= 10) isActive = true;
        else isActive = false;
    }

    public double getImpact_sum() {
        return impact_sum;
    }

    public void addDate(Date date){
        commitDates.add(date);
    }

    public void setImpact_sum(double impact_sum) {
        this.impact_sum = impact_sum;
    }

    public double getImpact_avg() {
        return impact_sum / commitDates.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Date> getCommitDates() {
        return commitDates;
    }

    public void setCommitDates(List<Date> commitDates) {
        this.commitDates = commitDates;
    }

    public boolean isInner() {
        return isInner;
    }

    public boolean isActive() {
        return isActive;
    }

}
