package spring.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_PERSONNE")
@DiscriminatorValue("Personne")
public class User {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	protected Long id;
	
	@Column(length = 50)
	protected String nom;
	
	@Column(length = 50)
	protected String prenom;
	protected String code;
	@Column(length = 50)
	protected String login;
	protected String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public User(String nom, String prenom, String code, String login, String password) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.code = code;
		this.login = login;
		this.password = password;
	}
	public User(Long id, String nom, String prenom, String code, String login, String password) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.code = code;
		this.login = login;
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [nom=" + nom + ", prenom=" + prenom + ", code=" + code + ", login=" + login + ", password="
				+ password + "]";
	}
	
	
	
}
