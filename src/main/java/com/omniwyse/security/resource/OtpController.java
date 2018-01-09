package com.omniwyse.security.resource;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.omniwyse.security.service.MyEmailService;
import com.omniwyse.security.service.OtpService;
import com.omniwyse.security.util.EmailTemplate;

/**
 * @author aashish.kumar
 */
@Controller
public class OtpController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public OtpService otpService;

	@Autowired
	public MyEmailService myEmailService;

	@GetMapping("/otp")
	public String generateOtp() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.getOtp(username);
		if(otp <= 0) {
			otp = otpService.generateOTP(username);
			// Generate The Template to send OTP
			EmailTemplate template = new EmailTemplate("SendOtp.html");
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", username);
			replacements.put("otpnum", String.valueOf(otp));
			String message = template.getTemplate(replacements);
			myEmailService.sendOtpMessage(username, "ilibrary OTP", message);
		}
		return "otppage";
	}

	@RequestMapping(value = "/otp", method = RequestMethod.POST)
	public String validateOtp(@RequestParam("otpnum") int otpnum) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		String view = "redirect:/otp?error";
		logger.info(" Otp Number : " + otpnum);

		// Validate the Otp
		if (otpnum >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0 && otpnum == serverOtp) {
				otpService.clearOTP(username);
				return "dashboard";
			}
		}
		return view;
	}
}
