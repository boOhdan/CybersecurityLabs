package com.security.lab5.user;

import com.security.lab5.auth.entity.AuthUser;
import com.security.lab5.user.UserDto.UserDto;
import com.security.lab5.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.map(u -> new AuthUser(u.getId(), u.getUsername(), u.getPassword()))
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));
	}

	public UserDto getUserById(UUID id) {
		return userRepository
				.findById(id)
				.map(this::userToUserDto)
				.orElse(null);
	}

	public UserDto save(User user) {
		return userToUserDto(userRepository.save(user));
	}

	private UserDto userToUserDto(User user) {
		return new UserDto(user.getId(), user.getUsername());
	}
 }
