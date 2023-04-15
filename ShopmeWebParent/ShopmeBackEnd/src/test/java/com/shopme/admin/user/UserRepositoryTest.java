package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=Replace.NONE)

@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin=entityManager.find(Role.class, 1);
		User userdurg=new User("Durgesh@codejava.net","Durgesh@98","Durgesh","Kadwe");
		userdurg.addRole(roleAdmin);
		
		User savedUser=repo.save(userdurg);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRole() {
		User userRavi=new User("ravi@gmail.com","ravi2020","Ravi","Kumar");
		Role roleEditor=new Role(3);
		Role roleAssitant=new Role(5);
		
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssitant);
		
		User savedUser=repo.save(userRavi);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers()
      { Iterable<User> listUsers=repo.findAll();
         listUsers.forEach(user -> System.out.println(user));
	  }
	
	@Test
	public void testGetUserById() {
		User username=repo.findById(1).get();
		System.out.println(username);
		assertThat(username).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User username=repo.findById(1).get();
		username.setEnabled(true);
		repo.save(username);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User username=repo.findById(2).get();
		Role roleEditor=new Role(3);
		Role roleSalesperson=new Role(5);
		
		username.getRoles().remove(roleEditor);
		username.addRole(roleSalesperson);
		repo.save(username);
	}
	@Test
	public void testEmail() {
		User username=repo.getUserByEmail("ravi@gmail.com");
		assertThat(username).isNotNull();
	}
	@Test
	public void testStatus() {
		repo.updateEnableStatus(2, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber=1;
		int pageSize=4;
		
		Pageable pageable=PageRequest.of(pageNumber, pageSize);
		Page<User> page=repo.findAll(pageable);
		
		List<User>listUsers=page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
}
