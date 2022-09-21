package com.e.commerce.controller;

import com.e.commerce.dto.SellerDto;
import com.e.commerce.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {

	private final SellerService sellerService;

	public SellerController(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	@GetMapping
	public ResponseEntity<Object> getSellerById(@RequestParam Long sellerId) {
		return ResponseEntity.ok(sellerService.getSellerByIdAsDto(sellerId));
	}

	@PostMapping
	public ResponseEntity<Object> saveSeller(@RequestBody SellerDto sellerDto) {
		return ResponseEntity.ok(sellerService.createAndSaveSeller(sellerDto));
	}

	@PutMapping
	public ResponseEntity<Object> updateSeller(@RequestParam Long sellerId,
											   @RequestBody SellerDto sellerDto) {
		return ResponseEntity.ok(sellerService.updateSeller(sellerId, sellerDto));
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteSeller(@RequestParam Long sellerId) {
		return ResponseEntity.ok(sellerService.deleteSeller(sellerId));
	}

	@GetMapping("/by_name")
	public ResponseEntity<Object> getSellerByName(@RequestParam String Name) {
		return ResponseEntity.ok(sellerService.getSellerByNameAsDto(Name));
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllSeller() {
		return ResponseEntity.ok(sellerService.getAllSeller());
	}

}