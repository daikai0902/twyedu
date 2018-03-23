package models;

import models.member.Student;

import javax.persistence.*;
import java.util.List;

/**
 * 成绩单
 * @autor kevin.dai
 * @Date 2018/3/21
 */

@Entity
public class Report extends BaseModel {

    @ManyToOne
    public Student student;


    @Lob
    public String comment;


    @Lob
    public String starComment;


    @Enumerated(EnumType.STRING)
    public Status status = Status.未填写;

    @Lob
    public String imgUrls;

    public enum Status{
        未填写,已完成,已填写未完成
    }


    public Boolean isSend = false;//是否发送



    public static Report findByStudent(Student student){
        return find(getDefaultContitionSql(" student = ? "),student).first();
    }


    public static Long countByStudents(List<Student> students,Status status){
        return find("select count(r) from Report r where  r.status = ? and r.student in(:students) ",status).bind("students",students.toArray()).first();
    }


    public static List<Report> fetchByStudents(List<Student> students){
        return find("select r from Report r where  r.isDeleted = 0 and r.isSend = 0 and r.student in(:students) ").bind("students",students.toArray()).fetch();
    }



    public void setIsSend(){
        this.isSend = true;
        this.save();
    }

    public static Report add(Student student,String comment,String starComment,String imgUrls){
        Report report = new Report();
        report.student = student;
        report.comment = comment;
        report.starComment = starComment;
        report.imgUrls = imgUrls;
        return report.save();
    }




    public void edit(String comment,String starComment,String imgUrls,Status status){
        this.comment = comment;
        this.starComment =starComment;
        this.imgUrls = imgUrls;
        this.status = status;
        this.save();
    }






}
