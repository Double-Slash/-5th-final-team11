package com.doubleslash.fifth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.doubleslash.fifth.service.AuthService;

public class AuthInterceptor implements HandlerInterceptor{
	
	@Autowired
	AuthService authService;

	//Controller에 접근하기 전에 실행되는 메소드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		if(authService.verifyToken(request) == null) {
			response.sendError(401, "Unauthorized");
			return false;
		}
		return true;
	}

	
	
}
