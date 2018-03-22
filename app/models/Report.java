package models;

import models.member.Student;

import javax.persistence.*;

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



    public enum Status{
        未填写,已完成,已填写未完成
    }




}
