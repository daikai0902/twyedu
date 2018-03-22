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


    public String arrive;//点到情况


    public static ClazzStudent add(Clazz clazz,Student student){
        ClazzStudent clazzStudent = new ClazzStudent();
        clazzStudent.clazz = clazz;
        clazzStudent.student = student;
        return  clazzStudent.save();
    }



    public static List<ClazzStudent> findClazz(Long clazzId){
        return find(getDefaultContitionSql(" clazz.id = ? "),clazzId).fetch();

    }
}
