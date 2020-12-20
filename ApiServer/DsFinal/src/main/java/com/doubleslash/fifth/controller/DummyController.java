package com.doubleslash.fifth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Dummy", description = "API")
@Controller
public class DummyController {

	@ApiOperation(value = "Firebase Token Test")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Ok"),
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	@GetMapping(value = "/test")
	@ResponseBody
	public void authTest() {
		
	}
	
}
