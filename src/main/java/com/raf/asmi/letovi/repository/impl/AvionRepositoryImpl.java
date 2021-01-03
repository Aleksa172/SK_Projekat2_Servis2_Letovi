package com.raf.asmi.letovi.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.raf.asmi.letovi.entiteti.Avion;
import com.raf.asmi.letovi.repository.AvionRepository;

public class AvionRepositoryImpl extends SimpleJpaRepository<Avion, Integer> implements AvionRepository{

	@PersistenceContext
	private EntityManager em;
	
	public AvionRepositoryImpl(EntityManager em) {
		super(Avion.class, em);
		this.em=em;
	}

	@Override
	public List<Avion> getAvione(int from, int count) {
		Query q = em.createQuery("SELECT a FROM Avion a", Avion.class);
		q.setMaxResults(count);
		q.setFirstResult(from);
		
		return q.getResultList();
	}
	
	@Override
	public Boolean isDodeljen(Integer avionId) {
		Query q = em.createQuery("SELECT count(l.id) FROM Let l WHERE l.avion.id=:avionId");
		q.setParameter("avionId", avionId);
		return ((long) q.getSingleResult()) > 0;
	}
	

}
