package com.security.lab6.user;

import com.security.lab6.auth.entity.AuthUser;
import com.security.lab6.user.UserDto.UserDto;
import com.security.lab6.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
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
		return new UserDto()
				.setId(user.getId())
				.setUsername(user.getUsername())
				.setPhone(user.getPhone());
	}
 }
