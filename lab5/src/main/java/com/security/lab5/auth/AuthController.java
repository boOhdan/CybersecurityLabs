package com.security.lab5.auth;

import com.security.lab5.auth.dto.UserLoginDto;
import com.security.lab5.auth.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/registration")
	public String registrationPage() {
		return "registration";
	}

	@PostMapping("/registration")
	public String register(UserRegistrationDto registrationDto, Model model) {
		model.addAttribute("user", authService.register(registrationDto));
		return "login";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@PostMapping("login")
	public String login(UserLoginDto loginDto, Model model) {
		model.addAttribute("user", authService.login(loginDto));
		return "login";
	}

}
