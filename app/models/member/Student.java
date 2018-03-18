package models.member;

import models.WePerson;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;

@Entity
public class Student extends WePerson {


	public String clothsize;

	public String shoessize;

	public String momname;

	public String momphone;

	public String dadname;

	public String dadphone;

	public String nursery;//幼儿园

	public String course;//课程

	public Boolean isSignup = false;//是否报名



	public static Student signup(String name,Integer age,String sex,String clothsize,String shoessize,String momname,
									String momphone,String dadname,String dadphone,String nursery,String address,String course,String remark){

		Student student = new Student();
		student.name = name;
		student.age = age;
		student.sex = sex.equals("男孩")?false:true;
		student.clothsize = clothsize;
		student.shoessize = shoessize;
		student.momname = momname;
		student.momphone = momphone;
		student.dadname = dadname;
		student.dadphone = dadphone;
		student.nursery = nursery;
		student.address = address;
		student.course = course;
		student.remark = remark;
		student.isSignup = true;
		return student.save();


	}





	public static Student registZhuji(Student student) {
		student.password = "111111";
		student.create();
		return student;
	}

	public static Long countStudent() {
		return Student.count("select count(*) from Student where isDeleted=false");
	}

}
