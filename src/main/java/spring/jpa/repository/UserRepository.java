package spring.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.jpa.model.Employe;
import spring.jpa.model.User;

public interface UserRepository extends JpaRepository<User, Long> {



	User findByLoginAndPassword(String login, String password);
}
