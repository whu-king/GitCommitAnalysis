package dataAccess;

import utils.EncodeUtils;
import utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Administrator on 2017/3/28.
 */
public class CommitFileReader {

    private static Map<String, String> headers;
    private static String token = "eb99f0ee871c0719ed503b85fd7d8051d8b9d235";

    public CommitFileReader(){
        this.headers = new HashMap<String, String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, sdch,br");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Content-Type","text/html;charset=utf-8");
        headers.put("Host", "github.com");
        headers.put("Upgrade-Insecure-Requests","1");

    }

    public static List<String> getFileOfCommit(String owner,String project,String file,String sha) {

        int prefixIndex = file.indexOf("/java",0);
        String prefix = "";
        if(prefixIndex != -1){
            prefix = file.substring(0,prefixIndex) + "java/";
            prefix = prefix.replace("test","main");
        }
        List<String> imports = new ArrayList<String>();
        String url = "https://github.com/" + owner +"/" + project + "/blob/" +
                     sha + "/" + file + "?access_token=" + token ;
        System.out.println(url);
        HttpResponse response = HttpUtils.doGet(url, headers);
        InputStream is = HttpUtils.getInputStreamFromResponse(response);
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseText = Utils.getStringFromInputStreamReader(isr);
        responseText = EncodeUtils.unicdoeToGB2312(responseText);
//      System.out.println(responseText);
        Document doc = Jsoup.parse(responseText);
        Elements as = doc.select("table[class=highlight tab-size js-file-line-container]");
        Iterator<Element> codeLines = as.select("tr").iterator();
        int importFlag = 0;
        while(codeLines.hasNext()){
            Element line = codeLines.next();
            String code = line.select("td[class=blob-code blob-code-inner js-file-line]").text();
            if(code.startsWith("import")){
                if(importFlag != 1) importFlag = 1;
                code = code.replace("import","");
                code = code.replace(";","");
                code = code.replaceAll("\\.","/");
                code = code.replaceAll("static","").trim();
                int index = 0;
                if((index = code.indexOf("*")) > -1){
                    code = code.substring(0,index-1);
                }
                code = prefix + code + ".java";
                imports.add(code.trim());
            }else {
                if(importFlag == 1) importFlag ++;
                if(importFlag > 20) break;
            }
        }
        System.out.println(imports);
        return imports;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
       getFileOfCommit("netty","netty","src/main/java/org/jboss/netty/logging/JdkLogger.java","10bc616b4b5599becbcbcc4b76d95fb2a8a4106c");
    }
}
