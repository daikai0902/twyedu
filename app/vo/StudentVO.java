package vo;

import models.member.Student;
import org.apache.commons.lang.BooleanUtils;

/**
 * @autor kevin.dai
 * @Date 2018/3/15
 */
public class StudentVO extends OneData {

    public String name;

    public Integer age;

    public String sex;// 0:男生 1:女生

    public String address;//住址

    public String remark;//备注

    public String password;

    public String cellPhone;

    public String clothsize;

    public String shoessize;

    public String momname;

    public String momphone;

    public String dadname;

    public String dadphone;

    public String nursery;//幼儿园

    public String course;//课程

    public Boolean isSignup;//是否报名


    public StudentVO(){

    }



    public StudentVO(Student student){
        this.name = student.name;
        this.age = student.age;
        this.sex = BooleanUtils.toString(student.sex,"女","男");
        this.address = student.address;
        this.remark = student.remark;
        this.clothsize = student.clothsize;
        this.shoessize = student.shoessize;
        this.momname = student.momname;
        this.momphone = student.momphone;
        this.dadname = student.dadname;
        this.dadphone = student.dadphone;
        this.nursery = student.nursery;
        this.course = student.course;
        this.isSignup = student.isSignup;
    }




}
