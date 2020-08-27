package com.dbsync.DbSync.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

public interface UserService {

	public String login() throws IOException, GeneralSecurityException;
}
