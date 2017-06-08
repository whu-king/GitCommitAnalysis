package statistic.ImpactAuthor;

import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.EncodeUtils;
import utils.HttpUtils;
import utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */
public class OrganizationMembers {

    private static Map<String, String> headers;
    private static String token = "eb99f0ee871c0719ed503b85fd7d8051d8b9d235";
    private static String id = "whu-king";
    private static String pwd = "070906king";
    public OrganizationMembers(){
        this.headers = new HashMap<String, String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, sdch,br");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Content-Type","text/html;charset=utf-8");
        headers.put("Host", "github.com");

    }

    public String getCollaborators(String projectName){
        headers.put("Host","api.github.com");
        String owner = Author.project2orgnization(projectName);
//        String owner = "whu-king";
//        String baseURL = "https://api.github.com/repos/" + owner + "/" + projectName + "/collaborators";
        String baseURL = "https://api.github.com/orgs/" + owner + "/members";
        String url = baseURL + "?access_token=" + token;
        System.out.println(url);
        HttpResponse response = HttpUtils.doGet(baseURL, headers);
        String responseText = HttpUtils.getStringFromResponse(response);
        responseText = EncodeUtils.unicdoeToGB2312(responseText);
        System.out.println(responseText);
        return "";
    }

    public  String getInner(String organize){
        int pageIndex = 1;
        String baseURL = "https://github.com/orgs/" + organize + "/people?page=" + pageIndex;
        System.out.println(baseURL);
        HttpResponse response = HttpUtils.doGet(baseURL, headers);
        String responseText = HttpUtils.getStringFromResponse(response);
        responseText = EncodeUtils.unicdoeToGB2312(responseText);

//        System.out.println(responseText);
        Document doc = Jsoup.parse(responseText);
        String countString = doc.select("span[class=counter]").text();
        int peopleCount = Integer.valueOf(countString.replaceAll(",",""));
        int pageVol = 30;
        int pageNum =(int) Math.ceil(peopleCount * 1.0 / pageVol);
        StringBuffer sb = new StringBuffer();
        for(pageIndex = 1; pageIndex < pageNum + 1; pageIndex++){

            baseURL = "https://github.com/orgs/" + organize + "/people?page=" + pageIndex;
            System.out.println(baseURL);
            response = HttpUtils.doGet(baseURL, headers);
            responseText = HttpUtils.getStringFromResponse(response);
            responseText = EncodeUtils.unicdoeToGB2312(responseText);
//            Utils.writeFileFromString("prs.txt", responseText);
//            System.out.println(responseText);
            Document docs = Jsoup.parse(responseText);
            Elements bignames = docs.select("a[class=css-truncate-target f4]");
            Iterator<Element> keywords = bignames.iterator();
            while(keywords.hasNext()){
                sb.append(keywords.next().text() + "\n");
            }

            Elements snames = docs.select("span[class=d-block css-truncate-target f5 text-gray-dark]");
            Iterator<Element> names = snames.iterator();
            while(names.hasNext()){
                sb.append(names.next().text() + "\n");
            }
        }

        System.out.println(sb);
        Utils.writeFileFromString(organize + "-members.txt", sb.toString());
        return "";
    }

    public static void main(String[] args){
//        String orgs[] = {"ReactiveX","Activiti","atmosphere","checkstyle","deeplearning4j",
//                        "google","netty","hibernate","naver","realm","spring-projects","MovingBlocks",
//                        "facebook","opensourceBIM","pentaho","imglib","SAP","alibaba","clojure",
//                  "thinkaurelius","mockito","apache","eclipse","netflix"};
        String orgs[] = {"FasterXML","syncany","graphhopper","liquibase"};
//
        for(String name : orgs){
            new OrganizationMembers().getInner(name);
        }

//        new OrganizationMembers().getCollaborators("hystrix");

    }
}
