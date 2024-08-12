package com.nt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nt.binding.CitizenAppRegistration;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.repository.IApplicationRegistrationRepository;

@Service
public class CitizenAppRegistrationserviceImpl implements IApplicationCitizenRegistrationService {
	@Autowired
	private IApplicationRegistrationRepository citizenRepo;
	@Autowired
	private RestTemplate template;
	@Value("${ar.ssa-web.url}")
	private String endPointurl;
	@Value("${ar.state}")
	private String targetState;
	@Override
	public Integer registerCitizenApplication(CitizenAppRegistration inputs) {
		//perform WebService call to check whether SSN is valid or not and to get the state name
		ResponseEntity<String> response=template.exchange(endPointurl, HttpMethod.GET,null,String.class,inputs.getSsn());
		//get citizen state
		String stateName = response.getBody();
		if(stateName.equalsIgnoreCase(targetState)) {
			//prepare the entity object
			CitizenAppRegistrationEntity entity=new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			int appId = citizenRepo.save(entity).getAppId();
			return appId;
		}		
		return 0;
	}

}
