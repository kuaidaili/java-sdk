import okhttp3.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class TestProxyOKHttpClient {
    public static void main(String args[]) throws IOException {
        // 目标网站
        String targetUrl = "https://dev.kdlapi.com/testproxy";

        // 用户名密码, 若已添加白名单则不需要添加
        final String username = "username";
        final String password = "password";

        String ip = "tpsXXX.kdlapi.com";   // 代理服务器IP
        int port = 15818;

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

        Authenticator authenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .proxyAuthenticator(authenticator)
                .build();

        Request request = new Request.Builder()
                .url(targetUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36")
                .addHeader("Connection","close")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

    }
}