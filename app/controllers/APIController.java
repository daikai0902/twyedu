package controllers;

import cn.bran.play.JapidController;
import models.member.Student;
import org.apache.commons.lang.StringUtils;
import vo.Result;
import vo.StudentVO;

/**
 * @autor kevin.dai
 * @Date 2018/3/15
 */

public class APIController extends JapidController{


    /**
     * 报名
     * @Date: 00:30 2018/3/15
     */
    public static void signup(String name,Integer age,boolean sex,String clothsize,String shoessize,String momname,
                              String momphone,String dadname,String dadphone,String nursery,String address,String course,String remark){
        if(StringUtils.isBlank(name)){
            renderJSON(Result.failed(Result.StatusCode.STUDENT_NAME_NULL));
        }
        Student student= Student.signup(name,age,sex,clothsize,shoessize,momname,momphone,dadname,dadphone,nursery,address,course,remark);
        renderJSON(Result.succeed(new StudentVO(student)));
    }






}
