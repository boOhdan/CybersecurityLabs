package com.security.lab5.auth.dto;

import lombok.Data;

@Data
public class RegistrationForm {
	private String username;
	private String password;
	private String repeatPassword;
}
