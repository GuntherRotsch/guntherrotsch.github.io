package io.github.guntherrotsch.simpledi.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import io.github.guntherrotsch.simpledi.RequestScoped;

public class MessageCollector {

	private List<String> messages = new ArrayList<>();
	
	public static Supplier<MessageCollector> supplier() {
		return RequestScoped.of(MessageCollector::new, MessageCollector.class);
	}

	public void putMessage(String message) {
		System.out.println("MessageCollector#putMessage called from "+Thread.currentThread().getName());
		messages.add(message);
	}
	
	public List<String> getMessages(){
		return Collections.unmodifiableList(messages);
	}
}
