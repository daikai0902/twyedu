package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @autor kevin.dai
 * @Date 2018/3/21
 */
@Entity
public class ReportPic extends BaseModel{

    public String imgUrl;

    @ManyToOne
    public Report report;


    public static  ReportPic add(String imgUrl,Report report){
        ReportPic pic = new ReportPic();
        pic.imgUrl = imgUrl;
        pic.report = report;
        return pic.save();
    }
}
