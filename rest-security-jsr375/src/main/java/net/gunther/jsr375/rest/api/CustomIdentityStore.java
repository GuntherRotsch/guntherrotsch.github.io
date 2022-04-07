package net.gunther.jsr375.rest.api;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

public class CustomIdentityStore implements IdentityStore {

	@Override
	public CredentialValidationResult validate(Credential credential) {
		CredentialValidationResult result = NOT_VALIDATED_RESULT;
		if (credential instanceof UsernamePasswordCredential) {
			UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;

			if ("gunther".equals(usernamePassword.getCaller())
					&& "secret".equals(usernamePassword.getPasswordAsString())) {
				result = new CredentialValidationResult("gunther", new HashSet<>(asList("users")));
			} else if ("gunther_admin".equals(usernamePassword.getCaller())
					&& "topsecret".equals(usernamePassword.getPasswordAsString())) {
				result = new CredentialValidationResult("gunther_admin", new HashSet<>(asList("users", "admins")));
			} else {
				result = INVALID_RESULT;
			}
		}
		return result;
	}

	@Override
	public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
		if ("gunther".equals(validationResult.getCallerPrincipal())) {
			return new HashSet<>(asList("users"));
		} else if ("gunther_admin".equals(validationResult.getCallerPrincipal())) {
			return new HashSet<>(asList("users", "admins"));
		}
		return emptySet();
	}

	@Override
	public int priority() {
		return 50;
	}

	@Override
	public Set<ValidationType> validationTypes() {
		return new HashSet<>(asList(PROVIDE_GROUPS, VALIDATE));
	}
}