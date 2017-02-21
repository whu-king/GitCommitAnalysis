package dataAccess;

import java.io.*;

/**
 * Created by Administrator on 2017/1/1.
 */
public class CallExeDemo {

    public static void main(final String[] args) throws IOException {

        getCommitMessage("all/pom.xml");

    }

    // 调用其他的可执行文件，例如：自己制作的exe，或是 下载 安装的软件.

    public static String getCommitMessage(String fileName) {
        final Runtime runtime = Runtime.getRuntime();
        String cmd[] = new String[3];
        cmd[0] = "cmd";
        cmd[1] = "/c";
        String format1 = "--date=format:\"%Y-%m-%d %H:%M:%S\" ";
        String format2 = "--pretty=format:\"{ %n 'commitSHA' : '%H' ," +
                "%n 'author' : '%aN <%aE>', %n 'date': '%ad' ,%n 'message' : '%f' %n }\" ";;
        cmd[2] = "git log " + format1 + format2 + fileName ;
//        String command = "git log\r\n";
        String projectDir = "C:\\Users\\Administrator\\Documents\\netty\\netty";
        StringBuffer output = new StringBuffer();

        try {
            final Process proc = runtime.exec(cmd,null,new File(projectDir));

//            OutputStream stdin = proc.getOutputStream();
//            stdin.write(command.getBytes());


            // 主线程负责读取并打印 proc 的标准输出。
            BufferedReader stdout = new BufferedReader(new InputStreamReader
                    (proc.getInputStream()));
            for (String line; null != (line = stdout.readLine()); ){
//                System.out.println(line);
                output.append(line + "\n");
            }
            BufferedReader stderr = new BufferedReader(new InputStreamReader
                    (proc.getErrorStream()));

            for (String line; null != (line = stderr.readLine()); ){
                System.out.println("*********************ERROR********************");
                System.out.println(line);
                System.out.println("*********************ERROR********************");
            }
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = transformIntoJson(output.toString());
//        System.out.println(json);
        return json;

    }

    public static String transformIntoJson(String cmdOutput){
        String json = cmdOutput.replaceAll("}","},");
        json = json.replaceAll("'","\"");
        json = json.substring(0,json.length()-2);
        json = "[" + json + "]";
        return json;
    }


}
