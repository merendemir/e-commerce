package com.e.commerce.controller;

import com.e.commerce.dto.product.ProductRequestDto;
import com.e.commerce.dto.product.ProductUpdateRequestDto;
import com.e.commerce.enums.ProductCategory;
import com.e.commerce.service.AdminService;
import com.e.commerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

	private final ProductService productService;

	private final AdminService adminService;

	public ProductController(ProductService productService, AdminService adminService) {
		this.productService = productService;
		this.adminService = adminService;
	}

	@GetMapping("by_id")
	public ResponseEntity<Object> getProductWithoutSellerById(@RequestParam Long productId) {
		return ResponseEntity.ok(productService.getProductWithoutSellerById(productId));
	}

	@GetMapping("/by_name")
	public ResponseEntity<Object> getAllProductWithoutSellerByName(@RequestParam String productName) {
		return ResponseEntity.ok(productService.getAllProductWithoutSellerByName(productName));
	}

	@GetMapping("/by_id/with_seller")
	public ResponseEntity<Object> getProductWithSellerInformationByProductId(@RequestParam Long productId) {
		return ResponseEntity.ok(productService.getProductWithSellerByProductId(productId));
	}

	@GetMapping("/by_name/with_seller")
	public ResponseEntity<Object> getProductWithSellerInformationByProductName(@RequestParam String productName) {
		return ResponseEntity.ok(productService.getAllProductWithSellerByName(productName));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<Object> saveProduct(@RequestBody ProductRequestDto requestDto) {
		return ResponseEntity.ok(productService.createAndSave(requestDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping
	public ResponseEntity<Object> updateProduct(@RequestParam Long productId,
												@RequestBody ProductUpdateRequestDto requestDto) {
		return ResponseEntity.ok(productService.updateProduct(productId, requestDto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping
	public ResponseEntity<Object> deleteProduct(@RequestParam Long productId) {
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}

	@GetMapping("by_brand")
	public ResponseEntity<Object> findAllProductByBrand(@RequestParam String brand) {
		return ResponseEntity.ok(productService.findAllProductByBrand(brand));
	}

	@GetMapping("by_category")
	public ResponseEntity<Object> findAllProductByCategory(@RequestParam ProductCategory productCategory) {
		return ResponseEntity.ok(productService.findAllProductByCategory(productCategory));
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllProductWithoutSeller() {
		return ResponseEntity.ok(productService.getAllProductWithoutSeller());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all/with_seller")
	public ResponseEntity<Object> getAllProductWithSeller() {
		return ResponseEntity.ok(adminService.getAllProductWithSellerForAdmin());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/image")
	public ResponseEntity<Object> getProductImage(@RequestParam Long productId) {

		return ResponseEntity.ok(productService.getProductImage(productId));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/image")
	public ResponseEntity<Object> addProductImage(@RequestParam Long productId,
												  @RequestParam String url) {

		return ResponseEntity.ok(productService.addProductImage(productId, url));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/image")
	public ResponseEntity<Object> updateProductImage(@RequestParam Long imageId,
													 @RequestParam String url) {

		return ResponseEntity.ok(productService.updateProductImage(imageId, url));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/image")
	public ResponseEntity<Object> deleteProductImage(@RequestParam Long productId,
													 @RequestParam Long imageId) {

		productService.deleteProductImage(productId, imageId);

		return ResponseEntity.ok(HttpStatus.OK);
	}

}