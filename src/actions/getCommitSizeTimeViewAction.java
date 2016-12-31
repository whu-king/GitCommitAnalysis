package actions;

import com.opensymphony.xwork2.ActionContext;
import dataAccess.DataMerge;
import model.gitLog.GitCommit;
import org.apache.struts2.ServletActionContext;
import statistic.CommitSizeAnalyzer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/12/31.
 */
public class getCommitSizeTimeViewAction {

    public void execute(){
        List<GitCommit> gitCommits =  DataMerge.MergeFrom("C:\\Users\\Administrator\\documents\\netty\\netty\\nettyStat2.json",
                "C:\\Users\\Administrator\\documents\\netty\\netty\\nettyCommitMessage.json");
        String jsonString = new CommitSizeAnalyzer(gitCommits).relateSizeAndTime();
        ActionContext ac = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
