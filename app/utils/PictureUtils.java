package utils;

import com.google.gson.Gson;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.multipart.FilePart;
import com.ning.http.multipart.StringPart;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import vo.URLResult;

import java.io.File;

/**
 * @autor kevin.dai
 * @Date 2018/3/29
 */
public class PictureUtils {


    public static String PICTURE_SERVER = "http://oss.iclass.cn/form";

    public static String BUCKT_NAME = "smallfiles";
    public static String SOURCE = "SOOC";

    public static String PICTURE_SERVER_BASEURL = "http://oss.iclass.cn/smallfiles";


    public static URLResult uploadImage(File file) {
        final AsyncHttpClient client = new AsyncHttpClient();
        try {
            final Response response = client.preparePost(PICTURE_SERVER)
                    .addBodyPart(new FilePart("qqfile", file))
                    .addBodyPart(new StringPart("bucketName", BUCKT_NAME))
                    .addBodyPart(new StringPart("source", SOURCE)).execute()
                    .get();
            final String responseBody = response.getResponseBody("utf8");
            try {
                return new Gson().fromJson(responseBody, URLResult.class);
            } catch (Exception e) {
                Logger.error("[EndServer has not started.]");
            }
        } catch (Exception e) {
            Logger.error(e, e.getMessage());
        } finally {
            client.close();
        }
        return null;
    }


    public enum Effect {
        PURE {// 原图

            @Override
            public String toString() {
                return "";
            }
        },
        FILL {// 填充

            @Override
            public String toString() {
                return "f";
            }
        },
        CUT {// 裁剪

            @Override
            public String toString() {
                return "c";
            }
        };

        @Override
        abstract public String toString();
    }

    public static boolean isFromPicServer(String picUrl) {
        return StringUtils.startsWithIgnoreCase(picUrl, PICTURE_SERVER_BASEURL);
    }



    public static String getPictureFromPicServer(int width, int height,
                                                 Effect effect, String defaultUrl) {
        if (!isFromPicServer(defaultUrl)) {
            return defaultUrl;
        }
        String fileName = StringUtils.substringAfterLast(defaultUrl, "/");
        effect = effect == null ? Effect.PURE : effect;
        return PICTURE_SERVER_BASEURL + "/" + width + "_" + height
                + effect.toString() + "/" + fileName;
    }

}
