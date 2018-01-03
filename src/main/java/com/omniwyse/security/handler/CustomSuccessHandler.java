package com.omniwyse.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);

		HttpSession session = request.getSession();

		try {
			User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (authUser.getUsername() != null || "".equals(authUser.getUsername().trim()))
				session.setAttribute("username", authUser.getUsername());
			session.setAttribute("authorities", authentication.getAuthorities());
			request.setAttribute("username", authUser.getUsername());
			/*
			 * if (CurrentUser.isUserInRole("USER")) { session.setAttribute("Flag", "user");
			 * }
			 */
			System.out.println("Session id : " + session.getId());
			System.out.println("Session careation time : " + session.getCreationTime());
			System.out.println("Session last used : " + session.getLastAccessedTime());
			System.out.println("username from httprequest : " + request.getAttribute("username"));
			System.out.println("username from session : " + session.getAttribute("username"));
		} catch (Exception e) {
			logger.error("Error in getting User()", e);
		}
	}

}