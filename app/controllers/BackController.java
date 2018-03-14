package controllers;

import cn.bran.play.JapidController;
import jobs.ImportGroupStudent;
import jobs.ImportGroupTeacher;
import models.ResultVO;
import models.WePerson;
import models.group.*;
import models.json.JsonData;
import models.json.JsonError;
import models.json.JsonSucceed;
import models.member.Student;
import models.member.Teacher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/14
 */
public class BackController extends JapidController{



    public static void orgList(){
        renderJapid();
    }




    /************************************** 人员管理 ***********************************************/

    // 人员管理
    public static void memberManage(Long groupId) {
        OrganizeGroup rootGroup = OrganizeGroup.findById(groupId);
        GenericGroupVO groupVO = GenericGroupVO.buildGroupTreeByGenericGroup(rootGroup);
        renderJapid(rootGroup, groupVO);
    }

    /**
     * 组织成员管理
     *
     */
    public static void invokeGroupMemberManage(Long groupId) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        List<GroupMember> groupMembers = GroupMember.fetchByGroupAndClass(group, "Teacher");
        renderJapid(group, groupMembers, "Teacher");
    }

    public static void switchGenericGroup(Long groupId) {
        renderJapidWith("@jsonGroupMember", groupId);
    }

    public static void updateGroupName(Long groupId, String groupName) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        group.updateName(groupName);
        renderJSON(new JsonSucceed());
    }

    public static void addGenericGroup(String groupName, Long parentGroupId) {
        AbstractGroup parentGroup = AbstractGroup.findById(parentGroupId);
        if (!parentGroup.isOrganizeGroup()) {
            renderJSON(new JsonError("请选中机构（学校）节点后再添加年级（系）"));
        }

        GenericGroup group = GenericGroup.addGroup(groupName, parentGroup);
        renderJSON(new JsonData(group.id + ""));
    }

    public static void addClazzGroup(String groupName, Long parentGroupId) {
        AbstractGroup parentGroup = AbstractGroup.findById(parentGroupId);
        if (!parentGroup.isGenericiGroup())
            renderJSON(new JsonError("请选中年级（系）节点后再添加班级"));

        Clazz clazz = Clazz.addClazz(groupName, parentGroup);
        renderJSON(new JsonData(clazz.id + ""));

    }

    public static void upGroup(Long groupId) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        if (group.findLast() == null)
            renderJSON(new JsonError("组织前面无同级组织，不能上移"));
        group.up();
        renderJSON(new JsonSucceed());
    }

    public static void downGroup(Long groupId) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        if (group.findNext() == null)
            renderJSON(new JsonError("组织后面无同级组织，不能下移"));
        group.down();
        renderJSON(new JsonSucceed());
    }

    public static void delGroup(Long groupId) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        if (group.isOrganizeGroup()) {
            renderJSON(new JsonError("初始组织，不能删除"));
        }
        group.del();
        renderJSON(new JsonSucceed());
    }

    public static void switchGroupMember(Long groupId, String personClass) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        List<GroupMember> groupMembers = GroupMember.fetchByGroupAndClass(group, personClass);
        renderJapidWith("@jsonGroupMemberTable", groupMembers, personClass);

    }

    public static void deleteGroupMember(Long groupId, Long[] memberIds) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        List<WePerson> persons = WePerson.fetchPersonsByIds(Arrays.asList(memberIds));
        for (WePerson person : persons) {
            person.logicDelete();
        }
        GroupMember.delUp2DownByGenericGroupAndMembers(group, persons);

        renderJSON(new JsonSucceed());
    }

    /**
     * 导入教师
     *
     * @throws Exception
     *
     */
    public static void importGroupTeacher(Long groupId, File file) throws Exception {
        OrganizeGroup group = OrganizeGroup.findById(groupId);
        String fileName = System.getProperty("user.dir") + "/tmp/" + RandomStringUtils.randomNumeric(8)
                + file.getName();
        File newfile = new File(fileName);
        FileUtils.copyFile(file, newfile);
        ImportGroupTeacher importJob = new ImportGroupTeacher(newfile, group);
        importJob.doJob();
        String message = "";
        if (StringUtils.isNotBlank(importJob.repeatEamil + "")) {
            message += importJob.repeatEamil + "等邮箱已被注册，请修改后重新引入";
        }
        if (StringUtils.isNotBlank(importJob.repeatCellPhone + "")) {
            message += importJob.repeatCellPhone + "手机号已被使用，请修改后重新引入";
        }
        renderJSON(ResultVO.succeed(message));
    }

    public static void addGroupTeahcer(Long groupId, Teacher teacher) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        if (!WePerson.isPhoneAvailable(teacher.cellPhone)) {
            renderJSON(new JsonError("手机号重复！"));
        }
		/*
		 * if (!WePerson.isEmailAvailable(teacher.email)) { renderJSON(new
		 * JsonError("邮箱已被注册！")); }
		 */
        WePerson person = Teacher.regist(teacher);
        if (person == null) {
            renderJSON(new JsonError("手机号或邮箱已被注册！"));
        }
        GroupMember.createAssociation(group, person);
        renderJSON(new JsonSucceed());
    }


    /**
     * 导入学生
     *
     * @throws Exception
     *
     */
    public static void importGroupStudent(Long groupId, File file) throws Exception {
        Clazz group = Clazz.findById(groupId);
        String fileName = System.getProperty("user.dir") + "/tmp/" + RandomStringUtils.randomNumeric(8)
                + file.getName();
        File newfile = new File(fileName);
        FileUtils.copyFile(file, newfile);
        ImportGroupStudent importJob = new ImportGroupStudent(newfile, group);
        importJob.doJob();
        String message = "";
        if (StringUtils.isNotBlank(importJob.repeatEamil + "")) {
            message += importJob.repeatEamil + "等邮箱已被注册，请修改后重新引入";
        }
        if (StringUtils.isNotBlank(importJob.repeatCellPhone + "")) {
            message += importJob.repeatCellPhone + "手机号已被使用，请修改后重新引入";
        }
        renderJSON(ResultVO.succeed(message));
    }

    public static void addGroupStudent(Long groupId, Student student) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        WePerson person = Student.registZhuji(student);
        GroupMember.createAssociation(group, person);
        renderJSON(new JsonSucceed());
    }

    public static void pullGroupTeacher(Long groupId) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        AbstractGroup parentGroup = group.parentGroup;
        List<WePerson> groupPersons = GroupMember.fetchTeachersByGroup(group);
        List<WePerson> allPersons = GroupMember.fetchTeachersByGroup(parentGroup);
        allPersons.removeAll(groupPersons);
        renderJapidWith("@jsonAddGroupPerson", allPersons);

    }

    public static void addGroupMember(Long groupId, Long[] memberIds) {
        AbstractGroup group = AbstractGroup.findById(groupId);
        List<WePerson> persons = WePerson.fetchPersonsByIds(Arrays.asList(memberIds));
        GroupMember.createAssociations(group, persons);
        renderJSON(new JsonSucceed());
    }




}
