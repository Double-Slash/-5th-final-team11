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

import com.doubleslash.fifth.dto.CustomTokenDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
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
			JSONObject obj2 = (JSONObject) obj.get("kakao_account");
			
			String id = obj2.get("id").toString();
			String email = obj2.get("email").toString();
			
			return id+"#"+email;
		}catch(Exception e) {
			//Access Token 오류
			return null;
		}
	}
	
	//Firebase Custom Token 발급
	public CustomTokenDTO getFirebaseCustomToken(String accessToken) throws Exception{
		String info = verifyKakaoAccessToken(accessToken);
		if(info == null) return null;
		
		String temp[] = info.split("#");
		String id = temp[0];
		String email = temp[1];

		CreateRequest request = new CreateRequest();
		request.setEmail(email);
		try {
			//신규 사용자 생성
			FirebaseAuth.getInstance().createUser(request);
		}
		catch(Exception e) {
			
		}
		//커스텀 토큰 생성
		String customToken = FirebaseAuth.getInstance().createCustomToken(id);
		return new CustomTokenDTO(customToken);
	}
}
