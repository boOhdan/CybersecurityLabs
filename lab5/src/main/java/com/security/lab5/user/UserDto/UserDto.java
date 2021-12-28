package com.security.lab5.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {
	private UUID id;
	private String username;
}
