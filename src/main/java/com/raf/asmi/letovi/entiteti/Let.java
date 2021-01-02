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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Avion getAvion() {
		return avion;
	}
	public void setAvion(Avion avion) {
		this.avion = avion;
	}
	public String getPocetnaDestinacija() {
		return pocetnaDestinacija;
	}
	public void setPocetnaDestinacija(String pocetnaDestinacija) {
		this.pocetnaDestinacija = pocetnaDestinacija;
	}
	public String getKrajnjaDestinacija() {
		return krajnjaDestinacija;
	}
	public void setKrajnjaDestinacija(String krajnjaDestinacija) {
		this.krajnjaDestinacija = krajnjaDestinacija;
	}
	public short getTrajanjeLeta() {
		return trajanjeLeta;
	}
	public void setTrajanjeLeta(short trajanjeLeta) {
		this.trajanjeLeta = trajanjeLeta;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public LetStatus getStatus() {
		return status;
	}
	public void setStatus(LetStatus status) {
		this.status = status;
	}
	
	
	
}
