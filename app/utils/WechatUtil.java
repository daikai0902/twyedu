package utils;

import japidviews._javatags.CommonUtils;
import models.wechat.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import play.Logger;
import play.Play;
import play.cache.Cache;

import java.util.*;
import java.util.Map.Entry;

/**
 * 微信相关接口
 *
 * @author xuyuzhu
 */
public class WechatUtil {
    public static final String originId = Play.configuration.getProperty("wx.originId");
    private static final String token = Play.configuration.getProperty("wx.token");
    public static final String appleId = Play.configuration.getProperty("wx.appId");
    private static final String mchId = Play.configuration.getProperty("wx.partnerId");
    private static final String appSecret = Play.configuration.getProperty("wx.appSecret");
    private static final String apiKey = Play.configuration.getProperty("wx.apiKey");
    private static final String partnerKey = Play.configuration.getProperty("wx.partnerKey");
    public static final String APPLICATION_URL = Play.configuration.getProperty("application.baseUrl");
    public static final String notify_url = APPLICATION_URL + "/wcapi/payNotify";

    public static final String OAUTH_BASEURL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static final String GET_OAUTH_ACCRSS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String GET_JS_API = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    private static final String GET_PERSON_OPENIDs = "https://api.weixin.qq.com/cgi-bin/user/get";
    private static final String GET_PERSON_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";
    private static final String GET_ONLINE_KF_LIST = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist";
    private static final String GET_KF_LIST = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist";
    private static final String GET_SESSION = "https://api.weixin.qq.com/customservice/kfsession/getsession";

    // 用户基本信息，无需关注公众号
    private static final String GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    // private static final String GET_ORDER_INFO =
    // "https://api.weixin.qq.com/pay/orderquery";
    private static final String UNIFIE_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String SET_MENUS = "https://api.weixin.qq.com/cgi-bin/menu/create";
    private static final String DELIVER_ORDER = "https://api.weixin.qq.com/pay/delivernotify";
    private static final String UPDATE_FEEDBACK = "https://api.weixin.qq.com/payfeedback/update";
    // 聊天接口
    private static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
    private static final String SEND_MESSAGE_KF = "http://panying.iclass.cn/checksignature";
    private static final String UPLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/upload";
    private static final String DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get";

    // 模板消息接口
    private static final String SEND_TEPLET_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    public static final String REGIST_TEMPLATE_ID = Play.configuration.getProperty("rgist.templet");// 报名成功的模板ID
    public static final String BUY_TEMPLATE_ID = Play.configuration.getProperty("buy.templet");// 购买成功的模板ID
    public static final String ACTIVE_SIGN_ID = Play.configuration.getProperty("active.templet");// 活动签到成功通知的模板ID

    private static final String TRANSFER = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";// 企业转账

    public static Log wcLogger = LogFactory.getLog("wcapi");

    /**
     * 签名认证
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {

        wcLogger.debug("signature=" + StringUtils.defaultIfBlank(signature, "") + ", timestamp="
                + StringUtils.defaultIfBlank(timestamp, "") + ", nonce=" + StringUtils.defaultIfBlank(nonce, ""));
        try {
            if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
                return false;
            } else {
                String[] array = new String[]{token, timestamp, nonce};
                Arrays.sort(array);
                return DigestUtils.shaHex(StringUtils.join(array)).equals(signature);
            }
        } catch (Exception e) {
            wcLogger.error("checkSignature error.");
        }
        return false;
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

    /**
     * 获取凭证(如果为空，则验证失败)
     */
    public static String getAccessTokenFromRemote() {
        Map<String, String> params = new HashMap();
        params.put("grant_type", "client_credential");
        params.put("appid", appleId);
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

    private static DatasOfWePerson getWePersonsFromRemote(String next_openid) {
        Map<String, String> params = new HashMap();
        params.put("access_token", getAccessToken());
        params.put("next_openid", next_openid);
        return HttpUtil.get(GET_PERSON_OPENIDs, params, DatasOfWePerson.class);
    }

    /**
     * 通过OAuth接口获取当前openId
     */
    public static String getOAuthOpenIdFromRemote(String code) {
        return getOAuthResult(code).openid;
    }

    public static OAuthResult getOAuthResult(String code) {
        Map<String, String> params = new HashMap();
        params.put("grant_type", "authorization_code");
        params.put("appid", appleId);
        params.put("secret", appSecret);
        params.put("code", code);
        return HttpUtil.get(GET_OAUTH_ACCRSS_TOKEN, params, OAuthResult.class);
    }

    /**
     * 获取所有关注者 OPENID 列表
     */
    public static List<String> getAllOpenIdsFromRemote() {
        List<String> openIdList = new ArrayList();
        DatasOfWePerson data = getWePersonsFromRemote("");
        openIdList.addAll(data.data);
        String next_openid = data.next_openid;
        while (StringUtils.isNotBlank(next_openid)) {
            data = getWePersonsFromRemote(next_openid);
            openIdList.addAll(data.data);
            next_openid = data.next_openid;
        }
        return openIdList;
    }

    /**
     * 通过openId获取用户信息 必须信任公众号才能获取 add by 戴凯
     */
    public static WeChatPerson getWePersonByOpenId(String openId) {
        Map<String, String> params = new HashMap();
        params.put("access_token", getAccessToken());
        params.put("openid", openId);
        params.put("lang", "zh_CN");// 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
        Logger.warn("微信服务器返回的access_token:" + getAccessToken() + "openid" + openId);
        return HttpUtil.get(GET_PERSON_INFO, params, WeChatPerson.class);
    }

    public static WeChatPerson getUserInfoByOpenId(String openId, String access_token) {
        Map<String, String> params = new HashMap();
        params.put("access_token", access_token);
        params.put("openid", openId);
        params.put("lang", "zh_CN");// 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
        return HttpUtil.get(GET_USER_INFO, params, WeChatPerson.class);
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
        paraMap.put("appid", appleId);
        paraMap.put("mch_id", mchId);
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


    // 签名

    public static String getNonceStr() {
        return RandomStringUtils.randomAlphabetic(32);
    }

    // config签名
    public static String getConfigSign(String url, String noceStr, long timestamp) {
        StringBuffer sb = new StringBuffer();
        sb.append("jsapi_ticket=" + CommonUtils.getJSAPI());
        sb.append("&noncestr=" + noceStr);
        sb.append("&timestamp=" + timestamp);
        sb.append("&url=" + url);
        return sha1Encrypt(sb.toString());

    }

    public static String getPaySign(String noceStr, long timestamp, String prepay_id) {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("appId", appleId);
        paraMap.put("nonceStr", noceStr);
        paraMap.put("package", "prepay_id=" + prepay_id);
        paraMap.put("signType", "MD5");
        paraMap.put("timeStamp", timestamp + "");
        return getSign(paraMap);

    }

    private static String getSign(Map<String, String> paramsMap) {
        List<String> lines = new ArrayList<String>();
        for (Entry<String, String> entry : paramsMap.entrySet()) {
            lines.add(entry.getKey() + "=" + entry.getValue() + "&");
        }
        Collections.sort(lines);
        String beforeMD5 = StringUtils.join(lines, "") + "key=" + apiKey;
        return DigestUtils.md5Hex(beforeMD5).toUpperCase();
    }

    private static String sha1Encrypt(String str) {
        return DigestUtils.shaHex(str).toUpperCase();
    }




}
