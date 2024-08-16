package com.nt.service;

import java.util.List;

import com.nt.binding.ChildInputs;
import com.nt.binding.DcSummaryReport;
import com.nt.binding.EducationInputs;
import com.nt.binding.IncomeInputs;
import com.nt.binding.PlanSelectionInput;

public interface IDcMgmtService {
	public Integer generateCaseNumber(Integer appId);
	public List<String> showAllPlanName();
	public Integer savePlanSelection(PlanSelectionInput inputs);
	public Integer saveIncomeDetails(IncomeInputs inputs);
	public Integer saveEducationDetails(EducationInputs inputs);
	public Integer saveChildrenDetails(List<ChildInputs> children);
	public DcSummaryReport showDcSummary(Integer caseNo);
}
