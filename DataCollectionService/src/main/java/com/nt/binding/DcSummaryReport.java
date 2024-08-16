package com.nt.binding;

import java.util.List;

import lombok.Data;

@Data
public class DcSummaryReport {
	private EducationInputs educationDetails;
	private List<ChildInputs> childDetails;
	private IncomeInputs incomeDetails;
	private CitizenAppRegistration citizenDetails;
	private String planName; 
}
