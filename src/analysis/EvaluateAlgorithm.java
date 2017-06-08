package analysis;

import model.fileGraph.CodeFile;
import model.fileGraph.FileGraph;
import model.gitLog.GitCommit;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface EvaluateAlgorithm {

    double getFileRisk(CodeFile cf);
    double getFileRisk(CodeFile cf, Date currentDate);
    double getFileEffort(CodeFile cf,GitCommit gitCommit);
    double getIMPForCommit(GitCommit gitCommit,FileGraph fg, int minThreshold);
    double getRISKForCommit(GitCommit gitCommt, FileGraph fg);
    double getEFFORTForCommit(GitCommit gitCommit, FileGraph fg);
}
