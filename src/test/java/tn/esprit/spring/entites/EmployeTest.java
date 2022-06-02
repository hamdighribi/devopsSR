package tn.esprit.spring.entites;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.services.IEmployeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeTest {

	@Autowired
	IEmployeService employeService;
	@Autowired
	EmployeRepository employeRepository;

	@Test
	public void testaddOrUpdateEmploye() {
		Employe emp = new Employe(1, "X", "X", "X", "X", true, null);
		int id = employeService.addOrUpdateEmploye(emp);
		Assert.assertEquals(1, id);
	}

	@Test
	public void testgetAllEmployes() {
		List<Employe> list = employeService.getAllEmployes();
		assertThat(list).size().isGreaterThan(0);
	}

	@Test
	public void testdeleteEmployeById() {
		employeService.deleteEmployeById(1);
		assertThat(employeRepository.existsById(1)).isFalse();
	}

	@Test
	public void testEmployeEntity() {
		// test de la methode getId
		Employe e = new Employe();
		e.setId(1);
		assertEquals(1, e.getId());
		// test de la methode getNom
		e.setNom("Ghannouchi");
		assertEquals("Ghannouchi", e.getNom());
		// test de la methode getPrenom
		e.setPrenom("Mustapha");
		assertEquals("Mustapha", e.getPrenom());
		// test de la methode getEmail
		e.setEmail("test@test.tn");
		assertEquals("test@test.tn", e.getEmail());
		// test de la methode getPassword
		e.setPassword("password");
		assertEquals("password", e.getPassword());
		// test de la methode getRole
		e.setRole(Role.ADMINISTRATEUR);
		assertEquals(Role.ADMINISTRATEUR, e.getRole());

	}

}