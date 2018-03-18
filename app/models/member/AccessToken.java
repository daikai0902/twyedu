package models.member;

import models.BaseModel;
import models.WePerson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class AccessToken extends BaseModel {
	@ManyToOne
	public WePerson person;

	public String appVersion;
	public String osVersion;

	public String deviceType;// IOS|ANDROID|WEB
	public String deviceToken;

	public String accessToken;
	public String pushToken;
	public boolean isNotificationOn = true;

	public long lastOnlineTime = 0l;

	public void savePushToken(String pushToken) {
		this.pushToken = pushToken;
		this.save();
	}

	public static AccessToken regist(WePerson person) {
		AccessToken at = findByPerson(person);
		if (at != null) {
			return at;
		}
		at = new AccessToken();
		at.person = person;
		at.accessToken = DigestUtils.md5Hex(person.id + RandomStringUtils.random(16) + new Date().getTime());
		return at.save();
	}

	public AccessToken update(String appVersion, String osVersion, String deviceType, String deviceToken) {
		this.osVersion = osVersion;
		this.appVersion = appVersion;
		this.deviceType = deviceType;
		this.deviceToken = deviceToken;
		return this.save();
	}

	public static AccessToken findByAccessToken(String accessToken) {
		return AccessToken
				.find("select at from AccessToken at where at.isDeleted=false and at.accessToken=?", accessToken)
				.first();
	}

	public static AccessToken findByPerson(WePerson person) {
		return AccessToken.find("select at from AccessToken at where at.isDeleted=false and at.person=?", person)
				.first();
	}

	public static WePerson findPersonByAccessToken(String accessToken) {
		return AccessToken
				.find("select at.person from AccessToken at where at.isDeleted=false and at.accessToken=?", accessToken)
				.first();
	}

}
