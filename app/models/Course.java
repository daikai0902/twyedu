package models;

import models.group.OrganizeGroup;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/18
 */
@Entity
public class Course extends BaseModel{

    @ManyToOne
    public OrganizeGroup group;

    public String name;

    public String feeType = "1";//收费方式 0 ：按课时算，1：按学期算


    public String fee;//金额

    public boolean isRelease = false;//是否发布

    public boolean isOrder = false;//是否允许预约


    public static Course add(OrganizeGroup group,String name,String feeType,String fee){

        Course course = new Course();
        course.group = group;
        course.name = name;
        course.feeType = feeType;
        course.fee = fee;
        return course.save();

    }



    public void edit(String name,String feeType,String fee){
        this.name = name;
        this.fee = fee;
        this.feeType = feeType;
        this.save();
    }



    public void setRelease(boolean status){
        this.isRelease = status;
        this.save();
    }


    public void setOrder(boolean status){
        this.isOrder = status;
        this.save();
    }


    public static List<Course> findByGroup(Long groupId){
        return find(getDefaultContitionSql( " group.id = ? "),groupId).fetch();
    }

    public static List<Long> findCourseIdsByGroup(Long groupId){
        return find("select c.id from Course c where c.isDeleted = 0 and c.group.id = ? ",groupId).fetch();
    }


    public static List<Course> findByCondition(Long groupId,boolean isOrder){
        if(isOrder){
            return find(getDefaultContitionSql( " group.id = ? and isRelease = 1 and  isOrder = 1 "),groupId).fetch();
        }else
            return find(getDefaultContitionSql( " group.id = ? and isRelease = 1"),groupId).fetch();
    }


}
