package statistic;

import model.gitLog.GitCommit;
import utils.FormatConversionUtil;

import java.util.*;

/**
 * Created by Administrator on 2017/5/16.
 */
public class GitCommitSorter {

    public static void timeAsc(List<GitCommit> gitCommits){
        Collections.sort(gitCommits, new Comparator<GitCommit>() {
            @Override
            public int compare(GitCommit gitCommit1, GitCommit gitCommit2) {
                Date date1 = FormatConversionUtil.getDateFromString(gitCommit1.getDate());
                Date date2 = FormatConversionUtil.getDateFromString(gitCommit2.getDate());
                if(FormatConversionUtil.isD1EarlierThanD2(date1,date2)) return -1;
                return 1;
            }
        });
    }

    public static void riskDsc(List<GitCommit> gitCommits){
        Collections.sort(gitCommits, new Comparator<GitCommit>() {
            @Override
            public int compare(GitCommit o1, GitCommit o2) {
                return -(int)(o1.getRisk() - o2.getRisk());
            }
        });
    }

}
