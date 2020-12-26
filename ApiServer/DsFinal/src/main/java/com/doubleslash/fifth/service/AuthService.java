package com.doubleslash.fifth.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

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
	public boolean verifyToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String idToken = getBearerToken(request);
		FirebaseToken decodedToken = null;
		String uid = "";
		
		//토큰이 존재하지 않음
		if(idToken == null) {
			response.sendError(401, "Unauthorized");
			return false;
		}
		try{
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
		}catch(FirebaseAuthException e) {
			//토큰이 유효하지 않음
			response.sendError(401, "Unauthorized");
			return false;
		}
	
		uid = decodedToken.getUid();	
		userService.insertUser(uid);
		
		return true;
	}
	
	//Kakao Access Token 검증
	public String verifyKakaoAccessToken(String accessToken) {
		String requestUrl = "https://kapi.kakao.com/v2/user/me";
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();			
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";
			
			while((line = br.readLine()) != null) {
				result += line;
			}
			
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result);
			
			return obj.get("email").toString();			
		}catch(Exception e) {
			//Access Token 오류
			return null;
		}
	}
	
	//Firebase Custom Token 발급
	public String getFirebaseCustomToken(String accessToken) {
		String email = verifyKakaoAccessToken(accessToken);
		if(email != null) {
			CreateRequest request = new CreateRequest();
			request.setEmail(email);
			try {
				UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
				String customToken = FirebaseAuth.getInstance().createCustomToken(userRecord.getUid());
				return customToken;
			}
			catch(Exception e) {
				//이메일 중복
				return null;
			}
		}
		return null;
	}
	
}
