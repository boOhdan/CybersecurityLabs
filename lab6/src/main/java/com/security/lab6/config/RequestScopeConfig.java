package com.security.lab6.config;

import com.security.lab6.auth.entity.AuthUser;
import com.security.lab6.user.UserDto.UserDto;
import com.security.lab6.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RequestScopeConfig {

	private final UserService userService;

	@Bean
	@RequestScope
	public UserDto currentUser() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof AuthUser) {
			UUID userId = ((AuthUser) principal).getId();
			return userService.getUserById(userId);
		}
		return null;
	}
}
