import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestProxyJsoup {
    // 用户名密码, 若已添加白名单则不需要添加
    final static String ProxyUser = "username";
    final static String ProxyPass = "password";

    // 隧道域名、端口号
    final static String ProxyHost = "tpsXXX.kdlapi.com";
    final static Integer ProxyPort = 15818;

    public static String getUrlProxyContent(String url) {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));

        try {
            // 此处自己处理异常、其他参数等
            Document doc = Jsoup.connect(url).followRedirects(false).timeout(3000).proxy(proxy).get();
            if (doc != null) {
                System.out.println(doc.body().html());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // 目标网站
        String targetUrl = "https://dev.kdlapi.com/testproxy";

        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

        getUrlProxyContent(targetUrl);
    }
}