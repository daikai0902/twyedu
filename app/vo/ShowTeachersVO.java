package vo;

import models.ShowTeachers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class ShowTeachersVO extends OneData{


    public String name;

    public String imgUrl;

    public String job;

    public String intro;//介绍

    public String listimgUrl;

    public String pcimgUrl;


    public ShowTeachersVO(){

    }



    public ShowTeachersVO(ShowTeachers showTeachers){
        this.id = showTeachers.id;
        this.name = showTeachers.name;
        this.imgUrl = showTeachers.imgUrl;
        this.job = showTeachers.job;
        this.intro = showTeachers.intro;
        this.listimgUrl = showTeachers.listimgUrl;
        this.pcimgUrl = showTeachers.pcimgUrl;
    }


    public static  List<ShowTeachersVO> list(List<ShowTeachers> showTeachers){
        if(showTeachers.isEmpty()){
           return Collections.emptyList();
        }
        return showTeachers.stream().map(s -> new ShowTeachersVO(s)).collect(Collectors.toList());
    }



}
