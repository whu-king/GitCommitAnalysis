package model.fileGraph;

import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import utils.FormatConversionUtil;

import java.util.*;

/**
 * Created by Administrator on 2016/12/7.
 */
public class FileGraph extends Graph {

    private double impact = 0;

    public static FileGraph valueOf(List<GitCommit> gitCommits) {
        FileGraph g = new FileGraph();

        for (GitCommit gitCommit : gitCommits) {
            String dateString = gitCommit.getDate();
            Date date = FormatConversionUtil.getDateFromString(dateString);
            List<Node> appearTogether = new ArrayList<Node>();
            List<FileChange> diffs = gitCommit.getFileDiff().getDiffs();
//            diffs = checkEmptyOperation(diffs);
            boolean isBugCommit = gitCommit.isBugCommit();
            for (FileChange change : diffs) {
                Node node = newOrUpdateNode(g, date, change);
                if (node == null) continue;

                CodeFile codeFile = node.getFile();
                if (isBugCommit) {
                    codeFile.addCommitExtent(Integer.valueOf(change.getDeletions())
                            + Integer.valueOf(change.getInsertions()));
                    codeFile.addUpdateDate(date);
                }
                codeFile.addChangeCommit(gitCommit);
                appearTogether.add(node);
                g.addNode(node);

            }

            //the codeFile is listed By ABC oder, or else edge weight is wrong.no situation like A-B:2 B-A:3
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
        return g;
    }

    private static Node newOrUpdateNode(FileGraph g, Date date, FileChange change) {

        String path = change.getPath();
        Node node = g.findNodeByPath(path);
        CodeFile codeFile;
        if (node != null) {
            codeFile = node.getFile();
            codeFile.setCommitNum(codeFile.getCommitNum() + 1);
        } else {
            node = new Node();
            codeFile = new CodeFile(path);
            codeFile.setCommitNum(1);
            node.setFile(codeFile);
        }
        return node;
    }

    public Node findNodeByPath(String path) {
        if (this.nodes.containsKey(path)) {
            return this.nodes.get(path);
        }

        return null;
    }

    public Edge findEdgeByEdge(Edge edge) {
        String path2 = edge.getFirstNode().getFile().getFilePath() + edge.getSecondNode().getFile().getFilePath();
        if (this.edges.containsKey(path2)) {
            return this.edges.get(path2);
        }
        return null;
    }

    public Edge findEdgeByNode(Node n1, Node n2) {
        String path = n1.getFile().getFilePath() + n2.getFile().getFilePath();
        String path2 = n2.getFile().getFilePath() + n1.getFile().getFilePath();
        return findEdgeByString(path, path2);
    }

    public Edge findEdgeByString(String path1, String path2) {
        String key1 = path1 + path2;
        String key2 = path2 + path1;
        if (this.edges.containsKey(key1))
            return this.edges.get(key1);
        if (this.edges.containsKey(key2))
            return this.edges.get(key2);
        return null;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }
}
