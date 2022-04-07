package net.gunther.jsr375.rest.api;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition
@DeclareRoles({"users","admins"})
public class RestApiConfig {
}
