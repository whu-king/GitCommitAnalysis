import model.fileGraph.FileGraph;
import statistic.CommitSizeAnalyzer;
import dataAccess.DataMergeFromAllCommit;
import model.gitLog.GitCommit;
import statistic.RiskRank;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<GitCommit> gitCommits =  DataMergeFromAllCommit.MergeFrom("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json",
                "C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");
        long before = System.currentTimeMillis();
        FileGraph fg = FileGraph.valueOf(gitCommits);
//        new CommitSizeAnalyzer(gitCommits).relateSizeAndTime();
        new RiskRank(fg).doRank();
//        long after = System.currentTimeMillis();
//        System.out.println("transform gitCommits to fileGraph took time " + (after - before) + "ms");
//        ViewDataPacker viewDataPacker = new ViewDataPacker(fg);
//        viewDataPacker.GetJsonForD3WithRange(10,100);
    }
}
