package com.raf.asmi.letovi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LetController {

	@GetMapping("/letovi")
	public String getDostupneLetove(
			@RequestParam(value = "from", defaultValue = "0") Integer from,
			@RequestParam(value = "count", defaultValue = "20") Integer count) {
		
		return from+" "+(from+count-1);
	}
}
