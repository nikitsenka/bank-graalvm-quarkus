package org.my.group.resteasyjackson;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Path("/hello")
public class GreetingResource {

    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> doSomethingAsync() {
        // Mimic an asynchronous computation.
        return Uni.createFrom()
                .item(() -> "Hello!")
                .onItem().delayIt().by(Duration.ofMillis(10));
    }
}
