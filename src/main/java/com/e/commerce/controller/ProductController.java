package com.e.commerce.controller;

import com.e.commerce.dto.BaseProductDto;
import com.e.commerce.dto.SellerAddNewProductDto;
import com.e.commerce.enums.ProductCategory;
import com.e.commerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<Object> getBaseProductById(@RequestParam Long productId) {
		return ResponseEntity.ok(productService.getBaseProductById(productId));
	}

	@PostMapping
	public ResponseEntity<Object> saveProduct(@RequestBody BaseProductDto baseProductDto) {
		return ResponseEntity.ok(productService.createAndSaveProduct(baseProductDto));
	}

	@PutMapping
	public ResponseEntity<Object> updateProduct(@RequestParam Long productId,
												@RequestBody BaseProductDto baseProductDto) {
		return ResponseEntity.ok(productService.updateProduct(productId, baseProductDto));
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteProduct(@RequestParam Long productId) {
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}

	@GetMapping("/by_name")
	public ResponseEntity<Object> getProductByNameAsDto(@RequestParam String name) {
		return ResponseEntity.ok(productService.getBaseProductByName(name));
	}

	@GetMapping("/by_brand")
	public ResponseEntity<Object> findAllProductByBrand(@RequestParam String brand) {
		return ResponseEntity.ok(productService.findAllProductByBrand(brand));
	}


	@GetMapping("/by_category")
	public ResponseEntity<Object> findAllProductByCategory(@RequestParam ProductCategory productCategory) {
		return ResponseEntity.ok(productService.findAllProductByCategory(productCategory));
	}

	@GetMapping("/with_seller/by_id")
	public ResponseEntity<Object> getProductWithSellerInformationByProductId(@RequestParam Long id) {
		return ResponseEntity.ok(productService.getProductWithSellerInformationByProductId(id));
	}

	@GetMapping("/with_seller/by_name")
	public ResponseEntity<Object> getProductWithSellerInformationByProductName(@RequestParam String name) {
		return ResponseEntity.ok(productService.getProductWithSellerInformationByProductName(name));
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllProduct() {
		return ResponseEntity.ok(productService.getAllProductAsBase());
	}

	@GetMapping("/all/with_seller")
	public ResponseEntity<Object> getAllProductWithSellerInformation() {
		return ResponseEntity.ok(productService.getAllProductWithSellerInformation());
	}

	@GetMapping("/sellers/by_product_name")
	public ResponseEntity<Object> findAllSellersByProductName(@RequestParam String productName) {
		return ResponseEntity.ok(productService.findAllSellersByProductName(productName));
	}

	@GetMapping("/sellers/by_product_id")
	public ResponseEntity<Object> findAllSellersByProductId(@RequestParam Long productId) {
		return ResponseEntity.ok(productService.findAllSellersByProductId(productId));
	}

	@PostMapping("seller/by_product_id")
	public ResponseEntity<Object> addNewSellerByProductId(@RequestBody SellerAddNewProductDto sellerAddNewProductDto) {
		return ResponseEntity.ok(productService.addNewSellerByProductId(sellerAddNewProductDto));
	}

	@DeleteMapping("seller/by_product_id")
	public ResponseEntity<Object> removeSellerByProductId(@RequestParam Long sellerId,
														  @RequestParam Long productId) {
		return ResponseEntity.ok(productService.removeSellerByProductId(sellerId, productId));
	}

}