package models;

import models.member.Student;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/21
 */
@Entity
public class ClazzStudent extends BaseModel {

    @ManyToOne
    public Clazz clazz;

    @ManyToOne
    public Student student;


    public String arrive;//点到情况:"1,1,1,0"


    public static ClazzStudent add(Clazz clazz,Student student){
        ClazzStudent clazzStudent = new ClazzStudent();
        clazzStudent.clazz = clazz;
        clazzStudent.student = student;
        return  clazzStudent.save();
    }


    public static Long countStudent(Clazz clazz){
        return find(" select count(cs) from ClazzStudent cs where cs.isDeleted = 0 and cs.clazz.isDeleted = 0 and cs.student.isDeleted = 0 and cs.clazz  = ? ",clazz).first();
    }



    public static List<ClazzStudent> findClazz(Long clazzId){
        return find(getDefaultContitionSql(" clazz.id = ? "),clazzId).fetch();

    }


    public static ClazzStudent findByStudent(Long studentId){
        return find(getDefaultContitionSql(" student.id = ? "),studentId).first();
    }


    public static List<Student> findByClazz(Long clazzId){
        return find("select cs.student from ClazzStudent cs where cs.clazz.isDeleted = 0 and cs.student.isDeleted = 0 " +
                "   and cs.clazz.id = ? ",clazzId).fetch();

    }


    //点到
    public void editArrive(String arrive){
        this.arrive = arrive;
        this.save();
    }
}
