package com.raf.asmi.letovi.entiteti;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.transaction.Transactional;

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
	private short duzinaLeta; // u km
	private double cena; // u evrima
	@Enumerated(EnumType.STRING) // Stanje leta - OPEN, CANCELLED
	private LetStatus status;
	private short trenutnoZauzeto; // Koliko mesta je trenutno zauzeto u avionu
	public Integer getId() {
		return id;
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
	public short getDuzinaLeta() {
		return duzinaLeta;
	}
	public void setDuzinaLeta(short duzinaLeta) {
		this.duzinaLeta = duzinaLeta;
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
	public short getTrenutnoZauzeto() {
		return trenutnoZauzeto;
	}
	
	public Short preostaloMesta(){
		if(this.avion == null){
			return null;
		}
		return new Short((short) (this.avion.getKapacitet() - this.trenutnoZauzeto));
	}
	
	@Transactional
	public Short zauzmiMesto(short brojMesta){
		if(this.avion == null){
			return null;
		}
		
		this.trenutnoZauzeto+=brojMesta;

		// Broj zauzetih mesta ne sme nikada biti negativan
		if(this.trenutnoZauzeto<0) {
			this.trenutnoZauzeto=0;
		}
		// Sa druge strane - ne bi ni trebalo da bude veci od kapaciteta trenutnog aviona
		if(this.trenutnoZauzeto>this.avion.getKapacitet()){
			this.trenutnoZauzeto=this.avion.getKapacitet();
		}
		return new Short((short) (this.avion.getKapacitet() - this.trenutnoZauzeto));
	}
	
}
