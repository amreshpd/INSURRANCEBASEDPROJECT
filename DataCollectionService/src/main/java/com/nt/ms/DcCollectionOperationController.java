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

import com.nt.binding.ChildInputs;
import com.nt.binding.DcSummaryReport;
import com.nt.binding.EducationInputs;
import com.nt.binding.IncomeInputs;
import com.nt.binding.PlanSelectionInput;
import com.nt.service.IDcMgmtService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/dc-api")
@Tag(name = "doc-api",description = "Data Collection Module MicroService")
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

	@PostMapping("/saveIncome")
	public ResponseEntity<Integer> saveIncomeDetails(@RequestBody IncomeInputs income) {
		// use service
		Integer caseNo = dcService.saveIncomeDetails(income);
		return new ResponseEntity<Integer>(caseNo, HttpStatus.CREATED);
	}

	@PostMapping("/saveEducation")
	public ResponseEntity<Integer> saveEducationDetails(@RequestBody EducationInputs education) {
		// use service
		Integer caseNo = dcService.saveEducationDetails(education);
		return new ResponseEntity<Integer>(caseNo, HttpStatus.CREATED);
	}

	@PostMapping("/saveChild")
	public ResponseEntity<Integer> saveIncomeDetails(@RequestBody List<ChildInputs> child) {
		// use service
		Integer caseNo = dcService.saveChildrenDetails(child);
		return new ResponseEntity<Integer>(caseNo, HttpStatus.CREATED);
	}

	@GetMapping("/report/{caseNo}")
	public ResponseEntity<DcSummaryReport> showSummaryReport(@PathVariable Integer caseNo) {
		// use service
		DcSummaryReport showDcSummary = dcService.showDcSummary(caseNo);
		return new ResponseEntity<DcSummaryReport>(showDcSummary, HttpStatus.OK);
	}

}
