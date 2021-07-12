package cn.lynu.controller;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import cn.lynu.model.User;
import cn.lynu.service.UserService;
import cn.lynu.util.Utils;

@Controller
@RequestMapping("/userController")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="/isuser",method=RequestMethod.POST)
	public boolean isUser(String account) {
		return userService.isUser(account);
	}
	
	@ResponseBody
	@RequestMapping(value="/ispassword",method=RequestMethod.POST)
	public boolean ispassword(@RequestParam("password")String password,HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			return userService.ispassword(Utils.md5(password),user.getUserId());
		}
		return false;
	}
	
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(HttpSession session,String randStr,String account,String password) {
		String randStr2=(String) session.getAttribute("randStr");
			if(randStr2!=null&&randStr2.equals(randStr)) {
				password=Utils.md5(password);
				Subject currentUser = SecurityUtils.getSubject();
				if (!currentUser.isAuthenticated()) {
					//把用户名和密码封装为UsernamePasswordToken
		            UsernamePasswordToken token = new UsernamePasswordToken(account, password);
		            try {
		            	//执行登录
		                currentUser.login(token);
		                return toUI(session,account,password);
		            }
		            //用户不存在
		            catch (UnknownAccountException ae) {
		            	return "passwordError";
		            }
		            //用户被锁定
		            catch (LockedAccountException e) {
		            	System.out.println("suoding");
		            	return "passwordError";
					}
				}else {
					System.out.println("chenggong2");
					return toUI(session,account,password);
				}
			}else {
				return "randStrError";
			}
	}
	
	private String toUI(HttpSession session,String account,String password) {
		User user = userService.login(account, password);
		if(user!=null) {
			if(2==user.getUserRoles()) {
				session.setAttribute("user", user);
				return "student/sindex.html";
			}
			if(1==user.getUserRoles()) {
				session.setAttribute("user", user);
				return "teacher/tindex.html";
			}
			if(3==user.getUserRoles()){
				session.setAttribute("user",user);
				return "admin/aindex.html";
			}
		}
		return "passwordError";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:http://localhost:8080/graduation/login.html";
	}
	
	
	@ResponseBody
	@RequestMapping(value="/updateInfo",method=RequestMethod.POST)
	public boolean updateStudentInfo(User user,
			@RequestParam(value="portrait",required=false)MultipartFile portrait,HttpServletRequest request) {
		if(portrait!=null&&portrait.getSize()>0) {
			if(portrait.getSize()>(10*1024*1024)) {
				return false;
			}
			String filename=portrait.getOriginalFilename();
			String dbPath=request.getServletContext().getContextPath()+"/portrait/"+user.getUserId();
			String basePath=request.getServletContext().getRealPath("/portrait/"+user.getUserId());
			new File(basePath).mkdir();
        	File portraitFile=new File(basePath,filename);
        	try {
				portrait.transferTo(portraitFile);
				user.setUserPortrait(dbPath+"/"+filename);
				return userService.updateUserInfo(user);
			} catch (Exception e) {
				e.printStackTrace();
			} 
        	
		}
		return userService.updateUserInfo(user);
	}
	
	@ResponseBody
	@RequestMapping(value="/updatePwd",method=RequestMethod.PUT)
	public boolean updateStudentPwd(User user,HttpSession session) {
		String password=user.getUserPassword();
		if(password!=null&&!password.isEmpty()) {
			user.setUserPassword(Utils.md5(password));
			boolean bool = userService.updateUserInfo(user);
			if(bool==true) {
				session.removeAttribute("user");
				return true;
			}
		}
		return false;
	}

	@ResponseBody
	@RequestMapping(value="/insertUser",method=RequestMethod.POST)
	public boolean insertUser(User user,HttpServletRequest request){
		user.setUserRoles(2);
		String password=Utils.md5(user.getUserPassword());
		user.setUserPassword(password);
		String account=Utils.md5(user.getUserAccount());
		user.setUserId(account);
		//System.out.println(user.getUserRoles());
		//System.out.println(user.getUserId());
		return userService.insertUser(user);
	}
	@ResponseBody
	@RequestMapping(value="/updateUser",method=RequestMethod.POST)
	public boolean updateUser(User user,HttpServletRequest request){
		System.out.println("updateUser:"+user.getUserName());
//		System.out.println(user.getUserName());
//		System.out.println(user.getUserAccount());
//		System.out.println(user.getUserGender());
//		System.out.println(user.getUserTel());
//		System.out.println(user.getUserId());
//		return true;
	return userService.updateUserInfo(user);
	}

	@ResponseBody
	@RequestMapping(value="/deleteUser",method=RequestMethod.POST)
	public boolean deleteUser(User user){
		System.out.println("deleteUser:"+user.getUserName());
//		return true;
		return userService.deleteUser(user.getUserId());
	}

	@ResponseBody
	@RequestMapping(value="resetUser",method=RequestMethod.POST)
	public boolean resetUser(User user){
		String password=Utils.md5(user.getUserAccount());
		user.setUserPassword(password);
		return userService.updateUserInfo(user);
	}

	@ResponseBody
	@RequestMapping(value="/insertUserTeacher",method=RequestMethod.POST)
	public boolean insertUserTeacher(User user,HttpServletRequest request){
		user.setUserRoles(1);
		String password=Utils.md5(user.getUserPassword());
		user.setUserPassword(password);
		String account=Utils.md5(user.getUserAccount());
		user.setUserId(account);
		//System.out.println(user.getUserRoles());
		//System.out.println(user.getUserId());
		return userService.insertUser(user);
	}
}
