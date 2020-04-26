package com.privatekit.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaypointsController {
	@PostMapping("/v1.0/{app_id}/waypoints")
	public ResponseEntity<String> create(@PathVariable("app_id") String app_id) {
		String body = "Waypoints inserted successfully";
		return ResponseEntity.ok(body);
	}
}
