package com.in28minutes.springboot.service;

import org.springframework.stereotype.Service;

//Spring to manage this bean and create instance of this class
//@Component
@Service
public class WelcomeService {

	public String retrieveWelcomeMessage() {
		return "Good morning! Updated. Spring-boot-devtools added. Now it works!***";
	}
}