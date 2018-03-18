package models.group;

import models.BaseModel;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */

@Entity
public class OrganizeGroup extends BaseModel{


    public String name;

    @Lob
    public String address;


    public String telphone;

    public int indexOrder;


    public static OrganizeGroup add(String name,String address,String telphone){
        OrganizeGroup group = new OrganizeGroup();
        group.name = name;
        group.address = address;
        group.telphone = telphone;
        group.indexOrder = findMaxIndex()+1;
        return group.save();
    }


    public void edit(String name,String address,String telphone){
        if(!StringUtils.equals(name,this.name)){
            this.name = name;
        }
        if(!StringUtils.equals(address,this.address)){
            this.address = address;
        }
        if(!StringUtils.equals(telphone,this.telphone)){
            this.telphone = telphone;
        }
        this.save();
    }

    public static int findMaxIndex() {
        Integer count = OrganizeGroup.find("select max(indexOrder) from OrganizeGroup").first();
        return count == null?0:count;
    }



    public static List<OrganizeGroup> fetchAll(){
        return find(defaultCondition()).fetch();
    }











}
