package com.raf.asmi.letovi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.entiteti.Let;
import com.raf.asmi.letovi.entiteti.LetStatus;
import com.raf.asmi.letovi.repository.AvionRepository;
import com.raf.asmi.letovi.repository.LetRepository;
import com.raf.asmi.letovi.repository.impl.LetRepositoryImpl;

@RestController
public class LetController {
	
	@Autowired
	private LetRepository letRepository;
	@Autowired
	private AvionRepository avionRepository;

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
		result.put("total_count", letRepository.getFilteredTotalRows(null, params));
		
		return result;
		
	}
	
	@GetMapping("/let/{id}")
	public Optional<Let> getLet(@PathVariable(required = true) Integer id) {
		
		return letRepository.findById(id);
	}
	
	// DODATI ZASTITU!
	@GetMapping("/let/{id}/preostalo-mesta")
	public Short getPreostaloMesta(@PathVariable(required = true) Integer id) {
		Optional<Let> lTemp = letRepository.findById(id);
		if(!lTemp.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trazeni let ne postoji");
		}
		return lTemp.get().preostaloMesta();
	}
	
	// DODATI ZASTITU!
	@PostMapping("/let/{id}/zauzmi-mesta")
	public Short zauzmiMesto(@PathVariable(required = true) Integer id,
			@RequestParam(value = "broj", defaultValue = "1") Short count) {
		Optional<Let> lTemp = letRepository.findById(id);
		if(!lTemp.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trazeni let ne postoji");
		}
		Let l = lTemp.get();
		if(l.preostaloMesta()<count) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Nema vise mesta");
			// Zasto ovo postoji?
			// HttpStatus.I_AM_A_TEAPOT
		}
		Short preostaloNakon = l.zauzmiMesto(count);
		letRepository.save(l);
		return preostaloNakon;
	}
	
	// DODATI ZASTITU!
	@PostMapping("/let/{id}/oslobodi-mesta")
	public Short oslobodiMesto(@PathVariable(required = true) Integer id,
			@RequestParam(value = "broj", defaultValue = "1") Short count) {
		Optional<Let> lTemp = letRepository.findById(id);
		if(!lTemp.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trazeni let ne postoji");
		}
		Let l = lTemp.get();
		Short preostaloNakon = l.zauzmiMesto((short) (count*-1));
		letRepository.save(l);
		return preostaloNakon;
	}
	
	// DODATI ZASTITU!
	@PostMapping("/napravi-let")
	public String napraviLet(@RequestParam(value = "pocetnaDestinacija", required = true) String pocetnaDestinacija,
			@RequestParam(value = "krajnjaDestinacija", required = true) String krajnjaDestinacija,
			@RequestParam(value = "cena", required = true) Double cena,
			@RequestParam(value = "trajanje", required = true) Short trajanjeLeta,
			@RequestParam(value = "avion_id", required = true) Integer avionId){
		if(cena<0){
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cena ne moze biti negativna");
		}
		if(trajanjeLeta<0){
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Trajanje ne moze biti negativno");
		}
		Optional<Avion> aTemp = avionRepository.findById(avionId);
		if(!aTemp.isPresent()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avion nije pronadjen");
		}
		
		
		Let l = new Let();
		l.setPocetnaDestinacija(pocetnaDestinacija);
		l.setKrajnjaDestinacija(krajnjaDestinacija);
		l.setCena(cena);
		l.setTrajanjeLeta(trajanjeLeta);
		l.setAvion(aTemp.get());
		l.setStatus(LetStatus.OPEN);
		
		letRepository.save(l);
		return "ok";
	}
	
	// DODATI ZASTITU!
	@PostMapping("/ukloni-let/{id}")
	public String ukloniLet(@PathVariable(required = true) Integer id){
		Optional<Let> lTemp = letRepository.findById(id);
		if(!lTemp.isPresent()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Let nije pronadjen");
		}
		
		Let let = lTemp.get();
		// Provera da li je prodata karta
		// Ukoliko je broj mesta manji od mogucih mesta na avionu - neka karta je prodata
		if(let.preostaloMesta() < let.getAvion().getKapacitet()) {
			// Poslati mail korisniku
			// Javiti servisu1 i servisu3
			let.setStatus(LetStatus.CANCELLED);
			letRepository.save(let);
		}
		// U suprotnom nije nista prodato, samo obrisi let
		else {
			letRepository.delete(let);
		}
		
		return "ok";
	}
}
