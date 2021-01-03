package com.raf.asmi.letovi.repository;

import java.util.HashMap;
import java.util.List;

import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.entiteti.Let;

public interface LetRepositoryCustom {
	List<Let> getDostupneLetove(
			int from,
			int count,
			Avion avionFilter, 
			HashMap<String, Object> filterParams);
	
	Long getFilteredTotalRows(Avion avionFilter, HashMap<String, Object> filterParams);
}
