package com.raf.asmi.letovi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raf.asmi.letovi.entiteti.Let;
import com.raf.asmi.letovi.repository.LetRepository;
import com.raf.asmi.letovi.repository.impl.LetRepositoryImpl;

@RestController
public class LetController {
	
	@Autowired
	private LetRepository letRepository;

	@GetMapping("/letovi")
	public List<Let> getDostupneLetove(
			@RequestParam(value = "from", defaultValue = "0") Integer from,
			@RequestParam(value = "count", defaultValue = "20") Integer count) {
		
		letRepository.getDostupneLetove(null, null, null, (short) 0, (short) 0, 0.0, 9999.0);
		
		return letRepository.getDostupneLetove(null, null, null, (short) 0, (short) 0, 0.0, 9999.0);
		
	}
}
