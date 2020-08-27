package com.dbsync.DbSync.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbsync.DbSync.service.CalendarService;


@RestController()
@RequestMapping("/dbsync")
public class CalendarController {

	@Autowired
	CalendarService calendarService;
	
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getCalendarEvents() {
		List<String> events;
		try {
			events = calendarService.getCalendarEvents();
			return events;
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
