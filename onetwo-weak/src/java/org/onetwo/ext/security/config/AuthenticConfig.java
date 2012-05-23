package org.onetwo.ext.security.config;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.ext.security.Authenticator;

public class AuthenticConfig {

	public static AuthenticConfig DEFAULT_CONIFG = new AuthenticConfig(false);

	private String name;
	private boolean needAuthencation;

	private List<String> members;

	private List<Authenticator> authenticators;

	private boolean onlyAuthenticator;

	private AuthenticConfig(boolean needAuthencation) {
		this.needAuthencation = needAuthencation;
		if (needAuthencation) {
			this.members = new ArrayList<String>(5);
			this.authenticators = new ArrayList<Authenticator>(5);
		}
	}

	public AuthenticConfig(String name, boolean onlyAuthenticator) {
		this(true);
		this.name = name;
		this.onlyAuthenticator = onlyAuthenticator;
	}

	public String getName() {
		return name;
	}

	public boolean isNeedAuthencation() {
		return needAuthencation;
	}

	public void setNeedAuthencation(boolean needAuthencation) {
		this.needAuthencation = needAuthencation;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public AuthenticConfig addMember(String member) {
		this.members.add(member);
		return this;
	}

	public boolean isOnlyAuthenticator() {
		return onlyAuthenticator;
	}

	public List<Authenticator> getAuthenticators() {
		return authenticators;
	}

	public void addAuthenticator(Authenticator authenticator) {
		this.authenticators.add(authenticator);
	}
}
