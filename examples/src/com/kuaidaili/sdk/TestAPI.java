package com.kuaidaili.sdk;

/**
 * 使用jdk原生库调用API, 运行环境要求: jdk >= 1.6
 */
public class TestAPI {

	private static String apiUrl = "http://dev.kdlapi.com/api/getproxy/?orderid=965102959536478&num=100&protocol=1&method=2&an_ha=1&sep=1"; //API链接
	
	public static void main(String[] args) {
		HttpRequest request = new HttpRequest();
		
		try{
			HttpResponse response = request.sendGet(apiUrl, null);
			System.out.println(response.getCode());
			System.out.println(response.getContent());
		}
		catch (Exception e) {
        	e.printStackTrace();
		}
	}
}
