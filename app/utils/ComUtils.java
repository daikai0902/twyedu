package utils;

import cn.bran.play.JapidPlayAdapter;
import models.WeChatMember;
import org.apache.commons.lang.StringUtils;
import play.Play;
import play.mvc.Http;
import play.mvc.Scope;
import play.templates.JavaExtensions;

import java.util.Date;

/**
 * @autor kevin.dai
 * @Date 2018/3/21
 */
public class ComUtils {

    public static String formatDate(Long date,String format){
        return JavaExtensions.format(new Date(date),format);
    }

    public static String formatDate(Date date,String format){
        return JavaExtensions.format(date,format);
    }

    public static String formatNumber(Number number, String format) {
        return JavaExtensions.format(number, format);
    }




    public static String escapeURL(String url) {
        return url.replace("=", "%3D").replace("/", "%2F").replace(":", "%3A").replace("&", "%26").replace("amp;",
                "%26");
    }



    public static String getIP() {
        String ip = Http.Request.current().remoteAddress;
        if (ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }


    public static String getConfigSign(String nocestrc, long timestatm, String url) {
        if (StringUtils.isBlank(url)) {
            Http.Request request = Http.Request.current();
            url = request.getBase() + request.url;
        }

        return WeChatUtils.getConfigSign(url, nocestrc, timestatm);
    }


    /**
     * 若需要识别当前用户，必须调用此方法向微信服务器发起请求获取用户openId。
     */
    public static String getOAuthUrl(String action, Object... args) {
        String renderURL = WeChatUtils.APPLICATION_URL + JapidPlayAdapter.lookup(action, args);
        String url = WeChatUtils.OAUTH_BASEURL + "?appid=" + WeChatUtils.appId
                + "&redirect_uri=" + escapeURL(renderURL) + "&response_type=code" + "&scope=snsapi_base"
                + "&state=123#wechat_redirect";
        return url;
    }




    public static String getCurrentOpendId(String code) {
        String openId = Scope.Session.current().get("openId");
        if (StringUtils.isBlank(openId)) {
            openId = WeChatUtils.getOAuthOpenIdFromRemote(code);
            WeChatMember member = WeChatMember.findByOpenId(openId);
            if (member == null && StringUtils.isNotBlank(openId)) {
                WeChatMember.add(openId);
            }
            Scope.Session.current().put("openId", openId);
        }
        return openId;
    }




    public static String getCurrentOpendId() {
        return Scope.Session.current().get("openId");
    }






}
