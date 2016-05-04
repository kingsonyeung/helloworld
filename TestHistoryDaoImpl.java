package com.jeecms.cms.dao.logicthinking.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jeecms.cms.dao.logicthinking.TestHistoryDao;
import com.jeecms.cms.entity.logicthinking.TestHistory;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

public class TestHistoryDaoImpl extends HibernateBaseDao<TestHistory, Integer> implements TestHistoryDao {

	/* (non-Javadoc)
	 * @see com.jeecms.cms.dao.logicthinking.impl.TestHistoryDao#getTestHistoryList()
	 */
	@Override
	public List<TestHistory> getTestHistoryList() {
		return getSession().createQuery("from TestHistory order by createDate desc").list();
	}

	/* (non-Javadoc)
	 * @see com.jeecms.cms.dao.logicthinking.impl.TestHistoryDao#getAllTestHistoryListByUser(int)
	 */
	@Override
	public List<TestHistory> getAllTestHistoryListByUser(int userId) {
		return getSession().createQuery(
				"from TestHistory where createBy=:createBy order by createDate desc").list();
	}

	/* (non-Javadoc)
	 * @see com.jeecms.cms.dao.logicthinking.impl.TestHistoryDao#getTestHistoryListByPeriod(int, int)
	 */
	@Override
	public List<TestHistory> getTestHistoryListByPeriod(int userId,
			int periodType) throws Exception {

		Calendar calendar = Calendar.getInstance();

		switch (periodType) {
		case 1:
			// 一周内
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			break;
		case 2:
			// 一个月内
			calendar.add(Calendar.MONTH, -1);
			break;
		case 3:
			// 三个月内
			calendar.add(Calendar.MONTH, -3);
			break;
		case 4:
			// 半年内
			calendar.add(Calendar.MONTH, -6);
			break;
		default:
			return getSession()
					.createQuery("from TestHistory where createBy=:createBy order by createDate desc")
					.setInteger("createBy", userId).list();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return getSession()
				.createQuery(
						"from TestHistory where createBy=:createBy and createDate>:createDate order by createDate desc")
				.setInteger("createBy", userId)
				.setDate("createDate",
						format.parse(format.format(calendar.getTime()))).list();
	}

	@Override
	protected Class<TestHistory> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Pagination getPage(String type,Integer userId,int pageNo, int pageSize) {
		Finder f=Finder.create(" from TestHistory bean where 1=1");
		if(userId!=null){
			f.append(" and bean.createBy =:userId").setParam("userId", userId);
		}
		if(!StringUtils.isBlank(type)){
			//创建Date对象
			Date endDate = new Date();
			//创建基于当前时间的日历对象
			Calendar cl = Calendar.getInstance();
			cl.setTime(endDate);
			if(type.equals("week")){
				cl.add(Calendar.DATE, -7);
				Date startDate = cl.getTime();
				f.append(" and bean.createDate>=:startDate").setParam("startDate", startDate);
				f.append(" and bean.createDate<=:endDate").setParam("endDate", endDate);
			}else if(type.equals("month")){
				cl.add(Calendar.MONTH, -1);
				Date startDate = cl.getTime();
				f.append(" and bean.createDate>=:startDate").setParam("startDate", startDate);
				f.append(" and bean.createDate<=:endDate").setParam("endDate", endDate);
			}else if(type.equals("three")){
				cl.add(Calendar.MONTH, -3);
				Date startDate = cl.getTime();
				f.append(" and bean.createDate>=:startDate").setParam("startDate", startDate);
				f.append(" and bean.createDate<=:endDate").setParam("endDate", endDate);
			}else if(type.equals("six")){
				cl.add(Calendar.MONTH, -6);
				Date startDate = cl.getTime();
				f.append(" and bean.createDate>=:startDate").setParam("startDate", startDate);
				f.append(" and bean.createDate<=:endDate").setParam("endDate", endDate);
			}
		}
		f.append(" order by createDate desc");
		return find(f, pageNo, pageSize);
	}
	
}
