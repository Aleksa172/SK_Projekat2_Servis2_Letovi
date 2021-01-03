package com.raf.asmi.letovi.repository;

import java.util.List;

import com.raf.asmi.letovi.entiteti.Avion;

public interface AvionRepositoryCustom {
	List<Avion> getAvione(int from, int count);
	Boolean isDodeljen(Integer avionId);
}
