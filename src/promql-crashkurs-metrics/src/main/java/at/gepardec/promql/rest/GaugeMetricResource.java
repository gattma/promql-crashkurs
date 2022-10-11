package at.gepardec.promql.rest;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/gauge")
public class GaugeMetricResource {

    List<String> names = new ArrayList<>();

    GaugeMetricResource(MeterRegistry registry) {
        registry.gaugeCollectionSize("promql.gauge", Tags.empty(), names);
    }

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String add(String name) {
        names.add(name);
        return name;
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String remove(String name) {
        names.remove(name);
        return name;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> get() {
        return names;
    }
}
