package spring.jpa.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("administrateur")
public class Administrateur extends User{

	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Administrateur() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Administrateur(Long id, String nom, String prenom, String code, String login, String password) {
		super(id, nom, prenom, code, login, password);
		// TODO Auto-generated constructor stub
	}

	public Administrateur(String nom, String prenom, String code, String login, String password) {
		super(nom, prenom, code, login, password);
		// TODO Auto-generated constructor stub
	}

	public Administrateur(String nom, String prenom, String code, String login, String password,String role) {
		super(nom, prenom, code, login, password);
		this.role = role;
	}

	@Override
	public String toString() {
		return "Administrateur [role=" + role + ", id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", code=" + code
				+ ", login=" + login + ", password=" + password + "]";
	}
	
	public String getClassName() {
		return "Administrateur";
	}
	
}
