package com.raf.asmi.letovi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.entiteti.Let;

public interface LetRepository extends CrudRepository<Let, Integer>{
	List<Let> getDostupneLetove(
			Avion avionFilter, 
			String pocetnaDestinacijaFilter, 
			String krajnjaDestinacijaFilter, 
			short trajanjeOdFilter, short trajanjeDoFilter,
			double cenaOdFilter, double cenaDoFilter);
}
