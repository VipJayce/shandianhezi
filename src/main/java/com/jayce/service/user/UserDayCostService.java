package com.jayce.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jayce.comm.util.EmptyUtil;
import com.jayce.dao.UserDayCostDao;
import com.jayce.dao.UserDayCostTempDao;
import com.jayce.entity.User;
import com.jayce.entity.UserDayCost;
import com.jayce.entity.UserDayCostTemp;
import com.jayce.service.ad.CaptureService;

@Service
@Transactional
public class UserDayCostService {

	private Logger logger = LoggerFactory.getLogger(UserDayCostService.class);
	
	@Autowired
	private CaptureService captureService;
	
	@Autowired
	private UserDayCostDao userDayCostDao;
	
	@Autowired
	private UserDayCostTempDao userDayCostTempDao;

	public void saveDiffCost(User user) {
		Map<String,UserDayCostTemp> oldUserMap = new HashMap<String,UserDayCostTemp>();
		List<UserDayCostTemp> oldTmpList = userDayCostTempDao.getUserCostTemps(user.getUserName());
		for (UserDayCostTemp tmp : oldTmpList) {
			oldUserMap.put(tmp.getCampaignId(), tmp);
		}
		List<UserDayCostTemp> nowTmpList = captureService.getUserCostTmps(user.getUserName(), user.getCookie());
		List<UserDayCost> changeUserCostList = new ArrayList<UserDayCost>();
		String campaignId = "";
		Date date = new Date();
		for (UserDayCostTemp tmp : nowTmpList) {
			campaignId = tmp.getCampaignId();
			//插入差异
			UserDayCost diffCost = new UserDayCost();
			diffCost.setUserName(user.getUserName());
			Integer oldImpression = 0;//曝光量
			Integer oldClick = 0;//点击
			Double oldCost = 0.0;//消耗
			if(oldUserMap.containsKey(campaignId)) {
				UserDayCostTemp oldTmp = oldUserMap.get(campaignId);
				oldImpression = oldTmp.getImpression();
				oldClick = oldTmp.getClick();
				oldCost = oldTmp.getCost();
			}
			diffCost.setImpression(tmp.getImpression() - oldImpression);
			diffCost.setClick(tmp.getClick() - oldClick);
			diffCost.setCost(tmp.getCost() - oldCost);
			diffCost.setCreateDate(date);
			changeUserCostList.add(diffCost);
		}
		//删除tmp历史记录
		userDayCostTempDao.deleteAll();
		//插入tmp最新记录
		if(!EmptyUtil.isEmpty(nowTmpList)) {
			userDayCostTempDao.saveAll(nowTmpList);
		}
		//插入差异记录
		if(!EmptyUtil.isEmpty(changeUserCostList)) {
			userDayCostDao.saveAll(changeUserCostList);
		}
		logger.info(user.getUserName()+"记录差异"+changeUserCostList.size());
	}

}
