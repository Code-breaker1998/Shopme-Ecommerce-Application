package com.shopme.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController
 {

	@Autowired
	private UserService service;
	
	@PostMapping("/users/check_email")
	public String checkduplicateEmail(@RequestParam String email ) {
		return service.isEmailUnique(email)? "OK":"Duplicate";
	}
}