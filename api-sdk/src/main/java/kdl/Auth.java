package kdl;

import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * 用于保存用户secret_id, secret_key, 以及计算签名的对象。
 */
public class Auth {
    private final static String CHARSET = "UTF-8";

    private final String secret_id;
    private final String secret_key;

    public Auth(String secret_id, String secret_key) {
        this.secret_id = secret_id;
        this.secret_key = secret_key;
    }

    public String sign(String s) throws Exception {
        return sign(s, "HmacSHA1");
    }

    /**
     * 生成原字符串
     *
     * @param method  请求方式
     * @param s  已经拼接好的原字符串
     * @return Hmacsha1加密，并base64编码之后的原字符串。
     * @throws Exception
     */
    public String sign(String s, String method) throws Exception {
        Mac mac = Mac.getInstance(method);
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.secret_key.getBytes(CHARSET), mac.getAlgorithm());
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(s.getBytes(CHARSET));
        return DatatypeConverter.printBase64Binary(hash);
    }

    /**
     * 生成原字符串
     *
     * @param endpoint 访问的地址
     * @param method  请求方式
     * @param params  其他参数
     * @return 返回拼接好的原字符串
     * @throws Exception
     */
    public  String getStringToSign(String method, String endpoint, TreeMap<String, Object> params) {
        String s = method + endpoint.split(".com")[1] + "?";
        StringBuilder s2s = new StringBuilder();
        // 签名时要求对参数进行字典排序，此处用TreeMap保证顺序
        for (String k : params.keySet()) {
            s2s.append(k).append("=").append(params.get(k).toString()).append("&");
        }
        return s + s2s.toString().substring(0, s2s.length() - 1);
    }

    public String getSecretId() {
        return this.secret_id;
    }

    public String getSecretKey() {
        return this.secret_key;
    }

}
