package com.e.commerce.controller;

import com.e.commerce.dto.UserDto;
import com.e.commerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user")
	public ResponseEntity<Object> getUser(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.getUserAsDto(userId));
	}

	@GetMapping("/all/user")
	public ResponseEntity<Object> getAllUser() {
		return ResponseEntity.ok(userService.getAllUser());
	}

	@PostMapping("/user")
	public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
		return ResponseEntity.ok(userService.createAndSaveUser(userDto));
	}

	@PutMapping("/user")
	public ResponseEntity<Object> updateUser(@RequestParam Long userId, @RequestBody UserDto userDto) {
		return ResponseEntity.ok(userService.updateUser(userId, userDto));
	}

	@DeleteMapping("/user")
	public ResponseEntity<Object> deleteUser(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}

	@GetMapping("/test")
	public ResponseEntity<Object> testGet() {
		userService.testGet();
		return ResponseEntity.ok("ok");
	}

	@PostMapping("/test")
	public ResponseEntity<Object> testSave() {
		userService.testSave();
		return ResponseEntity.ok("ok");
	}

}