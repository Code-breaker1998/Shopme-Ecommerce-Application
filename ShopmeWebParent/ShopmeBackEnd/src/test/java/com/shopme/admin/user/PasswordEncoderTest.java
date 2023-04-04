package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		String rawPassword="nam2020";
		String encoderPassword=passwordEncoder.encode(rawPassword);
		System.out.println(encoderPassword);
		
		boolean matches =passwordEncoder.matches(rawPassword,encoderPassword);
		assertThat(matches).isTrue();
	}
}
