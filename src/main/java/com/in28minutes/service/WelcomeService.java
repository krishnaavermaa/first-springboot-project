package com.in28minutes.service;

import org.springframework.stereotype.Service;

@Service
public class WelcomeService
{
	public String retrieveWelcomeMessage() {
		return "Good Morning";
	}
}