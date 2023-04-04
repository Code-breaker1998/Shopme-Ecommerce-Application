package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){
		return (List<User>)userrepo.findAll();
	}
	
	public List<Role> listRoles(){
		return (List<Role>)roleRepo.findAll();
	}

	public void usersave(User user) {
		encodePassword(user);
		userrepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(String user) {
		User username=userrepo.getUserByEmail(user);
		return username==null;
	}
}
