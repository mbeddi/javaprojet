package spring.jpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import spring.jpa.model.Administrateur;
import spring.jpa.model.Conge;
import spring.jpa.model.Employe;
import spring.jpa.model.EtatConge;
import spring.jpa.model.User;
import spring.jpa.repository.AdminRepository;
import spring.jpa.repository.CongeRepository;
import spring.jpa.repository.EmployeRepository;
import spring.jpa.repository.UserRepository;

@SpringBootApplication
public class SpringBootMiniProjetApplication {

	private static final EtatConge Refuse = null;
	static AdminRepository adminRepos ;
	// Récupérer une implémentation de l'interface "CategorieRepository" par injection de dépendance
	static EmployeRepository employeRepos;
	// Récupérer une implémentation de l'interface "StockRepository" par injection de dépendance
	static CongeRepository congeRepos;
	//Déclaration d'un repository pour gérer les responsables
	static UserRepository userRepos;
	
	public static void main(String[] args) {
	
		// référencer le contexte 
		ApplicationContext contexte = SpringApplication.run(SpringBootMiniProjetApplication.class, args);
		
	// Récupérer une implémentation de l'interface "ProduitRepository" par injection de dépendance
		userRepos =contexte.getBean(UserRepository.class);

	// Récupérer une implémentation de l'interface "CategorieRepository" par injection de dépendance
		congeRepos =contexte.getBean(CongeRepository.class);
	
	// Récupérer une implémentation de l'interface "StockRepository" par injection de dépendance
		employeRepos =contexte.getBean(EmployeRepository.class);
	
	// Récupérer une implémentation de l'interface "ResponsableRepository" par injection de dépendance
		adminRepos = contexte.getBean(AdminRepository.class);
	
		Administrateur a1 = new Administrateur ("germain", "Ndo","NGR", "ngr", "123456", "admin");
		adminRepos.save(a1);

	Employe e1 = new Employe ("Ben Saleh", "Ali","BSA", "ali", "123456");
	employeRepos.save(e1);
	
	Employe e2 = new Employe ("Mohamed", "Abi","MA", "abi", "123456");
	employeRepos.save(e2);
	
	Employe e3 = new Employe ("John", "lee","MA", "lee", "123456");
	employeRepos.save(e3);
	
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date1 = null;
	java.util.Date date2 = null;
	java.util.Date date3 = null;
	try {
		date1 = sdf.parse("2022-04-15");
		date2 = sdf.parse("2022-04-20");
		date3 = sdf.parse("2022-05-15");
	} catch (ParseException e) {
	// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// créer deux users;
	Conge c1 = new Conge("maladie", date1,date2,e1);
	Conge c2 = new Conge("maladie", date2,date3,e1);
	Conge c3 = new Conge("maladie", date1,date3,e2);
	Conge c4 = new Conge("maternite", date1,date3,EtatConge.Refuse,e3);
	Conge c5 = new Conge("vacances", date1,date3,EtatConge.Fini,e3);
	//Attacher les deux catégories à la BD (insertion)
	congeRepos.save(c1);
	congeRepos.save(c2);
	congeRepos.save(c3);
	congeRepos.save(c4);
	//Synchroniser avec la BD
	//congeRepos.saveAndFlush(c1);
	//congeRepos.saveAndFlush(c2);
	
	
	
	
	//f) Afficher la liste des employés)
		System.out.println("***********************************************");
		System.out.println("Afficher tous les employés");
		Collection<Employe> lr = employeRepos.findAll();
		for (Employe p : lr)
		{
			
			System.out.print(p);
			System.out.print("\n");
		}
		System.out.println("***************************************");
		
		
		System.out.println("***************************************");
		

		
		 //Afficher la liste des employés)
		System.out.println("***********************************************");
		System.out.println("Afficher tous les employés");
		Collection<Employe> lr1 = employeRepos.findByLoginAndPassword("ali", "123456");
		for (Employe p : lr1)
		{
			
			System.out.print(p);
			System.out.print("\n");
		}
		System.out.println("***************************************");
		
		
		System.out.println("***************************************");

				//Afficher la liste des users)
				System.out.println("***********************************************");
				System.out.println("Afficher tous les users");
				User us = userRepos.findByLoginAndPassword("ali", "123456");
				
					System.out.print(us);
					System.out.print("\n");
				
				System.out.println("***************************************");
				
				
				System.out.println("***************************************");
		
		//Afficher la liste des users)
		System.out.println("***********************************************");
		System.out.println("Afficher tous les users");
		Collection<User> lu = userRepos.findAll();
		for (User p : lu)
		{
			
			System.out.print(p);
			System.out.print("\n");
		}
		System.out.println("***************************************");
		
		
		System.out.println("***************************************");
		
				//Afficher la liste des congés)
				System.out.println("***********************************************");
				System.out.println("Afficher tous les congés");
				Collection<Conge> lc = congeRepos.findAll();
				for (Conge p : lc)
				{
					
					System.out.print(p);
					System.out.print("\n");
				}
				System.out.println("***************************************");
				
				
				System.out.println("***************************************");
				
				//Afficher la liste des congés)
				System.out.println("***********************************************");
				System.out.println("Afficher tous les congés par employé");
				Collection<Conge> lc1 = congeRepos.findByEmploye(e1);
				for (Conge p : lc1)
				{
					
					System.out.print(p);
					System.out.print("\n");
				}
				System.out.println("***************************************");
				
				
				System.out.println("***************************************");
				
				//Afficher la liste des congés coté admin)
				System.out.println("***********************************************");
				System.out.println("Afficher tous les congés par Etat");
			
				Collection<Conge> lc2 = congeRepos.findByEtatconge(EtatConge.Fini);
				int count = 0;
				for (Conge p : lc2)
				{
					 count++;
					System.out.print(p);
					System.out.print("\n");
				}
				System.out.println("***************************************");
				
				System.out.println("Number of holidays in the list: " + count);
				System.out.println("***************************************");
	}
	
	
	
}
