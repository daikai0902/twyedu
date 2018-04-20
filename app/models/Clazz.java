package models;

import models.member.Teacher;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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




    public static List<Clazz> findbyCourse(Course course){
        return find(getDefaultContitionSql(" course = ? "),course).fetch();
    }


    public static List<Clazz> findByTeacher(Teacher teacher){
        List<Clazz> list = new ArrayList<>();
        List<Clazz> list1 = find(getDefaultContitionSql(" teacherA = ? "),teacher).fetch();
        List<Clazz> list2 = find(getDefaultContitionSql(" teacherB = ? "),teacher).fetch();
        list.addAll(list1);
        list.addAll(list2);
        return  list.stream().distinct().collect(Collectors.toList());
    }



    public static  Long countClazzsByTeachers(Teacher teacher){
        return count(getDefaultContitionSql(" teacherA = ? and  teacherA != teacherB "),teacher)+count(getDefaultContitionSql(" teacherB = ?  "),teacher);
    }




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




    public void edit(String name,Course course,Teacher teacherA,Teacher teacherB,String num,String time,String duration){
        this.name = name;
        this.course =course;
        this.teacherA = teacherA;
        this.teacherB = teacherB;
        this.time = time;
        this.num = num;
        this.duration = duration;
        this.save();
    }
}
