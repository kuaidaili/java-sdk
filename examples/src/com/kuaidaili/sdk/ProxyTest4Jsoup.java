
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用Jsoup发起请求。
 */
public class ProxyTest4Jsoup {

    private static String pageUrl = "http://dev.kdlapi.com/testproxy"; // 访问的目标网页

    public static void main(String[] args) {
        doCrawl();
    }

    public static void doCrawl() {

        List<String> proxyList = null;
        try {
            proxyList = getProxyListFromAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> articles = new ArrayList<String>();
        articles.add(pageUrl);

        //使用线程池，多线程同时抓取
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < articles.size(); i++) {
            executorService.execute(new CrawlThread(articles.get(i), proxyList));
        }
    }

    private static List<String> getProxyListFromAPI() throws IOException {
        Document doc = null;
        String apiUrl = "http://svip.kdlapi.com/api/getproxy/?orderid=947449222924633&num=100&protocol=1&method=2&an_an=1&an_ha=1&quality=2&format=json&sep=1"; //API链接
        try {
            doc = Jsoup.connect(apiUrl)
                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50")
                    .ignoreContentType(true)
                    .get(); // 调用API接口
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解析请求到的代理ip
       String ipStr = doc.body().text();

        Map<String,Object> resultMap= (Map<String, Object>) JSONObject.parse(ipStr);
        List<String> ipList= (List<String>) ((JSONObject) resultMap.get("data")).get("proxy_list");

        ipList.forEach(s->{
            System.out.println(s);
        });
        return ipList;
    }

    public static void visit(String proxy, String url) {
        String[] r = proxy.split(":");
        String ip = r[0];
        Integer port = Integer.valueOf(r[1]);

        System.out.println("-----------------");
        System.out.println("使用代理:" + proxy);
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    // .cookie("auth", "token")
                    // .timeout(3000)
                    .proxy(ip, port)
                    .ignoreContentType(true)
                    .get();

            System.out.println(document.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

