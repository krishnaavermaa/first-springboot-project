package com.in28minutes.springboot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.springboot.WelcomeService;
import com.in28minutes.springboot.configuration.BasicConfiguration;

@RestController
public class WelcomeController {
	
	@Autowired
	private BasicConfiguration configuration;
	
	@Autowired
	private WelcomeService service;
	
	@RequestMapping("/welcome")
	public String welcomePage()
	{
		return service.retrieveWelcomeMessage();
	}
	
	@RequestMapping("/dynamic-configuration")
	public Map dynamicConfiguration()
	{
		Map map=new HashMap();
		map.put("message",configuration.getMessage());
		map.put("number",configuration.getNumber());
		map.put("value",configuration.isValue());
		return map;
	}

}

