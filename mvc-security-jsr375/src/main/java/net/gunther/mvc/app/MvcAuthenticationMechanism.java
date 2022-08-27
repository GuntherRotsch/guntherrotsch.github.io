package net.gunther.mvc.app;

import org.glassfish.soteria.identitystores.annotation.Credentials;
import org.glassfish.soteria.identitystores.annotation.EmbeddedIdentityStoreDefinition;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EmbeddedIdentityStoreDefinition({
	  @Credentials(callerName = "gunther", password = "secret", groups = {"user"}),
	  @Credentials(callerName = "gunther_admin", password = "topsecret", groups = {"user","admin"})
	})
@AutoApplySession
@FormAuthenticationMechanismDefinition(

		loginToContinue = @LoginToContinue(

				loginPage = "/login.html", errorPage = "/login-error.html"))

@ApplicationScoped
public class MvcAuthenticationMechanism implements HttpAuthenticationMechanism {

	@Inject
	private IdentityStore identityStore;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {
		Credential credential = httpMessageContext.getAuthParameters().getCredential();

		if (credential != null) {
			return httpMessageContext.notifyContainerAboutLogin(identityStore.validate(credential));
		} else {
			return httpMessageContext.doNothing();
		}
	}

	public Boolean isRememberMe(HttpMessageContext httpMessageContext) {
		return httpMessageContext.getAuthParameters().isRememberMe();
	}
}
