package vo;

import models.Course;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/22
 */
public class CourseVO extends OneData {

    public Long groupId;

    public String name;

    public String feeType = "1";//收费方式 0 ：按课时算，1：按学期算

    public String fee;//金额

    public Boolean release;

    public Boolean order;


    public CourseVO(){

    }



    public CourseVO(Course course){
        this.id = course.id;
        this.groupId = course.group.id;
        this.name = course.name;
        this.feeType = course.feeType;
        this.fee = course.fee;
        this.release = course.release;
        this.order = course.order;
    }



    public static List<CourseVO> list(List<Course> courses){
        if(courses.isEmpty()){
            return Collections.emptyList();
        }
        return courses.stream().map(c -> new CourseVO(c)).collect(Collectors.toList());
    }

}
