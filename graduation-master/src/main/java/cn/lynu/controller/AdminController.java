package cn.lynu.controller;

import cn.lynu.model.*;
import cn.lynu.service.AdminService;
import cn.lynu.service.TeacherService;
import cn.lynu.service.YansoouService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private YansoouService yansoouService;

    @ResponseBody
    @RequestMapping("/getStudent")
    public List<Student> getStudent(){
        return adminService.getStudent();
    }

    @ResponseBody
    @RequestMapping("/findAdmin")
    public Admin findAdmin(HttpSession session) {
        User user=(User) session.getAttribute("user");
        System.out.println(user);
        if(user!=null) {
            Admin admin = adminService.getAdminByUserId(user.getUserId());
            System.out.println(admin.getAdminId());
            return admin;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value="/getAllStudent",method= RequestMethod.GET)
    public PageInfo<Student> getAllStudent(@RequestParam(defaultValue ="1")int pageNum,@RequestParam(defaultValue="8")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Student> list=adminService.getStudent();
        return new PageInfo<Student>(list);
    }

    @ResponseBody
    @RequestMapping(value="/getAllTeacher",method= RequestMethod.GET)
    public PageInfo<Teacher> getAllTeacher(@RequestParam(defaultValue ="1")int pageNum,@RequestParam(defaultValue="8")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Teacher> list=adminService.getAllTeacher();
        return new PageInfo<Teacher>(list);
    }

    @ResponseBody
    @RequestMapping(value="/getAllTeacherYansou",method= RequestMethod.GET)
    public List<YansouTeacher> getAllTeacherYansou(){
        List<Teacher> teacher=adminService.getAllTeacher();
        List<YansouTeacher> yansouTeachers=new LinkedList<>();
        for (int i=0;i<teacher.size();i++){
            if(yansoouService.selectYansouInfoBytid(teacher.get(i).getTeacherId())!=null){
                YansouTeacher yansouTeacher=yansoouService.selectYansouInfoBytid(teacher.get(i).getTeacherId());
                yansouTeacher.setYansouTeam(yansoouService.selectYanSouInfoByid(yansouTeacher.getYansouTeamId()));
                yansouTeacher.setTeacher(teacher.get(i));
                yansouTeachers.add(yansouTeacher);
            }
            else {
                YansouTeacher yansouTeacher=new YansouTeacher();
                Teacher teacher1=teacher.get(i);
                yansouTeacher.setTeacher(teacher1);
                yansouTeachers.add(yansouTeacher);
            }
        }
        return  yansouTeachers;
    }

    @ResponseBody
    @RequestMapping(value="/updateTeacherYansou",method= RequestMethod.POST)
    public boolean updateTeacherYansou(String teacherId,String yansouteamName,String isLeader){
        YansouTeacher yansouTeacher=yansoouService.selectYansouInfoBytid(teacherId);
        if (yansouteamName.equals("空")){
            if (yansouTeacher!=null){
                return yansoouService.deleteYansouBytid(teacherId);
            }
        }else{
            if (yansouTeacher==null){
                YansouTeacher yansouTeacher1=new YansouTeacher();
                yansouTeacher1.setTeacherId(teacherId);
                if(yansouteamName.equals("验收一组")){
                    yansouTeacher1.setYansouTeamId(1);
                }
                else if(yansouteamName.equals("验收二组")){
                    yansouTeacher1.setYansouTeamId(2);
                }else if(yansouteamName.equals("验收三组")){
                    yansouTeacher1.setYansouTeamId(3);
                }else if(yansouteamName.equals("验收四组")){
                    yansouTeacher1.setYansouTeamId(4);
                }
                if(isLeader.equals("是")){
                    yansouTeacher1.setIsLeader(1);
                }else if(isLeader.equals("否")) {
                    yansouTeacher1.setIsLeader(0);
                }
                else {
                    return false;
                }
              return  yansoouService.insertYansouTeacher(yansouTeacher1);
            }
            else {
                yansouTeacher.setTeacherId(teacherId);
                if(yansouteamName.equals("验收一组")){
                    yansouTeacher.setYansouTeamId(1);
                }
                else if(yansouteamName.equals("验收二组")){
                    yansouTeacher.setYansouTeamId(2);
                }else if(yansouteamName.equals("验收三组")){
                    yansouTeacher.setYansouTeamId(3);
                }else if(yansouteamName.equals("验收四组")){
                    yansouTeacher.setYansouTeamId(4);
                }
                if(isLeader.equals("是")){
                    yansouTeacher.setIsLeader(1);
                }else if(isLeader.equals("否")) {
                    yansouTeacher.setIsLeader(0);
                }
                else {
                    return false;
                }
                return yansoouService.updateYansouTeacher(yansouTeacher);
            }
        }
        return true;
    }
}
