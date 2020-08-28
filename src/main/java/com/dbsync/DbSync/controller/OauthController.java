package com.dbsync.DbSync.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsync.DbSync.service.UserService;

/**
 * Rest controller to handle user login
 */
@RestController()
@RequestMapping("/dbsync")
public class OauthController {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

	/**
	 * User service
	 */
	@Autowired
	UserService userService;

	/**
	 * API for oauth login
	 */
	@GetMapping(value = "/oauth/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getCalendarEvents() {
		logger.info("Trying Oauth login");
		try {
			userService.login();
			logger.error("Oauth login successfull");
		} catch (Exception e) {
			logger.error("Oauth login failed: ", e.fillInStackTrace());
		}
	}
}
