package com.doubleslash.fifth.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

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
	public String verifyToken(HttpServletRequest request) throws Exception{
		String idToken = getBearerToken(request);
		FirebaseToken decodedToken = null;
		String uid = "";
		
		//토큰이 존재하지 않음
		if(idToken == null) {
			return null;
		}
		try{
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
		}catch(FirebaseAuthException e) {
			//토큰이 유효하지 않음
			return null;
		}
	
		uid = decodedToken.getUid();	
		userService.insertUser(uid);
		
		return uid;
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
			JSONObject mainObj = (JSONObject) parser.parse(result);		
			JSONObject subObj = (JSONObject) mainObj.get("kakao_account");
			
			String email = subObj.get("email").toString();
			
			return email;
		}catch(Exception e) {
			//Access Token 오류
			return null;
		}
	}
	
	//Firebase Custom Token 발급
	public CustomTokenDTO getFirebaseCustomToken(String accessToken) throws Exception{
		String email = verifyKakaoAccessToken(accessToken);
		if(email == null) return null;

		String uId;
		CreateRequest request = new CreateRequest();
		request.setEmail(email);

		try {
			//신규 사용자의 uId 가져오기
			uId = FirebaseAuth.getInstance().createUser(request).getUid();
		}
		catch(FirebaseAuthException e) {
			//기존 사용자의 uId 가져오기
			uId = FirebaseAuth.getInstance().getUserByEmail(email).getUid();
		}
		//커스텀 토큰 생성
		String customToken = FirebaseAuth.getInstance().createCustomToken(uId);
		return new CustomTokenDTO(customToken);
	}
}
