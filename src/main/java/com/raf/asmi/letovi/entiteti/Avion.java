package com.raf.asmi.letovi.entiteti;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Avion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String naziv;
	private short kapacitet;
	
	public Integer getId() {
		return id;
	}
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public short getKapacitet() {
		return kapacitet;
	}
	public void setKapacitet(short kapacitet) {
		this.kapacitet = kapacitet;
	}
}
