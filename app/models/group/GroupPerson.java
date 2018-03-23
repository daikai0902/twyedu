package models.group;

import models.BaseModel;
import models.WePerson;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
@Entity
public class GroupPerson extends BaseModel{


    @ManyToOne
    public OrganizeGroup group;

    @ManyToOne
    public WePerson person;



    public static GroupPerson add(OrganizeGroup group,WePerson person){
        GroupPerson groupPerson = new GroupPerson();
        groupPerson.group= group;
        groupPerson.person = person;
        return groupPerson.save();
    }


    public static GroupPerson findAdminbyPerson(WePerson person){
        return find(" select gp from GroupPerson gp where gp.group.isDeleted = 0 and gp.person.isDeleted = 0 " +
                "   and gp.person=? and gp.person.class = 'SysAdmin' ",person).first();
    }


    public static GroupPerson findTeacherbyPerson(WePerson person){
        return find(" select gp from GroupPerson gp where gp.group.isDeleted = 0 and gp.person.isDeleted = 0 " +
                "   and gp.person=? and gp.person.class = 'Teacher' ",person).first();
    }


    public static GroupPerson findAdminbyGroup(OrganizeGroup group){
        return find(" select gp from GroupPerson gp where gp.group.isDeleted = 0 and gp.person.isDeleted = 0 " +
                "   and gp.group=? and gp.person.class = 'SysAdmin' ",group).first();
    }


    public static List<GroupPerson> findTeachersbyGroup(OrganizeGroup group){
        return find(" select gp from GroupPerson gp where gp.group.isDeleted = 0 and gp.person.isDeleted = 0 " +
                "   and gp.group=? and gp.person.class = 'Teacher' ",group).fetch();
    }

}
