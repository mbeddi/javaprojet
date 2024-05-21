package spring.jpa.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.EtatConge;

public interface CongeRepository extends JpaRepository<Conge, Long> {

	List<Conge> findByEmploye(Employe emp);
	List<Conge> findByEtatconge(EtatConge etatConge);
	
}
