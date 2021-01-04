package com.raf.asmi.letovi;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.raf.asmi.letovi.entiteti.Let;

@Component
public class ListenerComponent {

	@JmsListener(destination="test.queue")
	public void consume(String message){
		System.out.println("Message received "+message);
	}
	
	// Test - da li neko drugi cuje
	@JmsListener(destination="obrisanlet.queue")
	public void obrisanLet(String id) {
		// Integer nije trusted
		System.out.println(id);
	}
}
