package com.jayce.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.http.client.utils.DateUtils;

import com.jayce.entity.User;
import com.jayce.service.ad.CaptureService;

public class QueryUserMsgThread implements Runnable {
	private List<Map<String,Object>> allList;
	private User user;
	private CountDownLatch latch;
	private CaptureService captureService;

	public QueryUserMsgThread(CaptureService captureService,CountDownLatch latch,List<Map<String,Object>> allList,User user) {
		this.allList = allList;
		this.user = user;
		this.latch = latch;
		this.captureService = captureService;
	}

	@Override
	public void run() {
		try {
			String cookie = user.getCookie();
			String nowDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
			BigDecimal totalCost = captureService.getRecharge(cookie, nowDate, nowDate);// 当前消耗金额
			BigDecimal reFee = captureService.queryCurrentCost(cookie);// 账户余额
			String status = captureService.getStatusByUserKey(cookie,user.getCsrfToken());// 获取每个账户的所有广告列表，若全部投放，则显示投放；若全部暂停，则显示全部暂停；否则显示未全部投放
			Map<String, Object> tmp = new HashMap<String, Object>();
			tmp.put("id", user.getId());
			tmp.put("userName", user.getUserName());
			tmp.put("totalCost", totalCost);
			tmp.put("reFee", reFee);
			tmp.put("status", status);
			tmp.put("timestamp", user.getTimestamp());
			synchronized (allList) {
				allList.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}

}
