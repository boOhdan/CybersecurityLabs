package com.security.lab5.auth;

import com.security.lab5.auth.dto.AuthUserDto;
import com.security.lab5.auth.dto.UserLoginForm;
import com.security.lab5.auth.dto.RegistrationForm;
import com.security.lab5.auth.entity.AuthUser;
import com.security.lab5.user.UserService;
import com.security.lab5.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public AuthUserDto register(RegistrationForm registrationForm) {
		var user = new User();
		user.setUsername(registrationForm.getUsername());
		user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
		userService.save(user);

		return login(user.getUsername(), user.getPassword());
	}

	public AuthUserDto login(UserLoginForm loginForm) {
		return login(loginForm.getUsername(), loginForm.getPassword());
	}

	public AuthUserDto login(String username, String password) {
		Authentication auth;
		try {
			auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password)
			);
		} catch (BadCredentialsException e) {
			return new AuthUserDto()
					.setHasErrors(true)
					.setErrors("Incorrect username or password");

		}

		var currentUser = (AuthUser) auth.getPrincipal();
		var user = userService.getUserById(currentUser.getId());
		return new AuthUserDto(true, user);
	}

}
