package vo;

import models.News;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class NewsVO extends OneData{


    public String name;

    public String content;

    public String videoUrl;

    public String link;

    public String type;//"1:普通 2:链接"



    public NewsVO(){

    }


    public NewsVO(News news){
        this.id = news.id;
        this.name = news.name;
        this.content = news.content;
        this.videoUrl = news.videoUrl;
        this.link = news.link;
        this.type = news.type;
    }


    public static  List<NewsVO> list(List<News> news){
        if(news.isEmpty()){
            return Collections.emptyList();
        }
        return news.stream().map(n -> new NewsVO(n)).collect(Collectors.toList());
    }





}
