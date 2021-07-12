package cn.lynu.service;

import java.util.List;

import cn.lynu.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.lynu.mapper.ProjectMapper;
import cn.lynu.mapper.TeacherMapper;
import cn.lynu.model.Project;

@Service
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ProjectService {
	
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private TeacherMapper teacherMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<Project> getProjectListByTeacherId(String teacherId){
		return projectMapper.getProjectListByTeacherId(teacherId);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public Project getProjectByStudentId(String studentId) {
		return projectMapper.getProjectByStudentId(studentId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateStudentIdByProjectId(String studentId,String projectId) {
		return projectMapper.updateProjectByStudentId(studentId, projectId);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateNoStudent(String projectId) {
		return projectMapper.updateNoStudent(projectId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public int getCountProjectNum() {
		return projectMapper.getCountProjectNum();
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public int thisTeacherYesProjectNum(String teacherId) {
		return projectMapper.thisTeacherYesProjectNum(teacherId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean insertSelective(Project project,String teacherId) {
		Teacher teacher=teacherMapper.findTeacherByTeacherId(teacherId);
		if(teacher.getTeacherProjectNum()==null){
			teacher.setTeacherProjectNum(Integer.toString(1));
			teacherMapper.updateByPrimaryKeySelective(teacher);
		}
		else {
			Integer num=Integer.parseInt(teacher.getTeacherProjectNum())+1;
			teacher.setTeacherProjectNum(Integer.toString(num));
			teacherMapper.updateByPrimaryKeySelective(teacher);
		}
		int num = projectMapper.insertSelective(project);
		if(num>0) {
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public List<Project> getProjectByTeacherId(String teacherId) {
		return projectMapper.getProjectByTeacherId(teacherId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateByPrimaryKeySelective(Project project) {
		int num = projectMapper.updateByPrimaryKeySelective(project);
		if(num>0) {
			return true;
		}
		return false;
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean deleteProjectByPrimaryKey(Integer projectId){
		int bool=projectMapper.deleteByPrimaryKey(projectId);
		if(bool>0){
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Project getProjectByPrimaryKey(Integer projectId){
		Project project=projectMapper.selectByPrimaryKey(projectId);
		return project;
	}



}
