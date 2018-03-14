package models;

import japidviews._javatags.CommonUtils;
import models.wechat.WeChatPerson;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import utils.WechatUtil;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

/**
 * 会员用户
 */
@Entity
@EntityListeners(WeChatMemberListener.class)
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

	public static WeChatMember registerFromWechat(WeChatPerson wePerson) {
		WeChatMember member = findByOpenId(wePerson.openid);
		if (null == member) {
			member = new WeChatMember();
			member.openId = wePerson.openid;
			member.nickName = wePerson.nickname;
			member.city = wePerson.city;
			member.country = wePerson.country;
			member.province = wePerson.province;
			member.language = wePerson.language;
			member.avatarURL = wePerson.headimgurl;
			member.subscribeTime = new Date(wePerson.subscribe_time);
			member.hasSubscribed = 0 == wePerson.subscribe ? false : true;
			member.sex = 1 == wePerson.sex ? Sex.Male
					: (2 == wePerson.sex ? Sex.Female : Sex.Unknown);
			member.create();
		} else {
			// update member info
		}
		return member;
	}

	public static WeChatMember registerByOpenId(String openId) {
		WeChatMember member = findByOpenId(openId);
		if (null == member) {
			member = new WeChatMember();
			member.openId = openId;
			member.create();
			member.vipSerial = member.genVIPSerial();
			member.save();
			Logger.warn("register member by openId: " + openId);
		}
		return member;
	}

	public static List<WeChatMember> fetchMembers(String nickName, int page,
                                                  int pageSize) {
		if (StringUtils.isBlank(nickName)) {
			return find(
					getDefaultContitionSql("openId is not null order by points desc, createTime desc"))
					.fetch();
		} else {
			return find(
					getDefaultContitionSql("nickName like? and openId is not null order by points desc, createTime desc"),
					"%" + nickName + "%").fetch(page, pageSize);
		}
	}

	/**
	 * 积分排名
	 */
	public static int getRank(WeChatMember member) {
		List<WeChatMember> members = find(
				"openId is not null and isDeleted=false order by points desc, createTime desc")
				.fetch();
		for (int i = 0; i < members.size(); i++) {
			if (members.get(i).id.equals(member.id)) {
				return i + 1;
			}
		}
		return -1;
	}

	public static long countAll() {
		return count("isDeleted=false");
	}

	/****** 实例方法 ******/

	/**
	 * 通过接口回执更新属性值
	 */
	public void updateProperties(WeChatPerson wePerson) {
		if (StringUtils.isNotBlank(wePerson.nickname)
				&& CommonUtils.escapeEmoji(wePerson.nickname)) {
			nickName = wePerson.nickname;
		}
		if (StringUtils.isNotBlank(wePerson.city)) {
			city = wePerson.city;
		}
		if (StringUtils.isNotBlank(wePerson.country)) {
			country = wePerson.country;
		}
		if (StringUtils.isNotBlank(wePerson.province)) {
			province = wePerson.province;
		}
		if (StringUtils.isNotBlank(wePerson.language)) {
			language = wePerson.language;
		}
		if (StringUtils.isNotBlank(wePerson.headimgurl)) {
			avatarURL = wePerson.headimgurl;
		}
		subscribeTime = new Date(wePerson.subscribe_time);
		hasSubscribed = 0 == wePerson.subscribe ? false : true;
		sex = 1 == wePerson.sex ? Sex.Male : (2 == wePerson.sex ? Sex.Female
				: Sex.Unknown);
	}

	/**
	 * 获取昵称（如果没有，则调用微信接口获取并保存）。
	 */
	public String nickName() {
		if (StringUtils.isBlank(nickName)) {
			WeChatPerson wePerson = WechatUtil.getWePersonByOpenId(openId);
			if (null != wePerson && CommonUtils.escapeEmoji(wePerson.nickname)) {
				this.updateProperties(wePerson);
				this.save();
			} else {
				System.err.println("weperson is null");
			}
		}
		return nickName;
	}

	/**
	 * 8位会员编号
	 */
	private String genVIPSerial() {
		return CommonUtils.formatNumber(id, "00000000");
	}

	public String vipSerial() {
		if (StringUtils.isBlank(vipSerial)) {
			vipSerial = genVIPSerial();
			this.save();
		}
		return vipSerial;
	}

	public String getVipSerail() {
		return this.vipSerial;
	}
}
