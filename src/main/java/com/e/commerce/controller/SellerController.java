package com.e.commerce.controller;

import com.e.commerce.dto.PasswordChangeRequestDto;
import com.e.commerce.dto.SellerProductCustomizationDto;
import com.e.commerce.dto.seller.SellerRequestDto;
import com.e.commerce.dto.seller.SellerWithoutProductsDto;
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

	@GetMapping(value = "/by_id")
	public ResponseEntity<Object> getSellerWithoutProductById(@RequestParam Long sellerId) {
		System.out.println(sellerId);
		SellerWithoutProductsDto sellerWithoutProductById = sellerService.getSellerWithoutProductById(sellerId);
		System.out.println(sellerWithoutProductById);
		return ResponseEntity.ok(sellerWithoutProductById);
	}

	@GetMapping("/by_name")
	public ResponseEntity<Object> getSellerWithoutProductByName(@RequestParam String sellerName) {
		return ResponseEntity.ok(sellerService.getSellerWithoutProductByName(sellerName));
	}

	@GetMapping("/by_id/with_product")
	public ResponseEntity<Object> getSellerWithProductById(@RequestParam Long sellerId) {
		return ResponseEntity.ok(sellerService.getSellerWithProductById(sellerId));
	}

	@GetMapping("/by_name/with_product")
	public ResponseEntity<Object> getSellerWithProductById(@RequestParam String sellerName) {
		return ResponseEntity.ok(sellerService.getSellerWithProductByName(sellerName));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<Object> getAllSeller() {
		return ResponseEntity.ok(sellerService.findAll());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<Object> saveSeller(@RequestBody SellerRequestDto sellerRequestDto) {
		return ResponseEntity.ok(sellerService.createAndSave(sellerRequestDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping
	public ResponseEntity<Object> updateSeller(@RequestParam Long sellerId,
											   @RequestBody SellerRequestDto sellerRequestDto) {
		return ResponseEntity.ok(sellerService.updateSeller(sellerId, sellerRequestDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping
	public ResponseEntity<Object> deleteSeller(@RequestParam Long sellerId) {
		return ResponseEntity.ok(sellerService.deleteSeller(sellerId));
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@PutMapping("/change/password")
	public ResponseEntity<Object> changeSellerPassword(HttpServletRequest request,
													   @RequestBody PasswordChangeRequestDto passwordChangeRequestDto) {
		Long sellerId = jwtUtil.getSellerIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		sellerService.changeSellerPassword(sellerId, passwordChangeRequestDto);
		return ResponseEntity.ok("your password has been successfully changed");
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@PostMapping("/add/product")
	public ResponseEntity<Object> sellerAddNewProduct(HttpServletRequest request,
													  @RequestBody SellerProductCustomizationDto sellerProductCustomizationDto) {
		Long sellerId = jwtUtil.getSellerIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(sellerService.sellerAddNewProduct(sellerId, sellerProductCustomizationDto));
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@DeleteMapping("/remove/product")
	public ResponseEntity<Object> removeSellerByProductId(HttpServletRequest request,
														  @RequestParam Long productId) {
		Long sellerId = jwtUtil.getSellerIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		return ResponseEntity.ok(sellerService.sellerRemoveProduct(sellerId, productId));
	}

}