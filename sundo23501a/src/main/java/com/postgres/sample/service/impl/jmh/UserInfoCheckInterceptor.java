package com.postgres.sample.service.impl.jmh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.postgres.sample.controller.jmh.JmhController;
import com.postgres.sample.dto.AccessLog;
import com.postgres.sample.dto.LoginLog;
import com.postgres.sample.dto.UserInfo;
import com.postgres.sample.service.jmh.JmhUserInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserInfoCheckInterceptor extends EgovAbstractServiceImpl implements HandlerInterceptor {
	public UserInfoCheckInterceptor() {
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, 
						   HttpServletResponse response, 
						   Object handler, 
						   ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
							 HttpServletResponse response, 
							 Object handler) throws Exception {
		// TODO Auto-generated method stub
		//return HandlerInterceptor.super.preHandle(request, response, handler);
		
		System.out.println();
		HttpSession session = request.getSession(false);
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
		
		if(userInfo == null) {
		  System.out.println("preHandle userInfo is no exists");
		  response.sendRedirect("/user_login");
		  return false;   // 컨트롤러 진행 x
		} else {
		  session.setMaxInactiveInterval(3600); //세션아웃 (1시간)
		}
		return true;   // 컨트롤러 진행 o
		
	}
}
