package spring.jpa.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import spring.jpa.model.Administrateur;
import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.EtatConge;
import spring.jpa.model.User;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;
import spring.jpa.repository.UserRepository;
import spring.jpa.service.CongeService;

@Controller // pour déclarer un contrôleur
@SessionAttributes("user")
@RequestMapping(value = "/conge") // nom logique dans l'URL pour accéder au contrôleur
public class UserController {

	@Autowired
	private CongeService congeService;

	@Autowired
	private UserRepository userRepos;

	@Autowired
	private CongeRepository congeRepos;

	@Autowired
	private EmployeRepository employeRepos;

	// Afficher le formulaire de connexion
	@GetMapping("/welcome")
	public String welcome(Model model, HttpSession session) {
		if (model.getAttribute("user") == null) {
			return "redirect:login";
		}
		return "welcome";
	}

	// Affiche du dashBoard administrateur
	@GetMapping("/admin")
	public String admin(Model model, HttpSession session,
			// paramètre pour le numero de la page (0 par défaut)
			@RequestParam(name = "page", defaultValue = "0") int p,
			// paramètres pour la date de debut et de fin
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			// paramètres pour l'état
			@RequestParam(name = "state", required = false, defaultValue = "tout") String state,
			// paramètres pour le code de l'employé
			@RequestParam(name = "employeeCode", required = false, defaultValue = "") String employeeCode) {
		if (session.getAttribute("user") == null) {
			return "redirect:auth?redirectUri=" + URLEncoder.encode("/conge/admin", StandardCharsets.UTF_8);
		}

		Page<Conge> conges = null;

		// Conversion des dates de String à Date si elles ne sont pas nulles
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = new Date();
		try {
			start = (startDate != null && !startDate.isEmpty()) ? dateFormat.parse(startDate) : null;
			end = (endDate != null && !endDate.isEmpty()) ? dateFormat.parse(endDate) : null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Recherche employe à partir du code
		Employe emp = new Employe();
		if (!employeeCode.isEmpty()) {
			emp = (Employe) userRepos.findByCode(employeeCode);
		}

		if (state.equals("tout")) {
			state = "";
		}
		// Nombre d'elements par page
		int pMax = 10;

		// Application des filtres en fonction des paramètres fournis

		if ((startDate == null || startDate == "") && (endDate == null || endDate == "") && state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeService.getCongesSortPage(p, pMax, "dateDebut", "asc");
		} else if ((startDate == null || startDate == "") && (endDate == null || endDate == "") && state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByEmployeId(emp.getId(), PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate == null || endDate == "") && !state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByEtatconge(state, PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate == null || endDate == "") && !state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByEtatcongeAndEmployeId(state, emp.getId(), PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate != null || endDate != "") && state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByDateFinBefore(end, PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate != null || endDate != "") && state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByDateFinBeforeAndEmployeId(end, emp.getId(), PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate != null || endDate != "") && !state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByDateFinBeforeAndEtatconge(end, state, PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate != null || endDate != "") && !state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByDateFinBeforeAndEtatcongeAndEmployeId(end, state, emp.getId(),
					PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate == null || endDate == "") && state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByDateDebutAfter(start, PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate == null || endDate == "") && state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByDateDebutAfterAndEmployeId(start, emp.getId(), PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate == null || endDate == "") && !state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByDateDebutAfterAndEtatconge(start, state, PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate == null || endDate == "") && !state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByDateDebutAfterAndEtatcongeAndEmployeId(start, state, emp.getId(),
					PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate != null || endDate != "") && state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByDateDebutBetween(start, end, PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate != null || endDate != "") && state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByDateDebutBetweenAndEmployeId(start, end, emp.getId(), PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate != null || endDate != "") && !state.isEmpty()
				&& employeeCode.isEmpty()) {
			conges = congeRepos.findByDateDebutBetweenAndEtatconge(start, end, state, PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate != null || endDate != "") && !state.isEmpty()
				&& (!employeeCode.isEmpty() && emp != null)) {
			conges = congeRepos.findByDateDebutBetweenAndEtatcongeAndEmployeId(start, end, state, emp.getId(),
					PageRequest.of(p, pMax));
		}

		// nombre total des pages
		int nbrePages = conges.getTotalPages();
		// créer un tableau d'entier qui contient les numéros des pages
		int[] pages = new int[nbrePages];
		for (int i = 0; i < nbrePages; i++) {
			pages[i] = i;
		}
		// placer le tableau dans le "Model"
		model.addAttribute("pages", pages);
		// retourner le numéro de la page courante
		model.addAttribute("pageCourante", p);

		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("state", state);
		model.addAttribute("employeeCode", employeeCode);
		model.addAttribute("employe", emp);
		model.addAttribute("conges", conges);
		System.out.println("Date debut : " + startDate + "\nDate fin: " + endDate + "\nEtat: " + state
				+ "\nCode employe: " + employeeCode);
		return "admin";
	}

	// Affiche tous les utilisateur
	@GetMapping("/allusers")
	public String allUsers(Model model, HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect:login";
		}
		List<User> usersGroupe = userRepos.findAll();
		List<Employe> employes = usersGroupe.stream().filter(user -> user instanceof Employe)
				.map(user -> (Employe) user).collect(Collectors.toList());
		model.addAttribute("users", employes);
		return "allUserList";
	}

	@GetMapping("/userlist")
	public String userList(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect:login";
		}
		return "userList";
	}

	@GetMapping("/auth")
	public String showLoginForm(@RequestParam(value = "redirectUri", required = false) String redirectUri,
			Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("redirectUri", redirectUri);
		// System.out.println("Show LoginForm");
		return "login";
	}

	@PostMapping("/login")
	public String login(@Valid User user, BindingResult bindingResult, Model model, HttpSession session,
			@RequestParam(value = "redirectUri", required = false) String redirectUri) {
		if (bindingResult.hasErrors()) {
			return "login";
		}
		User foundUser = userRepos.findByLoginAndPassword(user.getLogin(), user.getPassword());
		if (foundUser != null) {
			session.setAttribute("user", foundUser);
			if (redirectUri != null && !redirectUri.isEmpty()) {
				return "redirect:" + redirectUri;
			}
			if (foundUser instanceof Employe) {
				Collection<Conge> conges = congeRepos.findByEmploye((Employe) foundUser);
				model.addAttribute("id", foundUser.getId());
				model.addAttribute("conges", conges);
				model.addAttribute("user", foundUser);
				return "redirect:welcome";
			} else if (foundUser instanceof Administrateur) {
				model.addAttribute("user", foundUser);
				return "redirect:admin";
			}
		} else {
			model.addAttribute("error", true);
			return "login";
		}
		return "login";
	}

	@GetMapping("/congelist")
	public String empCongeList(@Valid Employe emp, Model model, HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect:auth";
		}
		List<Conge> conges = congeRepos.findByEmploye(emp);
		model.addAttribute("conges", conges);
		return "userList";
	}

//    @GetMapping("/statistic")
//    public String statistic(@Valid EtatConge etatConge, Model model, HttpSession session) {
//        if (session.getAttribute("user") == null) {
//            return "redirect:auth";
//        }
//        System.out.println(etatConge);
//        Collection<Conge> conges = congeRepos.findByEtatconge(etatConge.toString());
//        Map<String, Long> congeCountByEtat = new HashMap<>();
//        for (Conge conge : conges) {
//            String etat = conge.getEtatconge();
//            congeCountByEtat.put(etat, congeCountByEtat.getOrDefault(etat, 0L) + 1);
//        }
//        model.addAttribute("congeCountByEtat", congeCountByEtat);
//        return "historique";
//    }

	@GetMapping("/employeId/{employeId}")
	public String getCongesByEmploye(@PathVariable("employeId") Long employeId, Model model, HttpSession session,
			// paramètre pour le numero de la page (0 par défaut)
			@RequestParam(name = "page", defaultValue = "0") int p,
			// paramètres pour la date de debut et de fin
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate,
			// paramètres pour l'état
			@RequestParam(name = "state", required = false, defaultValue = "tout") String state) {
		if (session.getAttribute("user") == null) {
			return "redirect:/conge/auth?redirectUri="
					+ URLEncoder.encode("/conge/employeId/{employeId}", StandardCharsets.UTF_8);
		}
		Employe employe = employeRepos.findById(employeId).orElse(null);
		if (employe == null) {
			model.addAttribute("error", "Employé non trouvé");
			return "redirect:auth"; // Assurez-vous d'avoir une page d'erreur appropriée
		}
		// Liste de conge vide
		Page<Conge> conges = null;

		// Conversion des dates de String à Date si elles ne sont pas nulles
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = new Date();
		try {
			start = (startDate != null && !startDate.isEmpty()) ? dateFormat.parse(startDate) : null;
			end = (endDate != null && !endDate.isEmpty()) ? dateFormat.parse(endDate) : null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (state.equals("tout")) {
			state = "";
		}
		int pMax = 10;
		// Application des filtres en fonction des paramètres fournis
		if ((startDate == null || startDate == "") && (endDate == null || endDate == "") && state.isEmpty()) {
			conges = congeRepos.findByEmployeId(employeId, PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate == null || endDate == "") && !state.isEmpty()) {
			conges = congeRepos.findByEtatcongeAndEmployeId(state, employeId, PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate != null || endDate != "") && state.isEmpty()) {
			conges = congeRepos.findByDateFinBeforeAndEmployeId(end, employeId, PageRequest.of(p, pMax));
		} else if ((startDate == null || startDate == "") && (endDate != null || endDate != "") && !state.isEmpty()) {
			conges = congeRepos.findByDateFinBeforeAndEtatcongeAndEmployeId(end, state, employeId,
					PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate == null || endDate == "") && state.isEmpty()) {
			conges = congeRepos.findByDateDebutAfterAndEmployeId(start, employeId, PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate == null || endDate == "") && !state.isEmpty()) {
			conges = congeRepos.findByDateDebutAfterAndEtatcongeAndEmployeId(start, state, employeId,
					PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate != null || endDate != "") && state.isEmpty()) {
			conges = congeRepos.findByDateDebutBetweenAndEmployeId(start, end, employeId, PageRequest.of(p, pMax));
		} else if ((startDate != null || startDate != "") && (endDate != null || endDate != "") && !state.isEmpty()) {
			conges = congeRepos.findByDateDebutBetweenAndEtatcongeAndEmployeId(start, end, state, employeId,
					PageRequest.of(p, pMax));
		}

		// nombre total des pages
		int nbrePages = conges.getTotalPages();
		// créer un tableau d'entier qui contient les numéros des pages
		int[] pages = new int[nbrePages];
		for (int i = 0; i < nbrePages; i++) {
			pages[i] = i;
		}
		// placer le tableau dans le "Model"
		model.addAttribute("pages", pages);
		// retourner le numéro de la page courante
		model.addAttribute("pageCourante", p);

		// List<Conge> conges = congeRepos.findByEmploye(employe);
		model.addAttribute("conges", conges);
		model.addAttribute("employe", employe);
		model.addAttribute("id", employeId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("state", state);

		return "userList";
	}

	@GetMapping("/formconge")
	public String formConge(Model model, HttpSession session) {
		// Vérifiez si l'utilisateur est connecté
		if (session.getAttribute("user") == null) {
			return "redirect:auth";
		}

		// Récupérer l'ID de l'utilisateur à partir de la session
		User user = (User) session.getAttribute("user");

		// Ajouter l'ID de l'utilisateur au modèle
		// model.addAttribute("userId", user.getId());
		session.setAttribute("userId", user.getId());
		System.out.println("Erreur affiche du forùulaire : " + user.getId());

		// Ajouter un nouvel objet Conge au modèle
		Conge conge = new Conge();
		model.addAttribute("conge", conge);
		System.out.println("Erreur affiche du forùulaire ( conge ): " + conge.getId());

		// Retourner le nom de la vue
		return "formConge";
	}

	@PostMapping("/save")
	public String saveConge(@ModelAttribute("conge") Conge conge, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("employeId") Long employeId, HttpSession session) {
		// Vérifiez si l'utilisateur est connecté
		if (session.getAttribute("user") == null) {
			return "redirect:auth";
		}

		// Assurez-vous que le congé soumis n'est pas nul
		if (conge != null) {
			// Convertir les dates de type String en objet Date
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date parsedStartDate = formatter.parse(startDate);
				Date parsedEndDate = formatter.parse(endDate);
				conge.setDateDebut(parsedStartDate);
				conge.setDateFin(parsedEndDate);
			} catch (ParseException e) {
				// Gérer les erreurs de formatage des dates
				e.printStackTrace();
			}

			// Récupérer l'employé à partir de l'ID
			Optional<Employe> employeOptional = employeRepos.findById(employeId);
			if (employeOptional.isPresent()) {
				Employe employe = employeOptional.get();
				conge.setEmploye(employe);
			} else {
				// Si l'employé n'est pas trouvé, afficher un message d'erreur et rediriger vers
				// une autre page
				return "redirect:formconge"; // Ou toute autre page appropriée pour afficher le formulaire de nouveau
			}

			// Enregistrer le congé
			congeRepos.save(conge);

			// Redirection vers une autre page après la sauvegarde
			return "redirect:welcome";
		} else {
			// Si le congé soumis est nul, affichez un message d'erreur et redirigez
			// l'utilisateur vers une autre page
			return "redirect:formconge"; // Ou toute autre page appropriée pour afficher le formulaire de nouveau
		}
	}

	// Faire unde demande de congé
	@SuppressWarnings("unused")
	@PostMapping("/solliciterconge")
	public String solliciter(@Valid Conge conge, BindingResult bindingResult, Model model, HttpSession session) {
		if (bindingResult.hasErrors()) {
			return "formConge";
		}
		Long userId = (Long) session.getAttribute("userId"); // Get user ID from session
		if (userId == null) {
			return "redirect:auth";
		}
		Optional<Employe> emp = employeRepos.findById(userId);
		if (!emp.isPresent()) {
			model.addAttribute("error", true);
			model.addAttribute("errMsg", "Employé non trouvé");
			return "formConge";
		}
		if (conge.getDescription() == null || conge.getDateDebut() == null || conge.getDateFin() == null) {
			model.addAttribute("error", true);
			model.addAttribute("errMsg", "Remplissez tous les champs");
			System.out.println("les champs sont vides");
			return "formConge";
		}
		// Comparaison de l'intervalle conge et du nombre de jour de conge restant à
		// l'employe
		LocalDate dbConge = conge.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dfConge = conge.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int nb = emp.get().getNbreJoursConge();
		long daysOfLeave = ChronoUnit.DAYS.between(dbConge, dfConge);

		// Verifier si l'employe a travaillé au moins 1 an
		Date dateEmbauche = new Date(emp.get().getDateEmbauche().getTime());
		LocalDate dateEmbauchement = dateEmbauche.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = LocalDate.now();
		// Calcule de la difference de jour
		long daysBetween = ChronoUnit.DAYS.between(dateEmbauchement, currentDate);
		if (daysBetween < 365) {
			model.addAttribute("errMsg", "Vous n'avez pas le droit de demander un congé");
			return "formConge";
		}

		if (daysOfLeave > nb || nb == 0) {
			System.out.println("Echec de l'envoi de la demande pour l'employe " + emp.get().getNom());
			model.addAttribute("error", true);
			model.addAttribute("errMsg", "Nombre de jours de conge maximal atteint");
			System.out.println(
					"Vous allez depasser le nombre de jours de conge maximal \n Veillez entrer des dates valides");
			return "formConge";
		}
		if (conge.getDateDebut().before(new Date()) || conge.getDateFin().before(new Date())
				|| conge.getDateFin().before(conge.getDateDebut())) {
			System.out.println("Echec de l'envoi de la demande pour l'employe " + emp.get().getNom());
			model.addAttribute("error", true);
			System.out.println("Entrez des dates valides");
			model.addAttribute("errMsg", "Entrez des dates valides");
			return "formConge";
		}
		conge.setEmploye(emp.get());
		congeRepos.save(conge);
		return "redirect:/conge/employeId/" + emp.get().getId();
	}

	// Annuler un demande de conge
	@GetMapping("cancelConge/{id}")
	public String annulerConge(@PathVariable("id") Long congeId, Model model, HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect:/conge/auth";
		}
		// Récupérer l'utilisateur connecté depuis la session
		User user = (User) session.getAttribute("user");

		Conge conge = congeRepos.findById(congeId).orElse(null);
		if (conge == null) {
			model.addAttribute("error", "Congé non trouvé");
			System.out.println("Congé non trouvé");
			return "errorPage"; // Assurez-vous d'avoir une page d'erreur appropriée
		}
		if (conge.getEtatconge().equals(EtatConge.Sollicite.toString())) {
			System.out.println("Congé annulé");
			conge.setEtatconge(EtatConge.Annule.toString());
			congeRepos.save(conge);
		}
		// Rediriger vers la page d'historique des congés de l'utilisateur connecté
		return "redirect:/conge/employeId/" + user.getId();
	}

	// Deconnexion de l'utilisateur
	@GetMapping("/logout")
	public String logout(SessionStatus sessionStatus, HttpSession session) {
		System.out.println("Deconnexion: " + session.getAttribute("user").toString());
		sessionStatus.setComplete();
		session.invalidate(); // Invalidate the session to remove all attributes
		return "redirect:auth"; // Redirige vers la page de connexion
	}
}