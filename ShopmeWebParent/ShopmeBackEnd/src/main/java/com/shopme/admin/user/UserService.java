package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
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

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userrepo.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
			
		} else {		
			encodePassword(user);
		}
		
		 return userrepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id,String user) {
		User username=userrepo.getUserByEmail(user);
		
		if(username==null) return true;
		
		boolean isCreatingNew =(id==null);
		
		if(isCreatingNew) {
			if(username != null) return false;
		}else {
			if(username.getId() != id) {
				return false;
			}
		}
		
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
		User username=userrepo.findById(id).get();
		return username;
		}catch(Exception ex) {
			throw new UserNotFoundException("Could not find any user with ID"+id);
		}
	}
	
	public void delete(int id) throws UserNotFoundException {
		try {
			Long Userdelete=userrepo.countById(id);
			userrepo.deleteById(id);
			
		}
		catch(Exception ex) {
			throw new UserNotFoundException("Could not find any user with ID:" + id);
		}
	}
	
	public void generateStatus(int id, boolean status) {
		 userrepo.updateEnableStatus(id, status);
		
	}
}
