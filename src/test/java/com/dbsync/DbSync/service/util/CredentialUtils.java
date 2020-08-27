package com.dbsync.DbSync.service.util;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.dbsync.DbSync.service.impl.CalendarServiceImpl;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.docs.v1.DocsScopes;

@Component
public class CredentialUtils {

	private static final Logger LOGGER = Logger.getLogger(AuthorizationCodeInstalledApp.class.getName());
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = new ArrayList<>();
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	/** Authorization code flow. */
	private final AuthorizationCodeFlow flow;

	/** Verification code receiver. */
	private final VerificationCodeReceiver receiver;

	/**
	 * @param flow     authorization code flow
	 * @param receiver verification code receiver
	 */
	public CredentialUtils(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
		this.flow = Preconditions.checkNotNull(flow);
		this.receiver = Preconditions.checkNotNull(receiver);
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		SCOPES.addAll(CalendarScopes.all());
		SCOPES.addAll(DocsScopes.all());

		// Load client secrets.
		InputStream in = CalendarServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		System.out.println("Getting access");
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new CredentialUtils(flow, receiver).authorize("user");
	}
	
	public String getCredentialsUrl(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		SCOPES.addAll(CalendarScopes.all());
		SCOPES.addAll(DocsScopes.all());

		// Load client secrets.
		InputStream in = CalendarServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		System.out.println("Getting access");
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return getLoginUrl("user");
	}

	public Credential authorize(String userId) throws IOException {
		try {
			Credential credential = flow.loadCredential(userId);
			if (credential != null && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
				return credential;
			}
			// open in browser
			String redirectUri = receiver.getRedirectUri();
			AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);
			onAuthorization(authorizationUrl);
			// receive authorization code and exchange it for an access token
			String code = receiver.waitForCode();
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
			// store credential and return it
			return flow.createAndStoreCredential(response, userId);
		} finally {
			receiver.stop();
		}
	}

	public String getLoginUrl(String userId) throws IOException {
		Credential credential = flow.loadCredential(userId);
		// open in browser
		String redirectUri = receiver.getRedirectUri();
		AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);
		return authorizationUrl.build();
	}

	protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
		browse(authorizationUrl.build());
	}

	public static void browse(String url) {
		Preconditions.checkNotNull(url);
		// Ask user to open in their browser using copy-paste
		System.out.println("Please open the following address in your browser:");
		System.out.println("  " + url);
		// Attempt to open it in the browser
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Action.BROWSE)) {
					System.out.println("Attempting to open that address in the default browser now...");
					desktop.browse(URI.create(url));
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Unable to open browser", e);
		} catch (InternalError e) {
			// A bug in a JRE can cause Desktop.isDesktopSupported() to throw an
			// InternalError rather than returning false. The error reads,
			// "Can't connect to X11 window server using ':0.0' as the value of the
			// DISPLAY variable." The exact error message may vary slightly.
			LOGGER.log(Level.WARNING, "Unable to open browser", e);
		}
	}

	/** Returns the authorization code flow. */
	public final AuthorizationCodeFlow getFlow() {
		return flow;
	}

	/** Returns the verification code receiver. */
	public final VerificationCodeReceiver getReceiver() {
		return receiver;
	}
}
