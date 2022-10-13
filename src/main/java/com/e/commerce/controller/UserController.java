package com.e.commerce.controller;

import com.e.commerce.dto.AddressDto;
import com.e.commerce.dto.PasswordChangeRequestDto;
import com.e.commerce.dto.UserCreateRequestDto;
import com.e.commerce.dto.UserDto;
import com.e.commerce.service.UserService;
import com.e.commerce.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	private final JWTUtil jwtUtil;

	@GetMapping
	public ResponseEntity<Object> getUser(HttpServletRequest request) {
		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(userService.getUserAsDto(userId));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<Object> getAllUser() {
		return ResponseEntity.ok(userService.getAllUser());
	}

	@PostMapping("/register")
	public ResponseEntity<Object> saveUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
		return ResponseEntity.ok(userService.createAndSaveUser(userCreateRequestDto));
	}

	@PutMapping()
	public ResponseEntity<Object> updateUser(HttpServletRequest request, @RequestBody UserDto userDto) {
		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(userService.updateUser(userId, userDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping
	public ResponseEntity<Object> deleteUser(HttpServletRequest request) {

		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(userService.deleteUser(userId));
	}

	@GetMapping("/addresses")
	public ResponseEntity<Object> getUserAddresses(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.getUserAddress(userId));
	}

	@PostMapping("/addresses")
	public ResponseEntity<Object> saveUserAddresses(HttpServletRequest request,
													@RequestBody AddressDto addressDto) {
		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(userService.saveUserAddress(userId, addressDto));
	}

	@PutMapping("/addresses")
	public ResponseEntity<Object> updateUserAddresses(HttpServletRequest request,
													  @RequestParam Long addressId,
													  @RequestBody AddressDto addressDto) {

		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(userService.updateUserAddress(userId, addressId, addressDto));
	}

	@DeleteMapping("/addresses")
	public ResponseEntity<Object> deleteUserAddresses(HttpServletRequest request,
													  @RequestParam Long addressId) {
		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(userService.deleteUserAddress(userId, addressId));
	}

	@PutMapping("/change/password")
	public ResponseEntity<Object> changeUserPassword(HttpServletRequest request,
													 @RequestBody PasswordChangeRequestDto passwordChangeRequestDto) {

		Long userId = jwtUtil.getUserIdByToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		userService.changeUserPassword(userId, passwordChangeRequestDto);
		return ResponseEntity.ok("your password has been successfully changed");
	}

}