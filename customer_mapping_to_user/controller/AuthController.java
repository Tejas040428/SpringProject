package com.tnqsoftware.customer_mapping_to_user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Authentication")
@RestController
@RequestMapping("/service")
public class AuthController {

	@ApiOperation("Login.")
	@PostMapping("/login")
	public void fakeLogin(@ApiParam("User") @RequestParam String username, @ApiParam("Password") @RequestParam String password) {
	    throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
	}

	@ApiOperation("Logout.")
	@PostMapping("/logout")
	public void fakeLogout() {
	    throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
	}
}
