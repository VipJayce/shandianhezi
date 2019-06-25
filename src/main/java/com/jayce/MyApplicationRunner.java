package com.jayce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jayce.comm.util.EmptyUtil;
import com.jayce.dao.UserDayCostTempDao;
import com.jayce.entity.User;
import com.jayce.entity.UserDayCostTemp;
import com.jayce.service.ad.CaptureService;
import com.jayce.service.user.UserService;

@Component // 被spring容器管理
@Order(1) // 如果多个自定义ApplicationRunner，用来标明执行顺序
public class MyApplicationRunner implements ApplicationRunner {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserDayCostTempDao userDayCostTempDao;
	@Autowired
	private CaptureService captureService;
	
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		userService.initUsers();
		List<User> list = userService.getAllUsers();
		for (User user : list) {
			List<UserDayCostTemp> oldList = userDayCostTempDao.getUserCostTemps(user.getUserName());
			if(EmptyUtil.isEmpty(oldList)) {
				List<UserDayCostTemp> nowList = captureService.getUserCostTmps(user.getUserName(), user.getCookie());
				if(!EmptyUtil.isEmpty(nowList)) {
					userDayCostTempDao.saveAll(nowList);
				}
			}
		}
	}
}
