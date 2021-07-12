package cn.lynu.service;

import java.util.List;

import cn.lynu.mapper.*;
import cn.lynu.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AdminService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Transactional(propagation=Propagation.SUPPORTS)
    public List<Student> getStudent() {
        return studentMapper.findAllStudent();
    }
    @Transactional(propagation=Propagation.SUPPORTS)
    public List<Teacher> getAllTeacher(){
        return  teacherMapper.getAllTeacher();
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public Admin getAdminByUserId(String userId){
        return adminMapper.getAdminByUserId(userId);
    }

}
