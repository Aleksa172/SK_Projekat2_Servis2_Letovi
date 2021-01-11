package com.raf.asmi.letovi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.jms.Queue;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.entiteti.Let;
import com.raf.asmi.letovi.entiteti.LetStatus;
import com.raf.asmi.letovi.repository.AvionRepository;
import com.raf.asmi.letovi.repository.LetRepository;
import com.raf.asmi.letovi.repository.impl.LetRepositoryImpl;
import com.raf.asmi.letovi.security.SecurityConstants;

@RestController
public class LetController {
	
	@Autowired
	private LetRepository letRepository;
	@Autowired
	private AvionRepository avionRepository;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Queue obrisanLetQueue;
	@Autowired
	private Queue obrisanLetS1Queue;

	@GetMapping("/letovi")
	public ResponseEntity<HashMap<String, Object>> getDostupneLetove(
			@RequestParam(value = "from", defaultValue = "0") Integer from,
			@RequestParam(value = "count", defaultValue = "10") Integer count,
			@RequestParam(value = "pocetnaDestinacija", required = false) String pocetnaDestinacija,
			@RequestParam(value = "krajnjaDestinacija", required = false) String krajnjaDestinacija,
			@RequestParam(value = "cenaOd", required = false) Double cenaOd,
			@RequestParam(value = "cenaDo", required = false) Double cenaDo,
			@RequestParam(value = "duzinaOd", required = false) Short duzinaOd,
			@RequestParam(value = "duzinaDo", required = false) Short duzinaDo,
			@RequestParam(value = "avionId", required = false) Integer avionId) {
		
		
		Avion avion = null;
		// Filtracija po avionu
		if(avionId != null) {
			Optional<Avion> aTemp = avionRepository.findById(avionId);
			if(!aTemp.isPresent()) {
				HashMap<String, Object> result = new HashMap<>();
				result.put("message", "Trazeni avion ne postoji");
				return new ResponseEntity(result, HttpStatus.NOT_FOUND);
			}
			avion = aTemp.get();
		}
		
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
		if(duzinaOd != null){
			params.put("duzinaOd", duzinaOd);
		}
		if(duzinaDo != null){
			params.put("duzinaDo", duzinaDo);
		}
		
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("data", letRepository.getDostupneLetove(from, count, avion, params));
		result.put("total_count", letRepository.getFilteredTotalRows(avion, params));
		return new ResponseEntity(result, HttpStatus.OK);
		
	}
	
	@GetMapping("/let/{id}")
	public Optional<Let> getLet(@PathVariable(required = true) Integer id) {
		
		return letRepository.findById(id);
	}
	
	
	@GetMapping("/let/{id}/preostalo-mesta")
	public Short getPreostaloMesta(@PathVariable(required = true) Integer id) {
		Optional<Let> lTemp = letRepository.findById(id);
		if(!lTemp.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trazeni let ne postoji");
		}
		return lTemp.get().preostaloMesta();
	}
	
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
	
	@PostMapping("/napravi-let")
	public String napraviLet(@RequestParam(value = "pocetnaDestinacija", required = true) String pocetnaDestinacija,
			@RequestParam(value = "krajnjaDestinacija", required = true) String krajnjaDestinacija,
			@RequestParam(value = "cena", required = true) Double cena,
			@RequestParam(value = "duzina", required = true) Short duzinaLeta,
			@RequestParam(value = "avion_id", required = true) Integer avionId,
			@RequestHeader(value = SecurityConstants.HEADER_STRING) String token){
		String role = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getClaim("role").asString();

		// Zabrani svima koji nisu admini
		if(!role.equals("ADMIN")) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Ova strana je dozvoljena samo adminima");
		}
		
		if(cena<0){
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cena ne moze biti negativna");
		}
		if(duzinaLeta<0){
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
		l.setDuzinaLeta(duzinaLeta);
		l.setAvion(aTemp.get());
		l.setStatus(LetStatus.OPEN);
		
		letRepository.save(l);
		return "ok";
	}
	
	@PostMapping("/ukloni-let/{id}")
	public String ukloniLet(@PathVariable(required = true) Integer id,
			@RequestHeader(value = SecurityConstants.HEADER_STRING) String token){
		String role = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getClaim("role").asString();

		// Zabrani svima koji nisu admini
		if(!role.equals("ADMIN")) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Ova strana je dozvoljena samo adminima");
		}
		
		Optional<Let> lTemp = letRepository.findById(id);
		if(!lTemp.isPresent()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Let nije pronadjen");
		}
		
		Let let = lTemp.get();
		// Da li je let vec obrisan?
		if(let.getStatus().equals(LetStatus.CANCELLED)) {
			throw new ResponseStatusException(HttpStatus.GONE, "Let je vec uklonjen");
		}
		// Provera da li je prodata karta
		// Ukoliko je broj mesta manji od mogucih mesta na avionu - neka karta je prodata
		if(let.preostaloMesta() < let.getAvion().getKapacitet()) {
			// Poslati mail korisniku
			// Javiti servisu1 i servisu3
			let.setStatus(LetStatus.CANCELLED);
			letRepository.save(let);
			jmsTemplate.convertAndSend(obrisanLetQueue, let.getId()+"");
		}
		// U suprotnom nije nista prodato, samo obrisi let
		else {
			letRepository.delete(let);
		}
		
		// Ovo bi zapravo trebalo da radi servis 3 zato sto on zna ko je kupio kartu
		// this.sendCancellationEmail(destination, let);
		
		return "ok";
	}
	

}
