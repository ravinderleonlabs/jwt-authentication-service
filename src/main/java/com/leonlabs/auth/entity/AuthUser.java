package com.leonlabs.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usr_user")
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username", length = 50, unique = true)
    @Size(min = 4, max = 50)
    private String username;
    
    @Column(name = "first_name", length = 50)
    @Size(min = 4, max = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    @Size(min = 4, max = 50)
    private String middleName;
    
    @Column(name = "last_name", length = 50)
    @Size(min = 4, max = 50)
    private String lastName;

    @Column(name = "password", length = 100)
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "user_roles", length = 20)
    @Size(min = 4, max = 20)
    private String userRoles;
    
    @Column(name = "tenant_name", nullable=true)
	private String tenantName;
	
	@Column(name = "organization_id", nullable=true)
	private Integer organizationId;
    
    
}