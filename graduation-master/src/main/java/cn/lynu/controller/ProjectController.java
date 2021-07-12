package cn.lynu.controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import cn.lynu.model.*;
import cn.lynu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/projectController")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;

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


	@ResponseBody
	@RequestMapping(value="/getProjectListByTeacherId",method=RequestMethod.GET)
	public PageInfo<Project> getProjectListByTeacherId(@RequestParam(required=true)String teacherId,
			  @RequestParam(defaultValue="1")int pageNum,@RequestParam(defaultValue="8")int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Project> list = projectService.getProjectListByTeacherId(teacherId);
		return new PageInfo<>(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/getCountProjectNum",method=RequestMethod.GET)
	public int getCountProjectNum() {
		return projectService.getCountProjectNum();
	}
	
	@ResponseBody
	@RequestMapping(value="/thisTeacherYesProjectNum",method=RequestMethod.GET)
	public int thisTeacherYesProjectNum(HttpSession session) {
		User user=(User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				String teacherId = teacher.getTeacherId();
				return projectService.thisTeacherYesProjectNum(teacherId);
			}
		}
		return 0;
	}
	
	@ResponseBody
	@RequestMapping(value="/thisTeacherUndefinedStudentNum",method=RequestMethod.GET)
	public int thisTeacherUndefinedStudentNum(HttpSession session) {
		User user=(User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				String teacherId = teacher.getTeacherId();
				return studentService.thisTeacherUndefinedStudentNum(teacherId);
			}
		}
		return 0;
	}
	
	/**
	 * 添加与更新方法
	 * @param project
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertSelective",method=RequestMethod.POST)
	public boolean insertSelective(Project project,HttpSession session) {
		User user=(User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				project.setTeacherId(teacher.getTeacherId());
				if(project.getProjectId()!=null) {
					return projectService.updateByPrimaryKeySelective(project);
				}else {
					return projectService.insertSelective(project,teacher.getTeacherId());
				}
			}
		}
		return false;
	}
	
	@ResponseBody
	@RequestMapping(value="/getProjectByTeacherId")
	public List<Project> getProjectByTeacherId(HttpSession session){
		User user=(User) session.getAttribute("user");
		if(user!=null) {
			Teacher teacher = teacherService.findTeacherByUserId(user.getUserId());
			if(teacher!=null) {
				return projectService.getProjectByTeacherId(teacher.getTeacherId());
			}
		}
		return null;
	}
	@ResponseBody
	@RequestMapping(value="/updateProject",method=RequestMethod.POST)
	public boolean updateProject(Project project){
//		System.out.println("aaaa");
//		System.out.println(project.getProjectId());
//		System.out.println(project.getStudentId());
		return true;
	}

	@ResponseBody
	@RequestMapping(value="/deleteProject",method=RequestMethod.POST)
	public boolean deleteProject(Project project) {
		Project project1 = projectService.getProjectByPrimaryKey(project.getProjectId());
		Teacher teacher = teacherService.findTeacherByTeacherId(project1.getTeacherId());
		Integer projectnum = Integer.parseInt(teacher.getTeacherProjectNum());
		projectnum = projectnum - 1;
		if (projectnum == 0) {
			teacher.setTeacherProjectNum(null);
		} else {
			teacher.setTeacherProjectNum(Integer.toString(projectnum));
		}
		teacherService.updateTeacherInfoNull(teacher);
		String studentId = project1.getStudentId();
		if (studentId != null) {
			Student student=studentService.getStudentBySid(studentId);
			student.setTeacherId(null);
			studentService.updateStudentNull(student);
			if(student.getStudentScore()!=null){
				student.setStudentScore(null);
				studentService.updateStudentNull(student);
			}
			if(student.getTeacherEvaluate()!=null){
				student.setTeacherEvaluate(null);
				studentService.updateStudentNull(student);
			}
			if(student.getProjectNum()!=0){
				Integer projectnum1=student.getProjectNum();
				projectnum1=projectnum1-1;
				if(projectnum1==0){
					student.setProjectNum(0);
				}
				else {
					student.setProjectNum(projectnum1);
				}
				studentService.updateStudentNull(student);
			}
			DaBian daBian = dabianService.getDaBianByStudentId(studentId);
			if (daBian != null) {
				dabianService.deleteDabian(daBian.getId());
			}
			Ktbg ktbg = ktbgService.getKtbgByStudentId(studentId);
			if (ktbg != null) {
				ktbgService.deleteKtbgById(ktbg.getKtbgId());
			}
			Lunwen lunwen = lunwenService.getLunwenBySid(studentId);
			if (lunwen != null) {
				lunwenService.deleteLunWenById(lunwen.getLunwenId());
			}
			Zqjc zqjc = zqjcService.getzqjcByStudentId(studentId);
			if (zqjc != null) {
				zqjcService.deleteZqjcById(zqjc.getZqjcId());
			}
			Mdb mdb = mdbService.getMdbByStudentId(studentId);
			if (mdb != null) {
				mdbService.deleteMdbById(mdb.getMdbId());
			}

		}
		return projectService.deleteProjectByPrimaryKey(project1.getProjectId());
	}

}
