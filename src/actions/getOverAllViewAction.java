package actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dataAccess.DataMerge;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;
import org.apache.struts2.ServletActionContext;
import statistic.RiskRank;
import viewAdapter.ViewDataPacker;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/12/29.
 */
public class getOverAllViewAction {

    private int downThreshold ;
    private int upThreshold = 100;

    public String execute(){
        java.util.List<GitCommit> gitCommits =  DataMerge.MergeFrom("C:\\Users\\Administrator\\documents\\netty\\netty\\nettyStat2.json",
                "C:\\Users\\Administrator\\documents\\netty\\netty\\nettyCommitMessage.json");
        long before = System.currentTimeMillis();
        FileGraph fg = FileGraph.valueOf(gitCommits);
//        RiskRank.doRank(fg);
        long after = System.currentTimeMillis();
        System.out.println("transform gitCommits to fileGraph took time " + (after - before) + "ms");
        ViewDataPacker viewDataPacker = new ViewDataPacker(fg);

        //todo put graph into DB
        ActionContext ac = ActionContext.getContext();
        ac.getSession().put("fileGraph",fg);

        System.out.println("down:" + downThreshold + " up:" + upThreshold);

        try {
            if(downThreshold < 5) downThreshold = 10;
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
