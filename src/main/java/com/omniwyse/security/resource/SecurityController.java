package com.omniwyse.security.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/api/accessDenied")
	public String accessDeniedTest() {
		return "Sorry you dont have permission to this link...";
	}
	
	@GetMapping("/user/test")
	public String test(HttpServletRequest req) {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		System.out.println("user name for terst : "+ auth.getAuthorities());
		HttpSession session = req.getSession();
		System.out.println("username from session : "+session.getAttribute("username"));
		return "Testing details .. ";
	}
	
	@GetMapping("/test")
	public String testService() {
		return "testing successfull ...";
	}
	/*@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}*/
}
