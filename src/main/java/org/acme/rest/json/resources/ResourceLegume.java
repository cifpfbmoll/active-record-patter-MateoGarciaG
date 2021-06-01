package org.acme.rest.json.resources;

import org.acme.rest.json.entities.Fruit;
import org.acme.rest.json.entities.Legume;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/legumes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResourceLegume {

    private Set<Legume> legumes = Collections.synchronizedSet(new LinkedHashSet<>());

    public ResourceLegume() {
        legumes.add(new Legume("Carrot", "Root vegetable, usually orange"));
        legumes.add(new Legume("Zucchini", "Summer squash"));
    }

    @GET
    public Response list() {
        return Response.ok(legumes).build();
    }

    @POST
    public Response add(Legume legume) {
        legumes.add(legume);
        return Response.ok(legumes).build();
    }

    @DELETE
    public Response delete(Fruit fruit) {
        legumes.removeIf(existingLegume -> existingLegume.name.contentEquals(fruit.name));
        return Response.ok(legumes).build();
    }
}