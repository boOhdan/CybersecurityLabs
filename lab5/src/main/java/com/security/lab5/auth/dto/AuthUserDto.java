package com.security.lab5.auth.dto;

import com.security.lab5.user.UserDto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserDto {
	private boolean loggedIn;
	private UserDto user;
}
