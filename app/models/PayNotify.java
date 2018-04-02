package models;

import utils.ComUtils;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信支付回执（微信支付交易记录）<br>
 * 原则：每次支付后的回执都保存，方便存根查询。（out_trade_no对应OrderSum.orderSerial）
 *
 * @author xuyuzhu
 */
@Entity
public class PayNotify extends BaseModel {

    public String appid;
    public String mch_id;
    public String nonce_str;
    public String result_code;
    public String openid;
    public String is_subscribe;// 是否关注公众账号,用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
    public String trade_type;

    public String sign_type;// 签名类型, 取值:MD5、RSA, 默认:MD5
    public String service_version = "1.0";// 版本号, 默认为 1.0
    public String input_charset = "GBK";// 字符编码, 取值:GBK、UTF-8, 默认:GBK。
    public String sign;// not null, 签名
    public int sign_key_index = 1;// 多密钥支持的密钥序号, 默认1
    public int trade_mode;// not null, 交易模式，1：即时到账
    public int trade_state;// not null, 支付结果，0：成功，其他保留
    public String pay_info;// 支付结果信息, 支付成功时为空
    public String partner;// not null, 商户号
    public String bank_type;// not null, 银行类型,在微信中使用WX
    public String bank_billno;// 银行订单号
    public int total_fee;// not null,总金额，分为单位，如果discount有值,
    // 通知的total_fee + discount = 请求的 total_fee
    public String fee_type;// not null, 现金支付币种, 目前只支持人民币, 默认值是1, 人民币
    public String notify_id;// not null, 支付结果通知id, 对于某些特定商户, 只返回通知id, 要求
    // 商户据此查询交易结果
    public String transaction_id;// not null, 交易号, 28位长的数值, 其中前10位为商户号, 之后8位
    // 为订单产生的日期, 如20090415, 最后10位是流水号。
    public String out_trade_no;// not null, 商户系统的订单号, 与请求一致。
    public String attach;// 商家数据包, 原样返回
    public String time_end;// not null, 支付完成时间, 格式为yyyyMMddhhmmss
    public int transport_fee;// 物流费用, 单位分, 默认 0。 如果有值,必须保证transport_fee +
    // product_fee = total_fee
    public int product_fee;// 物品费用, 单位分 。如果有值, 必须保证transport_fee
    // +product_fee=total_fee
    public int discount;// 折扣价格, 单位分, 如果有值, 通知的total_fee + discount = 请求的
    // total_fee
    public String buyer_alias;// 买家别名, 对应买家账号的一个加密串

    private PayNotify(String sign_type, String service_version,
                      String input_charset, String sign, int sign_key_index,
                      int trade_mode, int trade_state, String pay_info, String partner,
                      String bank_type, String bank_billno, int total_fee,
                      String fee_type, String notify_id, String transaction_id,
                      String out_trade_no, String attach, String time_end,
                      int transport_fee, int product_fee, int discount, String buyer_alias) {
        super();
        this.sign_type = sign_type;
        this.service_version = service_version;
        this.input_charset = input_charset;
        this.sign = sign;
        this.sign_key_index = sign_key_index;
        this.trade_mode = trade_mode;
        this.trade_state = trade_state;
        this.pay_info = pay_info;
        this.partner = partner;
        this.bank_type = bank_type;
        this.bank_billno = bank_billno;
        this.total_fee = total_fee;
        this.fee_type = fee_type;
        this.notify_id = notify_id;
        this.transaction_id = transaction_id;
        this.out_trade_no = out_trade_no;
        this.attach = attach;
        this.time_end = time_end;
        this.transport_fee = transport_fee;
        this.product_fee = product_fee;
        this.discount = discount;
        this.buyer_alias = buyer_alias;
    }

    public PayNotify() {
        // TODO Auto-generated constructor stub
    }

    public static PayNotify findOneByNotifyId(String notifyId) {
        return find(getDefaultContitionSql("notify_id=?"), notifyId).first();
    }

    public static PayNotify createPayNotify(String sign_type,
                                            String service_version, String input_charset, String sign,
                                            int sign_key_index, int trade_mode, int trade_state,
                                            String pay_info, String partner, String bank_type,
                                            String bank_billno, int total_fee, String fee_type,
                                            String notify_id, String transaction_id, String out_trade_no,
                                            String attach, String time_end, int transport_fee, int product_fee,
                                            int discount, String buyer_alias) {
        PayNotify notify = findOneByNotifyId(notify_id);
        if (null == notify) {
            notify = new PayNotify(sign_type, service_version, input_charset,
                    sign, sign_key_index, trade_mode, trade_state, pay_info,
                    partner, bank_type, bank_billno, total_fee, fee_type,
                    notify_id, transaction_id, out_trade_no, attach, time_end,
                    transport_fee, product_fee, discount, buyer_alias).save();
        }
        return notify;
    }

    public void saveOne() {
        this.save();
    }

    public static PayNotify createPayNotifyByMap(Map<String, String> resultMap) {
        return null;

    }

    public static List<PayNotify> fetchPayNotifies(Date dateFrom, Date dateTo,
                                                   int page, int pageSize) {
        StringBuffer hql = new StringBuffer(
                "select pn from OrderSum o, PayNotify pn where o.orderSerial=pn.out_trade_no and pn.isDeleted=false ");
        List<Object> params = new ArrayList();
        if (null != dateFrom) {
            hql.append("and str_to_date(pn.time_end,'%Y%m%d%H%i%s')>=? ");
            params.add(dateFrom);
        }
        if (null != dateTo) {
            hql.append("and str_to_date(pn.time_end,'%Y%m%d%H%i%s')<? ");
            params.add(dateTo);
        }
        hql.append("order by str_to_date(pn.time_end,'%Y%m%d%H%i%s') desc");
        return find(hql.toString(), params.toArray()).fetch(page, pageSize);
    }

    public static int countAll() {
        StringBuffer hql = new StringBuffer(
                "select count(pn) from PayNotify pn, OrderSum s where pn.out_trade_no=s.orderSerial and pn.isDeleted=false");
        Long rs = find(hql.toString()).first();
        return null == rs ? 0 : rs.intValue();
    }

    public String getFormatDate(String format) {
        return ComUtils.formatDate(
                ComUtils.getDate(time_end, "yyyyMMddHHmmss"), format);
    }

    /**
     * 总销售数
     */
    public static long getTotalAmount() {
        StringBuffer sb = new StringBuffer(
                "select sum(i.amount) from PayNotify pn, OrderSum s, OrderItem i where pn.out_trade_no=s.orderSerial and s=i.orderSum and pn.isDeleted=false");
        Long rs = find(sb.toString()).first();
        return null == rs ? 0 : rs;
    }

    /**
     * 总销售额(单位：分)
     */
    public static long getTotalFee() {
        StringBuffer sb = new StringBuffer(
                "select sum(pn.total_fee) from PayNotify pn, OrderSum s where pn.isDeleted=false and pn.out_trade_no=s.orderSerial");
        Long rs = find(sb.toString()).first();
        return null == rs ? 0 : rs;
    }
}
