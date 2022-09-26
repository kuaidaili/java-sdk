import kdl.Auth;
import kdl.Client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类使用示例
 * 接口鉴权说明：
 * 目前支持的鉴权方式有 "token" 和 "hmacsha1" 两种，默认使用 "token"鉴权。
 */
public class useTool {

    public static void main(String[] args) throws Exception {
        Auth auth = new Auth("secret_id", "secret_key");
        Client client = new Client(auth);

        // 获取ua
        Map<String, Object> params1 = new HashMap<String, Object>();
        String[] ua_list = client.get_ua(10, params1);
        System.out.println("ua_list: " + Arrays.toString(ua_list));

        // 获取指定地区编码
        Map<String, Object> params2 = new HashMap<String, Object>();
        Map<String, String> areaRes = client.get_area_code("北京市", params2);
        System.out.println("areaRes: " + Arrays.toString(areaRes.entrySet().toArray()));

        // 获取账号余额
        Map<String, Object> params3 = new HashMap<String, Object>();
        Map<String, String> balance = client.get_account_balance(params3);
        System.out.println("balance: " + Arrays.toString(balance.entrySet().toArray()));
    }
}
