package models;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * 会员用户
 */
@Entity
public class WeChatMember extends BaseModel {

	public String openId;// 唯一对应微信的用户OpenId
	public String nickName;
	public String avatarURL;
	public String city;
	public String country;
	public String province;
	public String language;
	public Date subscribeTime;
	public boolean hasSubscribed;
	@Enumerated(EnumType.STRING)
	public Sex sex;
	private String vipSerial;// 会员ID

	public enum Sex {
		Female, Male, Unknown
	}

	public static WeChatMember findByOpenId(String openId) {
		WeChatMember member = null;
		member = find(getDefaultContitionSql("openId=?"), openId).first();
		return member;
	}

	public static WeChatMember findMemberById(long memberId) {
		return find(getDefaultContitionSql("id=?"), memberId).first();
	}


}
