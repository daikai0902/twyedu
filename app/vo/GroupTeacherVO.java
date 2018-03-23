package vo;

import models.Clazz;
import models.group.GroupPerson;
import models.member.Teacher;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/22
 */
public class GroupTeacherVO extends OneData{

    public String name;

    public String cellphone;

    public String number;//工号

    public String imgUrl;//头像

    public String IDcard;//身份证


    public Long clazzNum;//上课的班级


    public Long groupId;

    public Long teacherId;


    public GroupTeacherVO(){


    }


    public GroupTeacherVO(GroupPerson groupTeacher){
        this.name = groupTeacher.person.name;
        this.cellphone = groupTeacher.person.cellPhone;
        this.number = groupTeacher.person.number;
        this.imgUrl =((Teacher)groupTeacher.person).imgUrl;
        this.IDcard =((Teacher)groupTeacher.person).IDcard;
        this.groupId = groupTeacher.group.id;
        this.teacherId = ((Teacher) groupTeacher.person).id;
        this.clazzNum = Clazz.countClazzsByTeachers((Teacher)groupTeacher.person);
    }



    public static  List<GroupTeacherVO> list(List<GroupPerson> groupPersonList){
        if(groupPersonList.isEmpty()){
            return Collections.emptyList();
        }
        return groupPersonList.stream().map(g -> new GroupTeacherVO(g)).collect(Collectors.toList());


    }



}
