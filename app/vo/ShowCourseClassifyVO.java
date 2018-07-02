package vo;

import models.ShowCourseClassify;
import models.ShowCourseGroup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/6/30
 */
public class ShowCourseClassifyVO extends OneData{

    public String name;

    public Long courseNum;

    public String coverUrl;

    public ShowCourseClassifyVO(ShowCourseClassify showCourseClassify){
        this.name = showCourseClassify.name;
        this.id = showCourseClassify.id;
        this.courseNum = ShowCourseGroup.countCourseByClassify(showCourseClassify);
        this.coverUrl =(ShowCourseGroup.findCourseByClassify(showCourseClassify,1,1) == null ||
                ShowCourseGroup.findCourseByClassify(showCourseClassify,1,1).size() <1) ?""
                :ShowCourseGroup.findCourseByClassify(showCourseClassify,1,1).get(0).coverUrl;
    }


    public static List<ShowCourseClassifyVO> toList(List<ShowCourseClassify> classifies){
        if(classifies.isEmpty()){
            return Collections.emptyList();
        }
        return classifies.stream().map(c -> new ShowCourseClassifyVO(c)).collect(Collectors.toList());
    }
}
