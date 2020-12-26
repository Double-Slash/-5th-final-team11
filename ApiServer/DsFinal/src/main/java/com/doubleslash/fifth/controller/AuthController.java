package com.doubleslash.fifth.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doubleslash.fifth.dto.CustomTokenDTO;
import com.doubleslash.fifth.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Auth", description = "Auth API")
@RequestMapping(value = "/auth")
@Controller
public class AuthController {
	
	@Autowired
	AuthService authService;

	@ApiOperation(value = "Kakao Access Token Verification & Get Firebase Custom Token")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Verification & Get Custom Token Success"),
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	@ApiImplicitParam(name = "AccessToken", value = "Kakao Access Token", required = true, dataType = "string", paramType = "header")
	@GetMapping(value = "/kakao")
	@ResponseBody
	public CustomTokenDTO verifyKakao(@RequestHeader(value = "AccessToken") String accessToken, HttpServletResponse response) throws Exception{
		String customToken = authService.verifyKakaoAccessToken(accessToken);
		if(customToken != null) {
			CustomTokenDTO dto = new CustomTokenDTO();
			dto.setCustomToken(customToken);
			return dto;
		}else {
			response.sendError(401, "Unauthorized");
			return null;
		}
	}
	
}
