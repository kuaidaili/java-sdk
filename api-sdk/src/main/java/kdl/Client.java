package kdl;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.net.URI;

import java.util.*;
import java.util.regex.Pattern;

import javax.xml.ws.Endpoint;

import kdl.enums.EndPoint;
import kdl.enums.OpsOrderLevel;

import kdl.exceptions.KdlException;
import kdl.exceptions.KdlNameError;
import kdl.exceptions.KdlStatusError;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * client 封装所有api接口
 */
public class Client {
    private Auth auth;

    public Client(Auth auth) {
        this.auth = auth;
    }

    public String get_order_expire_time() throws Exception {
        return get_order_expire_time("simple");
    }

    /**
     * 获取订单到期时间, 强制签名验证
     *
     * @param sign_type 签名方式： "simple" or "hmacsha1"
     * @return 订单过期时间字符串
     * @throws Exception
     */
    public String get_order_expire_time(String sign_type) throws Exception {
        String endpoint = EndPoint.GetOrderExpireTime.getValue();
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("sign_type", sign_type);
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        String[] res = this._get_base_res("GET", endpoint, params);
        if (res[1].equals("json")) {
            JSONObject data = new JSONObject(res[0]).getJSONObject("data");
            return data.getString("expire_time");
        }
        return res[0];
    }


    public String[] get_ip_whitelist() throws Exception {
        return get_ip_whitelist("simple");
    }

    /**
     * 获取ip白名单, 强制签名验证
     *
     * @param sign_type "simple" or "hmacsha1"
     * @return String[] ip白名单数组
     * @throws Exception
     */
    public String[] get_ip_whitelist(String sign_type) throws Exception {
        String endpoint = EndPoint.GetIpWhitelist.getValue();
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("sign_type", sign_type);
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        String[] res = this._get_base_res("GET", endpoint, params);
        if (res[1].equals("json")) {
            JSONObject data = new JSONObject(res[0]).getJSONObject("data");
            JSONArray arr = data.getJSONArray("ipwhitelist");
            String[] ip_whitelist = new String[arr.length()];
            for (int i=0; i<arr.length(); i++) {
                ip_whitelist[i] = arr.getString(i);
            }
            return ip_whitelist;
        }
        return new String[]{};
    }

    public void set_ip_whitelist(String iplist) throws Exception {
        set_ip_whitelist(iplist, "simple");
    }

    public void set_ip_whitelist(String[] iplist) throws Exception {
        if (iplist.length == 0) {
            set_ip_whitelist("", "simple");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip: iplist) {
            stringBuilder.append(ip).append(",");
        }
        set_ip_whitelist(stringBuilder.toString().substring(0, stringBuilder.length()-1), "simple");
    }

    public void set_ip_whitelist(String[] iplist, String sign_type) throws Exception {
        if (iplist.length == 0) {
            set_ip_whitelist("", sign_type);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip: iplist) {
            stringBuilder.append(ip).append(",");
        }
        set_ip_whitelist(stringBuilder.toString().substring(0, stringBuilder.length()-1), sign_type);
    }

    /**
     * 设置ip白名单，无返回数据, 强制签名验证
     *
     * @param iplist  ip字符串, 逗号隔开
     * @param sign_type "simple" or "hmacsha1"
     * @throws Exception
     */
    public void set_ip_whitelist(String iplist, String sign_type) throws Exception{
        String endpoint = EndPoint.SetIpWhitelist.getValue();
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("iplist", iplist);
        kwargs.put("sign_type", sign_type);
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        this._get_base_res("POST", endpoint, params);
    }

    public String[] get_dps(int num) throws Exception {
        return get_dps(num, new HashMap<String, Object>());
    }

    /**
     * 提取私密代理, 默认不需要鉴权
     *
     * @param num 提取数量
     * @param kwargs 其他参数, 键值对形式存入Map<String, Object>中
     * @return String[] proxy数组
     * @throws Exception
     */
    public String[] get_dps(int num, Map<String, Object> kwargs) throws Exception {
        String endpoint = EndPoint.GetDpsProxy.getValue();
        kwargs.put("num", num);
        return this._get_proxy_core(endpoint, kwargs);
    }

    public Map<String, Boolean> check_dps_valid(String proxy) throws Exception {
        return check_dps_valid(proxy, "simple");
    }

    public Map<String, Boolean> check_dps_valid(String[] proxy) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip: proxy) {
            stringBuilder.append(ip).append(",");
        }
        return check_dps_valid(stringBuilder.toString().substring(0, stringBuilder.length()-1), "simple");
    }

    public Map<String, Boolean> check_dps_valid(String[] proxy, String sign_type) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip: proxy) {
            stringBuilder.append(ip).append(",");
        }
        return check_dps_valid(stringBuilder.toString().substring(0, stringBuilder.length()-1), sign_type);
    }

    /**
     * 检测私密代理有效性, 强制签名验证
     *
     * @param proxy ip字符串，逗号隔开
     * @param sign_type "simple" or "hmacsha1"
     * @return Map<String, Boolean> proxy: true/false
     * @throws Exception
     */
    public Map<String, Boolean> check_dps_valid(String proxy, String sign_type) throws Exception {
        String endpoint = EndPoint.CheckDpsValid.getValue();
        return this._check_proxy_core(endpoint, proxy, sign_type);
    }

    public int get_ip_balance() throws Exception {
        return get_ip_balance("simple");
    }

    /**
     * 私密代理计数版：获取订单的ip余额, 强制签名验证
     *
     * @param sign_type "simple" or "hmacsha1"
     * @return int ip余额
     * @throws Exception
     */
    public int get_ip_balance(String sign_type) throws Exception {
        String endpoint = EndPoint.GetIpBalance.getValue();
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("sign_type", sign_type);
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        String[] res = this._get_base_res("GET", endpoint, params);
        if (res[1].equals("json")) {
            JSONObject data = new JSONObject(res[0]).getJSONObject("data");
            return data.getInt("balance");
        }
        return -1;
    }

    private String[] _get_proxy_core(String endpoint, Map<String, Object> kwargs) throws Exception {
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        String[] res = this._get_base_res("GET", endpoint, params);
        if (res[1].equals("json")) {
            JSONObject data = new JSONObject(res[0]).getJSONObject("data");
            JSONArray arr = data.getJSONArray("proxy_list");
            String[] proxies = new String[arr.length()];
            for (int i=0; i<arr.length(); i++) {
                proxies[i] = arr.getString(i);
            }
            return proxies;
        } else {
            String format = "text";
            if (kwargs.containsKey("format")) {
                format = kwargs.get("format").toString();
            }
            if (!format.equals("xml")) {
                String sep = "\n";
                if (kwargs.containsKey("sep")) {
                    sep = kwargs.get("sep").toString();
                }
                return res[0].split(Pattern.quote(sep));
            }
            return new String[]{res[0]};
        }
    }

    private Map<String, Boolean> _check_proxy_core(String endpoint, String proxy, String sign_type) throws Exception {
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("proxy", proxy);
        kwargs.put("sign_type", sign_type);
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        String[] res = this._get_base_res("GET", endpoint, params);
        if (res[1].equals("json")) {
            JSONObject data = new JSONObject(res[0]).getJSONObject("data");
            Set<String> keys = data.keySet();
            Map<String, Boolean> valids = new HashMap<String, Boolean>();
            for (String key: keys) {
                valids.put(key, data.getBoolean(key));
            }
            return valids;
        }
        return new HashMap<String, Boolean>();
    }

    public String[] get_kps(int num) throws Exception {
        return get_kps(num, new HashMap<String, Object>());
    }

    /**
     * 提取独享代理, 默认不需要鉴权
     *
     * @param num 提取数量
     * @param kwargs 其他参数
     * @return String[] 代理数组
     * @throws Exception
     */
    public String[] get_kps(int num, Map<String, Object> kwargs) throws Exception {
        String endpoint = EndPoint.GetKpsProxy.getValue();
        kwargs.put("num", num);
        return this._get_proxy_core(endpoint, kwargs);
    }

    public String[] get_proxy(int num) throws Exception {
        return get_proxy(num, OpsOrderLevel.NORMAL.getValue(), new HashMap<String, Object>());
    }

    public String[] get_proxy(int num, Map<String, Object> kwargs) throws Exception {
        return get_proxy(num, OpsOrderLevel.NORMAL.getValue(), kwargs);
    }

    public String[] get_proxy(int num, String order_level) throws Exception {
        return get_proxy(num, order_level, new HashMap<String, Object>());
    }

    /**
     * 提取开放代理, 默认不需要鉴权
     *
     * @param num 提取数量
     * @param order_level 订单级别, 默认是dev, 如果是svip订单，请传入"svip", 如果是ent订单, 请传入"ent"
     * @param kwargs 其他参数
     * @return String[] 代理数组
     * @throws Exception
     */
    public String[] get_proxy(int num, String order_level, Map<String, Object> kwargs) throws Exception {
        String endpoint = EndPoint.GetOpsProxyNormalOrVip.getValue();
        if (order_level.equals(OpsOrderLevel.SVIP.getValue())) {
            endpoint = EndPoint.GetOpsProxySvip.getValue();
        } else if (order_level.equals(OpsOrderLevel.PRO.getValue())) {
            endpoint = EndPoint.GetOpsProxyEnt.getValue();
        }
        kwargs.put("num", num);
        return this._get_proxy_core(endpoint, kwargs);
    }

    public Map<String, Boolean> check_ops_valid(String proxy) throws Exception {
        return check_ops_valid(proxy, "simple");
    }

    public Map<String, Boolean> check_ops_valid(String[] proxy) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for(String ip: proxy) {
            stringBuilder.append(ip).append(",");
        }
        return check_ops_valid(stringBuilder.toString().substring(0, stringBuilder.length()-1), "simple");
    }

    public Map<String, Boolean> check_ops_valid(String[] proxy, String sign_type) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for(String ip: proxy) {
            stringBuilder.append(ip).append(",");
        }
        return check_ops_valid(stringBuilder.substring(0, stringBuilder.length()-1), sign_type);
    }

    /**
     * 检测开放代理有效性, 强制鉴权
     *
     * @param proxy 代理字符串, 逗号隔开
     * @param sign_type "simple"
     * @return Map<String, Boolean> 格式为: proxy: true/false
     * @throws Exception
     */
    public Map<String, Boolean> check_ops_valid(String proxy, String sign_type) throws Exception {
        String endpoint = EndPoint.CheckOpsValid.getValue();
        return this._check_proxy_core(endpoint, proxy, sign_type);
    }

    public Map<String, Integer> get_dps_valid_time(String proxy) throws Exception {
        return get_dps_valid_time(proxy, "simple");
    }

    public Map<String, Integer> get_dps_valid_time(String[] proxy) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for(String ip: proxy) {
            stringBuilder.append(ip).append(",");
        }
        return get_dps_valid_time(stringBuilder.substring(0, stringBuilder.length()-1), "simple");
    }

    public Map<String, Integer> get_dps_valid_time(String[] proxy, String sign_type) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for(String ip: proxy) {
            stringBuilder.append(ip).append(",");
        }
        return get_dps_valid_time(stringBuilder.substring(0, stringBuilder.length()-1), sign_type);
    }

    /**
     * 检测私密代理ip有效时间
     *
     * @param proxy 代理字符串, 逗号隔开
     * @param sign_type "simple"
     * @return Map<String, Integer> 格式为: proxy: seconds(秒数)
     * @throws Exception
     */
    public Map<String, Integer> get_dps_valid_time(String proxy, String sign_type) throws Exception {
        String endpoint = EndPoint.GetDpsValidTime.getValue();
        Map<String, Object> kwargs = new HashMap<String, Object>();
        kwargs.put("proxy", proxy);
        kwargs.put("sign_type", sign_type);
        Map<String, Object> params = this._get_params(endpoint, kwargs);
        String[] res = this._get_base_res("GET", endpoint, params);
        if (res[1].equals("json")) {
            JSONObject data = new JSONObject(res[0]).getJSONObject("data");
            Set<String> keys = data.keySet();
            Map<String, Integer> valids = new HashMap<String, Integer>();
            for (String key: keys) {
                valids.put(key, data.getInt(key));
            }
            return valids;
        }
        return new HashMap<String, Integer>();
    }


    /**
     * 构造请求参数
     *
     * @param endpoint 请求主机和路径
     * @param kwargs   请求参数, Map<String, Object>类型
     * @return Map<String, Object> 参数Map
     * @throws Exception
     */
    private Map<String, Object> _get_params(String endpoint, Map<String, Object> kwargs) throws Exception {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("orderid", this.auth.getOrderId());
        for (String k : kwargs.keySet()) {
            params.put(k, kwargs.get(k).toString());
        }

        String sign_type = null;
        if (kwargs.containsKey("sign_type")) {
            sign_type = kwargs.get("sign_type").toString();
        } else {
            sign_type = "simple";
            params.put("sign_type", sign_type);
        }

        if (sign_type.equals("simple")) {
            params.put("signature", this.auth.getApiKey());
        } else if (sign_type.equals("hmacsha1")) {
            params.put("timestamp", System.currentTimeMillis() / 1000);
            String raw_str = null;
            if (endpoint.equals(EndPoint.SetIpWhitelist.getValue())) {
                raw_str = this.auth.getStringToSign("POST", endpoint, params);
            } else {
                raw_str = this.auth.getStringToSign("GET", endpoint, params);
            }
            params.put("signature", this.auth.sign(raw_str));
        } else {
            throw new KdlNameError(String.format("unknown sign_type %s", sign_type));
        }

        return params;
    }

    /**
     * 处理基础请求
     *
     * @param method  请求方法："GET" or "POST"
     * @param endpoint 请求主机+地址
     * @param params 请求参数Map
     * @return String[2], 0: 响应字符串, 1: dataType "json" or "raw"
     * @throws Exception
     */
    private String[] _get_base_res(String method, String endpoint, Map<String, Object> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        if (method.equals("GET")) {
            URIBuilder uriBuilder = new URIBuilder("https://" + endpoint);
            for (String k : params.keySet()) {
                uriBuilder.addParameter(k, params.get(k).toString());
            }
            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            response = httpClient.execute(httpGet);

        } else if (method.equals("POST")) {
            HttpPost httpPost = new HttpPost("https://" + endpoint);
            List<NameValuePair> kvparams = new ArrayList<NameValuePair>();
            for (String k : params.keySet()) {
                kvparams.add(new BasicNameValuePair(k, params.get(k).toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(kvparams, "UTF-8"));
            response = httpClient.execute(httpPost);

        } else {
            throw new KdlNameError(String.format("invalid method: %s", method));
        }

        int statusCode = response.getStatusLine().getStatusCode();
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        if (statusCode != HttpStatus.SC_OK) {
            throw new KdlStatusError(statusCode, content);
        }

        String dataType = "json";
        try {
            JSONObject jsonObject = new JSONObject(content);
            int code = jsonObject.getInt("code");
            if (code != 0) {
                throw new KdlException(code, jsonObject.getString("msg"));
            }
        } catch(JSONException e) {
            if (content.trim().startsWith("ERROR")) {
                throw new KdlException(-3, content);
            }
            dataType = "raw";
        }
        return new String[]{content, dataType};
    }

}
