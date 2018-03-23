package vo;

import models.Clazz;
import models.ClazzStudent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/22
 */
public class ClazzVO extends OneData{

    public String name;

    public Long courseId;

    public Long teacherAId;

    public String teacherAName;

    public Long teacherBId;

    public String teacherBName;

    public Long studentNum;

    public String num;

    public String time;

    public String duration;


    public ClazzVO(){

    }


    public ClazzVO(Clazz clazz){
        this.id = clazz.id;
        this.name = clazz.name;
        this.courseId = clazz.course.id;
        this.teacherAId = clazz.teacherA.id;
        this.teacherBId = clazz.teacherB.id;
        this.num  = clazz.num;
        this.time = clazz.time;
        this.duration = clazz.duration;
        this.teacherAName = clazz.teacherA.name;
        this.teacherBName = clazz.teacherB.name;
        this.studentNum = ClazzStudent.countStudent(clazz);
    }



    public static List<ClazzVO> list(List<Clazz> clazzes){
        if(clazzes.isEmpty()){
            return Collections.emptyList();
        }
        return clazzes.stream().map(c -> new ClazzVO(c)).collect(Collectors.toList());
    }



}
