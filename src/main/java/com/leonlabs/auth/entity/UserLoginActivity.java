package com.leonlabs.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.leonlabs.core.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usr_user_login_activity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginActivity extends BaseEntity {

	private static final long serialVersionUID = 4737788438561281023L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="username")
	private String username;
	
	@Column(name="device_type")
	private String deviceType;
	
	@Column(name="timestamp")
	private Date timestamp;
	
	@Column(name="ip_addr")
	private String ipAddr;
	
	@Column(name="inet_addr")
	private String inetAddr;
	
	@Column(name="hostname")
	private String hostname;
	
	@Column(name="access_token")
	private String accessToken;
	
	
	

}
