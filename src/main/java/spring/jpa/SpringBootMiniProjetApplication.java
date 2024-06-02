/********************************Dechateau******************************************/
package spring.jpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;

import spring.jpa.model.Administrateur;
import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.EtatConge;
import spring.jpa.repository.AdminRepository;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;

@SpringBootApplication
public class SpringBootMiniProjetApplication {
	// Reccuprration des repository
	static EmployeRepository empRepos;
	static CongeRepository congeRepos;
	static AdminRepository adminRepos;

	public static void main(String[] args) {
		// Reccuperation du contexte
		ApplicationContext contexte = SpringApplication.run(SpringBootMiniProjetApplication.class, args);

		// Récupérer une implémentation des repository par injection de dépendance
		empRepos = contexte.getBean(EmployeRepository.class);
		congeRepos = contexte.getBean(CongeRepository.class);
		adminRepos = contexte.getBean(AdminRepository.class);

		// Phase de testdans le main terminée

		// Initialisation des dates
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = null;
		java.util.Date date2 = null;
		java.util.Date date3 = null;
		java.util.Date date4 = null;
		java.util.Date date5 = null;
		java.util.Date date6 = null;
		java.util.Date date7 = null;
		java.util.Date date8 = null;
		java.util.Date dateEmbauche1 = null;
		java.util.Date dateEmbauche2 = null;
		java.util.Date dateEmbauche3 = null;
		// java.util.Date dateEmbauche4= null;
		try {
			dateEmbauche1 = sdf.parse("2023-02-17");
			dateEmbauche2 = sdf.parse("2023-05-15");
			dateEmbauche3 = sdf.parse("2024-03-15");
			// dateEmbauche4 = sdf.parse("2014-02-15");
			date1 = sdf.parse("2024-05-16");
			date2 = sdf.parse("2024-05-28");
			date3 = sdf.parse("2024-05-16");
			date4 = sdf.parse("2024-05-17");
			date5 = sdf.parse("2024-05-18");
			date6 = sdf.parse("2024-06-23");
			date7 = sdf.parse("2025-02-23");
			date8 = sdf.parse("2024-12-23");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		//Insertion des employe
//		Employe e1 = new Employe("Foyou","Dechateau","Kamklesh2035","123456",dateEmbauche1);
//		Employe e2 = new Employe("Ndo","Dechateau","Kamklesh2035","123456",dateEmbauche2);
//		Employe e3 = new Employe("Mohammed","Beddi","beddi242","123456",dateEmbauche3);
//		empRepos.save(e1);
//		empRepos.save(e2);
//		empRepos.save(e3);
//		
//		//Insertion des administrateurs
//		Administrateur a1 = new Administrateur("Mahoukou","Parker","parker236","123456");
//		adminRepos.save(a1);

		/*
		 * 
		 * Conge c1 = new Conge("Congé de paternité",date1, date2); Conge c2 = new
		 * Conge("Congé de paternité",date3, date4); Conge c3 = new
		 * Conge("Congé de paternité",date5, date6); Conge c4 = new
		 * Conge("Congé de paternité",date8, date7); congeRepos.save(c1);
		 * congeRepos.save(c2); congeRepos.save(c3); congeRepos.save(c4);
		 */

//		//Insertion des congés
//		demanderConge("Conge de paternite",1L,date1, date2);
//		demanderConge("Assistance malade",2L,date3, date4);
//		demanderConge("Congé de paternité",3L,date5, date6);
//		
//		validerConge(1L,4L);
//		//refuserConge(2L,4L);
//		validerConge(2L,4L);
//		//annulerConge(1L,1L);
//		//annulerConge(1L,1L);
//		
//		verifieEtatConge();
//		
////		arreterConge(1L,4L);
////		arreterConge(2L,4L);
//		
//		//Verifie l'etat des congés
		verifieEtatConge();

//		adminConge();
//		employeConge(1L);
//		employeConge(2L);
//		employeConge(3L);

//		List<Conge> conges = congeRepos.findByDateDebutBetweenAndEtatcongeAndEmployeId(date1, date5,"Annule",1L,PageRequest.of(p, 6));
//		for(Conge c:conges) {
//			System.out.println(c+"\n");
//		}

	}

	// Demander un congé ( actiion faites par l'employé )
	public static void demanderConge(String desc, Long empId, Date dateDbt, Date dateFn) {
		Employe emp = empRepos.findById(empId).get();

		// Verifier si l'employe a travaillé au moins 1 an
		Date dateEmbauche = new Date(emp.getDateEmbauche().getTime());
		LocalDate dateEmbauchement = dateEmbauche.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = LocalDate.now();
		// Calcule de la difference de jour
		long daysBetween = ChronoUnit.DAYS.between(dateEmbauchement, currentDate);
		if (daysBetween < 365) {
			System.out.println("Echec de l'envoi de la demande pour l'employe " + emp.getNom());
		} else {
			Conge conge = new Conge(desc, dateDbt, dateFn, emp);
			congeRepos.save(conge);
			System.out.println("Demande envoyé avec Succès pour l'employe " + emp.getNom());
		}
	}

	// Annuler la demande de congé ( actiion faites par l'employé )
	public static void annulerConge(Long congId, Long empId) {
		Conge conge = congeRepos.findById(congId).get();
		Employe emp = empRepos.findById(empId).get();
		if (conge != null && conge.getEmploye().getId().equals(empId)) {
			if (conge.getEtatconge().equals(EtatConge.Sollicite.toString())) {
				conge.setEtatconge(EtatConge.Annule.toString());
				congeRepos.save(conge);
			} else {
				System.out.println("Imossible d'annuler ce congé pour " + emp.getNom());
			}
		} else {
			System.out.println("Aucune demande de congé exixtante avec cet identifiant");
		}
	}

	// Valider la demande de congé ( actiion faites par l'employé )
	public static void validerConge(Long congId, Long adminId) {
		Conge conge = congeRepos.findById(congId).get();
		Administrateur admin = adminRepos.findById(adminId).get();
		if (conge != null && conge.getEtatconge().equals(EtatConge.Sollicite.toString())) {
			// Determine le nobre de jour que va prendre le congé
			long diffInMillies = Math.abs(conge.getDateFin().getTime() - conge.getDateDebut().getTime());
			long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			// Veriife si le nbreJour restant de l'employe <= nbre de jour du conge
			Employe emp = conge.getEmploye();
			if (emp.getNbreJoursConge() >= daysBetween) {
				conge.setEtatconge(EtatConge.Valide.toString());
				// Mettre à jour le nombre de jour de congés restant d'un employe dans l'année
				emp.setNbreJoursConge((int) (emp.getNbreJoursConge() - daysBetween));
				empRepos.save(emp);
			} else {
				System.out.println("L'employé n'a pas suffisamment de jours de congé restants.");
//				conge.setEtatconge(EtatConge.Valide.toString());
			}
			congeRepos.save(conge);
		} else {
			System.out.println("la demande congé n'existe pas ou n'est plus sollicité");
		}
	}

	// Refuser la demande de congé ( actiion faites par l'employé )
	public static void refuserConge(Long congId, Long adminId) {
		Conge conge = congeRepos.findById(congId).get();
		Administrateur admin = adminRepos.findById(adminId).get();
		if (conge != null && conge.getEtatconge().equals(EtatConge.Sollicite.toString())) {
			conge.setEtatconge(EtatConge.Refuse.toString());
			congeRepos.save(conge);
		} else {
			System.out.println("la demande congé n'existe pas ou n'est plus sollicité");
		}
	}

	// Verifier etat des congés
	public static void verifieEtatConge() {
		Collection<Conge> conges = congeRepos.findAll();
		if (conges != null) {
			LocalDate currentDate = LocalDate.now();
			for (Conge conge : conges) {
				Date dateDt = new Date(conge.getDateDebut().getTime());
				Date dateFn = new Date(conge.getDateFin().getTime());
				LocalDate dateDebut = dateDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate dateFin = dateFn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				/*
				 * System.out.
				 * println("\n\nla demande congé n'existe pas ou n'est plus sollicité");
				 * System.out.println("DD = CD : "+dateDebut.isEqual(currentDate));
				 * System.out.println("DD < CD : "+dateDebut.isBefore(currentDate));
				 * System.out.println("DF > CD : "+dateDebut.isBefore(currentDate));
				 * System.out.println("DF < CD : "+dateFin.isBefore(currentDate));
				 * System.out.println("DF = CD : "+dateFin.isEqual(currentDate));
				 */

				if (conge.getEtatconge().equals(EtatConge.Valide.toString())) {
					if (dateDebut.isEqual(currentDate)
							|| (dateDebut.isBefore(currentDate) && dateFin.isAfter(currentDate))) {
						conge.setEtatconge(EtatConge.En_cours.toString());
						congeRepos.save(conge);
					}
					if (dateFin.isBefore(currentDate) || dateFin.isEqual(currentDate)) {
						conge.setEtatconge(EtatConge.Fini.toString());
						congeRepos.save(conge);
					}
				}
			}
		}
	}

	// Arreter un congé deja en cours avant la date de fin ( actiion faites par
	// l'employé )
	public static void arreterConge(Long congId, Long adminId) {
		Conge conge = congeRepos.findById(congId).get();
		Administrateur admin = adminRepos.findById(adminId).get();
		if (conge != null && conge.getEtatconge().equals(EtatConge.En_cours.toString())) {
			// Mettre à jour l'état du congé
			conge.setEtatconge(EtatConge.Arrete.toString());
			Date dateRupture = new Date();
			conge.setDateRupture(dateRupture);
			congeRepos.save(conge);
			// Calculer le nombre de jours restants entre la date de rupture et la date de
			// fin
			long diffInMillies = Math.abs(conge.getDateFin().getTime() - dateRupture.getTime());
			long daysRemaining = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			// Mettre à jour le nombre de jours de congé restants de l'employé
			Employe emp = conge.getEmploye();
			emp.setNbreJoursConge(emp.getNbreJoursConge() + (int) daysRemaining);

			// Sauvegarder les changements
			congeRepos.save(conge);
			empRepos.save(emp);
			System.out.println("Le congé a été arreté avec success par " + admin.getNom());
		} else {
			System.out.println("la demande de congé n'existe pas ou n'est deja terminé " + conge.getDescription());
		}
	}

	// Afficher tous les congés ( Admin )
	public static void adminConge() {
		List<Conge> conges = congeRepos.findAll();
		System.out.println(
				"******************************************************************************************************\n");
		System.out.println(
				"***********************************Liste de tous les congés*******************************************\n");
		System.out.println(
				"******************************************************************************************************\n");
		for (Conge conge : conges) {
			System.out.println(conge);
		}
	}

	// Afficher tous les congés ( Employé )
	public static void employeConge(Long empLId) {
		Employe empl = empRepos.findById(empLId).get();

		if (empl != null) {
			System.out.println(
					"******************************************************************************************************\n");
			System.out.println("****************Liste de tous les congés de l'employe [" + empl.getCode()
					+ "]****************************************\n");
			System.out.println(
					"******************************************************************************************************\n");
			Collection<Conge> conges = empl.getConges();
			if (conges != null) {
				for (Conge conge : conges) {
					System.out.println(conge);
				}
			} else {
				System.out.println("Aucun historique de congés pour cet employe");
			}
		} else {
			System.out.println("Employe inexistant");
		}
	}

	public static void mettreAJourCongesAnnuel() {
		List<Employe> employes = empRepos.findAll();

		for (Employe employe : employes) {
			Date dateEmbauche = employe.getDateEmbauche();
			LocalDate dateEmbaucheLocal = dateEmbauche.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateActuelle = LocalDate.now();

			// Calculer la différence en années entre la date d'embauche et la date actuelle
			int anneesEcoulees = dateActuelle.getYear() - dateEmbaucheLocal.getYear();

			// Si au moins une année s'est écoulée depuis la dernière mise à jour
			if (anneesEcoulees > 0) {
				// Mettre à jour le nombre de jours de congé restants
				int nouveauxJoursConges = employe.getNbreJoursConge() + 30;
				employe.setNbreJoursConge(nouveauxJoursConges);

				// Mettre à jour la date d'embauche pour la prochaine mise à jour
				// Optionnel: dépend si vous voulez modifier la date d'embauche ou juste marquer
				// la mise à jour annuelle
				// employe.setDateEmbauche(Date.from(dateActuelle.atStartOfDay(ZoneId.systemDefault()).toInstant()));

				empRepos.save(employe);
			}
		}
	}

}
