package org.sid;

import java.util.Date;

import org.sid.dao.ClientRepository;
import org.sid.dao.CompteRepository;
import org.sid.dao.OperationRepository;
import org.sid.entities.Client;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.CompteEpargne;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.sid.metier.EBanqueMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class BanqueApplication implements CommandLineRunner{
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CompteRepository compteRepository;
	
	@Autowired
	private OperationRepository operationRepository;
	
	@Autowired
	private EBanqueMetier banqueMetier;
	
	public static void main(String[] args) {
		SpringApplication.run(BanqueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		  Client C1 = clientRepository.save(new Client("Hassan", "hassan@gmail.com"));
		  Client C2 = clientRepository.save(new Client("Rachid", "rachid@gmail.com"));
		  Client C3 = clientRepository.save(new Client("Sylla", "sylla@gmail.com"));
		  
		  Compte cp1 = compteRepository.save(new CompteCourant("C1", new Date(), 900, C1, 6000));
		  Compte cp2 = compteRepository.save(new CompteCourant("C2", new Date(), 2000, C2, 45000));
		  Compte cp3 = compteRepository.save(new CompteEpargne("C3", new Date(), 2000, C3, 45000));
		  
		 		
		  operationRepository.save(new Versement(new Date(), 9000, cp1));
		  operationRepository.save(new Versement(new Date(), 6000, cp1));
		  operationRepository.save(new Versement(new Date(), 2300, cp1));
		  operationRepository.save(new Retrait(new Date(), 9000, cp1));
		  
		  operationRepository.save(new Versement(new Date(), 9000, cp2));
		  operationRepository.save(new Versement(new Date(), 6000, cp2));
		  operationRepository.save(new Versement(new Date(), 2300, cp2));
		  operationRepository.save(new Retrait(new Date(), 9000, cp2));
		
			
		  banqueMetier.verser("C1", 1000000);
		 
	}

}
