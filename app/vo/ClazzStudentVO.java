package vo;

import models.ClazzStudent;
import models.Report;
import models.member.Student;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/15
 */
public class ClazzStudentVO extends OneData {

    public String name;

    public Integer age;

    public String sex;// 0:男生 1:女生

    public String address;//住址

    public String remark;//备注

    public String password;

    public String cellPhone;

    public String clothsize;

    public String shoessize;

    public String momname;

    public String momphone;

    public String dadname;

    public String dadphone;

    public String nursery;//幼儿园

    public String arrive;//点到

    public String arriveDetail;

    public String number;

    public String reportStatus;//成绩单状态

    public Long reportId;

    public Long studentId;

    public String clazzName;

    public String courseName;






    public ClazzStudentVO(){

    }



    public ClazzStudentVO(ClazzStudent clazzStudent){
        this.id = clazzStudent.id;
        this.number = clazzStudent.student.number;
        this.studentId = clazzStudent.student.id;
        this.name = clazzStudent.student.name;
        this.age = clazzStudent.student.age;
        this.sex = BooleanUtils.toString(clazzStudent.student.sex,"女","男");
        this.address = clazzStudent.student.address;
        this.remark = clazzStudent.student.remark;
        this.clothsize = clazzStudent.student.clothsize;
        this.shoessize = clazzStudent.student.shoessize;
        this.momname = clazzStudent.student.momname;
        this.momphone = clazzStudent.student.momphone;
        this.dadname = clazzStudent.student.dadname;
        this.dadphone = clazzStudent.student.dadphone;
        this.nursery = clazzStudent.student.nursery;
        this.clazzName = clazzStudent.clazz.name;
        this.courseName = clazzStudent.clazz.course.name;
        this.arrive = formatArrive(clazzStudent.arrive)+"/"+clazzStudent.clazz.num;
        this.arriveDetail = clazzStudent.arrive;
        this.reportStatus = Report.findByStudent(clazzStudent.student) == null?"":Report.findByStudent(clazzStudent.student).status.toString();
        this.reportId = Report.findByStudent(clazzStudent.student) == null?null:Report.findByStudent(clazzStudent.student).id;
    }




    public static String formatArrive(String arrive){
        if(StringUtils.isBlank(arrive)){
            return "0";
        }
        int count = 0 ;
        for(String s : arrive.split(",")){
            if(s.equals("1")){
                count++;
            }
        }
        return count+"";
    }




    public static List<ClazzStudentVO> list(List<ClazzStudent> clazzStudents){
        if(clazzStudents.isEmpty()){
            return Collections.emptyList();

        }
        return clazzStudents.stream().map(c -> new ClazzStudentVO(c)).collect(Collectors.toList());
    }


}
