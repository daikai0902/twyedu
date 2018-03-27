package vo;

import models.Report;

/**
 * @autor kevin.dai
 * @Date 2018/3/27
 */
public class ReportVO extends OneData {




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
        this.studentId = report.student.id;
        this.comment = report.comment;
        this.starComment = report.starComment;
        this.status = report.status.toString();
        this.imgUrls = report.imgUrls;
        this.isSend = report.isSend;
    }



}
