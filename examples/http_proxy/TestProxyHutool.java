import java.net.Authenticator;
import java.net.PasswordAuthentication;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpRequest;

// 代理验证信息
class ProxyAuthenticator extends Authenticator {
    private String user, password;

    public ProxyAuthenticator(String user, String password) {
        this.user     = user;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }
}


public class TestProxyHutool {
    // 用户名密码认证(私密代理/独享代理)
    final static String ProxyUser = "username";
    final static String ProxyPass = "password";

    // 代理IP、端口号
    final static String ProxyHost = "59.38.241.25";
    final static Integer ProxyPort = 23916;

    public static void main(String[] args) {
        // 目标网站
        String url = "https://dev.kdlapi.com/testproxy";
        // JDK 8u111版本后，目标页面为HTTPS协议，启用proxy用户密码鉴权
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        // 设置请求验证信息
        Authenticator.setDefault(new ProxyAuthenticator(ProxyUser, ProxyPass));

        // 发送请求
        HttpResponse result = HttpRequest.get(url)
                .setHttpProxy(ProxyHost, ProxyPort)
                .timeout(20000)//设置超时，毫秒
                .execute();
        System.out.println(result.body());
    }
}