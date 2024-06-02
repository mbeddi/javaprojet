package spring.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.jpa.model.Conge;
import spring.jpa.model.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Long> {

	List<Employe> findByLoginAndPassword(String login, String password);
	// List<Conge> findBEmploye(Employe emp);
}
