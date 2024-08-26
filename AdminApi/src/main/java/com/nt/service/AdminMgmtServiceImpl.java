package com.nt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.binding.PlanData;
import com.nt.cfgs.AppConfigProperties;
import com.nt.constant.PlanConstant;
import com.nt.entity.PlanCategoryEntity;
import com.nt.entity.PlanRegisterEntity;
import com.nt.repository.IPlanCategoryRepository;
import com.nt.repository.IPlanRepository;

@Service
public class AdminMgmtServiceImpl implements IAdminmgmtService {
	@Autowired
	private IPlanCategoryRepository planCategoryRepo;
	@Autowired
	private IPlanRepository planRepo;
	private Map<String ,String> messages;	
	
	public AdminMgmtServiceImpl(AppConfigProperties props) {
		messages=props.getMessages();
	}
	
	@Override
	public String registerPlan(PlanData plan) {
		//convert planData Binding object to plan entity object
		PlanRegisterEntity entity=new PlanRegisterEntity();
		BeanUtils.copyProperties(plan, entity);		
		//save the object
		PlanRegisterEntity savedEntity = planRepo.save(entity);
		return savedEntity.getPlanId()!=null?messages.get(PlanConstant.SAVE_SUCCESS)+savedEntity.getPlanId():messages.get(PlanConstant.SAVE_FAILURE);
	}
	
	@Override
	public Map<Integer,String> getPlanCategories() {
		// get All travel Plan
		List<PlanCategoryEntity> list = planCategoryRepo.findAll();
		Map<Integer,String> categoriesMap=new HashMap<Integer, String>();
		list.forEach(category->{
			categoriesMap.put(category.getCategoryId(), category.getCategoryName());
		});
		return categoriesMap;
	}

	@Override
	public List<PlanData> showAllPlan() {
		// show all Travel Plan
		List<PlanData> listPlan=new ArrayList<PlanData>();
		 List<PlanRegisterEntity> listEntities = planRepo.findAll();
		 listEntities.forEach(list->{
			PlanData data=new PlanData();
			BeanUtils.copyProperties(list, data);
			listPlan.add(data);
		 });
		 return listPlan;
	}

	@Override
	public PlanData showPlan(Integer id) {
		// show travel plan by id
		PlanRegisterEntity entity = planRepo.findById(id).orElseThrow(()-> new IllegalArgumentException(messages.get(PlanConstant.FIND_BY_ID_FAILURE)));
		//convert entity obj to Binding obj
		PlanData data=new PlanData();
		BeanUtils.copyProperties(entity, data);
		return data;
	}

	@Override
	public String upadtePlan(PlanData plan) {
		// updateTravel Plan
		 Optional<PlanRegisterEntity> planEntity = planRepo.findById(plan.getPlanId());
		if(planEntity.isPresent()) {
			//update the object
			PlanRegisterEntity entity=planEntity.get();
			BeanUtils.copyProperties(plan, entity);
			planRepo.save(entity);
			return plan.getPlanId()+messages.get(PlanConstant.UPDATE_SUCCESS);
		}else {
			return plan.getPlanId()+messages.get(PlanConstant.UPDATE_FAILURE);	
		}	
		
	}

	@Override
	public String deletePlan(Integer planId) {
		// delete plan id
		Optional<PlanRegisterEntity> optEntity = planRepo.findById(planId);
		if(optEntity.isPresent()) {
			//update the object
			planRepo.deleteById(planId);
			return planId+messages.get(PlanConstant.DELETE_SUCCESS);
		}else
		return planId+" "+messages.get(PlanConstant.DELETE_FAILURE);
	}

	@Override
	public String changePlan(Integer planId, String status) {
		// changeTravel plan 
		Optional<PlanRegisterEntity> opt = planRepo.findById(planId);
		if(opt.isPresent()) {
			PlanRegisterEntity entity = opt.get();
			entity.setActiveSw(status);;
			planRepo.save(entity);
			return planId+messages.get(PlanConstant.STATUS_CHANGE_SUCCESS);
		}else		
		return planId+messages.get(PlanConstant.STATUS_CHANGE_FAILURE);
	}
}