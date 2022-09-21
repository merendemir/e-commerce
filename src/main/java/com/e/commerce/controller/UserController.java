package com.e.commerce.controller;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<Object> getUser(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.getUserAsDto(userId));
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllUser() {
		return ResponseEntity.ok(userService.getAllUser());
	}

	@PostMapping
	public ResponseEntity<Object> saveUser(@RequestBody UserDto userDto) {
		return ResponseEntity.ok(userService.createAndSaveUser(userDto));
	}

	@PutMapping
	public ResponseEntity<Object> updateUser(@RequestParam Long userId,
											 @RequestBody UserDto userDto) {
		return ResponseEntity.ok(userService.updateUser(userId, userDto));
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteUser(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}

	@GetMapping("/addresses")
	public ResponseEntity<Object> getUserAddresses(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.getUserAddress(userId));
	}

	@PostMapping("/addresses")
	public ResponseEntity<Object> saveUserAddresses(@RequestParam Long userId,
													@RequestBody AddressDto addressDto) {
		return ResponseEntity.ok(userService.saveUserAddress(userId, addressDto));
	}

	@PutMapping("/addresses")
	public ResponseEntity<Object> updateUserAddresses(@RequestParam Long userId,
													  @RequestParam Long addressId,
													  @RequestBody AddressDto addressDto) {
		return ResponseEntity.ok(userService.updateUserAddress(userId, addressId, addressDto));
	}

	@DeleteMapping("/addresses")
	public ResponseEntity<Object> deleteUserAddresses(@RequestParam Long userId,
													  @RequestParam Long addressId) {
		return ResponseEntity.ok(userService.deleteUserAddress(userId, addressId));
	}

}