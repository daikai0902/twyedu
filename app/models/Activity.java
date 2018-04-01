package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
@Entity
public class Activity extends BaseModel{


    public String name;

    public String time;


    public String address;

    @Lob
    public String content;

    @Lob
    public String desc;

    public String videoUrl;

    public String link;

    public String type;//"1:普通 2:链接"





    public static Activity add(String name,String time,String address,String content,String desc,String videoUrl){
        Activity activity = new Activity();
        activity.name = name;
        activity.time = time;
        activity.address = address;
        activity.desc = desc;
        activity.content = content;
        activity.videoUrl = videoUrl;
        activity.type = "1";
        return activity.save();
    }



    public static Activity addLink(String name,String link){
        Activity activity = new Activity();
        activity.name = name;
        activity.link = link;
        activity.type = "2";
        return activity.save();
    }



    public void edit(String name,String time,String address,String content,String desc,String videoUrl){
        this.name = name;
        this.time = time;
        this.address = address;
        this.content = content;
        this.videoUrl = videoUrl;
        this.desc = desc;
        this.save();
    }



    public void editLink(String name,String link){
        this.name = name;
        this.link = link;
        this.save();
    }



    public static List<Activity> fetchAll(){
        return Activity.find(defaultCondition()).fetch();
    }

}


