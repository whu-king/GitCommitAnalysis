package viewAdapter;

import model.fileGraph.Edge;
import model.fileGraph.Node;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface Property2ViewMapper {


    double getRadiusFrom(Node node);

    double getColorValueFrom(Node node);

    double getLengthFrom(Edge edge);

    String getStrokeWidth(Node node);

    String getStrokeColor(Node node);
}
