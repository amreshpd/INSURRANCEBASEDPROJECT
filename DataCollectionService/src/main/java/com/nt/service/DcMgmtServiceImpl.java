package com.nt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.binding.ChildInputs;
import com.nt.binding.CitizenAppRegistration;
import com.nt.binding.DcSummaryReport;
import com.nt.binding.EducationInputs;
import com.nt.binding.IncomeInputs;
import com.nt.binding.PlanSelectionInput;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.PlanRegisterEntity;
import com.nt.repository.IApplicationRegistrationRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IDcChildrenRepository;
import com.nt.repository.IDcEducationRepository;
import com.nt.repository.IDcIncomeRepository;
import com.nt.repository.IPlanRepository;

@Service
public class DcMgmtServiceImpl implements IDcMgmtService {
	@Autowired
	private IApplicationRegistrationRepository applicationRepository;
	@Autowired
	private IDcCaseRepository caseRepository;
	@Autowired
	private IDcChildrenRepository childRepo;
	@Autowired
	private IDcEducationRepository educationRepo;
	@Autowired
	private IDcIncomeRepository incomeRepo;
	@Autowired
	private IPlanRepository planRepo;

	@Override
	public Integer generateCaseNumber(Integer appId) {
		// load citizen Data
		Optional<CitizenAppRegistrationEntity> appCitizens = applicationRepository.findById(appId);
		if (appCitizens.isPresent()) {
			DcCaseEntity caseEntity = new DcCaseEntity();
			caseEntity.setAppId(appId);
			return caseRepository.save(caseEntity).getCaseNo();
		}
		return 0;
	}

	@Override
	public List<String> showAllPlanName() {
		List<PlanRegisterEntity> planList = planRepo.findAll();
		// get only plan name using stream Api
		List<String> planNameList = planList.stream().map(plan -> plan.getPlanName()).toList();
		return planNameList;
	}

	@Override
	public Integer savePlanSelection(PlanSelectionInput inputs) {
		// load DcCaseEntity Object
		Optional<DcCaseEntity> opt = caseRepository.findById(inputs.getCaseNo());
		if (opt.isPresent()) {
			DcCaseEntity caseEntity = opt.get();
			caseEntity.setPlanId(inputs.getPlanId());
			// update the DcCaseEntity with Plan Id
			caseRepository.save(caseEntity);
			return caseEntity.getCaseNo();
		}
		return 0;
	}

	@Override
	public Integer saveIncomeDetails(IncomeInputs inputs) {
		// convert binding object data to entity object data
		DcIncomeEntity incomeEntity = new DcIncomeEntity();
		BeanUtils.copyProperties(inputs, incomeEntity);
		// save the income details
		incomeRepo.save(incomeEntity);
		// return case no
		return inputs.getCaseNo();
	}

	@Override
	public Integer saveEducationDetails(EducationInputs inputs) {
		// convert binding object data to entity object data
		DcEducationEntity educationEntity = new DcEducationEntity();
		BeanUtils.copyProperties(inputs, educationEntity);
		// save the income details
		educationRepo.save(educationEntity);
		// return case no
		return inputs.getCaseNo();
	}

	@Override
	public Integer saveChildrenDetails(List<ChildInputs> children) {
		// convert each binding class object data to each entity object data
		children.forEach(child -> {
			DcChildrenEntity childEntity = new DcChildrenEntity();
			BeanUtils.copyProperties(child, childEntity);
			// save each child object
			childRepo.save(childEntity);

		});
		return children.get(0).getCaseNo();
	}

	@Override
	public DcSummaryReport showDcSummary(Integer caseNo) {
		// get multiple entity objs based on caseNo
		DcIncomeEntity incomeEntity = incomeRepo.findByCaseNo(caseNo);
		DcEducationEntity educationEntity = educationRepo.findByCaseNo(caseNo);
		List<DcChildrenEntity> childList = childRepo.findByCaseNo(caseNo);

		Optional<DcCaseEntity> optCaseEntity = caseRepository.findById(caseNo);
		String planName = null;
		Integer appId = null;
		// get plan name
		if (optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity = optCaseEntity.get();
			Integer planId = caseEntity.getPlanId();
			appId = caseEntity.getAppId();
			Optional<PlanRegisterEntity> optPlanEntity = planRepo.findById(planId);
			if (optPlanEntity.isPresent()) {
				planName = optPlanEntity.get().getPlanName();
			}
		}

		Optional<CitizenAppRegistrationEntity> optCitizenRegistration = applicationRepository.findById(appId);
		CitizenAppRegistrationEntity citizenEntity = null;
		if (optCitizenRegistration.isPresent()) {
			citizenEntity = optCitizenRegistration.get();
		}

		// convert entity object to binding object
		IncomeInputs income = new IncomeInputs();
		BeanUtils.copyProperties(incomeEntity, income);

		EducationInputs educationInputs = new EducationInputs();
		BeanUtils.copyProperties(educationEntity, educationInputs);

		List<ChildInputs> listChild = new ArrayList<ChildInputs>();
		childList.forEach(childEntity -> {
			ChildInputs childInput = new ChildInputs();
			BeanUtils.copyProperties(childEntity, childInput);
			listChild.add(childInput);
		});

		CitizenAppRegistration citizenBind = new CitizenAppRegistration();
		BeanUtils.copyProperties(citizenEntity, citizenBind);

		// prepare DcSummary Report
		DcSummaryReport dcSummaryReport = new DcSummaryReport();
		dcSummaryReport.setChildrenDetails(listChild);
		dcSummaryReport.setCitizenDetails(citizenBind);
		dcSummaryReport.setEducationDetails(educationInputs);
		dcSummaryReport.setIncomeDetails(income);
		dcSummaryReport.setPlanName(planName);

		return dcSummaryReport;
	}
}
