package com.nt.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nt.binding.CitizenAppRegistration;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.exception.InvalidSSNException;
import com.nt.repository.IApplicationRegistrationRepository;

import reactor.core.publisher.Mono;

@Service
public class CitizenAppRegistrationserviceImpl implements IApplicationCitizenRegistrationService {
	@Autowired
	private IApplicationRegistrationRepository citizenRepo;
//	@Autowired
//	private RestTemplate template;
	@Autowired
	private WebClient client;
	@Value("${ar.ssa-web.url}")
	private String endPointurl;
	@Value("${ar.state}")
	private String targetState;

	@Override
	public Integer registerCitizenApplication(CitizenAppRegistration inputs) throws InvalidSSNException {
		// perform WebService call to check whether SSN is valid or not and to get the
		// state name
		// ResponseEntity<String> response=template.exchange(endPointurl,
		// HttpMethod.GET,null,String.class,inputs.getSsn());
		//perform using webservice to check whether SSN is valid or not and to get yhe state name(Using WebClient)
		
		Mono<String> response= client.get().uri(endPointurl, inputs.getSsn()).retrieve()
				.onStatus(HttpStatus.BAD_REQUEST::equals,res->res.bodyToMono(String.class).map(ex->new InvalidSSNException("Invalid SSN"))).bodyToMono(String.class);
		// get citizen state
		 String stateName =response.block();
		if (stateName.equalsIgnoreCase(targetState)) {
			// prepare the entity object
			CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			int appId = citizenRepo.save(entity).getAppId();
			return appId;
		}
		throw new InvalidSSNException("Invalid SSN");
	}
}