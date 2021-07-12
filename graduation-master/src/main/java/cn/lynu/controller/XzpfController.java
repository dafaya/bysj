package cn.lynu.controller;

import cn.lynu.model.Student;
import cn.lynu.model.User;
import cn.lynu.model.Xzpf;
import cn.lynu.service.StudentService;
import cn.lynu.service.XzpfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/xzpfController")
public class XzpfController {

    @Autowired
    private XzpfService xzpfService;
    @Autowired
    private StudentService studentService;

    @ResponseBody
    @RequestMapping(value = "/insertUpdateXzpf",method= RequestMethod.POST)
    public boolean insertUpdateXzpf(Xzpf xzpf){
        if(xzpf.getId()==null){
            return xzpfService.insertXzpf(xzpf);
        }
        else {
            return xzpfService.updateXzpf(xzpf);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/getThisStudentScore")
    public Integer getThisStudentScore(HttpSession httpSession){
        User user=(User) httpSession.getAttribute("user");
        Integer sum=0;
        if(user!=null){
            Student student=studentService.getStudentByUserId(user.getUserId());
            List<Xzpf> xzpfs=xzpfService.getXzpfByStudentId(student.getStudentId());
            for (int i=0;i<xzpfs.size();i++){
                if(xzpfs.get(i).getScore()!=null){
                    sum=sum+xzpfs.get(i).getScore();
                }
            }
            System.out.println("tset"+sum);
            if(sum!=0){
                return sum;
            }else {
                return -1;
            }
        }
        return -1;
    }


}
