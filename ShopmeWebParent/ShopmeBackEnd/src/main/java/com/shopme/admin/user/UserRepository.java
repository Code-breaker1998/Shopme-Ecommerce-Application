package com.shopme.admin.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends CrudRepository<User,Integer> {
	@Query("Select u from User u where u.email=:email")
	public User getUserByEmail(@Param("email") String email);
	

	public Long countById(@Param("id") int id);
	
	@Query("Update User u set u.enabled=:status where u.id=:id ")
	@Modifying
	public void updateEnableStatus(@Param("id") Integer id,@Param ("status") boolean status);
}
