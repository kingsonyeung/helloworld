package com.jeecms.cms.action.member;

import static com.jeecms.cms.Constants.TPLDIR_MEMBER;
import static com.jeecms.common.page.SimplePage.cpn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.manager.logicthinking.TestHistoryMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.educool.Oauth.OauthServer;
import com.jeecms.educool.entity.Collection;
import com.jeecms.educool.entity.OauthUser;
import com.jeecms.educool.manager.CollectionMng;
import com.jeecms.educool.manager.OauthUserMng;

/**
 * 收藏信息Action
 * 
 * @author 江西金磊科技发展有限公司
 * 
 */
@Controller
public class CollectionMemberAct {

	public static final String COLLECTION_LIST = "tpl.collectionList";

	/**
	 * 我的收藏信息
	 * 
	 * 如果没有登录则跳转到登陆页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/collection_list.jspx")
	public String collection_list(String type,String queryTitle, Integer queryChannelId,
			Integer pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String token=(String) request.getSession().getAttribute("token");
		if (token == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		OauthUser user = OauthServer.getUser(token,request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		user=oauthUserMng.findByUserId(user.getUserId());
		Pagination p = collectionMng.getPage(type,user.getId(),cpn(pageNo), CookieUtils.getPageSize(request));
		/*Pagination p = contentMng.getPageForCollection(site.getId(), user
				.getId(), cpn(pageNo), CookieUtils.getPageSize(request));*/
		model.addAttribute("pagination", p);
		model.addAttribute("type", type);
		List<Collection> collections=collectionMng.getList(user.getId(),false);
		model.addAttribute("count", collections.size());
		for(Collection c:collections){
			c.setStatus(true);
			collectionMng.update(c);
		}
		if (!StringUtils.isBlank(queryTitle)) {
			model.addAttribute("queryTitle", queryTitle);
		}
		if (queryChannelId != null) {
			model.addAttribute("queryChannelId", queryChannelId);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, COLLECTION_LIST);
	}
	
	@RequestMapping(value = "/member/test_list.jspx")
	public String test_list(String type,String queryTitle, Integer queryChannelId,
			Integer pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String token=(String) request.getSession().getAttribute("token");
		//if (token == null) {
		//	return FrontUtils.showLogin(request, model, site);
		//}
		OauthUser user = OauthServer.getUser(token,request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
		//	return FrontUtils.showLogin(request, model, site);
		}
		//user=oauthUserMng.findByUserId(user.getUserId());
		
		Pagination p = testHistoryMng.getPage(type,0,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", p);
		model.addAttribute("type", type);

		if (!StringUtils.isBlank(queryTitle)) {
			model.addAttribute("queryTitle", queryTitle);
		}
		if (queryChannelId != null) {
			model.addAttribute("queryChannelId", queryChannelId);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, "tpl.testList");
	}

	@RequestMapping(value = "/member/collect.jspx")
	public void collect(Integer cId, Integer operate,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		String token=(String) request.getSession().getAttribute("token");
		OauthUser user = OauthServer.getUser(token,request);
		JSONObject object = new JSONObject();
		if (user == null) {
			object.put("result", false);
		} else {
			user=oauthUserMng.findByUserId(user.getUserId());
			object.put("result", true);
			oauthUserMng.updateUserConllection(user,cId,operate);
		}
		ResponseUtils.renderJson(response, object.toString());
	}
	
	@RequestMapping(value = "/member/collect_cancel.jspx")
	public String  collect_cancel(String type,Integer[] cIds,Integer pageNo,HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		String token=(String) request.getSession().getAttribute("token");
		OauthUser user = OauthServer.getUser(token,request);
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		user=oauthUserMng.findByUserId(user.getUserId());
		for(Integer  id:cIds){
			oauthUserMng.updateUserConllection(user,id,0);
		}
		return collection_list(type,null, null, pageNo, request, response, model);
	}

	@RequestMapping(value = "/member/collect_exist.jspx")
	public void collect_exist(Integer cId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		String token=(String) request.getSession().getAttribute("token");
		OauthUser user = OauthServer.getUser(token,request);
		FrontUtils.frontData(request, model, site);
		JSONObject object = new JSONObject();
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			object.put("result", false);
		} else {
			user=oauthUserMng.findByUserId(user.getUserId());
			if (collectionMng.findByUserContent(user.getId(), cId)!=null) {
				object.put("result", true);
			} else {
				object.put("result", false);
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}
	@Autowired
	private CollectionMng collectionMng;
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private OauthUserMng oauthUserMng;
	@Autowired
	private TestHistoryMng testHistoryMng;

}
