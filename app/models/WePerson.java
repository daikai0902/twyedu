package models;

import models.member.Student;
import models.member.SysAdmin;
import models.member.Teacher;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

@Entity
public class WePerson extends BaseModel {

	public String name;

	public Integer age;

	public Boolean sex;// 0:男生 1:女生

	@Lob
	public String address;//住址

	@Lob
	public String remark;//备注

	public String password;

	public String cellPhone;

	public String number;//工号


	@ManyToOne
	public WeChatMember wMember;


	public void eidtPwd(String password){
		this.password = password;
		this.save();
	}



	public static WePerson findByName(String name){
		return WePerson.find(getDefaultContitionSql(" name = ? "),name).first();
	}

	public static WePerson findByPhone(String cellPhone){
		return WePerson.find(getDefaultContitionSql(" cellPhone = ? "),cellPhone).first();
	}

	public static boolean isPhoneAvailable(String cellPhone) {
		return StringUtils.isNotBlank(cellPhone)
				&& cellPhone.matches("^((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]))\\d{8}$")
				&& WePerson.count(getDefaultContitionSql("cellPhone=?"), cellPhone) == 0;
	}


	public boolean isStudent() {
		return this instanceof Student;
	}

	public boolean isTeacher() {
		return this instanceof Teacher;
	}

	public boolean isSysAdmin() {
		return this instanceof SysAdmin;
	}


}
