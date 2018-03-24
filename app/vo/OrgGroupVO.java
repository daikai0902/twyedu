package vo;

import models.group.GroupPerson;
import models.group.OrganizeGroup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
public class OrgGroupVO extends OneData{


    public String name;

    public String address;

    public String telphone;

    public Long groupId;

    public Long adminId;

    public String adminName;

    public int indexOrder;

    public boolean ispublic;


    public OrgGroupVO(){

    }



    public OrgGroupVO(OrganizeGroup group){
        this.indexOrder = group.indexOrder;
        this.name = group.name;
        this.address = group.address;
        this.telphone = group.telphone;
        this.groupId = group.id;
        this.ispublic = group.ispublic;
        GroupPerson groupPerson = GroupPerson.findAdminbyGroup(group);
        if(groupPerson != null && groupPerson.person != null){
            this.adminId = groupPerson.person.id;
            this.adminName = groupPerson.person.name;
        }
    }


    public static  List<OrgGroupVO> list(List<OrganizeGroup> groups){
        if(groups.isEmpty()){
           return Collections.emptyList();
        }
        return groups.stream().map(g -> new OrgGroupVO(g)).collect(Collectors.toList());
    }






}
