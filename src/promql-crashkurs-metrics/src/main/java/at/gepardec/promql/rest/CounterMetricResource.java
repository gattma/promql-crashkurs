package at.gepardec.promql.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/counter")
public class CounterMetricResource {

    private final Counter counter;

    CounterMetricResource(MeterRegistry registry) {
        this.counter = Counter.builder("promql.counter")
                .description("Calls of countUp Method")
                .register(registry);
    }

    @GET
    @Path("/echo/{msg}")
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(String msg) {
        counter.increment();
        return msg;
    }
}
