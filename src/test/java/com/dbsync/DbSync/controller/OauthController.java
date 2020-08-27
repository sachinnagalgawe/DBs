package com.dbsync.DbSync.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsync.DbSync.service.UserService;

@RestController()
@RequestMapping("/dbsync")
public class OauthController {

	@Autowired
	UserService userService;

	@GetMapping(value = "/oauth/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> getCalendarEvents() throws GeneralSecurityException, IOException {
		Map<String, String> url = new LinkedHashMap<>();
		url.put("url", userService.login());
		return url;
	}
}
