package com.dbsync.DbSync.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbsync.DbSync.service.UserService;
import com.dbsync.DbSync.service.util.CredentialUtils;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	CredentialUtils credentialUtils;
	
	@Override
	public String login() throws IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		String credential = credentialUtils.getCredentialsUrl(HTTP_TRANSPORT);
		return credential;
	}
}
