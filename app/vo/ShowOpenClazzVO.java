package vo;

import models.ShowOpenClazz;
import models.ShowTeachers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class ShowOpenClazzVO extends OneData{


    public String name;

    public String time;

    public String address;

    public String content;

    public String videoUrl;

    public String remark;


    public String link;

    public String type;//"1:普通 2:链接"

    public String imgUrl;



    public ShowOpenClazzVO(){

    }



    public ShowOpenClazzVO(ShowOpenClazz showOpenClazz){
        this.id = showOpenClazz.id;
        this.name = showOpenClazz.name;
        this.time = showOpenClazz.time;
        this.address = showOpenClazz.address;
        this.content = showOpenClazz.content;
        this.videoUrl = showOpenClazz.videoUrl;
        this.remark = showOpenClazz.remark;
        this.link = showOpenClazz.link;
        this.type = showOpenClazz.type;
        this.imgUrl = showOpenClazz.imgUrl;
    }


    public static  List<ShowOpenClazzVO> list(List<ShowOpenClazz> showOpenClazzes){
        if(showOpenClazzes.isEmpty()){
           return Collections.emptyList();
        }
        return showOpenClazzes.stream().map(s -> new ShowOpenClazzVO(s)).collect(Collectors.toList());
    }



}
