package com.raf.asmi.letovi;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raf.asmi.letovi.entiteti.Let;
import com.raf.asmi.letovi.repository.LetRepository;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	@Autowired
	private LetRepository letRepository;


	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
	@GetMapping("/test")
	public String test(@RequestParam(value = "name", defaultValue = "123") String name) {
		Let l = new Let();
		l.setTrajanjeLeta((short) 30);
		letRepository.save(l);
		
		
		return "Hello";
	}
	
}
