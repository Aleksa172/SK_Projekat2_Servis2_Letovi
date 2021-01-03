package com.raf.asmi.letovi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public HashMap<String, Object> getDostupneLetove(
			@RequestParam(value = "from", defaultValue = "0") Integer from,
			@RequestParam(value = "count", defaultValue = "10") Integer count,
			@RequestParam(value = "pocetnaDestinacija", required = false) String pocetnaDestinacija,
			@RequestParam(value = "krajnjaDestinacija", required = false) String krajnjaDestinacija,
			@RequestParam(value = "cenaOd", required = false) Double cenaOd,
			@RequestParam(value = "cenaDo", required = false) Double cenaDo,
			@RequestParam(value = "trajanjeOd", required = false) Short trajanjeOd,
			@RequestParam(value = "trajanjeDo", required = false) Short trajanjeDo) {
		
		HashMap<String, Object> params = new HashMap<>();
		if(pocetnaDestinacija != null){
			params.put("pocetnaDestinacija", pocetnaDestinacija);
		}
		if(krajnjaDestinacija != null){
			params.put("krajnjaDestinacija", krajnjaDestinacija);
		}
		if(cenaOd != null){
			params.put("cenaOd", cenaOd);
		}
		if(cenaDo != null){
			params.put("cenaOd", cenaDo);
		}
		if(trajanjeOd != null){
			params.put("trajanjeOd", trajanjeOd);
		}
		if(trajanjeDo != null){
			params.put("trajanjeDo", trajanjeDo);
		}
		
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("data", letRepository.getDostupneLetove(from, count, null, params));
		result.put("flight_count", letRepository.getFilteredTotalRows(null, params));
		
		return result;
		
	}
	
	@GetMapping("/let/{id}")
	public Optional<Let> getLet(@PathVariable(required = true) Integer id) {
		
		return letRepository.findById(id);
	}
	
}
