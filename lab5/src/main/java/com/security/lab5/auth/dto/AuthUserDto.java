package com.security.lab5.auth.dto;

import com.security.lab5.user.UserDto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AuthUserDto {
	private boolean loggedIn;
	private UserDto user;
	private String token;
	private boolean hasErrors;
	private String errors;

	public AuthUserDto(boolean loggedIn, UserDto user) {
		this.loggedIn = loggedIn;
		this.user = user;
	}
}
