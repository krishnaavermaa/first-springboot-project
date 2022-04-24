package com.in28minutes.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.service.WelcomeService;

@RestController
public class WelcomeController {
	
	@Autowired
	private WelcomeService service;
	
	@RequestMapping("/welcome")
	public String welcomePage()
	{
		return "Welcome"+service.retrieveWelcomeMessage();
	}

}

