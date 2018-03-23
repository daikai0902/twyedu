package controllers;

import models.*;
import models.group.GroupPerson;
import models.group.OrganizeGroup;
import models.member.AccessToken;
import models.member.Student;
import models.member.SysAdmin;
import models.member.Teacher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import play.Play;
import utils.ExcelUtil;
import vo.*;

import java.io.File;
import java.util.List;

/**
 * @autor kevin.dai
 * @Date 2018/3/15
 */

public class APIController extends BaseController{

    public static final String BASE_URL = Play.configuration.getProperty("application.baseUrl");


    /**
     * 报名
     * @Date: 00:30 2018/3/15
     */
    public static void signup(String name,Integer age,String sex,String clothsize,String shoessize,String momname,
                              String momphone,String dadname,String dadphone,String nursery,String address,String course,String remark){
        if(StringUtils.isBlank(name)){
            renderJSON(Result.failed(Result.StatusCode.STUDENT_NAME_NULL));
        }
        Student student= Student.signup(name,age,sex,clothsize,shoessize,momname,momphone,dadname,dadphone,nursery,address,course,remark);
        renderJSON(Result.succeed(new StudentVO(student)));
    }




    //************************后台***************************


    /**
     * 登录
     * @Date: 14:14 2018/3/17
     */
    public static void login(String username, String password) {
        WePerson person = WePerson.findByName(username);
        if (person != null){
            if(StringUtils.equals(password,person.password)){
                if(!person.isSysAdmin()){
                    renderJSON(Result.failed(Result.StatusCode.PERSON_NOT_ADMIN));
                }
                AccessToken accessToken = AccessToken.regist(person);
                renderJSON(Result.succeed(new WePersonVO(accessToken)));
            }else
                renderJSON(Result.failed(Result.StatusCode.ERROR_PWD));
        }else {
            person = WePerson.findByPhone(username);
            if(person != null){
                if(StringUtils.equals(password,person.password)){
                    if(!person.isTeacher()){
                        renderJSON(Result.failed(Result.StatusCode.PERSON_NOT_TEACHER));
                    }
                    AccessToken accessToken = AccessToken.regist(person);
                    renderJSON(Result.succeed(new WePersonVO(accessToken)));
                }else
                    renderJSON(Result.failed(Result.StatusCode.ERROR_PWD));
            }else{
                renderJSON(Result.failed(Result.StatusCode.PERSON_NOT_FOUND));
            }
        }
    }

    /**
     * 增加网点
     * @Date: 22:15 2018/3/15
     */
    public static void addOrg(String name,String address,String telphone,String adminname,String password){
        OrganizeGroup group = OrganizeGroup.add(name,address,telphone);
        WePerson person = SysAdmin.add(adminname,password, SysAdmin.AdminType.网点管理员);
        GroupPerson.add(group,person);
        renderJSON(Result.succeed(new OrgGroupVO(group)));
    }


    /**
     * 删除网点
     * @Date: 14:25 2018/3/17
     */
    public static void delOrg(Long groupId){
        OrganizeGroup group = OrganizeGroup.findById(groupId);
        group.logicDelete();
        renderJSON(Result.succeed());
    }



    /**
     * 编辑网点
     * @Date: 14:40 2018/3/17
     */
    public static void editOrg(Long groupId,String name,String address,String telphone,String password){
        OrganizeGroup group = OrganizeGroup.findById(groupId);
        group.edit(name,address,telphone);
        GroupPerson groupPerson = GroupPerson.findAdminbyGroup(group);
        if(groupPerson.person != null){
            WePerson person = groupPerson.person;
            person.eidtPwd(password);
        }
        renderJSON(Result.succeed(new OrgGroupVO(group)));
    }



    /**
     * 网点列表
     * @Date: 15:59 2018/3/17
     */
    public static void orgList(){
        List<OrganizeGroup> groups = OrganizeGroup.fetchAll();
        renderJSON(Result.succeed(new PageData(OrgGroupVO.list(groups))));
    }



    /**
     * 新增活动
     * @Date: 16:18 2018/3/17
     */
    public static void addActivity(String name,String time,String address,String content,String videoUrl){
        Activity activity =  Activity.add(name,time,address,content,videoUrl);
        renderJSON(Result.succeed(new ActivityVO(activity)));
    }


    /**
     * 新增活动链接
     * @Date: 16:18 2018/3/17
     */
    public static void addActivityLink(String name,String link){
        Activity activity =  Activity.addLink(name, link);
        renderJSON(Result.succeed(new ActivityVO(activity)));
    }



    /**
     * 删除活动
     * @Date: 16:18 2018/3/17
     */
    public static void delActivity(Long activityId){
        Activity activity = Activity.findById(activityId);
        activity.logicDelete();
        renderJSON(Result.succeed());
    }



    /**
     * 编辑活动
     * @Date: 16:18 2018/3/17
     */
    public static void editActivity(Long activityId,String name,String time,String address,String content,String videoUrl){
        Activity activity = Activity.findById(activityId);
        activity.edit(name,time,address,content,videoUrl);
        renderJSON(Result.succeed(new ActivityVO(activity)));
    }



    /**
     * 编辑活动链接
     * @Date: 16:18 2018/3/17
     */
    public static void editActivityLink(Long activityId,String name,String link){
        Activity activity = Activity.findById(activityId);
        activity.editLink(name,link);
        renderJSON(Result.succeed(new ActivityVO(activity)));
    }




    /**
     * 活动列表
     * @Date: 16:18 2018/3/17
     */
    public static void activityList(){
        List<Activity> activities = Activity.fetchAll();
        renderJSON(Result.succeed(new PageData(ActivityVO.list(activities))));
    }




    /**
     * 名师新增
     * @Date: 16:18 2018/3/17
     */
    public static void addShowTeachers(String name,String imgUrl,String job,String intro){
        ShowTeachers showTeachers = ShowTeachers.add(name,imgUrl,job,intro);
        renderJSON(Result.succeed(new ShowTeachersVO(showTeachers)));
    }


    /**
     * 名师删除
     * @Date: 16:18 2018/3/17
     */
    public static void delShowTeachers(Long showId){
        ShowTeachers showTeachers = ShowTeachers.findById(showId);
        showTeachers.logicDelete();
        renderJSON(Result.succeed());
    }


    /**
     * 名师编辑
     * @Date: 16:18 2018/3/17
     */
    public static void editShowTeachers(Long showId,String name,String imgUrl,String job,String intro){
        ShowTeachers showTeachers = ShowTeachers.findById(showId);
        showTeachers.edit(name,imgUrl,job,intro);
        renderJSON(Result.succeed(new ShowTeachersVO(showTeachers)));
    }



    /**
     * 名师列表
     * @Date: 16:18 2018/3/17
     */
    public static void showTeachersList(){
        List<ShowTeachers> showTeachers = ShowTeachers.fetchAll();
        renderJSON(Result.succeed(new PageData(ShowTeachersVO.list(showTeachers))));

    }





    public static void addShowOpenClazz(String name,String time,String address,String content,String videoUrl){
        ShowOpenClazz showOpenClazz =  ShowOpenClazz.add(name,time,address,content,videoUrl);
        renderJSON(Result.succeed(new ShowOpenClazzVO(showOpenClazz)));
    }



    public static void delShowOpenClazz(Long showId){
        ShowOpenClazz showOpenClazz = ShowOpenClazz.findById(showId);
        showOpenClazz.logicDelete();
        renderJSON(Result.succeed());
    }


    public static void editShowOpenClazz(Long showId,String name,String time,String address,String content,String videoUrl){
        ShowOpenClazz showOpenClazz = ShowOpenClazz.findById(showId);
        showOpenClazz.edit(name,time,address,content,videoUrl);
        renderJSON(Result.succeed(new ShowOpenClazzVO(showOpenClazz)));
    }


    public static void showOpenClazzList(){
        List<ShowOpenClazz> showOpenClazzes = ShowOpenClazz.fetchAll();
        renderJSON(Result.succeed(new PageData(ShowOpenClazzVO.list(showOpenClazzes))));
    }




    public static void addShowCourse(String name,String coverUrl,String age,String zc,String ksl,String sc,String intro){
        ShowCourse showCourse =  ShowCourse.add(name,coverUrl,age,zc,ksl,sc,intro);
        renderJSON(Result.succeed(new ShowCourseVO(showCourse)));
    }



    public static void delShowCourse(Long showId){
        ShowCourse showCourse = ShowCourse.findById(showId);
        showCourse.logicDelete();
        renderJSON(Result.succeed());
    }


    public static void editShowCourse(Long showId,String name,String coverUrl,String age,String zc,String ksl,String sc,String intro){
        ShowCourse showCourse = ShowCourse.findById(showId);
        showCourse.edit(name,coverUrl,age,zc,ksl,sc,intro);
        renderJSON(Result.succeed(new ShowCourseVO(showCourse)));
    }


    public static void showCourseList(){
        List<ShowCourse> showCourses = ShowCourse.fetchAll();
        renderJSON(Result.succeed(new PageData(ShowCourseVO.list(showCourses))));
    }


    /**
     * 新增新闻
     * @Date: 16:18 2018/3/17
     */
    public static void addNews(String name,String content,String videoUrl){
        News news =  News.add(name,content,videoUrl);
        renderJSON(Result.succeed(new NewsVO(news)));
    }


    /**
     * 新增新闻链接
     * @Date: 16:18 2018/3/17
     */
    public static void addNewsLink(String name,String link){
        News news =  News.addLink(name, link);
        renderJSON(Result.succeed(new NewsVO(news)));
    }



    /**
     * 删除新闻
     * @Date: 16:18 2018/3/17
     */
    public static void delNews(Long newsId){
        News news = News.findById(newsId);
        news.logicDelete();
        renderJSON(Result.succeed());
    }



    /**
     * 编辑新闻
     * @Date: 16:18 2018/3/17
     */
    public static void editNews(Long newsId,String name,String content,String videoUrl){
        News news = News.findById(newsId);
        news.edit(name,content,videoUrl);
        renderJSON(Result.succeed(new NewsVO(news)));
    }



    /**
     * 编辑新闻链接
     * @Date: 16:18 2018/3/17
     */
    public static void editNewsLink(Long newsId,String name,String link){
        News news = News.findById(newsId);
        news.editLink(name,link);
        renderJSON(Result.succeed(new NewsVO(news)));
    }




    /**
     * 新闻列表
     * @Date: 16:18 2018/3/17
     */
    public static void newsList(){
        List<News> newsList = News.fetchAll();
        renderJSON(Result.succeed(new PageData(NewsVO.list(newsList))));
    }


    //************************网点管理****************************************


    public static void getOrgInfo(){
        WePerson person = getPersonByToken();
        GroupPerson groupPerson  = GroupPerson.findAdminbyPerson(person);
        if(groupPerson == null){
            renderJSON(Result.failed(Result.StatusCode.PERSON_NOT_ADMIN));
        }
        renderJSON(Result.succeed(new OrgGroupVO(groupPerson.group)));
    }


    /**
     * 新增课程
     * @Date: 23:47 2018/3/21
     */
    public static void addCourse(Long groupId,String name,String feeType,String fee){
        OrganizeGroup group = OrganizeGroup.findById(groupId);
        Course course = Course.add(group,name,feeType,fee);
        renderJSON(Result.succeed(new CourseVO(course)));
    }



    /**
     * 课程列表
     * @Date: 00:39 2018/3/22
     */
    public static void courseList(Long groupId){
        List<Course> courses = Course.findByGroup(groupId);
        renderJSON(Result.succeed(new PageData(CourseVO.list(courses))));
    }


    /**
     * 删除课程
     * @Date: 00:48 2018/3/22
     */
    public static void deleteCourse(Long courseId){
        Course course = Course.findById(courseId);
        course.logicDelete();
        renderJSON(Result.succeed());
    }


    /**
     * 编辑课程
     * @Date: 00:53 2018/3/22
     */
    public static void editCourse(Long courseId,String name,String feeType,String fee){
        Course course = Course.findById(courseId);
        course.edit(name,feeType,fee);
        renderJSON(Result.succeed(new CourseVO(course)));
    }


    /**
     * 发布课程
     * @Date: 00:46 2018/3/22
     */
    public static void releaseCourse(Long courseId,boolean status){
        Course course = Course.findById(courseId);
        course.setRelease(status);
        renderJSON(Result.succeed(new CourseVO(course)));
    }


    /**
     * 预约课程
     * @Date: 00:50 2018/3/22
     */
    public static void orderCourse(Long courseId,boolean status){
        Course course = Course.findById(courseId);
        course.setOrder(status);
        renderJSON(Result.succeed(new CourseVO(course)));
    }


    /**
     * 新增老师
     * @Date: 01:23 2018/3/22
     */
    public static void addTeacher(Long groupId,String name,String cellphone,String IDcard,String imgUrl){
        OrganizeGroup group = OrganizeGroup.findById(groupId);
        Teacher teacher = Teacher.add(name,cellphone,IDcard,imgUrl);
        GroupPerson gp = GroupPerson.add(group,teacher);
        renderJSON(Result.succeed(new GroupTeacherVO(gp)));
    }



    /**
     * 删除老师
     * @Date: 19:05 2018/3/23
     */
    public static void delTeacher(Long teacherId){
        Teacher teacher = Teacher.findById(teacherId);
        GroupPerson gp = GroupPerson.findTeacherbyPerson(teacher);
        gp.logicDelete();
        teacher.logicDelete();
        renderJSON(Result.succeed());
    }



    /**
     * 教师列表
     * @Date: 21:52 2018/3/22
     */
    public static void teacherList(Long groupId){
        OrganizeGroup group = OrganizeGroup.findById(groupId);
        List<GroupPerson> groupPersonList = GroupPerson.findTeachersbyGroup(group);
        renderJSON(Result.succeed(new PageData(GroupTeacherVO.list(groupPersonList))));
    }




    /**
     * 查看班级列表
     * @Date: 19:55 2018/3/23
     */
    public static void teacherClazzs(Long teacherId){
        Teacher teacher = Teacher.findById(teacherId);
        List<Clazz> clazzes = Clazz.findByTeacher(teacher);
        renderJSON(Result.succeed(new PageData(ClazzVO.list(clazzes))));
    }


    /**
     * 新增班级
     * @Date: 21:49 2018/3/22
     */
    public static void addClazz(String name,Long courseId,Long teacherAId,Long teacherBId,String num,String time,String duration){
        Clazz clazz = Clazz.add(name,Course.findById(courseId),Teacher.findById(teacherAId),
                    Teacher.findById(teacherBId),num,time,duration);
        renderJSON(Result.succeed(new ClazzVO(clazz)));
    }




    /**
     * 班级列表
     * @Date: 20:25 2018/3/23
     */
    public static void clazzList(Long groupId){
        List<Course> courses = Course.findByGroup(groupId);
        renderJSON(Result.succeed(new PageData(CourseVO.listClazz(courses))));
    }



    /**
     * 班级删除
     * @Date: 20:25 2018/3/23
     */
    public static void delClazz(Long clazzId){
        Clazz clazz = Clazz.findById(clazzId);
        clazz.logicDelete();
        renderJSON(Result.succeed());
    }


    /**
     * 班级编辑
     * @Date: 20:25 2018/3/23
     */
    public static void editClazz(Long clazzId,String name,Long courseId,Long teacherAId,
                                    Long teacherBId,String num,String time,String duration){
        Clazz clazz = Clazz.findById(clazzId);
        clazz.edit(name,Course.findById(courseId),Teacher.findById(teacherAId),
                Teacher.findById(teacherBId),num,time,duration);
        renderJSON(Result.succeed(new ClazzVO(clazz)));
    }


    /**
     * 新增学生
     * @Date: 22:31 2018/3/22
     */
    public static void addStudent(Long clazzId,String name,Integer age,String sex,String clothsize,String shoessize,String momname,
                                  String momphone,String dadname,String dadphone,String nursery,String address){
        Student student =  Student.add(name,age,sex,clothsize,shoessize,momname,momphone,dadname,dadphone,nursery,address);
        Clazz clazz = Clazz.findById(clazzId);
        ClazzStudent.add(clazz,student);
        renderJSON(Result.succeed(new StudentVO(student)));
    }




    /**
     * 删除学生
     * @Date: 22:10 2018/3/23
     */
    public  static void deletStudent(Long id){
        ClazzStudent cs = ClazzStudent.findById(id);
        cs.logicDelete();
        renderJSON(Result.succeed());
    }




    /**
     * 班级列表
     * @Date: 21:09 2018/3/23
     */
    public static void studentList(Long clazzId){
        List<ClazzStudent> clazzStudents = ClazzStudent.findClazz(clazzId);
        renderJSON(Result.succeed(new PageData(ClazzStudentVO.list(clazzStudents))));
    }




    public static void exportStudents(Long clazzId) throws Exception{
        Clazz clazz = Clazz.findById(clazzId);
        List<ClazzStudent> clazzStudents = ClazzStudent.findClazz(clazzId);
        List<ClazzStudentVO> vos = ClazzStudentVO.list(clazzStudents);
        File templateFile = Play.getFile("documentation/student-list.xls");
        File resultFile = Play.getFile("/tmp/" +clazz.name+"学生列表"+ ".xls");
        if (!resultFile.exists()) {
            FileUtils.copyFile(templateFile, resultFile);
        }
        resultFile.setReadable(true, false);
        resultFile.setWritable(true, false);
        ExcelUtil.write(resultFile,vos);
        renderJSON(Result.succeed(new OneValueData(BASE_URL + "/tmp/" + resultFile.getName())));
    }


    /**
     * 课程下的班级
     * @Date: 22:21 2018/3/23
     */
    public static void getClazz(Long courseId){
        List<Clazz> clazzes = Clazz.findbyCourse(Course.findById(courseId));
        renderJSON(Result.succeed(new PageData(ClazzVO.list(clazzes))));
    }



    /**
     * 学生新分配到班
     * @Date: 00:01 2018/3/23
     */
    public static void addToClazz(Long courseStudentId,Long clazzId){
        CourseStudent cs = CourseStudent.findById(courseStudentId);
        ClazzStudent.add(Clazz.findById(clazzId),cs.student);
        //删除
        cs.logicDelete();
        //同时新增成绩单
        Report.add(cs.student,null,null,null);
        renderJSON(Result.succeed());
    }




    /**
     * 从报名的课程里删除
     * @Date: 23:01 2018/3/23
     */
    public static void delFromCourse(Long courseStudentId){
        CourseStudent cs = CourseStudent.findById(courseStudentId);
        cs.logicDelete();
        renderJSON(Result.succeed());
    }


    /**
     * 微信报名和预约报名
     * @Date: 00:18 2018/3/23
     */
    public static void wxStudentList(Long courseId,String type){
        List<CourseStudent> courseStudents = CourseStudent.findByCourse(courseId,type);
        renderJSON(Result.succeed(new PageData(CourseStudentVO.list(courseStudents))));
    }





    //****************************教师*******************************

    /**
     * 点到
     * @Date: 13:55 2018/3/23
     */
    public static void arrive(Long csId,String arrive){
        ClazzStudent student = ClazzStudent.findById(csId);
        student.editArrive(arrive);
        renderJSON(Result.succeed());
    }



    /**
     * 编辑成绩单
     * @Date: 14:37 2018/3/23
     */
    public static void saveReport(Long studentId,String comment,String starComment,String imgUrls,String status){
        Student student = Student.findById(studentId);
        Report report = Report.findByStudent(student);
        report.edit(comment,starComment,imgUrls, Report.Status.valueOf(status));
        renderJSON(Result.succeed());
    }



    /**
     * 发送成绩单
     * @Date: 14:57 2018/3/23
     */
    public static void sendReport(Long clazzId){
        List<Student> students = ClazzStudent.findByClazz(clazzId);
        Long undoNum = Report.countByStudents(students, Report.Status.已完成);
        if(students.size() != undoNum){
            renderJSON(Result.failed(Result.StatusCode.REPORT_NOT_FINISH));
        }
        List<Report> reports = Report.fetchByStudents(students);
        for (Report report:reports){
            report.setIsSend();
        }
        renderJSON(Result.succeed());
    }





}
