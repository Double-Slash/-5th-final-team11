package com.doubleslash.fifth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.auth.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Dummy", description = "API")
@Controller
public class DummyController {

	@Autowired
	AuthService authService;
	
	@ApiOperation(value = "Firebase Token Test")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Ok"),
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	@GetMapping(value = "/test")
	@ResponseBody
	public void authTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String idToken = authService.getBearerToken(request);
		FirebaseToken decodedToken = null;
		String uid = "";
		String result = "";
		
		if(idToken == null) {
			result = "토큰이 존재하지 않습니다.";
			response.sendError(401, "Unauthorized");
			System.out.println(result);
			return;
		}
		
		try{
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
		}catch(FirebaseAuthException e) {
			result = "유효하지 않은 토큰입니다.";
			response.sendError(401, "Unauthorized");
			System.out.println(result);
			return;
		}
	
		uid = decodedToken.getUid();

		
	}
	
}
