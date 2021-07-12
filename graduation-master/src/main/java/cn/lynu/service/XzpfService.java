package cn.lynu.service;


import cn.lynu.mapper.XzpfMapper;
import cn.lynu.model.Student;
import cn.lynu.model.Xzpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope(value="singleton",proxyMode= ScopedProxyMode.TARGET_CLASS)
public class XzpfService {

    @Autowired
    private XzpfMapper xzpfMapper;

    @Transactional(propagation= Propagation.REQUIRED)
    public boolean insertSelectiveXzpf(Xzpf xzpf){
        if(xzpfMapper.insertSelectiveXzpf(xzpf)>0){
            return true;
        }
        return false;
    }

    public boolean deleteXzpf(String studentId){
        if(xzpfMapper.deleteXzpfAll(studentId)>0){
            return true;
        }
        return false;
    }
    public boolean insertXzpf(Xzpf xzpf){
        if (xzpfMapper.insertSelectiveXzpf(xzpf)>0){
            return true;
        }
        return false;
    }
    public boolean updateXzpf(Xzpf xzpf){
        if (xzpfMapper.updateXzpf(xzpf)>0){
            return true;
        }
        return false;
    }

    public List<Xzpf> getXzpfByStudentId(String studentId){
        return xzpfMapper.selectXzpfByStudentId(studentId);
    }

}
