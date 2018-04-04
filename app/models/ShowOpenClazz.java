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


    @Lob
    public String remark;


    public String link;

    public String type;//"1:普通 2:链接"

    public String imgUrl;





    public static ShowOpenClazz add(String name,String time,String address,String content,String videoUrl,String remark,String imgUrl){
        ShowOpenClazz showOpenClazz = new ShowOpenClazz();
        showOpenClazz.name = name;
        showOpenClazz.time = time;
        showOpenClazz.address = address;
        showOpenClazz.content = content;
        showOpenClazz.videoUrl = videoUrl;
        showOpenClazz.type = "1";
        showOpenClazz.remark = remark;
        showOpenClazz.imgUrl = imgUrl;
        return showOpenClazz.save();
    }



    public static ShowOpenClazz addLink(String name, String link,String imgUrl){
        ShowOpenClazz showOpenClazz = new ShowOpenClazz();
        showOpenClazz.name = name;
        showOpenClazz.link = link;
        showOpenClazz.type = "2";
        showOpenClazz.imgUrl = imgUrl;
        return showOpenClazz.save();
    }

    public void edit(String name,String time,String address,String content,String videoUrl,String remark,String imgUrl){
        this.name = name;
        this.time = time;
        this.address = address;
        this.content = content;
        this.videoUrl = videoUrl;
        this.remark = remark;
        this.imgUrl = imgUrl;
        this.save();
    }



    public void editLink(String name,String link,String imgUrl){
        this.name = name;
        this.link = link;
        this.imgUrl = imgUrl;
        this.save();
    }



    public static List<ShowOpenClazz> fetchAll(){
        return ShowOpenClazz.find(getDefaultContitionSql(" 1=1 order by createTime desc")).fetch();
    }







}
