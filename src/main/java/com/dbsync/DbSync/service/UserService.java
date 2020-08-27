package com.dbsync.DbSync.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.auth.oauth2.Credential;

public interface UserService {

	public Credential login() throws IOException, GeneralSecurityException;
}
