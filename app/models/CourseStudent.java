package models;

import models.member.Student;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @autor kevin.dai
 * @Date 2018/3/21
 */
@Entity
public class CourseStudent extends BaseModel{

    @ManyToOne
    public Course course;

    @ManyToOne
    public Student student;


    public String type;//1:正式 2:预约


    public static  CourseStudent add(Course course,Student student,String type){
        CourseStudent cs = new CourseStudent();
        cs.course = course;
        cs.student = student;
        cs.type = type;
        return cs.save();
    }
}
