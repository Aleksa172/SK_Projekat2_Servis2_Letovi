package com.raf.asmi.letovi.repository;

import org.springframework.data.repository.CrudRepository;

import com.raf.asmi.letovi.entiteti.Avion;

public interface AvionRepository extends CrudRepository<Avion, Integer>, AvionRepositoryCustom {

}
