package controllers;

import com.google.gson.Gson;
import models.CourseStudent;
import models.PayNotify;
import models.WeChatMember;
import models.member.Student;
import org.apache.commons.lang.StringUtils;
import utils.WeChatUtils;
import utils.XMLUtil;
import vo.OrderResult;
import vo.Result;
import vo.WeChatMemberVO;

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



    public static void getOpenId(String code){
        if(StringUtils.isBlank(code)){
            renderJSON(Result.failed(Result.StatusCode.NULL_WX_CODE));
        }
        String openId =  WeChatUtils.getOAuthOpenIdFromRemote(code);
        WeChatMember chatMember = WeChatMember.findByOpenId(openId);
        if (chatMember == null){
            chatMember = WeChatMember.add(openId);
        }
        renderJSON(Result.succeed(new WeChatMemberVO(chatMember)));
    }



    /**
     * 获取学生信息
     * @Date: 22:07 2018/4/7
     */
    public static void students(String openId){
        WeChatMember chatMember =WeChatMember.findByOpenId(openId);
        if(chatMember != null){
            renderJSON(Result.succeed(new WeChatMemberVO(chatMember)));
        }
    }



    public  static void bindStudent(String openId,String number){
        Student student =Student.findByNum(number);
        if(student == null){
            renderJSON(Result.failed(Result.StatusCode.STUDENT_NOT_EXITS));
        }
        WeChatMember chatMember =WeChatMember.findByOpenId(openId);
        if(chatMember != null){
            if(StringUtils.isNotBlank(chatMember.studentIds)){
                if(chatMember.studentIds.contains(student.id+"")){
                    renderJSON(Result.succeed(new WeChatMemberVO(chatMember)));
                }
                String studentIds = chatMember.studentIds+","+student.id;
                chatMember.setStudentIds(studentIds);
            }else {
                chatMember.setStudentIds(student.id+"");
            }
        }
        renderJSON(Result.succeed(new WeChatMemberVO(chatMember)));

    }






    /**
     * 统一下单
     * @Date: 21:21 2018/4/2
     */
    public static void unifieOrder(Long courseStudentId,String currentUrl,String openId){

        CourseStudent cs = CourseStudent.findById(courseStudentId);
        if(cs != null){
            cs.genOrderNum();
            OrderResult orderResult = OrderResult.unifeOrder(cs.course.name,(int)(Double.valueOf(cs.course.fee)*100),cs.orderNum,currentUrl,"JSAPI",openId);
            renderJSON(Result.succeed(orderResult));
        }
    }






}
