package com.jayce.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.jayce.service.user.UserService;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@Service
@EnableScheduling   // 2.开启定时任务
public class LoginTask {
	
	@Autowired
	private UserService uservice;
	
    //1.刷新用户登录cookies，每天刷一次
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void configureTasks() throws Exception {
    	uservice.refreshUsers();
    }
    
}