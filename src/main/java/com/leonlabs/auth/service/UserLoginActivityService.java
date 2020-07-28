package com.leonlabs.auth.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonlabs.auth.entity.UserLoginActivity;
import com.leonlabs.auth.repository.UserLoginActivityRepository;
import com.leonlabs.core.exception.ApplicationException;

@Service
public class UserLoginActivityService {

	@Autowired
	private UserLoginActivityRepository userLoginActivityRepository;
	
	@Transactional
	//@Async
	public void saveUserLoginActivity(Integer userId, String username, HttpServletRequest request) 
			throws ApplicationException, UnknownHostException {

		UserLoginActivity userLoginActivity = new UserLoginActivity();
		
		if (request.getHeader("User-Agent").indexOf("Mobile") != -1) {
			userLoginActivity.setDeviceType("MOBILE");
		} else if (request.getHeader("User-Agent").indexOf("Postman") != -1) {
			userLoginActivity.setDeviceType("POSTMAN");
		} else {
			userLoginActivity.setDeviceType("WEB");
		}

		userLoginActivity.setUserId(userId);
		userLoginActivity.setUsername(username);
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		userLoginActivity.setIpAddr(ipAddress);
		
		InetAddress inetAddr=InetAddress.getLocalHost();
		String hostAddress = inetAddr.getHostAddress();
		String hostname = inetAddr.getHostName();
		userLoginActivity.setInetAddr(hostAddress);
		userLoginActivity.setHostname(hostname);
		
		userLoginActivity.setTimestamp(new Date());
		userLoginActivity = userLoginActivityRepository.save(userLoginActivity);
		//indexerService.saveMetaData(new DefaultSearchMetadataViewImpl(false,String.valueOf(userId),"timestamp",new Date()));
	}

}
