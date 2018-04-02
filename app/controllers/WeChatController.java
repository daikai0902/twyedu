package controllers;

import models.CourseStudent;
import vo.OrderResult;
import vo.Result;

/**
 * @autor kevin.dai
 * @Date 2018/3/14
 */
public class WeChatController extends BaseController{









    /**
     * 统一下单
     * @Date: 21:21 2018/4/2
     */
    public static void unifieOrder(Long courseStudentId,String currentUrl){

        CourseStudent cs = CourseStudent.findById(courseStudentId);
        if(cs != null){
            cs.genOrderNum();
            OrderResult orderResult = OrderResult.unifeOrder(cs.course.name,Integer.valueOf(cs.course.fee)*100,cs.orderNum,currentUrl,"JSAPI");
            renderJSON(Result.succeed(orderResult));
        }
    }






}
