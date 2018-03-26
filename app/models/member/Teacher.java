package models.member;

import models.WePerson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import utils.ComUtils;

import javax.persistence.Entity;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@Entity
public class Teacher extends WePerson {


	public String imgUrl;//头像

	public String IDcard;//身份证




	public static Teacher add(String name,String cellphone,String IDcard,String imgUrl) {
		Teacher teacher = new Teacher();
		teacher.name = name;
		teacher.cellPhone = cellphone;
		teacher.IDcard = IDcard;
		teacher.imgUrl = imgUrl;
		teacher.password = StringUtils.substring(IDcard,12,18);
		teacher.save();
		teacher.number = "T"+ ComUtils.formatDate(teacher.createTime,"yyMMdd")+ new DecimalFormat("0000").format(teacher.id);
		return teacher.save();
	}






}
