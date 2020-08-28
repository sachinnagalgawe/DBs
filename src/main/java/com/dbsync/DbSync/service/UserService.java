package com.dbsync.DbSync.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.auth.oauth2.Credential;

/**
 * UserService to handle user related operations
 */
public interface UserService {

	/**
	 * User login
	 * 
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public Credential login() throws IOException, GeneralSecurityException;
}
