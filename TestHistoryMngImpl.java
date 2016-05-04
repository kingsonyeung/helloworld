package com.jeecms.cms.manager.logicthinking.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.cms.dao.logicthinking.TestHistoryDao;
import com.jeecms.cms.entity.logicthinking.TestHistory;
import com.jeecms.cms.manager.logicthinking.TestHistoryMng;
import com.jeecms.common.page.Pagination;

public class TestHistoryMngImpl implements TestHistoryMng {

	private TestHistoryDao testHistoryDao;

	@Autowired	//自动绑定
	public void setTestHistoryDao(TestHistoryDao testHistoryDao) {
		this.testHistoryDao = testHistoryDao;
	}

	@Override
	public List<TestHistory> getTestHistoryList() {
		return testHistoryDao.getTestHistoryList();
	}

	@Override
	public List<TestHistory> getAllTestHistoryListByUser(int userId) {
		return testHistoryDao.getAllTestHistoryListByUser(userId);
	}

	@Override
	public List<TestHistory> getTestHistoryListByPeriod(int userId,
			int periodType) throws Exception {
		return testHistoryDao.getTestHistoryListByPeriod(userId, periodType);
	}
	
	@Override
	public Pagination getPage(String type,Integer userId,int pageNo, int pageSize){
		return testHistoryDao.getPage(type, userId, pageNo, pageSize);
	}
}
