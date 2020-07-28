package com.leonlabs.auth.rest.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.leonlabs.auth.service.AuthenticationService;
import com.leonlabs.auth.service.UserLoginActivityService;
import com.leonlabs.auth.view.AuthUserView;
import com.leonlabs.auth.view.AuthenticationRequest;
import com.leonlabs.core.exception.ApplicationException;
import com.leonlabs.core.exception.AuthenticationException;
import com.leonlabs.security.constant.AuthenticationConstants;
import com.leonlabs.security.security.JWTTokenUtility;
import com.leonlabs.security.security.UserInContext;
import com.leonlabs.security.security.UserInContextFactory;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping
@Slf4j
public class AuthenticationController {

	@Autowired
	private JWTTokenUtility jwtTokenUtility;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	UserLoginActivityService userLoginActivityService;
	
	@RequestMapping(value = "/generateToken", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authrequest, HttpServletRequest req) throws Exception {
		try {
			AuthUserView userVO = authenticationService.authenticateUser(authrequest.getUsername(), authrequest.getPassword(), req.getLocale());
			String token = null;
			JSONObject json = new JSONObject();

			if (userVO != null) {
				token = jwtTokenUtility.generateToken(UserInContextFactory.getUserInContext(
						userVO.getUsername(), userVO.getFirstName(), userVO.getMiddleName(), userVO.getLastName(), userVO.getId(), userVO.getUserRoles(),userVO.getOrganizationId(),userVO.getTenantName()));
				HttpHeaders headers = new HttpHeaders();
				log.debug("login token successfully created for username "+ userVO.getUsername());
				headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
				headers.add(AuthenticationConstants.JWT_HEADER, AuthenticationConstants.JWT_BEARER_PREFIX.concat(" ").concat(token));
				json.put("success", true);
				userLoginActivityService.saveUserLoginActivity(userVO.getId(), userVO.getUsername(), req);
				return ResponseEntity.ok().headers(headers).body(json);
			} else {
				log.debug("login attempt failed for username "+ authrequest.getUsername());
				json.put("success", false);
				return ResponseEntity.badRequest().body(json);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws ApplicationException {
		try {
			String refreshedToken = jwtTokenUtility.refreshToken(jwtTokenUtility.getJWTTokenFromRequest(request));

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
			headers.add(AuthenticationConstants.JWT_HEADER, AuthenticationConstants.JWT_BEARER_PREFIX.concat(" ").concat(refreshedToken));
			return ResponseEntity.ok().headers(headers).body(null);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) throws ApplicationException {
		try {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (Exception e1) {
			throw new ApplicationException(e1.getMessage(), e);
		}
	}
}
