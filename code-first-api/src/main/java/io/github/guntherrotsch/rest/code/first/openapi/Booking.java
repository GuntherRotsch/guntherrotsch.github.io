package io.github.guntherrotsch.rest.code.first.openapi;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Booking")
public class Booking {
    @Schema(required = true, description = "Booking id")
    private String id;
    @Schema(required = true, description = "Description of the destination")
    private Destination destination;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public static Booking booking(String id, Destination destination) {
        Booking result = new Booking();
        result.setId(id);
        result.setDestination(destination);
        return result;
    }
}
