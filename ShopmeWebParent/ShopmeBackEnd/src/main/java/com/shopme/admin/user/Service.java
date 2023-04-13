package com.shopme.admin.user;

import org.springframework.data.repository.CrudRepository;
import com.shopme.common.entity.User;

public interface Service extends CrudRepository<User,Integer>{

}
