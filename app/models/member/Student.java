package models.member;

import models.WePerson;
import org.apache.commons.codec.digest.DigestUtils;
import utils.ComUtils;

import javax.persistence.Entity;
import java.text.DecimalFormat;

@Entity
public class Student extends WePerson {


	public String clothsize;

	public String shoessize;

	public String momname;

	public String momphone;

	public String dadname;

	public String dadphone;

	public String nursery;//幼儿园





	public static Student signup(String name,Integer age,String sex,String clothsize,String shoessize,String momname,
									String momphone,String dadname,String dadphone,String nursery,String address,String remark){

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
		student.remark = remark;
		return student.save();


	}





	public static Student add(String name,Integer age,String sex,String clothsize,String shoessize,String momname,
							  String momphone,String dadname,String dadphone,String nursery,String address) {

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
		student.save();
		student.number = ComUtils.formatDate(student.createTime,"yyMMdd")+new DecimalFormat("00000").format(student.id);
		return student.save();
	}




	public static Student findByNum(String number){
		return find(getDefaultContitionSql(" number = ? "),number).first();
	}


}
