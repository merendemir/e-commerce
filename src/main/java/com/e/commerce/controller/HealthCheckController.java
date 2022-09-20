package com.e.commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/api/v1/health_check")
	public ResponseEntity<Object> getHealthCheck() {
		return ResponseEntity.ok(HttpStatus.OK);
	}

}