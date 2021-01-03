package com.raf.asmi.letovi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.repository.AvionRepository;

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
	
	// DODATI ZASTITU!
	@PostMapping("/napravi-avion")
	public Avion napraviAvion(@RequestParam(value = "naziv", required=true) String naziv,
			@RequestParam(value = "kapacitet", required=true) Short kapacitet) {
		if(kapacitet < 0){
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Avion a = new Avion();
		a.setNaziv(naziv);
		a.setKapacitet(kapacitet);
		avionRepository.save(a);
		return a;
	}
	
	// DODATI ZASTITU!
	@PostMapping("/ukloni-avion/{id}")
	public String ukloniAvion(@PathVariable(required = true) Integer id) {
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
