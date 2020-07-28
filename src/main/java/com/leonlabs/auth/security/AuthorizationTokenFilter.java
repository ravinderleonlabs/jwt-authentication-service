package com.leonlabs.auth.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.leonlabs.auth.service.AuthenticationService;
import com.leonlabs.security.constant.AuthenticationConstants;
import com.leonlabs.security.security.JWTTokenUtility;
import com.leonlabs.security.security.UserInContext;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    @Autowired JWTTokenUtility jwtTokenUtility;

    @Autowired
	private AuthenticationService authenticationService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.debug("processing authentication for '{}'", request.getRequestURL());

        String requestHeader = request.getHeader(AuthenticationConstants.JWT_HEADER);

        UserInContext userContext = null;
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            authToken = requestHeader.substring(7);
            try {
            	userContext = jwtTokenUtility.getUserContextFromToken(authToken);
            } catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from token", e);
               // throw new JwtException("Authentication credentials/token missing");
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore", e);
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
                //throw new JwtException("Authentication credentials/the token is expired and not valid anymore");
                
            }
        } else {
            log.warn("couldn't find bearer string, will ignore the header");
        }

        log.debug("checking authentication for user -id '{}'");
        if (userContext != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("security context was null, so authorizating user");

            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
           UserDetails userDetails = this.authenticationService.getUserById(userContext.getUserId());

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtility.isValidToken(authToken )) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authorizated user '{}', setting security context", userContext.getUsername());
                SecurityContextHolder.getContext().setAuthentication(authentication);;
            }
        }

        chain.doFilter(request, response);
    }
}
