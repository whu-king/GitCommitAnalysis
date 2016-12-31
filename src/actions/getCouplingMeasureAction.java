package actions;

import analysis.BasicChangeCouplingMeasure;
import analysis.ChangeCouplingMeasure;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.fileGraph.FileGraph;
import model.fileGraph.Node;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/29.
 */

public class getCouplingMeasureAction {

    private String className;
    private int threshold;
    private FileGraph fileGraph;

    public void execute() throws IOException {
        ChangeCouplingMeasure changeCouplingMeasure = new BasicChangeCouplingMeasure();
        Node node = fileGraph.findNodeByPath(className);
        double timeExpSum = changeCouplingMeasure.getTimeExpSumOfNCoupledLinks(node,threshold,fileGraph);
        double timeLineSum = changeCouplingMeasure.getTimeLinearSumOfNCoupledLinks(node,threshold,fileGraph);
        int classNum = changeCouplingMeasure.getNumberOfNCoupledClass(node,threshold,fileGraph);
        int couplingLinkNum = changeCouplingMeasure.getSumOfNCoupledLinks(node,threshold,fileGraph);
        String JsonString = "{classNum : " + classNum + ", couplingLinkNum : " + couplingLinkNum +
                ", timeExpSum : " + timeExpSum + "timeLineSum : " + timeLineSum;

        ActionContext ac = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(JsonString);

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threhold) {
        this.threshold = threhold;
    }

    public FileGraph getFileGraph() {
        return fileGraph;
    }

    public void setFileGraph(FileGraph fileGraph) {
        this.fileGraph = fileGraph;
    }
}
