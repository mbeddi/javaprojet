package spring.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import spring.jpa.model.Conge;
import spring.jpa.model.Employe;

public interface CongeRepository extends JpaRepository<Conge, Long> {

	List<Conge> findByEmploye(Employe emp);

	// Filtrage Administrateur
	Page<Conge> findByEmployeId(Long emp, Pageable pageable);
	// Page<Conge> findAll(Pageable pageable);

	Page<Conge> findByEtatconge(String string, Pageable pageable);

	Page<Conge> findByEtatcongeAndEmployeId(String state, Long emp, Pageable pageable);

	Page<Conge> findByDateFinBefore(Date endDate, Pageable pageable);

	Page<Conge> findByDateFinBeforeAndEmployeId(Date endDate, Long emp, Pageable pageable);

	Page<Conge> findByDateFinBeforeAndEtatconge(Date endDate, String state, Pageable pageable);

	Page<Conge> findByDateFinBeforeAndEtatcongeAndEmployeId(Date endDate, String state, Long emp, Pageable pageable);

	Page<Conge> findByDateDebutAfter(Date startDate, Pageable pageable);

	Page<Conge> findByDateDebutAfterAndEmployeId(Date start, Long emp, Pageable pageable);

	Page<Conge> findByDateDebutAfterAndEtatconge(Date startDate, String state, Pageable pageable);

	Page<Conge> findByDateDebutAfterAndEtatcongeAndEmployeId(Date start, String state, Long employe, Pageable pageable);

	Page<Conge> findByDateDebutBetween(Date startDate, Date endDate, Pageable pageable);

	Page<Conge> findByDateDebutBetweenAndEmployeId(Date startDate, Date endDate, Long employe, Pageable pageable);

	Page<Conge> findByDateDebutBetweenAndEtatconge(Date startDate, Date endDate, String state, Pageable pageable);

	Page<Conge> findByDateDebutBetweenAndEtatcongeAndEmployeId(Date startDate, Date endDate, String state, Long employe,
			Pageable pageable);
}
