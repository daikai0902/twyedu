package vo;

import models.ClazzStudent;
import models.WeChatMember;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/4/4
 */
public class WeChatMemberVO extends OneData{


    public String openId;

    public String studentId;


    public List<ClazzStudentVO> clazzStudentVOS;

    public WeChatMemberVO(WeChatMember chatMember){
        this.openId = chatMember.openId;
        this.studentId = chatMember.studentIds;
        List<ClazzStudent> clazzStudents = new ArrayList<>();
        if(StringUtils.isNotBlank(chatMember.studentIds)){
            for(String s:chatMember.studentIds.split(",")){
                ClazzStudent clazzStudent = ClazzStudent.findByStudent(Long.valueOf(s));
                clazzStudents.add(clazzStudent);
            }
            this.clazzStudentVOS = ClazzStudentVO.list(clazzStudents);
        }

    }
}
