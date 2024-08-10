package com.nt.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssa-web-api")
public class SSAWebRestControllerApi {
	@GetMapping("/find/{ssn}")
	public ResponseEntity<String> getStateBySSN(@PathVariable Integer ssn) {
		// check length for validation
		if (String.valueOf(ssn).length() != 9) {
			return new ResponseEntity<String>("Invalid SSN Please Enter The 9 digit SSN number",
					HttpStatus.BAD_REQUEST);
		}
		// get state
		else {
		int stateCode = ssn % 1000;
		String stateName = null;
		if (stateCode >= 531 && stateCode <= 539)
			stateName = "Washington DC";
		else if (stateCode >= 545 && stateCode <=573)
			stateName = "California";
		else if (stateCode >= 261 && stateCode <= 267)
			stateName = "Florida";
		else if (stateCode >= 001 && stateCode <= 003)
			stateName = "New Hampshire";
		else if(stateCode==520 )
			stateName="Wyoming";
		else if(stateCode>=503 && stateCode<=504 )
			stateName="South Dakota";
		else if(stateCode>=247 && stateCode<=251)
			stateName="South Carolina";
		else if(stateCode>=303 && stateCode<=317)
			stateName="Indiana";
		else if(stateCode==575 || stateCode==576 )
			stateName="Hawali";
		else if(stateCode>=252 && stateCode<=260 )
			stateName="Georgia";
		else if(stateCode>=416 && stateCode<=424)
			stateName="Alabama";
		else if(stateCode>=433 && stateCode<=439)
			stateName="Louisiana";
		else if(stateCode>=468 && stateCode<=477)
			stateName="Minnesota";
		else if(stateCode>=362 && stateCode<=386)
			stateName="Michigan";
		else if(stateCode>=050 && stateCode<=134)
			stateName="New York";
		else if(stateCode==525 || stateCode==585 )
			stateName="New Mexico";
		else if(stateCode>=135 && stateCode<= 158)
			stateName="New Jersey";
		else
			stateName="Invalid SSN";
		return new ResponseEntity<String>(stateName,HttpStatus.OK);
	}
	}
}
