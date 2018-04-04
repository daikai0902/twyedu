package vo;

import models.WeChatMember;

/**
 * @autor kevin.dai
 * @Date 2018/4/4
 */
public class WeChatMemberVO extends OneData{


    public String openId;

    public String studentId;

    public WeChatMemberVO(WeChatMember chatMember){
        this.openId = chatMember.openId;
        this.studentId = chatMember.studentIds;
    }
}
