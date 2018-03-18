package vo;

import models.WePerson;
import models.member.AccessToken;
import models.member.SysAdmin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WePersonVO extends OneData {

	public String accesstoken;

	public String name;// 姓名

	public String sex;// 性别

	public Integer age;

	public String cellphone;

	public String address;

	public String password;

	public String role;

	public WePersonVO() {
	}

	public WePersonVO(AccessToken accessToken) {
		final WePerson p = accessToken.person;
		this.id = p.id;
		this.name = p.name;
		this.password = p.password;
		this.age  = p.age;
		this.cellphone = p.cellPhone;
		if(p.sex != null){
			this.sex = p.sex?"女孩":"男孩";
		}
		this.address = p.address;
		this.accesstoken = accessToken.accessToken;
		if(p.isTeacher()){
			this.role = "teacher";
		}
		if (p.isSysAdmin()){
			SysAdmin admin = (SysAdmin) p;
			if(admin.type.equals(SysAdmin.AdminType.平台管理员)){
				this.role = "superadmin";
			}
			if(admin.type.equals(SysAdmin.AdminType.网点管理员)){
				this.role = "orgadmin";
			}
		}
	}


	public WePersonVO(WePerson p) {
		this.id = p.id;
		this.name = p.name;
		this.address = p.address;
		this.password = p.password;
		this.age  = p.age;
		this.cellphone = p.cellPhone;
		if(p.sex != null){
			this.sex = p.sex?"女孩":"男孩";
		}
		if(p.isTeacher()){
			this.role = "teacher";
		}
		if (p.isSysAdmin()){
			SysAdmin admin = (SysAdmin) p;
			if(admin.type.equals(SysAdmin.AdminType.平台管理员)){
				this.role = "superadmin";
			}
			if(admin.type.equals(SysAdmin.AdminType.网点管理员)){
				this.role = "orgadmin";
			}
		}
	}

	public static List<WePersonVO> list(List<WePerson> personList){
		if(personList.isEmpty()){
			return Collections.emptyList();
		}
		return personList.stream().map(p -> new WePersonVO(p)).collect(Collectors.toList());
	}

}
