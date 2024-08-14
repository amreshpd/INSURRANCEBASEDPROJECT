package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.binding.CitizenAppRegistration;
import com.nt.service.IApplicationCitizenRegistrationService;

@RestController
@RequestMapping("/CitizenAr-Api")
public class CitizenApplicationRegistrationOperationController {
	@Autowired
	private IApplicationCitizenRegistrationService registrationService;
	@PostMapping("/save")
	public ResponseEntity<String> saveCitizenApplication(@RequestBody CitizenAppRegistration input) throws Exception {
		// use service
		int appId = registrationService.registerCitizenApplication(input);
		return new ResponseEntity<String>("Citizen Application is Registered with id::" + appId, HttpStatus.CREATED);
	}
}