package test;

import kdl.Auth;
import kdl.Client;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestTools {
//    private Auth auth = new Auth("yourorderid", "yourapikey");
    private Auth auth = new Auth("999901638519220", "n2iu8nzo62hkuuu6yfxlrzjetlk6o4xd");
    private Client client = new Client(auth);

    @Test
    public void test_get_ua() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("num", 10);
        params.put("dt", "pc");                 // 多个筛选以,分隔。如pc,pad
        params.put("platform", "mac,linux");    // 多个筛选以,分隔。如linux,win
        params.put("browser", "chrome");        // 多个筛选以,分隔。如chrome,weixin

        String[] result = client.get_ua(1, params);
        for (String c:result) {
            System.out.println(c);
        }
    }

}
