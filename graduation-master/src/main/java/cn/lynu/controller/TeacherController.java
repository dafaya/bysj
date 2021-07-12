package cn.lynu.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.lynu.model.*;
import cn.lynu.service.*;
import cn.lynu.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/teacherController")
public class TeacherController {
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TeacherService teacherService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private DabianService dabianService;

	@Autowired
	private KtbgService ktbgService;

	@Autowired
	private LunwenService lunwenService;
	@Autowired
	private ZqjcService zqjcService;
	@Autowired
	private MdbService mdbService;
	@Autowired
	private AdminService adminService;
	
	@ResponseBody
	@RequestMapping(value="/getSubTeacher",method=RequestMethod.GET)
	public PageInfo<Teacher> getSubTeacher(@RequestParam(defaultValue="1")int pageNum,@RequestParam(defaultValue="8")int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<Teacher> list = teacherService.getSubTeacher();
		return new PageInfo<Teacher>(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/findTeacherByTeacherName",method=RequestMethod.GET)
	public PageInfo<Teacher> findTeacherByTeacherName(String teacherName,HttpServletResponse response,
			@RequestParam(defaultValue="1")int pageNum,@RequestParam(defaultValue="8")int pageSize){
		response.setContentType("application/json");
		PageHelper.startPage(pageNum, pageSize);
		List<Teacher> list = teacherService.findTeacherByTeacherName(teacherName);
		return new PageInfo<>(list);
	}
	
	@ResponseBody
	@RequestMapping("/findTeacherAndProject")
	public Teacher findTeacherAndProject(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				Teacher teacherPlus = teacherService.findTeacherAndProject(teacher.getTeacherId());
				if(teacherPlus!=null) {
					return teacherPlus;
				}
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/findTeacher")
	public Teacher findTeacher(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				Teacher teacherPlus = teacherService.findTeacherByTeacherId(teacher.getTeacherId());
				if(teacherPlus!=null) {
					return teacherPlus;
				}
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/getStudentByTeacherId")
	public List<Student> getStudentByTeacherId(HttpSession session){
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				return teacherService.getStudentByTeacherId(teacher.getTeacherId());
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/getStudentByTeacherId2")
	public List<Student> getStudentByTeacherId2(HttpSession session){
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				return teacherService.getStudentByTeacherId2(teacher.getTeacherId());
			}
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value="/updateTeacherInfo",method=RequestMethod.POST)
	public boolean updateTeacherInfo(Teacher teacher,
			@RequestParam(value="portrait",required=false)MultipartFile portrait,HttpServletRequest request) {
		if(portrait!=null&&portrait.getSize()>0) {
			if(portrait.getSize()>(10*1024*1024)) {
				return false;
			}
			String filename=portrait.getOriginalFilename();
			String dbPath=request.getServletContext().getContextPath()+"/portrait/"+teacher.getUser().getUserId();
			String basePath=request.getServletContext().getRealPath("/portrait/"+teacher.getUser().getUserId());
			new File(basePath).mkdir();
        	File portraitFile=new File(basePath,filename);
        	try {
				portrait.transferTo(portraitFile);
				teacher.getUser().setUserPortrait(dbPath+"/"+filename);
				return teacherService.updateTeacherInfo(teacher);
			} catch (Exception e) {
				e.printStackTrace();
			} 
        	
		}
		return teacherService.updateTeacherInfo(teacher);
	}
	
	@ResponseBody
	@RequestMapping("/getTeacherYansouInfo")
	public YansouTeam getTeacherYansouInfo(HttpSession session){
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				return teacherService.getTeacherYansouInfo(teacher.getTeacherId());
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/thisTeacherisLeader")
	public YansouTeacher thisTeacherisLeader(HttpSession session){
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			return teacherService.thisTeacherisLeader(user);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/getYansouTeamStu")
	public PageInfo<Student> getYansouTeamStu(HttpSession session,
			@RequestParam(defaultValue="1")int pageNum,	@RequestParam(defaultValue="8")int pageSize){
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				PageHelper.startPage(pageNum, pageSize);
				List<Student> list = teacherService.getYansouTeamStu(teacher.getTeacherId());
				return new PageInfo<Student>(list);
			}
		}
		return null;
	}
	@ResponseBody
	@RequestMapping("/getTeacherNum")
	public long getTeacherNum(){
		return teacherService.getTeacherNum();
	}

	@ResponseBody
	@RequestMapping(value="/updateTeacher",method=RequestMethod.POST)
	public boolean updateTeacher(Teacher teacher){
		System.out.println("updateTeacher："+teacher.getTeacherId());
		return teacherService.updateTeacher(teacher);
	}

	@ResponseBody
	@RequestMapping(value="/deleteTeacher",method=RequestMethod.POST)
	public boolean deleteTeacher(Teacher teacher){
		System.out.println("deleteTeacher:"+teacher.getTeacherId());
		List<Project> project=projectService.getProjectByTeacherId(teacher.getTeacherId());
		for(int i=0;i<project.size();i++){
			Project project1=project.get(i);
			String studentId=project1.getStudentId();
			if(studentId!=null){
				Student student=studentService.getStudentBySid(studentId);
				student.setProjectNum(0);
				studentService.updateStudentNull(student);
				if(student.getStudentScore()!=null){
					student.setStudentScore(null);
					studentService.updateStudentNull(student);
				}
				if(student.getTeacherEvaluate()!=null){
					student.setTeacherEvaluate(null);
					studentService.updateStudentNull(student);
				}

				DaBian daBian=dabianService.getDaBianByStudentId(studentId);
				if(daBian!=null){
					dabianService.deleteDabian(daBian.getId());
				}
				Ktbg ktbg=ktbgService.getKtbgByStudentId(studentId);
				if(ktbg!=null){
					ktbgService.deleteKtbgById(ktbg.getKtbgId());
				}
				Lunwen lunwen=lunwenService.getLunwenBySid(studentId);
				if(lunwen!=null){
					lunwenService.deleteLunWenById(lunwen.getLunwenId());
				}
				Zqjc zqjc=zqjcService.getzqjcByStudentId(studentId);
				if(zqjc!=null){
					zqjcService.deleteZqjcById(zqjc.getZqjcId());
				}
				Mdb mdb=mdbService.getMdbByStudentId(studentId);
				if(mdb!=null){
					mdbService.deleteMdbById(mdb.getMdbId());
				}
			}
			projectService.deleteProjectByPrimaryKey(project1.getProjectId());
		}
		return teacherService.deleteTeacher(teacher);
	}
	@ResponseBody
	@RequestMapping(value="/insertTeacher",method=RequestMethod.POST)
	public boolean insertTeacher(Teacher teacher){
		System.out.println("insertTeacher:"+teacher.getTeacherId());
		String account = Utils.md5(teacher.getTeacherId());
		teacher.setTeacherId(account);
		teacher.setUserId(account);
		return teacherService.insertTeacher(teacher);
	}


	@ResponseBody
	@RequestMapping(value = "/getStudentByTeacherId3",method= RequestMethod.GET)
	public List<Student> getStudentByTeacherId3() {
		List<Student> list=teacherService.getStudentByTeacherId3();
		return list;
	}

	@ResponseBody
	@RequestMapping("/getYansouTeamStuXzpf")
	public PageInfo<Student> getYansouTeamStuXzpf(HttpSession session, @RequestParam(defaultValue="1")int pageNum,	@RequestParam(defaultValue="8")int pageSize){
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				PageHelper.startPage(pageNum, pageSize);
				List<Student> list = teacherService.getYansouTeamStuXzpf(teacher.getTeacherId());
				return new PageInfo<Student>(list);
			}
		}
		return null;
	}
}
