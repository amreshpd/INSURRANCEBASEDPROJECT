package com.nt.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.binding.EligibilityDetailsOutput;
import com.nt.entity.COTriggerEntity;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildrenEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.EligibilityDetailsEntity;
import com.nt.entity.PlanRegisterEntity;
import com.nt.repository.IApplicationRegistrationRepository;
import com.nt.repository.ICOTriggerRepository;
import com.nt.repository.IDcCaseRepository;
import com.nt.repository.IDcChildrenRepository;
import com.nt.repository.IDcEducationRepository;
import com.nt.repository.IDcIncomeRepository;
import com.nt.repository.IEligibilityDetermineRepository;
import com.nt.repository.IPlanRepository;

@Service
public class EligibityServiceMgmtImpl implements IEligibilityDeterminationMgmtService {
	@Autowired
	private IDcCaseRepository caseRepository;
	@Autowired
	private IPlanRepository planRepo;
	@Autowired
	private IDcIncomeRepository incomeRepo;
	@Autowired
	private IDcChildrenRepository childRepo;
	@Autowired
	private IApplicationRegistrationRepository appRepo;
	@Autowired
	private IDcEducationRepository eduRepo;
	@Autowired
	private IEligibilityDetermineRepository eligibilityRepo;
	@Autowired
	private ICOTriggerRepository triggerRepo;

	@Override
	public EligibilityDetailsOutput determineEligiblity(Integer caseNo) {
		Integer appId = null;
		Integer planId = null;
		// get planId and appId based on caseNo
		Optional<DcCaseEntity> optCaseEntity = caseRepository.findById(caseNo);
		if (optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity = optCaseEntity.get();
			planId = caseEntity.getPlanId();
			appId = caseEntity.getAppId();
		}
		// get plan name
		String planName = null;
		Optional<PlanRegisterEntity> optPlanEntity = planRepo.findById(planId);
		if (optPlanEntity.isPresent()) {
			PlanRegisterEntity planentity = optPlanEntity.get();
			planName = planentity.getPlanName();
		}
		// calculate citizen age by citizen DOB Through appId
		int citizenAges = 0;
		String citizenName = null;
		Optional<CitizenAppRegistrationEntity> optCitizenEntity = appRepo.findById(appId);
		if (optCitizenEntity.isPresent()) {
			CitizenAppRegistrationEntity citizenEntity = optCitizenEntity.get();
			citizenName = citizenEntity.getFullName();
			citizenAges = Period.between(citizenEntity.getDob(), LocalDate.now()).getYears();
		}
		// call helper method to plan condition
		EligibilityDetailsOutput eligiOutput = applyPlanCondition(caseNo, planName, citizenAges);

		// set Holder name
		eligiOutput.setHolderName(citizenName);
		// save Eligibility entity object
		
		EligibilityDetailsEntity eligientity = new EligibilityDetailsEntity();
		BeanUtils.copyProperties(eligiOutput, eligientity);
		eligibilityRepo.save(eligientity);
		
		// save CoTriggers Object
		
		COTriggerEntity triggerEntity = new COTriggerEntity();
		triggerEntity.setCaseNo(caseNo);
		triggerEntity.setTriggerStatus("pending");
		triggerRepo.save(triggerEntity);

		return eligiOutput;
	}

	private EligibilityDetailsOutput applyPlanCondition(Integer caseNo, String planName, int citizenAges) {
		EligibilityDetailsOutput eligiOutput = new EligibilityDetailsOutput();
		eligiOutput.setPlanName(planName);

		// get Income details of the citizen
		DcIncomeEntity optIncome = incomeRepo.findByCaseNo(caseNo);
		double empIncome = optIncome.getEmpIncome();
		double propertyIncome = optIncome.getPropertyIncome();

		// for SNAP
		if (planName.equalsIgnoreCase("SNAP")) {
			if (empIncome <= 300) {
				eligiOutput.setPlanStatus("Approved");
				eligiOutput.setBenefitAmt(200.0);
			} else {
				eligiOutput.setPlanStatus("Denied");
				eligiOutput.setDenialReason("High Income");
			}

		} else if (planName.equalsIgnoreCase("CCAP")) {
			boolean kidsCountCondition = false;
			boolean kidsAgeCondition = false;
			List<DcChildrenEntity> listChild = childRepo.findByCaseNo(caseNo);
			if (listChild.isEmpty()) {
				kidsCountCondition = true;
				for (DcChildrenEntity child : listChild) {
					int kidsAge = Period.between(child.getChildDb(), LocalDate.now()).getYears();
					if (kidsAge > 16) {
						kidsAgeCondition = false;
						break;
					}
				}
			}
			if (empIncome <= 300 && kidsCountCondition && kidsAgeCondition) {
				eligiOutput.setPlanStatus("Approved");
				eligiOutput.setBenefitAmt(300.0);
			} else {
				eligiOutput.setPlanStatus("Denied");
				eligiOutput.setDenialReason("CCAP rules are not satisfied");
			}

		} else if (planName.equalsIgnoreCase("MEDCARE")) {
			if (citizenAges >= 65) {
				eligiOutput.setPlanStatus("Approved");
				eligiOutput.setBenefitAmt(300.0);
			} else {
				eligiOutput.setPlanStatus("Denied");
				eligiOutput.setDenialReason("MEDCARE rules are not satisfied");
			}
		} else if (planName.equalsIgnoreCase("CAJW")) {

			DcEducationEntity educationEntity = eduRepo.findByCaseNo(caseNo);
			Integer passOutYear = educationEntity.getPassOutYear();
			if (empIncome == 0 && passOutYear < LocalDate.now().getYear()) {
				eligiOutput.setPlanStatus("Approved");
				eligiOutput.setBenefitAmt(300.0);
			} else {
				eligiOutput.setPlanStatus("Denied");
				eligiOutput.setDenialReason("CAJW rules are not satisfied");
			}
		} else if (planName.equalsIgnoreCase("MEDIACARE")) {
			if (empIncome <= 300 && propertyIncome == 0) {
				eligiOutput.setPlanStatus("Approved");
				eligiOutput.setBenefitAmt(200.0);
			} else {
				eligiOutput.setPlanStatus("Denied");
				eligiOutput.setDenialReason("MEDIACARE rules are not satisfied");
			}
		} else if (planName.equalsIgnoreCase("QHP")) {
			if (citizenAges >= 1) {
				eligiOutput.setPlanStatus("Approved");
			} else {
				eligiOutput.setPlanStatus("Denied");
				eligiOutput.setDenialReason("QHP rules are not satisfied");
			}
		}

		// set the Common properties if plan is approved
		if (eligiOutput.getPlanStatus().equalsIgnoreCase("Approved")) {
			eligiOutput.setPlanStartDate(LocalDate.now());
			eligiOutput.setPlanEndDate(LocalDate.now().plusYears(2));
		}
		return eligiOutput;
	}
}