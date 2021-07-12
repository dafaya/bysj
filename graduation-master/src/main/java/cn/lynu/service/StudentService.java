package cn.lynu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.lynu.mapper.StudentMapper;
import cn.lynu.model.Student;
import cn.lynu.model.User;

import java.util.List;

@Service
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class StudentService {
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStudentByUserId(String userId) {
		return studentMapper.getStudentByUserId(userId);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public User getUserByStudentId(String studentId) {
		return studentMapper.getUserByStudentId(studentId);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateProjectNum(String studentId,String teacherId,String projectId) {
		return studentMapper.updateProjectNum(studentId,teacherId,projectId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Student projectNum(String studentId) {
		return studentMapper.getStudentByStudentId(studentId);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean updateNoProjectNum(String studentId) {
		return studentMapper.updateNoProjectNum(studentId);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStuAndProject(String studentId) {
		return studentMapper.getStuAndProject(studentId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStuAndProjectAndTeacher(String studentId) {
		return studentMapper.getStuAndProjectAndTeacher(studentId);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateTeacherId(String teacherId, String studentId) {
		return studentMapper.updateTeacherId(teacherId,studentId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public int thisTeacherUndefinedStudentNum(String teacherId) {
		return studentMapper.thisTeacherUndefinedStudentNum(teacherId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStudentAndKtbgBySid(String studentId) {
		return studentMapper.getStudentAndKtbgBySid(studentId);
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStudentAndZqjcBySid(String studentId) {
		return studentMapper.getStudentAndZqjcBySid(studentId);
	}

/*	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateStudentInfo(Student student) {
		int bool = userMapper.updateByPrimaryKeySelective(student.getUser());
		if(bool>0) {
			return true;
		}
		return false;
	}*/

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateStudent(Student student) {
		if(studentMapper.updateByPrimaryKeySelective(student)>0) {
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Integer getThisStudentScore(String studentId) {
		Integer score=studentMapper.getThisStudentScore(studentId);
		if(score!=null) {
			return score;
		}
		return -1;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStudentAndMdbBySid(String studentId) {
		Student student=studentMapper.getStudentAndMdbBySid(studentId);
		if(student!=null) {
			return student;
		}
		return null;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public long getStudentNum(){
		long num=studentMapper.getStudentNum();
		return num;

	}
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean insertStudent(Student student){
		int bool =studentMapper.insertSelective(student);
		if(bool>0){
			return true;
		}
		return false;
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	public  boolean deleteStudent(String studentId){
		int bool=studentMapper.deleteByPrimaryKey(studentId);
		if(bool>0){
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Student getStudentBySid(String studentId){
		Student student1=studentMapper.getStudentByStudentId(studentId);
		return student1;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateStudentNull(Student student) {
		if(studentMapper.updateByPrimaryKeyPNull(student)>0) {
			return true;
		}
		return false;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public  boolean updateStudentTeamId(String studentId,String teamId){
		if(studentMapper.updateStudentTeamId(studentId,teamId)>0){
			return true;
		}
		return false;
	}
}
