package com.doubleslash.fifth.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.service.AuthService;
import com.doubleslash.fifth.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "Dummy", description = "API")
@Controller
public class DummyController {

	@Autowired
	AuthService authService;
	
	@Autowired
	UserService userService;
	
	@ApiOperation(value = "Firebase Token Verification Test", notes="authorize value: Bearer idToken 입력 후 실행", authorizations = { @Authorization(value="idToken")})
	@ApiResponses({
		@ApiResponse(code = 200, message = "Verification Success"),
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	@GetMapping(value = "/test")
	@ResponseBody
	public void authTest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		authService.verifyToken(request, response);
	}
	
}
