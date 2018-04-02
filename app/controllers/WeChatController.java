package controllers;

import com.google.gson.Gson;
import models.CourseStudent;
import models.PayNotify;
import utils.XMLUtil;
import vo.OrderResult;
import vo.Result;

/**
 * @autor kevin.dai
 * @Date 2018/3/14
 */
public class WeChatController extends BaseController{



    // 微信回执调用格式：
    public static void payNotify() {
        String resultXML = XMLUtil.inputStream2String(request.body);
        String result = XMLUtil.parseXML(resultXML).toString();
        PayNotify payNotify = new Gson().fromJson(result.toString(),
                PayNotify.class);
        payNotify.saveOne();
        CourseStudent courseStudent = CourseStudent.findByOrderNum(payNotify.out_trade_no);
        if (null != courseStudent) {
            courseStudent.setPayStatus(CourseStudent.PayStatus.已支付);
            renderJSON("success");
        } else {
            renderJSON("fail");
        }

    }






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
