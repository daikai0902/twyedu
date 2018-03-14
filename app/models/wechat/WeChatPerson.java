package models.wechat;

public class WeChatPerson {
    public String openid;
    public int subscribe;// 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    public String nickname;
    public int sex;// 值为1时是男性，值为2时是女性，值为0时是未知
    public String city;
    public String country;
    public String province;
    public String language;// 用户的语言，简体中文为zh_CN
    public String headimgurl;
    public long subscribe_time;

}
