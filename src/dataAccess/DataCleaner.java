package dataAccess;

import model.gitLog.FileChange;
import model.gitLog.GitCommit;
import model.gitLog.GitStat;
import utils.FormatConversionUtil;

import java.util.*;

/**
 * Created by Administrator on 2017/1/1.
 */
public class DataCleaner {

    private static double COMMIT_INTERNAL_MAX_LENGTH = 1000 * 60 * 60 * 12.0;// 12 hour


    public static List<GitCommit> clean(List<GitCommit> gitCommits){
        List<GitCommit> newGits = new ArrayList<GitCommit>();
        newGits = mergeSimilarCommit(gitCommits);
        for(GitCommit gitCommit : newGits){
            GitStat gitStat = gitCommit.getFileDiff();
            gitStat = checkGitStat(gitStat);
        }
        return newGits;
    }


    public static GitStat checkGitStat(GitStat gitStat) {
        List<FileChange> changes = gitStat.getDiffs();
        changes = checkDuplicateCommit(changes);
        changes = checkEmptyOperation(changes);
        gitStat.setDiffs(changes);
        return gitStat;
    }

    public static List<GitCommit> mergeSimilarCommit(List<GitCommit> gitCommits){
        List<GitCommit> newGitCommits = new ArrayList<GitCommit>();
        HashMap<String,GitCommit> authorList = new HashMap<String,GitCommit>();
        for(GitCommit gitCommit : gitCommits){
            String author = gitCommit.getAuthor();
            GitCommit lastGitCommit = authorList.get(author);
            if(lastGitCommit != null){
                if(isInSameCommit(gitCommit,lastGitCommit)){
                    GitStat gitStat = gitCommit.getFileDiff();
                    GitStat lastGitStat = lastGitCommit.getFileDiff();
                    List<FileChange> fileChanges = gitStat.getDiffs();
                    fileChanges.addAll(lastGitStat.getDiffs());
                    gitStat.setDiffs(fileChanges);
                    int commitPlace = newGitCommits.indexOf(lastGitCommit);
                    newGitCommits.set(commitPlace,gitCommit);
                }else{
                    newGitCommits.add(gitCommit);
                }
            }else{
                newGitCommits.add(gitCommit);
            }
            authorList.put(gitCommit.getAuthor(),gitCommit);
        }

        return newGitCommits;
    }

    public static boolean isInSameCommit(GitCommit gitCommmit, GitCommit another){

        Date date = FormatConversionUtil.getDateFromString(gitCommmit.getDate());
        Date date2 = FormatConversionUtil.getDateFromString(another.getDate());

        long time1 = date.getTime();
        long time2 = date2.getTime();

        long internalMills = time1 > time2 ? time1 - time2 : time2 - time1;
        if(internalMills < COMMIT_INTERNAL_MAX_LENGTH){
            String author = gitCommmit.getAuthor();
            String author2 = another.getAuthor();
            if(author.equalsIgnoreCase(author2)){
                return true;
            }
        }

        return false;
    }


    private static List<FileChange> checkDuplicateCommit(List<FileChange> diffs) {

        List<FileChange> newDiff = new LinkedList<FileChange>();
        for(FileChange fc : diffs) {
            if (newDiff.contains(fc)) {
                int place = newDiff.indexOf(fc);
                FileChange existingFC = newDiff.get(place);
                if (!fc.getDeletions().equalsIgnoreCase(existingFC.getDeletions()) &&
                        !fc.getInsertions().equalsIgnoreCase(existingFC.getInsertions())) {
                    existingFC.setDeletions((Integer.parseInt(existingFC.getDeletions()) +
                            Integer.parseInt(fc.getDeletions())) + "");
                    existingFC.setInsertions((Integer.parseInt(existingFC.getInsertions()) +
                            Integer.parseInt(fc.getInsertions())) + "");
                }
                continue;
            }
            newDiff.add(fc);
        }
        return newDiff;
    }

    private static List<FileChange> checkEmptyOperation(List<FileChange> diffs) {
        List<FileChange> newDiff = new LinkedList<FileChange>();
        for (FileChange fc : diffs) {
            if (fc.getInsertions().equalsIgnoreCase("0") &&
                    fc.getDeletions().equalsIgnoreCase("0")) continue;
            if (fc.getDeletions().equalsIgnoreCase("-") &&
                    fc.getInsertions().equalsIgnoreCase("-")) continue;
            newDiff.add(fc);
        }
        return newDiff;
    }
}
