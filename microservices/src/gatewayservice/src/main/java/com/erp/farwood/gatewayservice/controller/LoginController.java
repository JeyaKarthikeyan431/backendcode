
package com.erp.farwood.gatewayservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.farwood.gatewayservice.dto.ChangePasswordDto;
import com.erp.farwood.gatewayservice.dto.ForgotPasswordDto;
import com.erp.farwood.gatewayservice.dto.LoginDto;
import com.erp.farwood.gatewayservice.dto.UserDto;
import com.erp.farwood.gatewayservice.dto.UserInformation;
import com.erp.farwood.gatewayservice.response.ListResponse;
import com.erp.farwood.gatewayservice.response.Response;
import com.erp.farwood.gatewayservice.services.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	LoginService loginService;

	@PostMapping("/signIn")
	public @ResponseBody Response<UserInformation> login(@Validated @RequestBody LoginDto login) {
		return loginService.login(login);
	}

	@PostMapping("/signOut")
	public @ResponseBody Response<UserInformation> signOut(@RequestHeader String userId) {
		return loginService.signOut(userId);
	}

	@PostMapping("/changePassword")
	public @ResponseBody Response<List<String>> changePassword(@RequestHeader String emailId,
			@RequestBody ChangePasswordDto changePasswordDto) {
		return loginService.changePassword(emailId, changePasswordDto);
	}

	@PostMapping("/forgotPassword")
	public @ResponseBody Response<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPassword) {
		return loginService.forgotPassword(forgotPassword);
	}

	@PostMapping("/createUser")
	public @ResponseBody Response<String> createUser(@RequestHeader String userId, @RequestBody UserDto userDto) {
		return loginService.createUser(userId, userDto);
	}

	@GetMapping("/getUsers")
	public ListResponse<List<UserDto>> getAllUsers(@RequestHeader String userId) {
		return loginService.getAllUsers(userId);
	}

	@GetMapping("/getUser")
	public @ResponseBody Response<UserDto> getUser(@RequestParam String userId) {
		return loginService.getUser(userId);
	}

	@PutMapping("/updateUser")
	public @ResponseBody Response<String> updateUser(@RequestParam String userId, @RequestBody UserDto userDto) {
		return loginService.updateUser(userId, userDto);
	}

	@DeleteMapping("/deleteUser")
	public @ResponseBody Response<String> deleteUser(@RequestParam String userId) {
		return loginService.deleteUser(userId);
	}

	@GetMapping("/getPasswordGuidelines")
	public @ResponseBody Response<List<String>> getPasswordGuidelines(@RequestHeader String userId) {
		return loginService.getPasswordGuidelines(userId);
	}
}
