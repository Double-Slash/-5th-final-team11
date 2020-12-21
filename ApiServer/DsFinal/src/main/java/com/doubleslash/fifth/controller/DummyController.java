package com.doubleslash.fifth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.auth.AuthService;

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
		
		String uid = (String) request.getAttribute("uid");
		
		if(uid == null) {
			response.sendError(401, "Unauthorized");
		}
		
		System.out.println("idToken : " + uid);
		
		
		
		
	}
	
}
