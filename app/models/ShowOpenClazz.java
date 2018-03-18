package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */

@Entity
public class ShowOpenClazz extends BaseModel{


    public String name;

    public String time;

    public String address;

    @Lob
    public String content;

    public String videoUrl;


    public static ShowOpenClazz add(String name,String time,String address,String content,String videoUrl){
        ShowOpenClazz showOpenClazz = new ShowOpenClazz();
        showOpenClazz.name = name;
        showOpenClazz.time = time;
        showOpenClazz.address = address;
        showOpenClazz.content = content;
        showOpenClazz.videoUrl = videoUrl;
        return showOpenClazz.save();
    }


    public void edit(String name,String time,String address,String content,String videoUrl){
        this.name = name;
        this.time = time;
        this.address = address;
        this.content = content;
        this.videoUrl = videoUrl;
        this.save();
    }


    public static List<ShowOpenClazz> fetchAll(){
        return ShowOpenClazz.find(defaultCondition()).fetch();
    }







}
