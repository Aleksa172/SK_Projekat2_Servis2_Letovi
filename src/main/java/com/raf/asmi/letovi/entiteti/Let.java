package com.raf.asmi.letovi.entiteti;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Let {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="avion_id")
	private Avion avion;
	private String pocetnaDestinacija;
	private String krajnjaDestinacija;
	private short trajanjeLeta; // u minutama
	private double cena; // u evrima
	@Enumerated(EnumType.STRING) // Stanje leta - OPEN, CANCELLED
	private LetStatus status;
	
}
