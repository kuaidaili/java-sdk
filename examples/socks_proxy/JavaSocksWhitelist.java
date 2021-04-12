// 请求socks代理服务器(IP白名单)
// http和https网页均适用

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class JavaSocksWhitelist {

    public static void main(String[] args) throws IOException {

        String proxyIp = "59.38.241.25"; // 代理服务器IP
        int proxyPort = 23918;  // 代理服务器端口

        // 要访问的目标网页
        String pageUrl = "http://dev.kuaidaili.com/testproxy";

        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp, proxyPort));
        URL url = new URL(pageUrl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection(proxy);
        httpUrlConnection.setRequestMethod("GET");
        httpUrlConnection.setConnectTimeout(5*1000);  // 设置超时时间
        httpUrlConnection.setRequestProperty("Accept-Encoding", "gzip");  // 添加gzip压缩让数据传输更块

        // 发起请求
        httpUrlConnection.connect();

        // 输出状态码
        System.out.println("code: " + httpUrlConnection.getResponseCode());

        // 读取返回内容
        InputStream inputStream = httpUrlConnection.getInputStream();
        String encoding = httpUrlConnection.getContentEncoding();
        // 处理gzip压缩
        if (encoding.equals("gzip")) inputStream = new GZIPInputStream(inputStream);
        String message = getContentFromInputStream(inputStream);

        // 输出返回内容
        System.out.println(message);

        // 关闭输入流和连接
        inputStream.close();
        httpUrlConnection.disconnect();
    }

    // 读取输入流中的内容
    public static String getContentFromInputStream(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (;;) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}