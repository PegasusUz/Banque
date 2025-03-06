package org.sid.metier;

import java.util.Date;
import java.util.Optional;

//import javax.management.RuntimeOperationsException;

import org.sid.dao.CompteRepository;
import org.sid.dao.OperationRepository;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class BanqueMetierImplementation implements EBanqueMetier{
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	
	@Override
	public Compte consulterCompte(String codeCpte) {
		Optional<Compte> CP=compteRepository.findById(codeCpte);
		if (!CP.isPresent()) throw new RuntimeException("Compte introuvable");
		return CP.get();
	}

	@Override
	public void verser(String codeCpte, double montant) {
		Compte CP=consulterCompte(codeCpte);
		Versement v=new Versement(new Date(), montant, CP);
		operationRepository.save(v);
		CP.setSolde(CP.getSolde()+montant);
		compteRepository.save(CP);
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		Compte CP=consulterCompte(codeCpte);
		
		double faciliteCaisse=0;
		if (CP instanceof CompteCourant)
			faciliteCaisse=((CompteCourant) CP).getDecouvert();
		if (CP.getSolde()+faciliteCaisse<montant)
			throw new RuntimeException("Solde insuffisant");
		Retrait r=new Retrait(new Date(), montant, CP);
		operationRepository.save(r);
		CP.setSolde(CP.getSolde()-montant);
		compteRepository.save(CP);
	}

	@Override
	public void virement(String codeCpte1, String codeCpte2, double montant) {
		retirer(codeCpte1, montant);
		verser(codeCpte2, montant);
	}

	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {
		
		//return operationRepository.listOperation(codeCpte, new PageRequest(page, size));
		return operationRepository.listOperation(codeCpte, PageRequest.of(page, size));
	}
	

}
