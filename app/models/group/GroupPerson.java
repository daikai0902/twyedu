package models.group;

import models.BaseModel;
import models.WePerson;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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



    public static GroupPerson findbyGroup(OrganizeGroup group){
        return find(getDefaultContitionSql(" group = ? "),group).first();
    }

}
