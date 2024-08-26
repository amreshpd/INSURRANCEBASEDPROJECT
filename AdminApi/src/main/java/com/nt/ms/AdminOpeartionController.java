package com.nt.ms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.binding.PlanData;
import com.nt.service.IAdminmgmtService;

@RestController
@RequestMapping("/plan-api") // Global Path optional
public class AdminOpeartionController {
	@Autowired
	private IAdminmgmtService planService;

//for register purpose we use @PostMapping() and For the Json part we use @RequestBody()
	@PostMapping("/register")
	public ResponseEntity<?> registerTravelPlan(@RequestBody PlanData plan) {
		// use service class
		try {
			String msg = planService.registerPlan(plan);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/categories")
	public ResponseEntity<Map<Integer, String>> showPlanCategories() {
		// use service class
		Map<Integer, String> categories = planService.getPlanCategories();
		return new ResponseEntity<Map<Integer, String>>(categories, HttpStatus.OK);

	}

	@GetMapping("/all")
	public ResponseEntity<List<PlanData>> viewAllPlan() {
		List<PlanData> show = planService.showAllPlan();
		return new ResponseEntity<List<PlanData>>(show, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<PlanData> showPlanById(@PathVariable Integer id) {
		// use service class
		PlanData plan = planService.showPlan(id);
		return new ResponseEntity<PlanData>(plan, HttpStatus.OK);

	}

	@PutMapping("/update")
	public ResponseEntity<String> updatePlan(@RequestBody PlanData plan) {
		// use service
		String update = planService.upadtePlan(plan);
		return new ResponseEntity<String>(update, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> removeTravelPlan(@PathVariable Integer id) {
		// use service
		String delete = planService.deletePlan(id);
		return new ResponseEntity<String>(delete, HttpStatus.OK);
	}

	@PutMapping("/status/{id}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer id, @PathVariable String status) {
		//use service
		String change = planService.changePlan(id, status);
		return new ResponseEntity<String>(change, HttpStatus.OK);
	}
}//class