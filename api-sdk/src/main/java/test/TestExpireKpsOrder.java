package test;

import kdl.Auth;
import kdl.Client;
import kdl.exceptions.KdlException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestExpireKpsOrder {
    private Auth auth = new Auth("secret_id", "secret_key");
    private Client client = new Client(auth);

    @Test(expected = KdlException.class)
    public void test_get_order_expire_time() throws Exception {
        String expire_time = client.get_order_expire_time("hmacsha1");
        assertNotNull(expire_time);
    }

    @Test(expected = KdlException.class)
    public void test_get_ip_whitelist() throws Exception {
        client.set_ip_whitelist(new String[]{}, "hmacsha1");
        String[] ip_whitelist = client.get_ip_whitelist();
        assertEquals(ip_whitelist.length, 0);
        client.set_ip_whitelist("10.31.89.93, 1.2.3.4", "token");
        ip_whitelist = client.get_ip_whitelist("hmacsha1");
        assertEquals(ip_whitelist.length, 2);
        client.set_ip_whitelist("");
    }

    @Test(expected = KdlException.class)
    public void test_get_kps() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sign_type", "hmacsha1");
        params.put("format", "json");
        params.put("area", "广东,云南");
        params.put("pt", 2);
        params.put("f_citycode", 1);
        String[] ips = client.get_kps(2, params);
        System.out.println("kps: " + Arrays.toString(ips));
        assertEquals(ips.length, 1);
    }

    @Test(expected = KdlException.class)
    public void test_check_dps_valid() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("format", "json");
        String[] ips = client.get_dps(2, params);
        System.out.println("ips: " + Arrays.toString(ips));
        Map<String, Boolean> valids = client.check_dps_valid(ips);
        System.out.println("valids: " + Arrays.toString(valids.entrySet().toArray()));
        assertEquals(valids.entrySet().toArray().length, 1);
    }

    @Test(expected = KdlException.class)
    public void test_get_ip_balance() throws Exception {
        int balance = client.get_ip_balance();
        System.out.println("balance: " + balance);
        assertTrue(balance > 0);
    }
}
