package com.raf.asmi.letovi.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.entiteti.Let;
import com.raf.asmi.letovi.repository.LetRepository;

public class LetRepositoryImpl extends SimpleJpaRepository<Let, Integer> implements LetRepository{

	@PersistenceContext
	private EntityManager em;

	public LetRepositoryImpl(EntityManager em) {
		super(Let.class, em);
		this.em = em;
	}

	@Override
	public List<Let> getDostupneLetove(Avion avionFilter, String pocetnaDestinacijaFilter,
			String krajnjaDestinacijaFilter, short trajanjeOdFilter, short trajanjeDoFilter, double cenaOdFilter,
			double cenaDoFilter) {
		
		Query q = this.em.createQuery("SELECT l FROM Let l",Let.class);
		
		return q.getResultList();
	}

}
