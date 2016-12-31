package analysis;

import model.fileGraph.*;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2016/12/28.
 */
public class BasicChangeCouplingMeasure implements ChangeCouplingMeasure {

    @Override
    public int getNumberOfNCoupledClass(Node node, int n, FileGraph graph) {
        HashMap<String, Integer> coupledClasses = getCoupledClasses(node, n, graph);
        return coupledClasses.values().size();
    }

    @Override
    public int getSumOfNCoupledLinks(Node node, int n, FileGraph graph) {
        HashMap<String, Integer> coupledClasses = getCoupledClasses(node, n, graph);
        int sum = 0;
        for(Integer weight : coupledClasses.values()){
            sum += weight;
        }
        return sum;
    }

    @Override
    public double getTimeExpSumOfNCoupledLinks(Node node, int n, FileGraph graph) {

        HashMap<String, Integer> coupledClasses = getCoupledClasses(node, n, graph);
        List<GitCommit> gitCommitOfTargetClass = node.getFile().getChangeLog();
        List<String> gitCommitSHA = new ArrayList<String>();
        for(GitCommit gitCommit : gitCommitOfTargetClass){
            gitCommitSHA.add(gitCommit.getCommitSHA());
        }
        int size = gitCommitSHA.size();
        double result = 0;

        for(String className : coupledClasses.keySet()){
            Node anotherNode = graph.findNodeByPath(className);
            List<GitCommit> anotherGitCommits = anotherNode.getFile().getChangeLog();
            for(GitCommit gitCommit : anotherGitCommits){
                String sha = gitCommit.getCommitSHA();
                int index = 0;
                if((index = gitCommitSHA.indexOf(sha)) > -1){
                    result += 1/Math.pow(2,size - index);
                }
            }
        }
        return result;
    }

    @Override
    public double getTimeLinearSumOfNCoupledLinks(Node node, int n, FileGraph graph) {
        HashMap<String, Integer> coupledClasses = getCoupledClasses(node, n, graph);
        List<GitCommit> gitCommitOfTargetClass = node.getFile().getChangeLog();
        List<String> gitCommitSHA = new ArrayList<String>();
        for(GitCommit gitCommit : gitCommitOfTargetClass){
            gitCommitSHA.add(gitCommit.getCommitSHA());
        }
        int size = gitCommitSHA.size();
        double result = 0;

        for(String className : coupledClasses.keySet()){
            Node anotherNode = graph.findNodeByPath(className);
            List<GitCommit> anotherGitCommits = anotherNode.getFile().getChangeLog();
            for(GitCommit gitCommit : anotherGitCommits){
                String sha = gitCommit.getCommitSHA();
                int index = 0;
                if((index = gitCommitSHA.indexOf(sha)) > -1){
                    result = result + 1.0/(size + 1 - index);
                }
            }
        }
        return result;
    }

    private HashMap<String, Integer> getCoupledClasses(Node node, int n, FileGraph graph) {
        HashMap<String,Integer> coupledClasses = new HashMap<String, Integer>();
        String nodeName = node.getFile().getFilePath();
        CodeFile codeFile = node.getFile();
        List<GitCommit> gitCommits = codeFile.getChangeLog();
        for(GitCommit gitCommit : gitCommits){
            List<FileChange> fileChanges = gitCommit.getFileDiff().getDiffs();
            for(FileChange fileChange : fileChanges ){
                String className = fileChange.getPath();
                if(coupledClasses.containsKey(className)) continue;
                Edge edge = graph.findEdgeByString(nodeName,className);
                if(edge == null) continue;
                if(edge.getWeight() >= n) coupledClasses.put(className,edge.getWeight());
            }
        }
        return coupledClasses;
    }
}
