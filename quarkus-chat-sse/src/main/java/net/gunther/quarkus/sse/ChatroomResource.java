package net.gunther.quarkus.sse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/chatroom")
public class ChatroomResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatroomResource.class);

	private Template chatroom;

	@Inject
	public ChatroomResource(Template chatroom) {
		this.chatroom = chatroom;
	}

	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance get(@QueryParam("name") String name) {
		LOGGER.info("{} enters chatroom");
		return chatroom.data("name", name);
	}
}
