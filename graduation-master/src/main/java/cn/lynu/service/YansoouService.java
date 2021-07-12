package cn.lynu.service;

import cn.lynu.mapper.YansouTeacherMapper;
import cn.lynu.model.YansouTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.lynu.mapper.YansouTeamMapper;
import cn.lynu.model.YansouTeam;

import java.util.List;

@Service
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class YansoouService {
	
	@Autowired
	private YansouTeamMapper yansouTeamMapper;
	@Autowired
	private YansouTeacherMapper yansouTeacherMapper;

	@Transactional(propagation=Propagation.SUPPORTS)
	public YansouTeam selectYanSouInfoByid(Integer yansouTeamId) {
		return yansouTeamMapper.selectYanSouInfoByid(yansouTeamId);
	}


	public YansouTeacher selectYansouInfoBytid(String tid){
		return yansouTeacherMapper.selectYansouTeacherByTeacherId(tid);
	}

	public boolean deleteYansouBytid(String tid){
		if(yansouTeacherMapper.deleteYansouTeacherBytid(tid)>0){
			return true;
		}else {
			return false;
		}
	}

	public boolean insertYansouTeacher(YansouTeacher yansouTeacher){
		if(yansouTeacherMapper.insertSelective(yansouTeacher)>0){
			return true;
		}else {
			return false;
		}
	}

	public  boolean updateYansouTeacher(YansouTeacher yansouTeacher){
		if(yansouTeacherMapper.updateByTeacherId(yansouTeacher)>0){
			return true;
		}else {
			return false;
		}
	}

	public List<YansouTeacher> selectByYansouId(Integer yansouteamId){
		return yansouTeacherMapper.selectByYansouId(yansouteamId);
	}
}
