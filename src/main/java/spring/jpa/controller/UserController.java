package spring.jpa.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import spring.jpa.model.Administrateur;
import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.EtatConge;
import spring.jpa.model.User;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;
import spring.jpa.repository.UserRepository;

@Controller // pour déclarer un contrôleur
@RequestMapping(value = "/conge") // nom logique dans l'URL pour accéder au contrôleur
public class UserController {

	@Autowired // pour l'injection de dépendances
	private UserRepository userRepos;

	@Autowired // pour l'injection de dépendances
	private CongeRepository congeRepos;

	@Autowired // pour l'injection de dépendances
	private EmployeRepository employeRepos;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login"; // Retourne le nom de la vue (login.html)
	}

	// @RequestMapping(value = "/congelist", method = RequestMethod.GET)
	// public String congelist() {
	// return "userList"; // Retourne le nom de la vue (welcome.html)
	// }

	@GetMapping("/welcome")
	public String welcome(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			model.addAttribute("userName", user.getNom()); // Ajoute le nom de l'utilisateur au modèle
			return "welcome"; // Retourne le nom de la vue (welcome.html)
		} else {
			return "redirect:/login"; // Redirige vers la page de login si l'utilisateur n'est pas trouvé dans la
										// session
		}

	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin(Model model) {
		List<Conge> lp = congeRepos.findAll();
		model.addAttribute("conges", lp);
		return "admin"; // Retourne le nom de la vue (welcome.html)
	}

	// méthode pour retourner tous les users
	@RequestMapping(value = "/allusers", method = RequestMethod.GET)
	public String allusers(Model model) {
		List<User> users = userRepos.findAll();
		model.addAttribute("users", users);
		return "allUserList";
	}

	@RequestMapping(value = "/userlist", method = RequestMethod.GET)
	public String userlist() {
		
		return "userList"; // Retourne le nom de la vue (welcome.html)
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(Model model, @Valid User user, BindingResult bindingResult, HttpSession session) {

		if (bindingResult.hasErrors()) {
			return "login";
		}
		user = userRepos.findByLoginAndPassword(user.getLogin(), user.getPassword());
		if (user != null) {
			session.setAttribute("user", user);
			if (user instanceof Employe) {
				Collection<Conge> list = congeRepos.findByEmploye((Employe) user);
				model.addAttribute("id", user.getId());
				model.addAttribute("conges", list);
				return "redirect:welcome";
			} else if (user instanceof Administrateur) {
				return "redirect:admin";
			}

		} else {
			// Authentification échouée, afficher un message d'erreur
			model.addAttribute("error", true);
			return "login";
		}
		return "login";
	}

	/*
	 * @RequestMapping(value = "/congelist", method = RequestMethod.GET) public
	 * String empCongeListe(Model model, @Valid Employe emp) {
	 * 
	 * List<Conge> lp = congeRepos.findByEmploye(emp); model.addAttribute("conges",
	 * lp); return "userList";
	 * 
	 * }
	 */

	@RequestMapping(value = "/statistic", method = RequestMethod.GET)
	public String statistic(Model model, @Valid EtatConge cg) {

		Collection<Conge> lp1 = congeRepos.findByEtatconge(cg);

		Map<EtatConge, Long> congeCountByEtat = new HashMap<>();
		for (Conge conge : lp1) {
			EtatConge etat = conge.getEtatconge();
			congeCountByEtat.put(etat, congeCountByEtat.getOrDefault(etat, 0L) + 1);
		}
		// Log the data for debugging
		System.out.println("Congés par état: " + congeCountByEtat);
		model.addAttribute("congeCountByEtat", congeCountByEtat);
		return "statistic";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String clearSession(SessionStatus sessionStatus) {
		sessionStatus.setComplete(); // Nettoie les attributs de session
		return "redirect:login"; // Redirige vers la page d'accueil ou une autre page appropriée
	}

	@GetMapping("/employeconge")
	public String employeconge(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null && user instanceof Employe) {
			Employe employe = (Employe) user;
			model.addAttribute("employe", employe);
			return "userList"; // Retourne le nom de la vue (profile.html)
		} else {
			return "redirect:login"; // Redirige vers la page de login si l'utilisateur n'est pas trouvé dans la session
									
		}
	}

	@RequestMapping(value = "/formconge", method = RequestMethod.GET)
	public String formconge(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null && user instanceof Employe) {
			//conge.setDateRupture(conge.getDateFin());
			model.addAttribute("conge", new Conge());
			return "formConge";
		} else {
			return "redirect:welcome"; // Redirige vers la page de login si l'utilisateur n'est pas trouvé dans la session ou n'est pas un employé
										
		}
	}
	
	//méthode pour remplir le formulaire et solliciter un congé 	
	@RequestMapping(value = "/solliciterconge", method = RequestMethod.POST)
	public String solliciterconge(@Valid @ModelAttribute("conge") Conge conge, BindingResult result,
			HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null || !(user instanceof Employe)) {
			return "redirect:login"; // Redirige vers la page de login si l'utilisateur n'est pas trouvé
										// dans la session ou n'est pas un employé
		}

		if (result.hasErrors()) {
			return "formConge"; // Retourne le formulaire avec les erreurs
		}
		//conge.setDateRupture(conge.getDateFin());
		Employe employe = (Employe) user;
		conge.setEmploye(employe);
		congeRepos.save(conge);
		model.addAttribute("employe", employe);
		return "redirect:employeconge"; // Redirige vers le profil de l'employé après avoir sauvegardé le congé
	}

	
	//méthode pour changer l'état du congé 	
	@PostMapping("/cancelConge")
	public String cancelConge(@RequestParam Long id) {
	    Conge conge = congeRepos.findById(id).orElse(null);
	    if (conge != null) {
	        conge.setEtatconge(EtatConge.Annule); // ou EtatConge.CANCELLED si vous avez une autre valeur pour "Annulé"
	        congeRepos.save(conge);
	    }
	    return "redirect:employeconge";
	}


}
