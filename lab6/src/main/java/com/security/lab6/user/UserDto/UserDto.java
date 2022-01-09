package com.security.lab6.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {
	private UUID id;
	private String username;
}
