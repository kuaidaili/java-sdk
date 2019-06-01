import kdl.Auth;
import kdl.Client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 开放代理使用示例
 * 方法的默认鉴权方式请参考帮助中心api文档: "https://help.kuaidaili.com/api/intro/"
 * 若需要使用鉴权，则auth对象必须将订单号对应的api key作为第二个参数传入构造函数中，否则只能调用
 * 不需要鉴权的api，比如提取代理的api
 */
public class useOps {
    public static void main(String[] args) throws Exception {
        Auth auth = new Auth("yourorderid", "yourapikey");
        Client client = new Client(auth);

        // ---------------------------------------------------------------------------

        // 获取订单到期时间，默认simple鉴权, 返回时字符串
        String expire_time = client.get_order_expire_time();
        System.out.println("expire_time: " + expire_time);

        // 获取订单到期时间, 用hmacsha1鉴权
        expire_time = client.get_order_expire_time("hmacsha1");
        System.out.println("expire_time using hmacsha1: " + expire_time);

        // ----------------------------------------------------------------------------


        /* 获取开放代理, 第一个参数为提取数量, int类型, 必填。
         * 第二个参数为order_level, String类型, 默认为dev, 如果您是SVIP订单, 请传入"svip", 如果您是企业版订单, 请传入"ent"
         * 第三个参数为其他参数, Map<String, Object>类型, 可选。
         * 具体有哪些参数可参考帮助中心api说明: https://help.kuaidaili.com/api/intro/
         * 返回String[] 代理数组
         **/
        String[] ops_proxies = client.get_proxy(10, "svip");
        System.out.println("ops_proxies: " + Arrays.toString(ops_proxies));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sign_type", "hmacsha1");
        params.put("area", "北京, 上海, 云南, 广州");
        ops_proxies = client.get_proxy(10, "svip", params);
        System.out.println("ops_proxies using other params: " + Arrays.toString(ops_proxies));

        // ----------------------------------`-----------------------------------------------

        // 检测开放代理有效性, 默认simple鉴权, 返回 Map<String, Boolean> 类型, 格式为 proxy: true/false
        Map<String, Boolean> valids = client.check_ops_valid(ops_proxies, "hmacsha1");
        System.out.println("osp valids: " + Arrays.toString(valids.entrySet().toArray()));

    }
}
