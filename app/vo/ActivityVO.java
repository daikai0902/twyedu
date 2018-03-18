package vo;

import models.Activity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class ActivityVO extends OneData{

    public Long id;

    public String name;

    public String time;


    public String address;

    public String content;

    public String videoUrl;

    public String link;

    public String type;//"1:普通 2:链接"


    public ActivityVO(){

    }


    public ActivityVO(Activity activity){
        this.name = activity.name;
        this.id = activity.id;
        this.time = activity.time;
        this.address = activity.address;
        this.content = activity.content;
        this.videoUrl = activity.videoUrl;
        this.link = activity.link;
        this.type = activity.type;
    }


    public static List<ActivityVO> list(List<Activity> activities){
        if(activities.isEmpty()){
            return Collections.emptyList();
        }
        return activities.stream().map(a -> new ActivityVO(a)).collect(Collectors.toList());
    }



}
