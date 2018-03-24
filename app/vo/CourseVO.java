package vo;

import models.Clazz;
import models.ClazzStudent;
import models.Course;
import models.CourseStudent;

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

    public List<ClazzVO> clazzs;


    public List<CourseStudentVO> students;


    public CourseVO(){

    }



    public CourseVO(Course course){
        this.id = course.id;
        this.groupId = course.group.id;
        this.name = course.name;
        this.feeType = course.feeType;
        this.fee = course.fee;
        this.release = course.isRelease;
        this.order = course.isOrder;
    }


    public CourseVO(Course course, List<Clazz> clazzes){
        this.id = course.id;
        this.groupId = course.group.id;
        this.name = course.name;
        this.feeType = course.feeType;
        this.fee = course.fee;
        this.release = course.isRelease;
        this.order = course.isOrder;
        this.clazzs = ClazzVO.list(clazzes);
    }


    public CourseVO(Course course, List<CourseStudent> courseStudents,String type){
        this.id = course.id;
        this.groupId = course.group.id;
        this.name = course.name;
        this.feeType = course.feeType;
        this.fee = course.fee;
        this.release = course.isRelease;
        this.order = course.isOrder;
        this.students = CourseStudentVO.list(courseStudents);
    }



    public static List<CourseVO> list(List<Course> courses){
        if(courses.isEmpty()){
            return Collections.emptyList();
        }
        return courses.stream().map(c -> new CourseVO(c)).collect(Collectors.toList());
    }



    public static List<CourseVO> listClazz(List<Course> courses){
        if(courses.isEmpty()){
            return Collections.emptyList();
        }
        return courses.stream().map(c -> new CourseVO(c,Clazz.findbyCourse(c))).collect(Collectors.toList());
    }



    /**
     * 后期学生数据量大可以优化，减少sql链接数
     * @Date: 19:21 2018/3/24
     */
    public static List<CourseVO> listStudents(List<Course> courses,String type){
        if(courses.isEmpty()){
            return Collections.emptyList();
        }
        return courses.stream().map(c -> new CourseVO(c,CourseStudent.findByCourse(c.id,type),null)).collect(Collectors.toList());
    }
}
