package com.security.lab5.auth.entity;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.UUID;

@Getter
public class AuthUser extends User {
	private UUID id;

	public AuthUser(UUID id, String username, String password) {
		super(username, password, Collections.emptyList());
		this.id = id;
	}
}
