package models;

import models.member.Student;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.List;

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


    @Enumerated(EnumType.STRING)
    public PayStatus payStatus = PayStatus.未支付;


    @Enumerated(EnumType.STRING)
    public PayMethod payMethod;


    @Enumerated(EnumType.STRING)
    public ContactStatus contactStatus = ContactStatus.等待联系;

    public String orderNum;


    public enum PayStatus{
        未支付,已支付
    }


    public enum PayMethod{
        微信,支付宝,刷卡,现金
    }


    public enum ContactStatus{
        等待联系,已联系,还未确认,已确认完毕
    }


    public static  CourseStudent add(Course course,Student student,PayMethod payMethod,String type){
        CourseStudent cs = new CourseStudent();
        cs.course = course;
        cs.student = student;
        cs.type = type;
        cs.payMethod = payMethod;
        if(payMethod != null && !payMethod.equals(PayMethod.微信)){
          cs.orderNum = RandomStringUtils.randomAlphanumeric(6);
        }
        return cs.save();
    }


    public static  List<CourseStudent> findByCourse(Long courseId,String type){
        return find("select cs from CourseStudent cs where cs.student.isDeleted = 0 and cs.course.isDeleted = 0 " +
                "   and cs.type = ? and cs.course.id = ? ",type,courseId).fetch();
    }



}
