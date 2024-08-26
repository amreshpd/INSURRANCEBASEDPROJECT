package com.nt.service;

import java.util.List;
import java.util.Map;

import com.nt.binding.PlanData;

public interface IAdminmgmtService {
	public String registerPlan(PlanData plan);  //save operation
	public Map<Integer,String> getPlanCategories(); //select operation
	public List<PlanData> showAllPlan(); //for select operation
	public PlanData showPlan(Integer id); //for Edit operation form launch
	public String upadtePlan(PlanData plan); //for update opration form submission
	public String deletePlan(Integer planId); //for Delete opration (hard deletion)
	public String changePlan(Integer planId,String status); //for soft deletion activity
}
