package models;

import models.member.Student;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import utils.ComUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;
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



    /**
     * 生成订单号
     */
    public void genOrderNum() {
        this.orderNum = ComUtils.formatDate(new Date(), "yyyyMMddHHmmss")
                + ComUtils.formatNumber(id, "000000000");
        this.save();
    }


    public void setPayStatus(PayStatus status){
        this.payStatus = status;
        this.save();
    }



    public void setContactStatus(ContactStatus status){
        this.contactStatus = status;
        this.save();
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




    public static CourseStudent findByOrderNum(String orderNum){
        return find(getDefaultContitionSql(" orderNum = ? "),orderNum).first();
    }

    public static  List<CourseStudent> findByCourse(Long courseId,String type){
        return find("select cs from CourseStudent cs where cs.student.isDeleted = 0 and cs.course.isDeleted = 0 " +
                "   and cs.type = ? and cs.course.id = ? and cs.isDeleted = 0 ",type,courseId).fetch();
    }



    public static  List<CourseStudent> findByCourses(List<Long> courseIds,String type,Long courseId,String payStatus,String age){
        StringBuffer sb = new StringBuffer();
        sb.append(" select cs from CourseStudent cs where cs.student.isDeleted = 0 and cs.course.isDeleted = 0  and  cs.isDeleted = 0 " +
                " and cs.type = ").append("'"+type+"'");
        if(StringUtils.isNotBlank(payStatus)){
            sb.append(" and cs.payStatus = ").append("'"+payStatus+"'");
        }
        if(StringUtils.isNotBlank(age)){
            String[] ages = age.split(",");
            sb.append(" and cs.student.age > ").append(ages[0]);
            sb.append(" and cs.student.age < ").append(ages[1]);
        }
        if(courseId != null){
            sb.append(" and cs.course.id = ").append(courseId);
        }else{
            if(courseIds != null && courseIds.size() > 0 ){
                sb.append(" and cs.course.id in (");
                for (int i = 0; i < courseIds.size(); i++) {
                    sb.append(courseIds.get(i));
                    if (i < courseIds.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append(")");
            }
        }
        sb.append(" order by cs.createTime desc ");
        return find(sb.toString()).fetch();
    }

}
