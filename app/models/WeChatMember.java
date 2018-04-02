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

	public String studentIds;



	public static WeChatMember add(String openId){
		WeChatMember member = new WeChatMember();
		member.openId = openId;
		return member.save();
	}


	public void setStudentIds(String studentIds){
		this.studentIds = studentIds;
		this.save();
	}



	public static WeChatMember findByOpenId(String openId) {
		WeChatMember member =  find(getDefaultContitionSql("openId=?"), openId).first();
		return member;
	}




}
