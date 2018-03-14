package models;

import models.member.Student;
import models.member.Teacher;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.List;

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


	@ManyToOne
	public WeChatMember wMember;







	public static List<WePerson> fetchPersonsByIds(List<Long> personIds) {
		return WePerson.find("select wp from WePerson wp where wp.id in (:personIds) ")
				.bind("personIds", personIds.toArray()).fetch();
	}



	public static boolean isPhoneAvailable(String cellPhone) {
		return StringUtils.isNotBlank(cellPhone)
				&& cellPhone.matches("^((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57]))\\d{8}$")
				&& WePerson.count(getDefaultContitionSql("cellPhone=?"), cellPhone) == 0;
	}

	public static boolean isEmailAvailable(String emailAddress) {
		return emailAddress.matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9]+(.[a-zA-Z]{2,4}){1,4}")
				&& WePerson.count(getDefaultContitionSql("lower(email)=lower(?)"), emailAddress) == 0;
	}

	public boolean isStudent() {
		return this instanceof Student;
	}

	public boolean isTeacher() {
		return this instanceof Teacher;
	}

//	public boolean isSysAdmin() {
//		return this instanceof SysAdmin;
//	}


}
