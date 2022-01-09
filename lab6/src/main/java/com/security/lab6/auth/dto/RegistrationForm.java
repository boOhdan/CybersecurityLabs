package com.security.lab6.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationForm {
	@NotBlank(message = "Not black")
	@Size(min = 3, message = "At least 3 symbols")
	private String username;
	private String password;
	private String repeatPassword;
	private String phone;
}
