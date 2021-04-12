import okhttp3.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

public class TestProxyOKHttpClient {
    public static void main(String args[]) throws IOException {
        // 目标网站
        String targetUrl = "https://dev.kdlapi.com/testproxy";

        // 用户名密码认证(私密代理/独享代理)
        final String username = "username";
        final String password = "password";

        String ip = "tpsxxx.kdlapi.com";   // 代理服务器IP
        int port = 20818;

        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));
        java.net.Authenticator.setDefault(new java.net.Authenticator(){//通过设置一个全局的Authenticator，实现socks设置Authenticator用户名密码

            private PasswordAuthentication authentication = new PasswordAuthentication(username, password.toCharArray());
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return authentication;
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        Request request = new Request.Builder()
                .url(targetUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

    }
}