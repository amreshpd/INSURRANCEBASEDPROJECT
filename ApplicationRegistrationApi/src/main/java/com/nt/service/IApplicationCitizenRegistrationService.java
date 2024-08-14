package com.nt.service;

import com.nt.binding.CitizenAppRegistration;
import com.nt.exception.InvalidSSNException;

public interface IApplicationCitizenRegistrationService {
	public Integer registerCitizenApplication(CitizenAppRegistration inputs) throws InvalidSSNException;
}
