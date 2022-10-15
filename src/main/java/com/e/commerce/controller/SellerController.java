package com.e.commerce.controller;

import com.e.commerce.dto.PasswordChangeRequestDto;
import com.e.commerce.dto.SellerDto;
import com.e.commerce.service.SellerService;
import com.e.commerce.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {

	private final SellerService sellerService;

	private final JWTUtil jwtUtil;

	public SellerController(SellerService sellerService, JWTUtil jwtUtil) {
		this.sellerService = sellerService;
		this.jwtUtil = jwtUtil;
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<Object> getSellerById(@RequestParam Long sellerId) {
		return ResponseEntity.ok(sellerService.findSellerByIdOrElseThrow(sellerId));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<Object> saveSeller(@RequestBody SellerDto sellerDto) {
		return ResponseEntity.ok(sellerService.createAndSaveSeller(sellerDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping
	public ResponseEntity<Object> updateSeller(@RequestParam Long sellerId,
											   @RequestBody SellerDto sellerDto) {
		return ResponseEntity.ok(sellerService.updateSeller(sellerId, sellerDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping
	public ResponseEntity<Object> deleteSeller(@RequestParam Long sellerId) {
		return ResponseEntity.ok(sellerService.deleteSeller(sellerId));
	}

	@GetMapping("/by_name")
	public ResponseEntity<Object> getSellerByName(@RequestParam String Name) {
		return ResponseEntity.ok(sellerService.getSellerByNameAsDto(Name));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<Object> getAllSeller() {
		return ResponseEntity.ok(sellerService.getAllSeller());
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@PutMapping("/change/password")
	public ResponseEntity<Object> changeSellerPassword(HttpServletRequest request,
													   @RequestBody PasswordChangeRequestDto passwordChangeRequestDto) {
		Long sellerId = jwtUtil.getSellerIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		sellerService.changeSellerPassword(sellerId, passwordChangeRequestDto);
		return ResponseEntity.ok("your password has been successfully changed");
	}

}