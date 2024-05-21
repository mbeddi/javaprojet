package spring.jpa.model;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@Entity
@DiscriminatorValue("employe")
public class Employe extends User
{
	private int NbreJoursConge = 30;
	
	@OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Conge> conges = new ArrayList<Conge>();


public Employe() {
super();
// TODO Auto-generated constructor stub
}

public Employe(Long id, String nom, String prenom, String code, String login, String password) {
	super(id, nom, prenom, code, login, password);
	
}
public Employe(String nom, String prenom, String code, String login, String password) {
	super(nom, prenom, code, login, password);
	// TODO Auto-generated constructor stub
}

public Employe(String nom, String prenom, String code, String login, String password,int nbreJoursConge, Collection<Conge> conges) {
	super(nom, prenom, code, login, password);
	NbreJoursConge = nbreJoursConge;
	this.conges = conges;
}

public Employe(String nom, String prenom, String code, String login, String password, Collection<Conge> conges) {
	super(nom, prenom, code, login, password);
	this.conges = conges;
}

public int getNbreJoursConge() {
	return NbreJoursConge;
}

public void setNbreJoursConge(int nbreJoursConge) {
	NbreJoursConge = nbreJoursConge;
}

public Collection<Conge> getConges() {
	return conges;
}

public void setConges(Collection<Conge> conges) {
	this.conges = conges;
}

@Override
public String toString() {
	return "Employe [NbreJoursConge=" + NbreJoursConge + ", conges=" + conges + ", id=" + id + ", nom=" + nom
			+ ", prenom=" + prenom + ", code=" + code + ", login=" + login + ", password=" + password + "]";
}

public String getClassName() {
	return "Employe";
}

}