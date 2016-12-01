package utils;

import java.io.*;

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
}
