package vo;

import models.ShowCourse;
import models.ShowOpenClazz;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class ShowCourseVO extends OneData{


    public String name;

    public String coverUrl;


    public String age;


    public String zc;//周次


    public String ksl;//课时量


    public String sc;//时长

    public String intro;


    public ShowCourseVO(){

    }



    public ShowCourseVO(ShowCourse showCourse){
        this.id = showCourse.id;
        this.name = showCourse.name;
        this.coverUrl = showCourse.coverUrl;
        this.age = showCourse.age;
        this.zc = showCourse.zc;
        this.ksl = showCourse.ksl;
        this.sc = showCourse.sc;
        this.intro = showCourse.intro;
    }


    public static  List<ShowCourseVO> list(List<ShowCourse> showCourses){
        if(showCourses.isEmpty()){
           return Collections.emptyList();
        }
        return showCourses.stream().map(s -> new ShowCourseVO(s)).collect(Collectors.toList());
    }



}
