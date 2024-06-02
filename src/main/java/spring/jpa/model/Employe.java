package spring.jpa.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("employe")
public class Employe extends User {
	private int NbreJoursConge = 30;

	@OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Conge> conges = new ArrayList<Conge>();

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dateEmbauche;

	public Employe() {
		super();
	}

	public Employe(String nom, String prenom, String login, String password) {
		super(nom, prenom, login, password);
	}

	public Employe(String nom, String prenom, String code, String login, String password) {
		super(nom, prenom, code, login, password);
		// TODO Auto-generated constructor stub
	}

	public Employe(String nom, String prenom, String login, @NotNull @Size(max = 10, min = 6) String password,
			Date dateEmbauche) {
		super(nom, prenom, login, password);
		this.dateEmbauche = dateEmbauche;
		// TODO Auto-generated constructor stub
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

	public Date getDateEmbauche() {
		return dateEmbauche;
	}

	public void setDateEmbauche(Date dateEmbauche) {
		this.dateEmbauche = dateEmbauche;
	}

	@Override
	public String toString() {
		return "Employe [NbreJoursConge=" + NbreJoursConge + ", conges=" + conges + ", dateEmbauche=" + dateEmbauche
				+ ", id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", code=" + code + ", login=" + login
				+ ", password=" + password + "]";
	}

}