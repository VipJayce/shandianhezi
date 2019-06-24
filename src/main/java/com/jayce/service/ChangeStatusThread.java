package com.jayce.service;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayce.entity.User;
import com.jayce.service.ad.CaptureService;

public class ChangeStatusThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(ChangeStatusThread.class);
	
	private Map<String, Object> map;
	private String status;
	private CountDownLatch latch;
	private CaptureService captureService;

	public ChangeStatusThread(CaptureService captureService,CountDownLatch latch, Map<String, Object> map, String status) {
		this.map = map;
		this.status = status;
		this.latch = latch;
		this.captureService = captureService;
	}

	@Override
	public void run() {
		try {
			User user = (User) map.get("user");
			String adnum = String.valueOf(map.get("adNum"));
			logger.info(user.getUserName() + ":" + status + "广告" + adnum);
//			System.out.println(captureService.changeAdStatus(user.getCookie(),user.getCsrfToken(), adnum, status));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}

}
