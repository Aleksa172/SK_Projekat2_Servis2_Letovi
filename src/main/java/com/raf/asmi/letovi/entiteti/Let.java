package com.raf.asmi.letovi.entiteti;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Let {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	String naziv;
}
