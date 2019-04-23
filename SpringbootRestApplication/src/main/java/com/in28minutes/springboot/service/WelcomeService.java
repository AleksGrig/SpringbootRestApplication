package com.in28minutes.springboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//Spring to manage this bean and create instance of this class
//@Component
@Service
public class WelcomeService {

	@Value("${welcome.message}") // Taking value from application.properties
	private String welcomeMessage;

	public String retrieveWelcomeMessage() {
		return welcomeMessage;
	}
}