package vo;

import utils.ComUtils;
import utils.WeChatUtils;

public class OrderResult extends OneData{

    public String return_code;// SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    public String return_msg;// 返回信息，如非空，为错误原因,签名失败,参数格式校验错误
    // 以下字段在return_code为SUCCESS的时候有返回
    public String appid;
    public String mch_id;
    public String device_info;
    public String nonce_str;
    public String sign;
    public String result_code;
    public String err_code;
    public String err_code_des;
    // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
    public String trade_type;
    public String prepay_id;// 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
    public String code_url;// trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付

    // 下面字段不是微信返回值
    public String noceStr;
    public long timestatm;

    public String configSign;// JSAPI支付，通过config接口注入权限验证配置，安全起见 在服务器端处理
    public String paySign;

    public long orderSumId;
    public String sumPrice;
    public String time;
    public String orderSerial;


    public static OrderResult unifeOrder(String goodsName, Integer price,String tradeNum,
                                         String currentURL, String trade_type,String openId) {
        String noceStr = WeChatUtils.getNonceStr();
        long timestatm = System.currentTimeMillis() / 1000;
        OrderResult result = WeChatUtils.unifieOrder(noceStr, goodsName, tradeNum, price + "", ComUtils.getIP(),trade_type, openId);
        if (result != null && result.return_code.equals("SUCCESS")
                && result.result_code.equals("SUCCESS")) {
            result.noceStr = noceStr;
        } else if (result != null && result.return_code.equals("SUCCESS")
                && result.err_code.equals("OUT_TRADE_NO_USED")) {// 订单号重复
            return null;
        }
        result.sumPrice = ComUtils
                .formatNumber(price / 100.0, "0.00");
        result.timestatm = timestatm;
        result.orderSerial = tradeNum;
        currentURL = currentURL.split("#")[0];
        result.configSign = ComUtils.getConfigSign(result.noceStr,
                result.timestatm, currentURL);
        result.paySign = WeChatUtils.getPaySign(result.noceStr,
                result.timestatm, result.prepay_id);
        return result;

    }
}
