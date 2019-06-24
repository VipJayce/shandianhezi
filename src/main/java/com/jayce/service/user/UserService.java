package com.jayce.service.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jayce.comm.util.EmptyUtil;
import com.jayce.comm.util.FileUtil;
import com.jayce.dao.UserDao;
import com.jayce.entity.User;
import com.jayce.service.ad.CaptureService;
import com.jayce.util.ReadFileUtil;

@Service
@Transactional
public class UserService {

	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private CaptureService captureService;

	@Autowired
	private UserDao userDao;

	/**
	 * 客户新增
	 * 
	 * @param userMap
	 */
	public void addUsers(List<String> userList) {
		List<User> list = new ArrayList<User>();
		for (String userStr : userList) {
			String[] ss = userStr.split(";");
			Map<String,String> cookieMap = this.captureService.simulateLogin(ss[0], ss[1]);
			if (!EmptyUtil.isEmpty(cookieMap)) {
				User user = combinUser(ss[0], ss[1], cookieMap);
				list.add(user);
			}
		}
		if(!EmptyUtil.isEmpty(list)) {
			this.userDao.saveAll(list);
		}
	}

	/**
	 * 首次运行初始化用户信息
	 */
	public void initUsers() {
		long count = userDao.count();
		if (count == 0) {// 无数据需做初始化
			List<String> userList = ReadFileUtil.readFileByLines(FileUtil.getFileName("classpath://user.txt"));
		      addUsers(userList);
		} else {
			logger.info("无需初始化");
		}
	}

	/**
	 * 刷新用户登录状态
	 */
	public void refreshUsers() {
		List<String> userList = ReadFileUtil.readFileByLines(FileUtil.getFileName("classpath://user.txt"));
		if(!EmptyUtil.isEmpty(userList)) {
			this.userDao.deleteAll();
			addUsers(userList);
		}
	}

	/**
	 * 用户对象拼装
	 * 
	 * @param userName 用户名
	 * @param pwd      密码
	 * @return
	 */
	public User combinUser(String userName, String pwd, Map<String, String> cookieMap) {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setUserName(userName);
		user.setPassword(pwd);
		user.setCookie(cookieMap.get("Cookie"));
		user.setCsrfToken(cookieMap.get("X-CSRFToken"));
		user.setTimestamp(new Date().getTime());
		return user;
	}

	/**
	 * 获取所有登录员工
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		List<User> list = new ArrayList<User>();
		Iterable<User> userIterable = userDao.findAll();
		Iterator<User> users = userIterable.iterator();
		while (users.hasNext()) {
			list.add(users.next());
		}
		return list;
	}
	
	/**
	 * 根据用户id获取对应用户列表
	 * 
	 * @return
	 */
	public List<User> getUsersByIds(String[] arr) {
		List<String> userIds = Arrays.asList(arr);
		List<User> list = new ArrayList<User>();
		Iterable<User> userIterable = userDao.findAllById(userIds);
		Iterator<User> users = userIterable.iterator();
		while (users.hasNext()) {
			list.add(users.next());
		}
		return list;
	}

}
