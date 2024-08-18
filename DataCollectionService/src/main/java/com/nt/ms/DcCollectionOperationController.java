package com.nt.ms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.binding.PlanSelectionInput;
import com.nt.service.IDcMgmtService;

@RestController
@RequestMapping("/dc-api")
public class DcCollectionOperationController {
	@Autowired
	private IDcMgmtService dcService;

	@GetMapping("/planName")
	public ResponseEntity<List<String>> displayPlanName() {
		// use service
		List<String> listPlanName = dcService.showAllPlanName();
		return new ResponseEntity<List<String>>(listPlanName, HttpStatus.OK);
	}

	@PostMapping("/generateCaseNo/{appId}")
	public ResponseEntity<Integer> generateCaseNo(@PathVariable Integer appId) {
		// use service
		Integer caseNumber = dcService.generateCaseNumber(appId);
		return new ResponseEntity<Integer>(caseNumber, HttpStatus.OK);
	}

	@PutMapping("/updatePlanSelection")
	public ResponseEntity<Integer> savePlanSelection(@RequestBody PlanSelectionInput input) {
		// use service
		Integer savePlanSelection = dcService.savePlanSelection(input);
		return new ResponseEntity<Integer>(savePlanSelection, HttpStatus.OK);
	}
}