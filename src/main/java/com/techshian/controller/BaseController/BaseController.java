package com.techshian.controller.BaseController;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BaseController {
	
	
	
	
	
	
	public String getLoggedInUser() throws JsonProcessingException, JSONException {
		ObjectMapper Obj = new ObjectMapper();
		String properties = Obj.writeValueAsString( SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		JSONObject jsonObj = new JSONObject(properties);
		
		return jsonObj.getString("username");
	}
	
	
		
	
	
}
