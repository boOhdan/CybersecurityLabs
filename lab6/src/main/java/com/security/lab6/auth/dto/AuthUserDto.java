package com.security.lab6.auth.dto;

import com.security.lab6.user.UserDto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AuthUserDto {
	private boolean loggedIn;
	private UserDto user;
	private boolean hasErrors;
	private String errors;

	public AuthUserDto(boolean loggedIn, UserDto user) {
		this.loggedIn = loggedIn;
		this.user = user;
	}
}
