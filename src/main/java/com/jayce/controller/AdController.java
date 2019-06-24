package com.jayce.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jayce.comm.constants.AdStatusEnum;
import com.jayce.comm.util.EmptyUtil;
import com.jayce.comm.util.PageHelper;
import com.jayce.entity.User;
import com.jayce.service.ChangeStatusThread;
import com.jayce.service.QueryUserMsgThread;
import com.jayce.service.ad.CaptureService;
import com.jayce.service.user.UserService;

@Controller
public class AdController {

	private Logger logger = LoggerFactory.getLogger(AdController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private CaptureService captureService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "show.html";
	}

	@ResponseBody
	@RequestMapping(value = "/queryUserMsg", method = RequestMethod.POST)
	public String queryUserMsg(Integer page, Integer rows) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 1、获取所有用户信息
		List<User> users = userService.getAllUsers();
		CountDownLatch latch = new CountDownLatch(users.size());
		ExecutorService taskExecutor = Executors.newFixedThreadPool(20);
		for (User user : users) {
			taskExecutor.submit(new QueryUserMsgThread(captureService, latch, result, user));
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("用户信息获取线程异常", e);
		}
		Collections.sort(result, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Long map1value = (Long) o1.get("timestamp");
				Long map2value = (Long) o2.get("timestamp");
				if (map1value.longValue() > map2value.longValue()) {
					return 1;
				}
				if (map1value.longValue() < map2value.longValue()) {
					return -1;
				}
				return 0;
			}
		});
		logger.info("用户列表刷新");
		PageHelper<Map<String, Object>> pager = new PageHelper<Map<String, Object>>(page, rows, result);
		return JSONObject.toJSONString(pager);
	}

	@ResponseBody
	@RequestMapping(value = "/setStatus", method = RequestMethod.POST)
	public Map<String,Object> setStatus(String status,String userIds) {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("status", "false");
		result.put("msg", "设定失败!");
		String queryStatus = "";
		String statusName = "";// 被操作广告的状态名称
		String operateStatusName = "";// 需设定的状态名称
		if (AdStatusEnum.START.getKey().equals(status)) {// 若要開啓則查詢暫停中的廣告
			queryStatus = AdStatusEnum.PAUSE.getKey();
			operateStatusName = "投放";
			statusName = AdStatusEnum.PAUSE.getValue();
		}
		if (AdStatusEnum.PAUSE.getKey().equals(status)) {// 若要暫停則查詢投放中的廣告
			queryStatus = AdStatusEnum.START.getKey();
			operateStatusName = AdStatusEnum.PAUSE.getValue();
			statusName = AdStatusEnum.START.getValue();
		}
		if (EmptyUtil.isEmpty(queryStatus)) {
			result.put("msg", "操作状态(" + status + ")有误");
			return result;
		}
		List<Map<String, Object>> adNums = new ArrayList<Map<String, Object>>();
		List<User> users = new ArrayList<User>();
		if(EmptyUtil.isEmpty(userIds)) {
			users = userService.getAllUsers();
		}else {
			String[] arr = userIds.split(",");
			users = userService.getUsersByIds(arr);
		}
		for (User user : users) {
			List<JSONObject> list = captureService.queryAdNumbersByStatus(user.getCookie(), user.getCsrfToken(),
					queryStatus);
			if (!EmptyUtil.isEmpty(list)) {
				for (JSONObject obj : list) {
					Map<String, Object> tmp = new HashMap<String, Object>();
					tmp.put("user", user);
					tmp.put("adNum", obj.get("id"));
					adNums.add(tmp);
				}
			}
		}
		if (EmptyUtil.isEmpty(adNums)) {
			result.put("msg", "当前无" + statusName + "广告");
			return result;
		}
		CountDownLatch latch = new CountDownLatch(adNums.size());
		ExecutorService taskExecutor = Executors.newFixedThreadPool(20);
		for (Map<String, Object> map : adNums) {
			taskExecutor.submit(new ChangeStatusThread(captureService, latch, map, queryStatus));
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("广告状态改变线程异常", e);
		}
		logger.info("共" + operateStatusName + "广告" + adNums.size() + "个");
		result.put("status", "true");
		result.put("msg", "共" + operateStatusName + "广告" + adNums.size() + "个");
		return result;
	}

}
