package vo;

import models.ClazzStudent;
import models.Report;

/**
 * @autor kevin.dai
 * @Date 2018/3/27
 */
public class ReportVO extends OneData {


    public String clazzName;

    public String studentNum;

    public String studentName;


    public Long studentId;


    public String comment;


    public String starComment;


    public String status;

    public String imgUrls;


    public Boolean isSend;



    public ReportVO(){

    }


    public ReportVO(Report report){
        this.id = report.id;
        this.studentName = report.student.name;
        this.clazzName  = ClazzStudent.findByStudent(report.student.id).clazz.name;
        this.studentNum = report.student.number;
        this.studentId = report.student.id;
        this.comment = report.comment;
        this.starComment = report.starComment;
        this.status = report.status.toString();
        this.imgUrls = report.imgUrls;
        this.isSend = report.isSend;
    }



}
