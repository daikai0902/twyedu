package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/6/30
 */
@Entity
public class ShowCourseGroup extends BaseModel{

    @ManyToOne
    public ShowCourseClassify classify;

    @ManyToOne
    public ShowCourse course;


    public static ShowCourseGroup add(ShowCourseClassify classify,ShowCourse course){
        ShowCourseGroup courseGroup = new ShowCourseGroup();
        courseGroup.classify = classify;
        courseGroup.course = course;
        return courseGroup.save();
    }


    public static List<ShowCourse> findCourseByClassify(ShowCourseClassify classify){
        return find("select sg.course from ShowCourseGroup sg where sg.isDeleted =0 and" +
                " sg.course.isDeleted = 0 and sg.classify.isDeleted = 0 and sg.classify = ? ",classify).fetch();
    }




}
