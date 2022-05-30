package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {
	private static final Logger logger= Logger.getLogger(EmployeServiceImpl.class);
	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	public Employe authenticate(String login, String password) {
		return employeRepository.getEmployeByEmailAndPassword(login, password);
	}

	@Override
	public int addOrUpdateEmploye(Employe employe) {
		employeRepository.save(employe);
		return employe.getId();
	}


	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		Optional<Employe> employe = employeRepository.findById(employeId);
		Employe emp = new Employe();
		if (employe.isPresent()) {
			emp = employe.get();
		}
		emp.setEmail(email);
		employeRepository.save(emp);
		logger.info("user modified with success");


	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		Optional<Departement> deppManagedEntity = deptRepoistory.findById(depId);
		Optional<Employe> employeManageddEntity = employeRepository.findById(employeId);
		Departement depManagedEntity = new Departement();
		Employe employeManagedEntity = new Employe();
		if (deppManagedEntity.isPresent()){
			depManagedEntity = deppManagedEntity.get();
		}
		if (employeManageddEntity.isPresent()){
			employeManagedEntity = employeManageddEntity.get();
		}
		if(depManagedEntity.getEmployes() == null){

			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);
		}else{

			depManagedEntity.getEmployes().add(employeManagedEntity);
		}

		// à ajouter? 
		deptRepoistory.save(depManagedEntity); 

	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		Optional<Departement> departement = deptRepoistory.findById(depId);
		Departement dep = new Departement();
		if (departement.isPresent()) {
			dep = departement.get();
		}
		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			if(dep.getEmployes().get(index).getId() == employeId){
				dep.getEmployes().remove(index);
				break;//a revoir
			}
		}
	} 
	
	// Tablesapce (espace disque) 

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		logger.info("contrat added with success");
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		Optional<Contrat> contratManageddEntity = contratRepoistory.findById(contratId);
		Optional<Employe> employeManageddEntity = employeRepository.findById(employeId);
		Contrat contratManagedEntity= new Contrat();
		Employe employeManagedEntity = new Employe();
		if (contratManageddEntity.isPresent()) {
			contratManagedEntity = contratManageddEntity.get();
		}
		if (employeManageddEntity.isPresent()) {
			employeManagedEntity = employeManageddEntity.get();
		}
		contratManagedEntity.setEmploye(employeManagedEntity);
		contratRepoistory.save(contratManagedEntity);

	}

	public String getEmployePrenomById(int employeId) {
		Optional<Employe> employeManageddEntity = employeRepository.findById(employeId);
		Employe employeManagedEntity = new Employe();
		if (employeManageddEntity.isPresent()) {
			employeManagedEntity = employeManageddEntity.get();
		}
		return employeManagedEntity.getPrenom();
	}
	 
	public void deleteEmployeById(int employeId)
	{
		Optional<Employe> emp = employeRepository.findById(employeId);
		Employe employe = new Employe();
		if (emp.isPresent()) {
			employe = emp.get();
		}
		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
		}

		employeRepository.delete(employe);
		logger.warn("user deleted");

	}

	public void deleteContratById(int contratId) {
		Optional<Contrat> contratManageddEntity = contratRepoistory.findById(contratId);
		Contrat contratManagedEntity = new Contrat();
		if (contratManageddEntity.isPresent()) {
			contratManagedEntity = contratManageddEntity.get();
		}
		contratRepoistory.delete(contratManagedEntity);

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
	}

}
