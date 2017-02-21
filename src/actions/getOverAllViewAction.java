package actions;

import com.opensymphony.xwork2.ActionContext;
import dataAccess.DataMergeFromAllCommit;
import dataAccess.DataMergeFromCurrentFiles;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;

import viewAdapter.ViewDataPacker;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class getOverAllViewAction {

    private int downThreshold = DefaultConfigure.DEFAULT_DOWN_THRESHOLD ;
    private int upThreshold = DefaultConfigure.DEFAULT_UP_THRESHOLD;

    public String execute(){

        DataMergeFromCurrentFiles dmfcf = new DataMergeFromCurrentFiles();
        dmfcf.setProjectDir("C:\\Users\\Administrator\\Documents\\netty\\netty");
        dmfcf.setGitStatPath("C:\\Users\\Administrator\\documents\\netty\\data\\nettyStat2.json");
        dmfcf.setGitCommitMessage("C:\\Users\\Administrator\\documents\\netty\\data\\nettyCommitMessage.json");

        long before = System.currentTimeMillis();
        List<GitCommit> gitCommits = DataMergeFromAllCommit.MergeFrom(dmfcf.getGitStatPath(),dmfcf.getGitCommitMessage());
        FileGraph fg = dmfcf.build(gitCommits);
        long after = System.currentTimeMillis();
        System.out.println("transform gitCommits to fileGraph took time " + (after - before) + "ms");
        ViewDataPacker viewDataPacker = new ViewDataPacker(fg);

        //todo put graph into DB
        ActionContext ac = ActionContext.getContext();
        ac.getSession().put("fileGraph",fg);

        System.out.println("down:" + downThreshold + " up:" + upThreshold);

        try {
            if(downThreshold < DefaultConfigure.DEFAULT_DOWN_THRESHOLD) downThreshold = DefaultConfigure.DEFAULT_DOWN_THRESHOLD;
            long fileId = viewDataPacker.GetJsonForD3WithRange(downThreshold,upThreshold);
//            HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
//            response.setContentType("text/html;charset=utf-8");
//            response.getWriter().write(String.valueOf(fileId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";

    }

    public int getDownThreshold() {
        return downThreshold;
    }

    public void setDownThreshold(int downThreshold) {
        this.downThreshold = downThreshold;
    }

    public int getUpThreshold() {
        return upThreshold;
    }

    public void setUpThreshold(int upThreshold) {
        this.upThreshold = upThreshold;
    }
}
