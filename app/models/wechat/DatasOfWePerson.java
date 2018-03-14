package models.wechat;

import java.util.List;

public class DatasOfWePerson {
    public int total;// 关注该公众账号的总用户数
    public int count;// 拉取的OPENID个数，最大值为10000
    public List<String> data;// 列表数据，OPENID的列表
    public String next_openid;// 拉取列表的后一个用户的OPENID
}
