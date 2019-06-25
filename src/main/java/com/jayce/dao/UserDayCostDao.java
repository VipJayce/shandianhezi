package com.jayce.dao;

import org.springframework.data.repository.CrudRepository;

import com.jayce.entity.UserDayCost;

public interface UserDayCostDao extends CrudRepository<UserDayCost, String>{
	
}
