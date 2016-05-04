package com.jeecms.cms.dao.logicthinking;

import java.util.List;

import com.jeecms.cms.entity.logicthinking.TestHistory;
import com.jeecms.common.page.Pagination;

public interface TestHistoryDao {

	public abstract List<TestHistory> getTestHistoryList();

	public abstract List<TestHistory> getAllTestHistoryListByUser(int userId);

	public abstract List<TestHistory> getTestHistoryListByPeriod(int userId,
			int periodType) throws Exception;
	
	public Pagination getPage(String type,Integer userId,int pageNo, int pageSize);
}