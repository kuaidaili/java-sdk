package com.kuaidaili.sdk;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 使用httpclient请求代理服务器
 * 请求http和https网页均适用
 * 运行环境要求: jdk >= 1.6
 * 依赖包:
 * httpclient-4.5.3.jar
 * httpcore-4.4.6.jar
 * commons-codec-1.9.jar
 * commons-logging-1.2.jar
 */

public class TestProxyHttpClient {

	private static String pageUrl = "http://dev.kuaidaili.com/testproxy"; //要访问的目标网页
	private static String proxyIp = "59.38.241.25"; //代理服务器IP
	private static int proxyPort = 23916; //代理服务器IP
	private static String username = "myusername"; //用户名
	private static String password = "mypassword"; //密码


    public static void main(String[] args) throws Exception {
    	CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxyIp, proxyPort),
                new UsernamePasswordCredentials(username, password));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        try {
        	URL url = new URL(pageUrl);
            HttpHost target = new HttpHost(url.getHost(), url.getDefaultPort(), url.getProtocol());
            HttpHost proxy = new HttpHost(proxyIp, proxyPort);

            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet(url.getPath());
            httpget.setConfig(config);
            httpget.addHeader("Accept-Encoding", "gzip"); //使用gzip压缩传输数据让访问更快

            System.out.println("Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxy);

            CloseableHttpResponse response = httpclient.execute(target, httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}