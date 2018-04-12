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

    @Lob
    public String remark;

    public String videoUrl;

    public String link;

    public String type;//"1:普通 2:链接"

    public String imgUrl;





    public static News add(String name,String content,String remark, String videoUrl,String imgUrl){
        News news = new News();
        news.name = name;
        news.content = content;
        news.remark = remark;
        news.videoUrl = videoUrl;
        news.type = "1";
        news.imgUrl = imgUrl;
        return news.save();
    }



    public static News addLink(String name, String link,String imgUrl,String remark){
        News news = new News();
        news.name = name;
        news.link = link;
        news.type = "2";
        news.imgUrl = imgUrl;
        news.remark = remark;
        return news.save();
    }



    public void edit(String name,String content,String remark, String videoUrl,String imgUrl){
        this.name = name;
        this.content = content;
        this.remark = remark;
        this.videoUrl = videoUrl;
        this.imgUrl = imgUrl;
        this.save();
    }



    public void editLink(String name,String link,String imgUrl,String remark){
        this.name = name;
        this.link = link;
        this.imgUrl = imgUrl;
        this.remark = remark;
        this.save();
    }



    public static List<News> fetchAll(){
        return News.find(getDefaultContitionSql(" 1=1 order by createTime desc ")).fetch();
    }


    public static List<News> fetchAllBySize(int page,int size){
        return News.find(getDefaultContitionSql(" 1=1 order by createTime desc ")).fetch(page, size);
    }



    public static Long countAll(){
        return count(getDefaultContitionSql(" 1=1 "));
    }
}


