package com.security.lab6.main;

import com.security.lab6.user.UserDto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

	private final UserDto currentUser;

	@GetMapping
	public String defaultPage() {
		return "redirect:/login";
	}

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("user", currentUser);
		return "/home";
	}
}
