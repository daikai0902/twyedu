package models;

import javax.persistence.Entity;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/6/30
 */
@Entity
public class ShowCourseClassify extends BaseModel {


    public String name;

    public static  ShowCourseClassify add(String name){
        ShowCourseClassify showCourseClassify = new ShowCourseClassify();
        showCourseClassify.name = name;
        return  showCourseClassify.save();
    }


    public void editName(String name){
        this.name = name;
        this.save();
    }




    public static  List<ShowCourseClassify> findAll(){
        return ShowCourseClassify.find(getDefaultContitionSql("1=1  ")).fetch();
    }



}
