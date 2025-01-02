package com.nnk.springboot.controllers;

import com.nnk.springboot.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@RequestMapping("/")
	public String home(Model model, Authentication authentication)
	{
		if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User principal = (OAuth2User) authentication.getPrincipal();
			model.addAttribute("name", principal.getAttribute("email"));
		} else if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			model.addAttribute("name", userDetails.getDisplayName());
		}
		return "home";
	}

	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		return "logout";
	}
}
