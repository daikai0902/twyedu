package controllers;

import cn.bran.play.JapidController;
import models.WePerson;
import models.member.AccessToken;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.mvc.Http;
import play.mvc.Util;
import vo.Result;


/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class BaseController extends JapidController {



    static void accessToken() {
        Logger.info("================================");
        Logger.info("[accessToken start] time:%d", System.currentTimeMillis());
        Logger.info("[accessToken] action:%s", Http.Request.current().action);
        final String accessToken = getToken();
        Logger.info("[accessToken] token:%s", accessToken);
        Logger.info("[accessToken end] time:%d", System.currentTimeMillis());
        if (StringUtils.isBlank(accessToken) || AccessToken.findByAccessToken(accessToken) == null) {
            renderJSON(Result.failed(Result.StatusCode.SYSTEM_TOKEN_UNVALID));
        }
        Logger.info("================================");
    }

    @Util
    protected static String getToken() {
        final Http.Header accessToken = Http.Request.current().headers.get("accesstoken");
        if (accessToken == null || StringUtils.isBlank(accessToken.value())) {
            return null;
        } else {
            return accessToken.value();
        }
    }

    @Util
    protected static WePerson getPersonByToken() {
        String token = getToken();
        return token == null ? null : AccessToken.findPersonByAccessToken(token);
    }

    @Util
    protected static AccessToken getAccessTokenByToken() {
        String token = getToken();
        return token == null ? null : AccessToken.findByAccessToken(token);
    }
}
