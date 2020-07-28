package com.leonlabs.auth.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leonlabs.auth.entity.AuthUser;
import com.leonlabs.auth.repository.AuthUserRepository;
import com.leonlabs.auth.view.AuthUserView;
import com.leonlabs.core.util.EncryptionUtil;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AuthUserRepository authUserRepository;
    
    @Autowired
	private MessageSource messages;
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = authUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(messages.getMessage("err.invalid.credentials", null, null));
        } else {
            return create(user);
        }
    }
    
    public UserDetails getUserById(Integer id) throws UsernameNotFoundException {
        Optional<AuthUser> user = authUserRepository.findById(id);

        if (user == null || user.get() == null) {
            throw new UsernameNotFoundException(messages.getMessage("error.user.not.found", null, null));
        } else {
            return create(user.get());
        }
    }
    
    public AuthUserView authenticateUser(String username, String password, Locale locale) throws Exception {
        AuthUser user = authUserRepository.findByUsernameAndPassword(username,  EncryptionUtil.encrypt(password));
        AuthUserView userVO = new AuthUserView();
        if (user == null) {
        	throw new UsernameNotFoundException(messages.getMessage("error.user.not.found", null, null));
        } else {
        	BeanUtils.copyProperties(user, userVO);
        }
        return userVO;
    }
    
	private AuthUserView create(com.leonlabs.auth.entity.AuthUser user) {
		return new AuthUserView(user.getId(), user.getUsername(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getPassword(), user.getUserRoles(),user.getOrganizationId(),user.getTenantName());
	}
}
