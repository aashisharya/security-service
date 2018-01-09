package com.omniwyse.security.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omniwyse.security.service.OtpService;

@Controller
public class SecurityController {
	
	@Autowired
	OtpService otpService;

	@GetMapping("/accessDenied")
	public String accessDeniedTest() {
		return "error/403";
	}
	
	@GetMapping("/index")
	public String index() {
		return "signin";
	}
	
	@GetMapping("/login")
	public String login() {
		return "signin";
	}
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}
	@RequestMapping(value="/logout", method = RequestMethod.GET)
    public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response){
    	
       Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
       if (auth != null){    
    	   String username = auth.getName();
    	   
    	   //Remove the recently used OTP from server. 
    	   otpService.clearOTP(username);
           
    	   new SecurityContextLogoutHandler().logout(request, response, auth);
       }
       
	   return "redirect:/login?logout";    	
    }
	
}
