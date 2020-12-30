package com.doubleslash.fifth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.dto.NicknameDTO;
import com.doubleslash.fifth.service.AuthService;
import com.doubleslash.fifth.service.UserService;
import com.doubleslash.fifth.vo.UserVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "User", description = "User API")
@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	AuthService authService;
	
	@Autowired
	UserService userService;

	@ApiOperation(value = "Register User Nickname")
	@ApiResponses({
		@ApiResponse(code = 200, message = "User Registration Success"),		
		@ApiResponse(code = 400, message = "Nickname input Error"),
		@ApiResponse(code = 401, message = "Unauthorized"),
		@ApiResponse(code = 409, message = "Duplicate Nickname")
	})
	@PostMapping(value = "/register")
	@ResponseBody
	public void registerUser(HttpServletRequest request, HttpServletResponse response, @RequestBody NicknameDTO requestBody) throws Exception {
		String nickname = requestBody.getNickname();
		String uid = authService.verifyToken(request);
		
		if(nickname == null) {
			response.sendError(400, "Nickname input Error");
		}
		
		UserVO nicknameChk = userService.nicknameCheck(nickname);
		if(nicknameChk != null) {
			response.sendError(409,"Duplicate Nickname");
		}
		userService.updateNickname(uid, nickname);
	}
}
