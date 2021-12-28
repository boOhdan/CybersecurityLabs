package com.security.lab5.auth;

import com.security.lab5.auth.dto.AuthUserDto;
import com.security.lab5.auth.dto.UserLoginDto;
import com.security.lab5.auth.dto.UserRegistrationDto;
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

	public AuthUserDto register(UserRegistrationDto userRegistration) {
		var user = new User();
		user.setUsername(userRegistration.getUsername());
		user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
		userService.save(user);

		return login(user.getUsername(), user.getPassword());
	}

	public AuthUserDto login(UserLoginDto userLogin) {
		return login(userLogin.getUsername(), userLogin.getPassword());
	}

	public AuthUserDto login(String username, String password) {
		Authentication auth;
		try {
			auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password)
			);
		}
		catch (BadCredentialsException e) {
			throw new RuntimeException("Incorrect username or password", e);
		}

		var currentUser = (AuthUser)auth.getPrincipal();
		var user = userService.getUserById(currentUser.getId());
		return new AuthUserDto(true, user);
	}

}
