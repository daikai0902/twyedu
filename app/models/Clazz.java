package models;

import models.member.Teacher;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @autor kevin.dai
 * @Date 2018/3/21
 */
@Entity
public class Clazz extends BaseModel {


    public String name;

    @ManyToOne
    public Course course;

    @ManyToOne
    public Teacher teacherA;


    @ManyToOne
    public Teacher teacherB;

    public String num;

    public String time;

    public String duration;




    public static Clazz add(String name,Course course,Teacher teacherA,Teacher teacherB,String num,String time,String duration){
        Clazz clazz = new Clazz();
        clazz.name = name;
        clazz.course =course;
        clazz.teacherA = teacherA;
        clazz.teacherB = teacherB;
        clazz.time = time;
        clazz.num = num;
        clazz.duration = duration;
        return clazz.save();
    }
}
