package com.nt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.binding.ChildInputs;
import com.nt.binding.DcSummaryReport;
import com.nt.binding.EducationInputs;
import com.nt.binding.IncomeInputs;
import com.nt.binding.PlanSelectionInput;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.DcCaseEntity;
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
		//load citizen Data
		Optional<CitizenAppRegistrationEntity> appCitizens=applicationRepository.findById(appId);
		if(appCitizens.isPresent()) {
			DcCaseEntity caseEntity=new DcCaseEntity();
			caseEntity.setAppId(appId);
			return caseRepository.save(caseEntity).getCaseNo();
		}
		return 0;
	}

	@Override
	public List<String> showAllPlanName() {

		return null;
	}

	@Override
	public Integer savePlanSelection(PlanSelectionInput inputs) {
		//load DcCaseEntity Object
		Optional<DcCaseEntity> opt=caseRepository.findById(inputs.getCaseNo());
		if(opt.isPresent()) {
			DcCaseEntity caseEntity=opt.get();
			caseEntity.setPlanId(inputs.getPlanId());
			//update the DcCaseEntity with Plan Id
			caseRepository.save(caseEntity);
			return caseEntity.getCaseNo();
		}
		return 0;
	}

	@Override
	public Integer saveIncomeDetails(IncomeInputs inputs) {

		return null;
	}

	@Override
	public Integer saveEducationDetails(EducationInputs inputs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer saveChildrenDetails(List<ChildInputs> children) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DcSummaryReport showDcSummary(Integer caseNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
