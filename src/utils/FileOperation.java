package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class FileOperation {


    public static String readStringFrom(String filePath) {

        try {
            StringBuffer sb = new StringBuffer();
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<String> getAllFileNameIn(String Dir,String prefix){
        File dir = new File(Dir);
        File[] files = dir.listFiles();
        List<String> allFiles = new ArrayList<String>();
        for(File file : files){
            if(file.isHidden()) continue;
            String fileName;
            if(prefix.equalsIgnoreCase("")) fileName = file.getName();
            else fileName = prefix + "\\" + file.getName();

            if(file.isDirectory())
                allFiles.addAll(getAllFileNameIn(file.getAbsolutePath(),fileName));
            else {
                allFiles.add(fileName);
            }
        }
        return allFiles;
    }
}
