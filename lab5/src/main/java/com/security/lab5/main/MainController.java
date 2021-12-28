package com.security.lab5.main;

import com.security.lab5.auth.TokenService;
import com.security.lab5.user.UserDto.UserDto;
import com.security.lab5.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

	private final UserService userService;

	@GetMapping
	public String defaultPage() {
		return "redirect:/login";
	}

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("user", userService.getUserById(TokenService.getUserId()));
		return "/home";
	}
}
