package models.member;

import models.WePerson;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;

@Entity
public class Teacher extends WePerson {

	public String job;// 职务



	public Teacher() {
	}

	public static Teacher regist(Teacher teacher) {
		teacher.password = DigestUtils.md5Hex("111111");
		teacher.save();
		return teacher;
	}


	public static Long countTeacher() {
		return Teacher.count("select count(*) from Teacher where isDeleted=false");
	}

	public static List<Teacher> fetchTeacherByIDs(List<Long> memberIdList) {
		if (memberIdList == null || memberIdList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return Teacher.find("select t from Teacher t where t.isDeleted=false and t.id in(:memberIds)")
				.bind("memberIds", memberIdList.toArray()).fetch();
	}

}
