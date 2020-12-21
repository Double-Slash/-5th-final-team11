package com.doubleslash.fifth.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class AuthService {
	
	public String getBearerToken(HttpServletRequest request) {
		String bearerToken = null;
		String authorization = request.getHeader("Authorization");
		if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
			bearerToken = authorization.substring(7);
		}
		
		return bearerToken;
	}
	
	private void verifyToken(HttpServletRequest request) throws FirebaseAuthException {
		String token = getBearerToken(request);
		FirebaseToken decodedToken = null;

		if(token == null) {
			request.setAttribute("uid", null);
			return;
			
		}
		try {
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
			
		}catch(FirebaseAuthException e){
			request.setAttribute("uid", null);
			return;
		}

		String uid = decodedToken.getUid();
		request.setAttribute("uid", uid);
	
	}
}
