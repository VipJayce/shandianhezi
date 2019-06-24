package com.jayce.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jayce.entity.User;

public interface UserDao extends CrudRepository<User, String>{
	
	/**
	 * 更新用户cookie信息
	 * @param firstName
	 * @param idCard
	 * @param userId
	 * @return
	 */
	@Modifying
	@Query("update User u set u.cookie = ?1,u.csrfToken = ?2 where u.id = ?3")
	int updateUserCookie(String cookie, String csrfToken,String userId);
	
}
