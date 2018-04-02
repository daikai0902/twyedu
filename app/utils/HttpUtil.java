package utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HttpUtil {

    protected static final Gson gs = new Gson();

    public static <T> T post(String method, String URL,
                             Map<String, String> params, String postData, String contentType,
                             Class<T> jsonClass) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestBuilder rb = new RequestBuilder(method).setUrl(URL);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            rb.addQueryParameter(entry.getKey(), entry.getValue());
        }
        if (StringUtils.isNotBlank(contentType)) {
            rb.setHeader("Content-Type", contentType);
        }
        if (StringUtils.isNotBlank(postData)) {
            rb.setBody(postData);
        }
        rb.setBodyEncoding("UTF-8");
        Request request = rb.build();
        try {
            Response response = asyncHttpClient.executeRequest(request).get();
            String body = response.getResponseBody("UTF-8");
            Logger.info("-----api url=" + request.getUrl() + ", response body="
                    + body);
            if (response.getStatusCode() == 200) {
                try {
                    T result = gs.fromJson(body, jsonClass);
                    return result;
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.error("接口服务器返回值出错，错误码：" + response.getStatusCode()
                        + "，返回信息：" + body);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncHttpClient.close();
        }
        return null;
    }

    public static String getMethod(String method, String URL,
                                   Map<String, String> params) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestBuilder rb = new RequestBuilder(method).setUrl(URL);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            rb.addQueryParameter(entry.getKey(), entry.getValue());
        }
        rb.setBodyEncoding("UTF-8");
        Request request = rb.build();
        try {
            Response response = asyncHttpClient.executeRequest(request).get();
            String body = response.getResponseBody("UTF-8");
            Logger.info("-----api url=" + request.getUrl() + ", response body="
                    + body);
            if (response.getStatusCode() == 200) {
                try {
                    return body;
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.error("接口服务器返回值出错，错误码：" + response.getStatusCode()
                        + "，返回信息：" + body);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncHttpClient.close();
        }
        return null;
    }

    public static <T> T postXML(String method, String URL, String paramsXML,
                                String postData, String contentType, Class<T> jsonClass) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestBuilder rb = new RequestBuilder(method).setUrl(URL);
        if (StringUtils.isNotBlank(contentType)) {
            rb.setHeader("Content-Type", contentType);
        }
        if (StringUtils.isNotBlank(postData)) {
            rb.setBody(postData);
        }
        rb.setBodyEncoding("UTF-8");
        rb.setBody(paramsXML);
        Request request = rb.build();
        try {
            Response response = asyncHttpClient.executeRequest(request).get();
            String body = response.getResponseBody("UTF-8");
            Logger.info("response body=" + XMLUtil.parseXML(body).toString());
            if (response.getStatusCode() == 200) {
                try {
                    T result = gs.fromJson(gs.toJson(XMLUtil.parseXML(body)),
                            jsonClass);// 有特殊字符的存在导致转换失败，暂时先转换成json 再转换
                    return result;
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.error("接口服务器返回值出错，错误码：" + response.getStatusCode()
                        + "，返回信息：" + body);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncHttpClient.close();
        }
        return null;
    }

    public static String post(String method, String URL,
                              Map<String, String> params) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestBuilder rb = new RequestBuilder(method).setUrl(URL);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            rb.addQueryParameter(entry.getKey(), entry.getValue());
        }
        rb.setBodyEncoding("UTF-8");
        Request request = rb.build();
        try {
            Response response = asyncHttpClient.executeRequest(request).get();
            String body = response.getResponseBody("UTF-8");
            Logger.error("-----api url=" + request.getUrl()
                    + ", response body=" + body);
            if (response.getStatusCode() == 200) {
                try {
                    return body;
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.error("接口服务器返回值出错，错误码：" + response.getStatusCode()
                        + "，返回信息：" + body);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            asyncHttpClient.close();
        }
        return null;
    }

    public static <T> T get(String URL, Map<String, String> params,
                            Class<T> jsonClass) {
        return post("GET", URL, params, "", "", jsonClass);
    }

    public static <T> T post(String URL, Map<String, String> params,
                             Class<T> jsonClass) {
        return postXML("POST", URL, XMLUtil.convert2XML(params).toString(), "",
                " text/xml; charset = utf-8", jsonClass);
    }



    public static File downloadMedia(String URL, Map<String, String> params,
                                     String postData, String contentType) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestBuilder rb = new RequestBuilder("GET").setUrl(URL);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            rb.addQueryParameter(entry.getKey(), entry.getValue());
        }
        if (StringUtils.isNotBlank(contentType)) {
            rb.setHeader("Content-Type", contentType);
        }
        if (StringUtils.isNotBlank(postData)) {
            rb.setBody(postData);
        }
        rb.setBodyEncoding("UTF-8");
        Request request = rb.build();
        InputStream stream = null;
        OutputStream os = null;
        try {
            Response response = asyncHttpClient.executeRequest(request).get();
            stream = response.getResponseBodyAsStream();
            if (response.getStatusCode() == 200) {
                String dispoString = response.getHeaders("Content-disposition")
                        .toString();
                int iFindStart = dispoString.indexOf("filename=\"") + 10;
                int iFindEnd = dispoString.indexOf("\"", iFindStart);
                File file = Play.getFile("tmp/"
                        + dispoString.substring(iFindStart, iFindEnd));
                os = new FileOutputStream(file);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                return file;
            } else {
                Logger.error("------downloadMedia------请求失败 ");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }

            asyncHttpClient.close();
        }
        return null;
    }


}
