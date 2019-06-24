package com.jayce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jayce.service.user.UserService;

@Component // 被spring容器管理
@Order(1) // 如果多个自定义ApplicationRunner，用来标明执行顺序
public class MyApplicationRunner implements ApplicationRunner {
	
	@Autowired
	private UserService userService;
	
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		userService.initUsers();
	}
}
