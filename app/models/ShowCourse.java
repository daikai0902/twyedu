package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */

@Entity
public class ShowCourse extends BaseModel{


    public String name;

    public String coverUrl;


    public String age;


    public String zc;//周次


    public String ksl;//课时量


    public String sc;//时长


    @Lob
    public String intro;


    public static ShowCourse add(String name,String coverUrl,String age,String zc,String ksl,String sc,String intro){
        ShowCourse showCourse = new ShowCourse();
        showCourse.name = name;
        showCourse.coverUrl = coverUrl;
        showCourse.age = age;
        showCourse.zc = zc;
        showCourse.ksl = ksl;
        showCourse.sc  = sc;
        showCourse.intro = intro;
        return showCourse.save();
    }


    public void edit(String name,String coverUrl,String age,String zc,String ksl,String sc,String intro){
        this.name = name;
        this.coverUrl = coverUrl;
        this.age = age;
        this.zc = zc;
        this.ksl = ksl;
        this.sc = sc;
        this.intro = intro;
        this.save();
    }


    public static List<ShowCourse> fetchAll(){
        return ShowCourse.find(defaultCondition()).fetch();
    }


    public static List<ShowCourse> fetchAllBySize(int page,int size){
        return ShowCourse.find(getDefaultContitionSql(" 1=1 order by createTime desc ")).fetch(page,size);
    }


    public static Long countAll(){
        return count(getDefaultContitionSql(" 1= 1"));
    }





}
