package br.com.joaogd53.ads.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class DefaultController {
	@GetMapping
	public String get() {

		return "ok";
	}
}
