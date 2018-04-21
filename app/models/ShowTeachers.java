package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
@Entity
public class ShowTeachers extends BaseModel{

    public String name;

    public String imgUrl;

    public String listimgUrl;

    public String pcimgUrl;

    @Lob
    public String job;

    public String intro;//介绍



    public static ShowTeachers add(String name,String imgUrl,String job,String intro,String listimgUrl,String pcimgUrl){
        ShowTeachers showTeachers = new ShowTeachers();
        showTeachers.name = name;
        showTeachers.imgUrl = imgUrl;
        showTeachers.job = job;
        showTeachers.intro = intro;
        showTeachers.listimgUrl = listimgUrl;
        showTeachers.pcimgUrl = pcimgUrl;
        return showTeachers.save();
    }


    public void edit(String name,String imgUrl,String job,String intro,String listimgUrl,String pcimgUrl){
        this.name = name;
        this.job = job;
        this.imgUrl = imgUrl;
        this.intro = intro;
        this.listimgUrl = listimgUrl;
        this.pcimgUrl = pcimgUrl;
        this.save();
    }


    public static List<ShowTeachers> fetchAll(){
        return ShowTeachers.find(defaultCondition()).fetch();
    }


}
