package com.raf.asmi.letovi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.repository.AvionRepository;
import com.raf.asmi.letovi.security.SecurityConstants;

@RestController
public class AvionController {
	
	@Autowired
	private AvionRepository avionRepository;
	
	@GetMapping("/avioni")
	public HashMap<String, Object> getAvione(@RequestParam(value = "from", defaultValue = "0") Integer from,
			@RequestParam(value = "count", defaultValue = "10") Integer count) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("data", avionRepository.getAvione(from, count));
		result.put("total_count", avionRepository.count());
		
		return result;
	}


	@PostMapping("/napravi-avion")
	public Avion napraviAvion(@RequestParam(value = "naziv", required=true) String naziv,
			@RequestParam(value = "kapacitet", required=true) Short kapacitet,
			@RequestHeader(value = SecurityConstants.HEADER_STRING) String token){
		String role = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getClaim("role").asString();

		// Zabrani svima koji nisu admini
		if(!role.equals("ADMIN")) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Ova strana je dozvoljena samo adminima");
		}
		if(kapacitet < 0){
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Avion a = new Avion();
		a.setNaziv(naziv);
		a.setKapacitet(kapacitet);
		avionRepository.save(a);
		return a;
	}
	
	
	@PostMapping("/ukloni-avion/{id}")
	public String ukloniAvion(@PathVariable(required = true) Integer id,
			@RequestHeader(value = SecurityConstants.HEADER_STRING) String token){
		
		String role = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getClaim("role").asString();

		// Zabrani svima koji nisu admini
		if(!role.equals("ADMIN")) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Ova strana je dozvoljena samo adminima");
		}
		Optional<Avion> aTemp = avionRepository.findById(id);
		if(!aTemp.isPresent()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if(avionRepository.isDodeljen(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		avionRepository.deleteById(id);
		return "ok";
	}
	
}
