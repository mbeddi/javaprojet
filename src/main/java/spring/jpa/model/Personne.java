package spring.jpa.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
// pour déclarer une classe mère (non entité)
@MappedSuperclass
public class Personne {
@Id
@GeneratedValue (strategy = GenerationType.AUTO)
protected Long id;
protected String nom;
protected String prenom;
public Personne(String nom, String prenom) {

super();
this.nom = nom;
this.prenom = prenom;
}
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
public Personne() {
super();
} }
