package utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.cache.Cache;
import vo.AccessResult;
import vo.JSAPIResult;
import vo.OAuthResult;
import vo.OrderResult;

import java.util.*;

/**
 * 微信
 * @autor kevin.dai
 * @Date 2018/4/2
 */
public class WeChatUtils {

    public static final   String APPLICATION_URL ="http://api.twyxedu.com";

    public static  final  String appId = "wx959b4c6d0334b80c";

    public static  final  String appSecret = "1a75ca280854dfe8738d89c5027825e8";

    public static  final  String merchId = "1500074842";

    public static final String apiKey = "twyxedu88269588twyxedu88269588dk";

    public static final String notify_url = APPLICATION_URL + "/wx/payNotify";



    public static final String OAUTH_BASEURL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static final String GET_OAUTH_ACCRSS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String UNIFIE_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String GET_JS_API = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";


    /**
     * 通过OAuth接口获取当前openId
     */
    public static String getOAuthOpenIdFromRemote(String code) {
        return getOAuthResult(code).openid;
    }

    public static OAuthResult getOAuthResult(String code) {
        Map<String, String> params = new HashMap();
        params.put("grant_type", "authorization_code");
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("code", code);
        return HttpUtil.get(GET_OAUTH_ACCRSS_TOKEN, params, OAuthResult.class);
    }





    //************************支付************************


    // 签名
    public static String getNonceStr() {
        return RandomStringUtils.randomAlphabetic(32);
    }


    private static String getSign(Map<String, String> paramsMap) {
        List<String> lines = new ArrayList<String>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            lines.add(entry.getKey() + "=" + entry.getValue() + "&");
        }
        Collections.sort(lines);
        String beforeMD5 = StringUtils.join(lines, "") + "key=" + apiKey;
        return DigestUtils.md5Hex(beforeMD5).toUpperCase();
    }



    /**
     * 统一性下单
     *
     * @param
     * @return
     */
    public static OrderResult unifieOrder(String nonce_str, String body, String out_trade_no, String total_fee,
                                          String spbill_create_ip, String trade_type, String opendId) {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("appid", appId);
        paraMap.put("mch_id", merchId);
        paraMap.put("nonce_str", nonce_str);
        paraMap.put("body", "商品:" + body);
        paraMap.put("out_trade_no", out_trade_no);
        paraMap.put("total_fee", total_fee);
        paraMap.put("spbill_create_ip", spbill_create_ip);
        paraMap.put("notify_url", notify_url);
        paraMap.put("trade_type", trade_type);
        if (trade_type.equals("JSAPI")) {
            paraMap.put("openid", opendId);
        }
        paraMap.put("sign", getSign(paraMap));
        OrderResult result = HttpUtil.post(UNIFIE_ORDER, paraMap, OrderResult.class);
        return result;
    }







    public static String getAccessTokenFromRemote() {
        Map<String, String> params = new HashMap();
        params.put("grant_type", "client_credential");
        params.put("appid", appId);
        params.put("secret", appSecret);
        AccessResult result = HttpUtil.get(GET_ACCESS_TOKEN, params, AccessResult.class);
        return null == result ? "" : result.access_token;
    }

    public static String getJSAPIFromRemote(String access_token) {
        Map<String, String> params = new HashMap();
        params.put("access_token", access_token);
        params.put("type", "jsapi");
        JSAPIResult result = HttpUtil.get(GET_JS_API, params, JSAPIResult.class);
        return null == result ? "" : result.ticket;
    }



    public static String setAccessToken() {
        String access_token = getAccessTokenFromRemote();
        Cache.safeSet("access_token", access_token, "7100s");
        Logger.warn("set access_token=" + access_token);
        return access_token;
    }

    public static String getAccessToken() {
        String access_token = Cache.get("access_token", String.class);
        if (StringUtils.isBlank(access_token)) {
            return setAccessToken();
        } else {
            return access_token;
        }
    }



    public static String setJSAPI() {
        String jsapi = getJSAPIFromRemote(getAccessToken());
        Cache.safeSet("JSAPI", jsapi, "7100s");
        Logger.warn("set JSAPI=" + jsapi);
        return jsapi;
    }

    public static String getJSAPI() {
        String jsapi = Cache.get("JSAPI", String.class);
        if (StringUtils.isBlank(jsapi)) {
            return setJSAPI();
        } else {
            return jsapi;
        }
    }


    private static String sha1Encrypt(String str) {
        return DigestUtils.shaHex(str).toUpperCase();
    }


    // config签名
    public static String getConfigSign(String url, String noceStr, long timestamp) {
        StringBuffer sb = new StringBuffer();
        sb.append("jsapi_ticket=" + getJSAPI());
        sb.append("&noncestr=" + noceStr);
        sb.append("&timestamp=" + timestamp);
        sb.append("&url=" + url);
        return sha1Encrypt(sb.toString());

    }



    public static String getPaySign(String noceStr, long timestamp, String prepay_id) {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("appId", appId);
        paraMap.put("nonceStr", noceStr);
        paraMap.put("package", "prepay_id=" + prepay_id);
        paraMap.put("signType", "MD5");
        paraMap.put("timeStamp", timestamp + "");
        return getSign(paraMap);

    }
















}
