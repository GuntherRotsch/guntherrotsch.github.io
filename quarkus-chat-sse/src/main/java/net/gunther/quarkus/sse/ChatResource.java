package net.gunther.quarkus.sse;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Path("/chat")
public class ChatResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatResource.class);

	/*
	 * Because the Sse objects are thread-safe, they can after initialization (see
	 * ChatResource#setSse method) be used without synchronization.
	 */
	private SseBroadcaster sseBroadcaster;
	private Sse sse;

	/**
	 * Although the JAX/RS resource is @ApplicationScoped, the Context will be
	 * injected on every call. But the initialization of instance variables Sse and
	 * SseBroadcast has to be performed only once. Otherwise, the registrations of
	 * clients get lost on every request with the re-initialization.
	 * 
	 * @param sse the server-sent event context object
	 */
	@Context
	public synchronized void setSse(Sse sse) {
		if (this.sse != null) {
			return;
		}
		LOGGER.info("Set Context on ChatResource: {}", this);
		this.sse = sse;
		this.sseBroadcaster = sse.newBroadcaster();
		this.sseBroadcaster.onClose(eventSink -> LOGGER.info("On close EventSink: {}", eventSink));
		this.sseBroadcaster.onError(
				(eventSink, throwable) -> LOGGER.info("On Error EventSink: {}, Throwable: {}", eventSink, throwable));
	}

	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void register(@Context SseEventSink eventSink, @QueryParam("name") String name) {
		LOGGER.info("Registering user: {}", name);
		// TODO: Relate user (and session?) with event sink to be able to distinguish
		// chat participants later
		sseBroadcaster.register(eventSink);
		eventSink.send(sse.newEvent(String.format("Welcome, %s!", name)));
		broadcast(String.format("%s entered the chat room...", name));
	}

	@POST
	public void broadcast(String message) {
		LOGGER.debug("Received message from user: {}", message);
		OutboundSseEvent event = sse.newEventBuilder().data(message).reconnectDelay(10000).build();
		LOGGER.info("Publish event: {}, using Broadcaster: {}", event, sseBroadcaster);
		sseBroadcaster.broadcast(event);
	}
}
