package com.kuaidaili.sdk;

/**
 * 使用jdk原生库调用API
 */
public class TestAPI {

    private static String apiUrl = "http://dev.kdlapi.com/api/getproxy?secret_id=o1fjh1re9o28876h7c08&signature=xxxxx&num=10&format=json&sep=1"; //API链接

    public static void main(String[] args) {
        HttpRequest request = new HttpRequest();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Encoding", "gzip"); //使用gzip压缩传输数据让访问更快
        try{
            HttpResponse response = request.sendGet(apiUrl, null, headers, null);
            System.out.println(response.getCode());
            System.out.println(response.getContent());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
