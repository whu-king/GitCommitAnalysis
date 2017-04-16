package actions;

import analysis.SingleCommitAnalysis;
import com.opensymphony.xwork2.ActionContext;
import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;
import viewAdapter.ViewDataPacker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/1.
 */
public class getCommitRelatedGraph {

    private String fileSet;

    public String execute(){

        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");
        List<GitCommit> gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(),
                dmfcf.getGitCommitMessage());
        FileGraph fg = dmfcf.build(gitCommits);
        if(fileSet != null) {
            String[] fileSets = fileSet.trim().split(",|;");
            FileGraph siftedFg = SingleCommitAnalysis.listCoupledFile(new Date(System.currentTimeMillis()),Arrays.asList(fileSets), fg, DefaultConfigure.DEFAULT_DOWN_THRESHOLD);
            ViewDataPacker viewDataPacker = new ViewDataPacker(siftedFg);

            //todo put graph into DB
            ActionContext ac = ActionContext.getContext();
            ac.getSession().put("fileGraph",siftedFg);
            try {
                viewDataPacker.GetJsonForD3WithRange(0,1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return "success";
    }

    public String getFileSet() {
        return fileSet;
    }

    public void setFileSet(String fileSet) {
        this.fileSet = fileSet;
    }
}
