package com.security.lab5.auth.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
	private String username;
	private String password;
	private String repeatPassword;
}
