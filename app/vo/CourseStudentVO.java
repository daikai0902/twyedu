package vo;

import models.CourseStudent;
import org.apache.commons.lang.BooleanUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/23
 */
public class CourseStudentVO  extends OneData{


    public String name;

    public Integer age;

    public String sex;// 0:男生 1:女生

    public String address;//住址

    public String remark;//备注


    public String momname;

    public String momphone;


    public String payMethod;


    public String payStatus;


    public String orderNum;



    public CourseStudentVO(CourseStudent courseStudent){
        this.id = courseStudent.id;
        this.name = courseStudent.student.name;
        this.age = courseStudent.student.age;
        this.sex =  BooleanUtils.toString(courseStudent.student.sex,"女","男");
        this.address = courseStudent.student.address;
        this.remark = courseStudent.student.remark;
        this.momname = courseStudent.student.momname;
        this.momphone = courseStudent.student.momphone;
        this.payMethod = courseStudent.payMethod.toString();
        this.payStatus = courseStudent.payStatus.toString();
        this.orderNum = courseStudent.orderNum;
    }



    public static List<CourseStudentVO> list(List<CourseStudent> courseStudents){
        if(courseStudents.isEmpty()){
            return Collections.emptyList();
        }
        return courseStudents.stream().map(c -> new CourseStudentVO(c)).collect(Collectors.toList());
    }



}
