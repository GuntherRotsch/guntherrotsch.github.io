package net.gunther.blog.codegen;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

@RequestScoped
public class JAXRSContext {

	private UriInfo uriInfo;
	private HttpHeaders httpHeaders;

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}
}
