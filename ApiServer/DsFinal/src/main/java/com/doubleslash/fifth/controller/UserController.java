package com.doubleslash.fifth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.dto.RegisterDTO;
import com.doubleslash.fifth.dto.SavePointDTO;
import com.doubleslash.fifth.service.AuthService;
import com.doubleslash.fifth.service.UserService;
import com.doubleslash.fifth.vo.UserVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "User", description = "회원 API")
@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	AuthService authService;
	
	@Autowired
	UserService userService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Nickname Check")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Nickname Check Success"),
		@ApiResponse(code = 409, message = "Duplicate Nickname")	
	})
	@GetMapping(value = "/nickcheck/{nickname}")
	@ResponseBody
	public String nicknameCheck(HttpServletResponse response, @PathVariable("nickname") String nickname) throws IOException {
		UserVO nicknameChk = userService.nicknameCheck(nickname);
		JSONObject jsonObj = new JSONObject();
		
		if(nicknameChk == null) {
			response.setStatus(200);
			jsonObj.put("result", "true");
		}else {
			response.setStatus(409);
			jsonObj.put("result", "false");
		}
		
		String result = jsonObj.toString();
		return result;
	}

	@ApiOperation(value = "Register User", notes="drink(float): 주량, hangover(int): 평균숙취")
	@ApiResponses({
		@ApiResponse(code = 200, message = "User Registration Success"),		
		@ApiResponse(code = 400, message = "User Registration Error"),
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	@PostMapping(value = "/register")
	@ResponseBody
	public String registerUser(HttpServletRequest request, HttpServletResponse response, @RequestBody RegisterDTO requestBody) throws Exception {
		String uid = authService.verifyToken(request);
		
		String nickname = requestBody.getNickname();
		Float drink = requestBody.getDrink();
		int hangover = requestBody.getHangover();
		
		userService.registerUser(uid, nickname, drink, hangover);
		return "{}";
	}
	
	@ApiOperation(value = "SavePoint Check", notes = "true : pass\nfalse : non-pass")
	@GetMapping(value = "/savepoint")
	@ResponseBody
	public SavePointDTO checkSavePoint(HttpServletRequest request) throws Exception{
		String uid = authService.verifyToken(request);
		int id = userService.getId(uid);
		
		return new SavePointDTO(userService.isSignUpCheck(uid), userService.isRecommendCheck(id));
	}
	
	
}
