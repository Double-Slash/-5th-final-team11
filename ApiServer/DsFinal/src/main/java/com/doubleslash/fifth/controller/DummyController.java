package com.doubleslash.fifth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Dummy", description = "API")
@Controller
public class DummyController {

	@ApiOperation(value = "Firebase Token Test")
	@GetMapping(value = "/test")
	@ResponseBody
	public void authTest() {
		
	}
	
}
