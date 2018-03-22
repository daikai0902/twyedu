package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
@Entity
public class News extends BaseModel{


    public String name;

    @Lob
    public String content;

    public String videoUrl;

    public String link;

    public String type;//"1:普通 2:链接"





    public static News add(String name,String content, String videoUrl){
        News news = new News();
        news.name = name;
        news.content = content;
        news.videoUrl = videoUrl;
        news.type = "1";
        return news.save();
    }



    public static News addLink(String name, String link){
        News news = new News();
        news.name = name;
        news.link = link;
        news.type = "2";
        return news.save();
    }



    public void edit(String name,String content, String videoUrl){
        this.name = name;
        this.content = content;
        this.videoUrl = videoUrl;
        this.save();
    }



    public void editLink(String name,String link){
        this.name = name;
        this.link = link;
        this.save();
    }



    public static List<News> fetchAll(){
        return News.find(defaultCondition()).fetch();
    }

}


