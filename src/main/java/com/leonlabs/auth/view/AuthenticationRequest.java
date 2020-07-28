package com.leonlabs.auth.view;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @NotNull
    private String username;
    
    @NotNull
    private String password;

}
