package com.jayce.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jayce.entity.UserDayCostTemp;

public interface UserDayCostTempDao extends CrudRepository<UserDayCostTemp, String>{
	
	@Query("select u from UserDayCostTemp u where u.userName = ?1")
	List<UserDayCostTemp> getUserCostTemps(String userName);

}
