import viewAdapter.ViewDataPacker;
import dataAccess.DataMerge;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;
import statistic.RiskRank;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<GitCommit> gitCommits =  DataMerge.MergeFrom("C:\\Users\\Administrator\\documents\\netty\\netty\\nettyStat2.json",
                "C:\\Users\\Administrator\\documents\\netty\\netty\\nettyCommitMessage.json");
        long before = System.currentTimeMillis();
        FileGraph fg = FileGraph.valueOf(gitCommits);
        RiskRank.doRank(fg);
        long after = System.currentTimeMillis();
        System.out.println("transform gitCommits to fileGraph took time " + (after - before) + "ms");
        ViewDataPacker viewDataPacker = new ViewDataPacker(fg);
        viewDataPacker.GetJsonForD3WithRange(10,100);
    }
}
