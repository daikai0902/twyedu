package models.member;

import models.WePerson;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @autor kevin.dai
 * @Date 2018/3/17
 */
@Entity
public class SysAdmin extends WePerson{


    @Enumerated(EnumType.STRING)
    public AdminType type;


    public enum AdminType{
        平台管理员,网点管理员
    }



    public static SysAdmin add(String name,String password,AdminType type){
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.name = name;
        sysAdmin.password = password;
        sysAdmin.type = type;
        return sysAdmin.save();
    }



    public static SysAdmin init(){
        if (findByType(AdminType.平台管理员) == null) {
            SysAdmin admin = new SysAdmin();
            admin.type = AdminType.平台管理员;
            admin.name = "admin";
            admin.password = "123456";
            return admin.save();
        }else
            return null;
    }




    public static SysAdmin findByType(AdminType type){
        return  find(getDefaultContitionSql(" type = ? "),type).first();
    }

}
