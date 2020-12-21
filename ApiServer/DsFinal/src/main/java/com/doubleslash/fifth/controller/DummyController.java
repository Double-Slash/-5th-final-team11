package com.doubleslash.fifth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.service.AuthService;
import com.doubleslash.fifth.service.UserService;
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
	
	@Autowired
	UserService userService;
	
	@ApiOperation(value = "Firebase Token Verification Test")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Verification Success"),
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	@GetMapping(value = "/test")
	@ResponseBody
	public void authTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(!authService.verifyToken(request)) response.sendError(401, "Unauthorized");
	}
	
}
