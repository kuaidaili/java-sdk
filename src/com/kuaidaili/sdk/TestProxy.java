package com.kuaidaili.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用jdk原生库请求代理服务器
 * 请求http和https网页均适用
 * 运行环境要求: jdk >= 1.6
 */
public class TestProxy {

	private static String pageUrl = "http://dev.kuaidaili.com/testproxy"; //要访问的目标网页
	private static String proxyIp = "59.38.241.25"; //代理服务器IP
	private static String proxyPort = "23916"; //代理服务器IP
	private static String username = "myusername"; //用户名
	private static String password = "mypassword"; //密码
	
	public static void main(String[] args) {
		HttpRequest request = new HttpRequest();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		
		headers.put("Accept-Encoding", "gzip"); //使用gzip压缩传输数据让访问更快
		
		Map<String, String> proxySettings = new HashMap<String, String>();
		proxySettings.put("ip", proxyIp);
		proxySettings.put("port", proxyPort);
		proxySettings.put("username", username);
		proxySettings.put("password", password);
		
		try{
			HttpResponse response = request.sendGet(pageUrl, params, headers, proxySettings);
			System.out.println(response.getCode());
			System.out.println(response.getContent());
		}
		catch (Exception e) {
        	e.printStackTrace();
		}
	}
}
