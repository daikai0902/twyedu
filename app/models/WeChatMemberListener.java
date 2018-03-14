package models;

import models.WeChatMember;
import models.wechat.WeChatPerson;
import utils.WechatUtil;

import javax.persistence.PrePersist;

public class WeChatMemberListener {
	@PrePersist
	public static void postPersist(WeChatMember member) {
		WeChatPerson wePerson = WechatUtil.getWePersonByOpenId(member.openId);
		member.updateProperties(wePerson);
	}

}
