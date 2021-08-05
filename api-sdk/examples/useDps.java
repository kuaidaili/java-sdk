import kdl.Auth;
import kdl.Client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 私密代理使用示例
 * 接口鉴权说明：
 * 目前支持的鉴权方式有 "simple" 和 "hmacsha1" 两种，默认使用 "simple"鉴权。
 */
public class useDps {

    public static void main(String[] args) throws Exception {
        Auth auth = new Auth("yourorderid", "yourapikey");
        Client client = new Client(auth);

        // ---------------------------------------------------------------------------
        // 获取订单到期时间，默认simple鉴权, 返回时字符串
        String expire_time = client.get_order_expire_time();
        System.out.println("expire_time: " + expire_time);


        // ---------------------------------------------------------------------------
        // 获取订单到期时间, 用hmacsha1鉴权
        expire_time = client.get_order_expire_time("hmacsha1");
        System.out.println("expire_time using hmacsha1: " + expire_time);

        // ----------------------------------------------------------------------------
        // 获取ip白名单, 默认simple鉴权, 返回String[] ip数组
        String[] ip_whitelist = client.get_ip_whitelist();
        System.out.println("ip_whitelist: " + Arrays.toString(ip_whitelist));

        // 获取ip白名单, hmacsha1方式鉴权
        ip_whitelist = client.get_ip_whitelist("hmacsha1");
        System.out.println("ip_whitelist using hmacsha1: " + Arrays.toString(ip_whitelist));

        // -------------------------------------------------------------------------------
        // 设置ip白名单, 无返回值, 默认simple鉴权，要设置的ip白名单可为字符串(IP之间用逗号隔开)或数组
        client.set_ip_whitelist("127.0.0.1, 192.168.0.139");
        client.set_ip_whitelist("", "hmacsha1");
        client.set_ip_whitelist(new String[]{"127.0.0.1", "192.168.0.139"}, "hmacsha1");
        client.set_ip_whitelist(new String[]{});

        // --------------------------------------------------------------------------------
        /* 获取私密代理, 第一个参数为提取数量, int类型, 必填。 第二个参数为其他参数, Map<String, Object>类型, 可选。
         * 具体有哪些参数可参考帮助中心api说明: https://help.kuaidaili.com/api/intro/
         * 返回String[] 代理数组
         **/
        String[] dps_proxies = client.get_dps(10);
        System.out.println("dps_proxies: " + Arrays.toString(dps_proxies));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sign_type", "hmacsha1");
        params.put("area", "北京, 上海, 云南, 广州");
        dps_proxies = client.get_dps(10, params);
        System.out.println("dps_proxies using other params: " + Arrays.toString(dps_proxies));

        // ---------------------------------------------------------------------------------
        // 检测私密代理有效性, 默认simple鉴权, 返回 Map<String, Boolean> 类型, 格式为 proxy: true/false
        Map<String, Boolean> valids = client.check_dps_valid(dps_proxies, "hmacsha1");
        System.out.println("dsp valids: " + Arrays.toString(valids.entrySet().toArray()));

        // --------------------------------------------------------------------------------
        // 获取计数版订单ip余额, 返回ip余额, int类型
        int ip_balance = client.get_ip_balance();
        System.out.println("ip_balance: " + ip_balance);

        // ----------------------------------------------------------------------------------
        // 获取私密代理剩余时间(秒数), 返回 Map<String, Integer> 类型, 格式为 proxy: seconds(秒数)
        String[] ips = client.get_dps(5);
        Map<String, Integer> seconds = client.get_dps_valid_time(ips);
        System.out.println("seconds: " + Arrays.toString(seconds.entrySet().toArray()));

    }
}
