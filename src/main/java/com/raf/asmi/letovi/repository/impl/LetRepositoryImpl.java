package com.raf.asmi.letovi.repository.impl;

import java.util.HashMap;
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
	public List<Let> getDostupneLetove(int from, int count, Avion avionFilter, HashMap<String,Object> filterParams) {
		
		// pocetnaDestinacija
		// krajnjaDestinacija
		// trajanjeOd
		// trajanjeDo
		// cenaOd
		// cenaDo
		
		Query q = this.em.createQuery("SELECT l FROM Let l " + 
				"WHERE (COALESCE(null, :pocetnaDestinacija) is NULL or l.pocetnaDestinacija = :pocetnaDestinacija) "+
				"AND (COALESCE(null, :krajnjaDestinacija) is NULL or l.krajnjaDestinacija = :krajnjaDestinacija) "+
				"AND (COALESCE(null, :trajanjeOd) is NULL or l.trajanjeLeta >= :trajanjeOd) "+
				"AND (COALESCE(null, :trajanjeDo) is NULL or l.trajanjeLeta <= :trajanjeDo) "+
				"AND (COALESCE(null, :cenaOd) is NULL or l.cena >= :cenaOd) "+
				"AND (COALESCE(null, :cenaDo) is NULL or l.cena <= :cenaDo) ", Let.class);
		q.setFirstResult(from);
		q.setMaxResults(count);
		
		
		// Postavice se na vrednost, ili na null ako je nema
		// a COALESCE ce se postarati da null vraca sve vrednosti 
		
		q.setParameter("pocetnaDestinacija", filterParams.get("pocetnaDestinacija"));
		q.setParameter("krajnjaDestinacija", filterParams.get("krajnjaDestinacija"));
		q.setParameter("trajanjeOd", filterParams.get("trajanjeOd"));
		q.setParameter("trajanjeDo", filterParams.get("trajanjeDo"));
		q.setParameter("cenaOd", filterParams.get("cenaOd"));
		q.setParameter("cenaDo", filterParams.get("cenaDo"));
		
		
		return q.getResultList();
	}
	
	@Override
	public Long getFilteredTotalRows(Avion avionFilter, HashMap<String, Object> filterParams) {
		Query q = this.em.createQuery("SELECT count(l.id) FROM Let l " + 
				"WHERE (COALESCE(null, :pocetnaDestinacija) is NULL or l.pocetnaDestinacija = :pocetnaDestinacija) "+
				"AND (COALESCE(null, :krajnjaDestinacija) is NULL or l.krajnjaDestinacija = :krajnjaDestinacija) "+
				"AND (COALESCE(null, :trajanjeOd) is NULL or l.trajanjeLeta >= :trajanjeOd) "+
				"AND (COALESCE(null, :trajanjeDo) is NULL or l.trajanjeLeta <= :trajanjeDo) "+
				"AND (COALESCE(null, :cenaOd) is NULL or l.cena >= :cenaOd) "+
				"AND (COALESCE(null, :cenaDo) is NULL or l.cena <= :cenaDo) ");
		
		q.setParameter("pocetnaDestinacija", filterParams.get("pocetnaDestinacija"));
		q.setParameter("krajnjaDestinacija", filterParams.get("krajnjaDestinacija"));
		q.setParameter("trajanjeOd", filterParams.get("trajanjeOd"));
		q.setParameter("trajanjeDo", filterParams.get("trajanjeDo"));
		q.setParameter("cenaOd", filterParams.get("cenaOd"));
		q.setParameter("cenaDo", filterParams.get("cenaDo"));
		
		return (Long) q.getSingleResult();
	}

}
