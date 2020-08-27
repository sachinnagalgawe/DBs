package com.dbsync.DbSync.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsync.DbSync.service.UserService;
import com.google.api.client.auth.oauth2.Credential;

@RestController()
@RequestMapping("/dbsync")
public class OauthController {

	@Autowired
	UserService userService;

	@GetMapping(value = "/oauth/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public Credential getCalendarEvents() throws GeneralSecurityException, IOException {
		Credential credential = userService.login();
		return credential;
	}
}
