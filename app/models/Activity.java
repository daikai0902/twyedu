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
    public String remark;

    public String videoUrl;

    public String link;

    public String type;//"1:普通 2:链接"

    public String imgUrl;





    public static Activity add(String name,String time,String address,String content,String remark,String videoUrl,String imgUrl){
        Activity activity = new Activity();
        activity.name = name;
        activity.time = time;
        activity.address = address;
        activity.remark = remark;
        activity.content = content;
        activity.videoUrl = videoUrl;
        activity.imgUrl = imgUrl;
        activity.type = "1";
        return activity.save();
    }



    public static Activity addLink(String name,String link,String imgUrl,String remark){
        Activity activity = new Activity();
        activity.name = name;
        activity.link = link;
        activity.type = "2";
        activity.imgUrl = imgUrl;
        activity.remark = remark;
        return activity.save();
    }



    public void edit(String name,String time,String address,String content,String remark,String videoUrl,String imgUrl){
        this.name = name;
        this.time = time;
        this.address = address;
        this.content = content;
        this.videoUrl = videoUrl;
        this.imgUrl  = imgUrl;
        this.remark = remark;
        this.save();
    }



    public void editLink(String name,String link,String imgUrl,String remark){
        this.name = name;
        this.link = link;
        this.imgUrl = imgUrl;
        this.remark = remark;
        this.save();
    }



    public static List<Activity> fetchAll(){
        return Activity.find(getDefaultContitionSql(" 1=1 order by createTime desc ")).fetch();
    }

    public static List<Activity> fetchAllBySize(int page,int size){
        return Activity.find(getDefaultContitionSql(" 1=1 order by createTime desc ")).fetch(page,size);
    }


    public static Long countAll(){
        return count(getDefaultContitionSql(" 1= 1"));
    }


}


