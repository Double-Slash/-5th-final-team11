package com.doubleslash.fifth.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class AuthService {
	
	@Autowired
	UserService userService;
	
	//idToken 추출
	public String getBearerToken(HttpServletRequest request) {
		String bearerToken = null;
		String authorization = request.getHeader("Authorization");
		if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
			bearerToken = authorization.substring(7);
		}
		
		return bearerToken;
	}
	
	//idToken 검증
	public boolean verifyToken(HttpServletRequest request) {
		String idToken = getBearerToken(request);
		FirebaseToken decodedToken = null;
		String uid = "";
		
		//토큰이 존재하지 않음
		if(idToken == null) {
			return false;
		}
		try{
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
		}catch(FirebaseAuthException e) {
			//토큰이 유효하지 않음
			return false;
		}
	
		uid = decodedToken.getUid();	
		userService.insertUser(uid);
		
		return true;
	}
	
}
