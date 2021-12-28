package com.security.lab5.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginDto {
	private String username;
	private String password;
}
