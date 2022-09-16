package test;

import kdl.Auth;
import kdl.Client;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestTools {
    private Auth auth = new Auth("o9oep1omndj2g2khzren", "ne5e2pcl2f2g7i3s3o0ypo0qs31ty5gw");
    private Client client = new Client(auth);

    @Test
    public void test_get_ua() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dt", "pc");                 // 多个筛选以,分隔。如pc,pad
        params.put("platform", "mac,linux");    // 多个筛选以,分隔。如linux,win
        params.put("browser", "chrome");        // 多个筛选以,分隔。如chrome,weixin
        String[] result = client.get_ua(5 ,params);
        for (String c:result) {
            System.out.println(c);
        }
    }

    @Test
    public void test_get_order_info() throws Exception {
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("sign_type", "token");
        Map<String, String> res = client.get_order_info(kwargs);
        System.out.println(res);
    }

    @Test
    public void TestGetSecretToken() throws Exception {
        String token = client.GetSecretToken();
        System.out.println(token);
    }

}
