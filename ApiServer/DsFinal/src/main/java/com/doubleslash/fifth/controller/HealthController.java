package com.doubleslash.fifth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;

@Api(value = "Health", description = "EB 서버 상태 체크 API")
@Controller
@RequestMapping(value = {"/", "/health"})
public class HealthController {

	@GetMapping
	@ResponseBody
	public Object healthCheck() {
		return "Server Status : ON";
	}
	
}