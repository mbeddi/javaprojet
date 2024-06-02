package spring.jpa.controller;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spring.jpa.model.Administrateur;
import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.EtatConge;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;
import spring.jpa.repository.UserRepository;

@Controller
@RequestMapping(value = "/conge")
public class CongeController {

	@Autowired // pour l'injection de dépendances
	private UserRepository userRepos;

	@Autowired // pour l'injection de dépendances
	private CongeRepository congeRepos;

	@Autowired // pour l'injection de dépendances
	private EmployeRepository employeRepos;

	/*
	 * //méthode pour changer l'état du congé
	 * 
	 * @PostMapping("/cancelConge") public String cancelConge(@RequestParam Long id)
	 * { Conge conge = congeRepos.findById(id).orElse(null); if (conge != null) {
	 * conge.setEtatconge(EtatConge.Annule); // ou EtatConge.CANCELLED si vous avez
	 * une autre valeur pour "Annulé" congeRepos.save(conge); } return
	 * "redirect:employeconge"; }
	 */

	/*
	 * @GetMapping("/validerConge") public String validerConge(@RequestParam Long
	 * id) { Conge conge = congeRepos.findById(id).orElse(null); if(conge != null &&
	 * conge.getEtatconge().equals(EtatConge.Sollicite)) {
	 * 
	 * conge.setEtatconge(EtatConge.Valide); congeRepos.save(conge);
	 * 
	 * //Mettre à jour le nombre de jour de congés restant d'un employe dans l'année
	 * Employe emp = conge.getEmploye(); long diffInMillies =
	 * Math.abs(conge.getDateFin().getTime() - conge.getDateDebut().getTime()); long
	 * daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	 * emp.setNbreJoursConge((int) (emp.getNbreJoursConge() - daysBetween));
	 * employeRepos.save(emp); } return "redirect:admin"; }
	 */

	@GetMapping("/validerConge")
	public String validerConge(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		Conge conge = congeRepos.findById(id).get();
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
				employeRepos.save(emp);
			} else {
				System.out.println("L'employé n'a pas suffisamment de jours de congé restants.");
//				conge.setEtatconge(EtatConge.Valide.toString());
			}
			congeRepos.save(conge);
		} else {
			System.out.println("la demande congé n'existe pas ou n'est plus sollicité");
		}
		return "redirect:admin";
	}

	@GetMapping("/refuserConge")
	public String refuserConge(@RequestParam Long id) {
		Conge conge = congeRepos.findById(id).orElse(null);
		if (conge != null && conge.getEtatconge().equals(EtatConge.Sollicite.toString())) {
			conge.setEtatconge(EtatConge.Refuse.toString());
			congeRepos.save(conge);
		}
		return "redirect:admin";
	}

	@GetMapping("/arreterConge")
	public String arreterConge(@RequestParam Long id) {
		Conge conge = congeRepos.findById(id).orElse(null);

		if (conge != null && conge.getEtatconge().equals(EtatConge.En_cours.toString())) {
			conge.setEtatconge(EtatConge.Arrete.toString());
			Date dateRupture = new Date();
			conge.setDateRupture(dateRupture);

			// Calculer le nombre de jours restants entre la date de rupture et la date de
			// fin
			long diffInMillies = Math.abs(conge.getDateFin().getTime() - dateRupture.getTime());
			long daysRemaining = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			// Mettre à jour le nombre de jours de congé restants de l'employé
			Employe emp = conge.getEmploye();
			emp.setNbreJoursConge(emp.getNbreJoursConge() + (int) daysRemaining);

			// Sauvegarder les changements
			congeRepos.save(conge);
			employeRepos.save(emp);

		}
		return "redirect:admin";
	}

	@GetMapping("/verifierConge")
	public String verifierConge(@RequestParam Long id) {
		Conge conge = congeRepos.findById(id).orElse(null);
		if (conge != null && conge.getEtatconge().equals(EtatConge.Sollicite.toString())) {
			conge.setEtatconge(EtatConge.Refuse.toString());
			congeRepos.save(conge);
		}
		return "redirect:admin";
	}
}
