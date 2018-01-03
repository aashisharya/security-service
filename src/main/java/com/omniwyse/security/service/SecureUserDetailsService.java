package com.omniwyse.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.omniwyse.security.constant.URIEnum;


@Service
public class SecureUserDetailsService implements UserDetailsService{

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	@SuppressWarnings({"unchecked"})
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username "+username);
		HttpHeaders  header = new HttpHeaders();
		header.add("username", username);
		HttpEntity<String> entity = new HttpEntity<String>(header);
		ResponseEntity<Object[]> responseBody = restTemplate.exchange(URIEnum.USER_SERVICE_URI+"/user-details", HttpMethod.POST, entity, Object[].class);
		Object[] loginDetails = responseBody.getBody();
		Set<GrantedAuthority> grantedAuthorities =  new HashSet<>();
		List<String>  roles = (List<String>) loginDetails[2];
		for(String role : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role));
		}
		return new User(loginDetails[0].toString(), loginDetails[1].toString(), true,true,true,true,grantedAuthorities);
	}
	
}
