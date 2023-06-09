package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class ServiceTest {
	@Autowired
	private Service servo;
	
	
	@Test
	public void testEncode() {
		User userRavi=new User("mangeshBard@gmail.com","ravi2020","Ravi","Kumar");
		User username=servo.save(userRavi);
		assertThat(username.getId()).isGreaterThan(0);
	}
}
