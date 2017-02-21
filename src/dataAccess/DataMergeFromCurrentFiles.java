package dataAccess;

import model.fileGraph.CodeFile;
import model.fileGraph.Edge;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;
import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;
import java.util.*;
import utils.FileOperation;
import utils.FormatConversionUtil;
import utils.GsonUtil;

import java.util.*;

/**
 * Created by Administrator on 2017/1/1.
 */
public class DataMergeFromCurrentFiles {

    private String projectDir;
    private String gitStatPath;
    private String gitCommitMessage;

    private FileGraph constructNode(List<String> allFiles) {
        FileGraph fg = new FileGraph();
        for (String fileName : allFiles) {
            Node node = new Node();
            CodeFile codeFile = new CodeFile(fileName.replaceAll("\\\\","/"));
            node.setFile(codeFile);
            fg.addNode(node);
        }
        return fg;
    }

    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public String getGitStatPath() {
        return gitStatPath;
    }

    public void setGitStatPath(String gitStatPath) {
        this.gitStatPath = gitStatPath;
    }

    public String getGitCommitMessage() {
        return gitCommitMessage;
    }

    public void setGitCommitMessage(String gitCommitMessage) {
        this.gitCommitMessage = gitCommitMessage;
    }

    public static void main(String[] args) {
        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");
        List<GitCommit> gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.gitStatPath,dmfcf.gitCommitMessage);
        FileGraph fg = dmfcf.build(gitCommits);
        System.out.println("done");
//        List<String> allFiles = FileOperation.getAllFileNameIn(dmfcf.getProjectDir(), "");
//        System.out.println(allFiles);
//        System.out.println(allFiles.size());
    }



    public FileGraph build(List<GitCommit> gitCommits){


        List<String> allFiles = FileOperation.getAllFileNameIn(projectDir, "");
//        System.out.println(allFiles);
//        System.out.println("FileSize : " + allFiles.size());

        FileGraph g = constructNode(allFiles);
        int count = 0;
        gitCommits = DataCleaner.clean(gitCommits);
        for (GitCommit gitCommit : gitCommits) {
            String dateString = gitCommit.getDate();
            Date date = FormatConversionUtil.getDateFromString(dateString);
            List<Node> appearTogether = new ArrayList<Node>();
            List<FileChange> diffs = gitCommit.getFileDiff().getDiffs();

            boolean isBugCommit = gitCommit.isBugCommit();
            for (FileChange change : diffs) {

                Node node = g.findNodeByPath(change.getPath().trim());
                if(node == null){
//                    System.out.println("Died File : " + change.getPath());
                    continue;
                }
                CodeFile codeFile = node.getFile();
                if (isBugCommit) {
                    codeFile.addCommitExtent(Integer.valueOf(change.getDeletions())
                            + Integer.valueOf(change.getInsertions()));
                    codeFile.addUpdateDate(date);
                }
                codeFile.addChangeCommit(gitCommit);
                appearTogether.add(node);
            }

            //the codeFile is listed By ABC oder, or else edge weight is wrong. no situation like A-B:2 B-A:3
            for (int i = 0; i < appearTogether.size(); i++) {
                for (int j = i + 1; j < appearTogether.size(); j++) {
                    Edge tEdge = new Edge();
                    tEdge.setFirstNode(appearTogether.get(i));
                    tEdge.setSecondNode(appearTogether.get(j));
                    Edge edge = g.findEdgeByEdge(tEdge);
                    if (edge == null) {
                        tEdge.setWeight(1);
                        edge = tEdge;
                    } else {
                        edge.setWeight(edge.getWeight() + 1);
                    }
                    g.addEdge(edge);
                }
            }
        }

        for(Node node : g.getNodes().values()){
            CodeFile codeFile = node.getFile();
            codeFile.setCommitNum(codeFile.getChangeLog().size());
        }

        return g;
    }


}
