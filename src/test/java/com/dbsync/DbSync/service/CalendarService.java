package com.dbsync.DbSync.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface CalendarService {

	public List<String> getCalendarEvents() throws GeneralSecurityException, IOException;
}
