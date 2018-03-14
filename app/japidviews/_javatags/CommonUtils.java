package japidviews._javatags;

import cn.bran.play.JapidPlayAdapter;
import org.apache.commons.lang.StringUtils;
import play.Play;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Scope.Session;
import play.templates.JavaExtensions;
import utils.WechatUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */

/**
 * @author mayan
 *
 */
public class CommonUtils {
    private static final String LOGGED_PERSON_ID = "loggedPersonId";
    private static final String KEEP_LOGIN_ID = "KEEP_LOGIN_ID";

    private static final String PLAT_GROUP_ID = Play.configuration.getProperty("platGroupId");

    public static void savePersonToSession(Long personId) {
        if (personId != null) {
            Session.current().put(LOGGED_PERSON_ID, personId);
        } else {
            Session.current().remove(LOGGED_PERSON_ID);
        }
    }

//    public static WePerson currentPerson() {
//        String personId = currentPersonId();
//        if (StringUtils.isNotBlank(personId)) {
//            return WePerson.findById(Long.parseLong(personId));
//        } else {
//            return null;
//        }
//    }

    public static String getDataFromCookie(String arg) {
        Map<String, Cookie> cookies = Request.current().cookies;
        Cookie cookie = cookies.get(arg);
        if (null != cookie) {
            return cookie.value;
        }
        return null;
    }


    public static String currentPersonId() {
        String personId = Session.current().get(LOGGED_PERSON_ID);
        if (personId == null) {
            personId = getDataFromCookie(KEEP_LOGIN_ID);
        }
        return personId;
    }





    public static String formatDate(Date date, String format) {
        return JavaExtensions.format(date, format);
    }

    public static String formatDate(Long date, String format) {
        return JavaExtensions.format(new Date(date), format);
    }

    public static Date getDate(String dateStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatNumber(Number number, String format) {
        return JavaExtensions.format(number, format);
    }




    public static String dealWithLinkAmp(String url) {
        return url.replaceAll("&amp;", "&");
    }

    public static String escape_Html(String html) {
        if (html == null)
            return "";
        html = html.replace("'", "&apos;");
        html = html.replaceAll("&", "&amp;");
        html = html.replace("\"", "&quot;"); // "
        html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
        html = html.replace(" ", "&nbsp;");// 替换空格
        html = html.replace("<", "&lt;");
        html = html.replaceAll(">", "&gt;");
        return html;
    }

    /*-----------------------------------微信使用的接口-----------------------------------------------------------*/
    public static String getJSAPI() {
        return WechatUtil.getJSAPI();
    }

    public static String getIP() {
        String ip = Request.current().remoteAddress;
        if (ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static boolean escapeEmoji(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (int i = 0; i < value.length(); i++) {
                if (value.codePointAt(i) >= 127744) {
                    i++;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static String escapeURL(String url) {
        return url.replace("=", "%3D").replace("/", "%2F").replace(":", "%3A").replace("&", "%26").replace("amp;",
                "%26");
    }

    public static String getConfigSign(String nocestrc, long timestatm, String url) {
        if (StringUtils.isBlank(url)) {
            Request request = Request.current();
            url = request.getBase() + request.url;
        }

        return WechatUtil.getConfigSign(url, nocestrc, timestatm);
    }

//    public static String getCurrentOpendId(String code) {
//        String openId = Session.current().get("openId");
//        if (StringUtils.isBlank(openId)) {
//            openId = WechatUtil.getOAuthOpenIdFromRemote(code);
//            WeChatMember member = WeChatMember.findByOpenId(openId);
//            if (member == null && StringUtils.isNotBlank(openId)) {
//                WeChatPerson chatPerson = WechatUtil.getUserInfoByOpenId(openId,
//                        WechatUtil.getOAuthResult(code).access_token);
//                if (chatPerson != null) {
//                    member = WeChatMember.registerFromWechat(chatPerson);
//                }
//            }
//            Session.current().put("openId", openId);
//        }
//        return openId;
//    }

    public static String getCurrentOpendId() {
        return Session.current().get("openId");
    }

    /**
     * 若需要识别当前用户，必须调用此方法向微信服务器发起请求获取用户openId。
     */
    public static String getOAuthUrl(String action, Object... args) {
        String renderURL = WechatUtil.APPLICATION_URL + JapidPlayAdapter.lookup(action, args);
        String url = WechatUtil.OAUTH_BASEURL + "?appid=" + Play.configuration.getProperty("wx.appId")
                + "&redirect_uri=" + escapeURL(renderURL) + "&response_type=code" + "&scope=snsapi_base"
                + "&state=123#wechat_redirect";
        return url;
    }

    /**
     * 授权登陆,获取用户信息
     */
    public static String getOAuthUserInfoUrl(String action, Object... args) {
        String renderURL = WechatUtil.APPLICATION_URL + JapidPlayAdapter.lookup(action, args);
        String url = WechatUtil.OAUTH_BASEURL + "?appid=" + Play.configuration.getProperty("wx.appId")
                + "&redirect_uri=" + escapeURL(renderURL) + "&response_type=code" + "&scope=snsapi_userinfo"
                + "&state=123#wechat_redirect";
        return url;
    }
//
//    public static WeChatMember getCurrentMember(String code) {
//        String openId = Session.current().get("openId");
//        OAuthResult oauth = WechatUtil.getOAuthResult(code);
//        if (StringUtils.isBlank(openId)) {
//            /*Session.current().put("openId", oauth.openid);
//			openId = Session.current().get("openId");*/
//            Http.Response.current().setCookie("openId", oauth.openid, "365d");
//            openId = oauth.openid;
//        }
//
//        WeChatMember currMember = WeChatMember.findByOpenId(openId);
//        if (null == currMember) {
//            WeChatPerson chatPerson = WechatUtil.getUserInfoByOpenId(openId, oauth.access_token);
//            if (chatPerson != null) {
//                currMember = WeChatMember.registerFromWechat(chatPerson);
//            }
//        } else {
//            if (StringUtils.isNotBlank(oauth.access_token)) {
//                currMember.updateMemberInfo(oauth.access_token);
//            }
//
//        }
//        return currMember;
//    }
//
//    public static WeChatMember getCurrentMember() {
//		/*String openId = Session.current().get("openId");*/
//        String openId = getDataFromCookie("openId");
//        WeChatMember currMember = WeChatMember.findByOpenId(openId);
//        if (null != currMember) {
//            WePerson person = currentPerson();
//            if (person != null && person.wMember == null) {
//                person.bindWeChatMember(currMember);
//            }
//
//        } else {
//            return null;
//            // currMember = WeChatMember.registerByOpenId(openId);
//        }
//        return currMember;
//    }

    public static String getPaySign(String nocestrc, long timestatm, String prepay_id) {
        return WechatUtil.getPaySign(nocestrc, timestatm, prepay_id);
    }


}
