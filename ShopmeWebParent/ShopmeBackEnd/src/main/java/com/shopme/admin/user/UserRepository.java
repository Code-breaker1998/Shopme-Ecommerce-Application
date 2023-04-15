package com.shopme.admin.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {
	@Query("Select u from User u where u.email=:email")
	public User getUserByEmail(@Param("email") String email);
	
	
	@Query("Select u from User u where concat(u.id,' ',u.email,' ',u.firstName,' ',u.lastName) like %?1% ")
	public Page<User> findAll(String keyword,Pageable pageable);
	
	public Long countById(@Param("id") int id);
	
	@Query("Update User u set u.enabled=:status where u.id=:id ")
	@Modifying
	public void updateEnableStatus(@Param("id") Integer id,@Param ("status") boolean status);
}
