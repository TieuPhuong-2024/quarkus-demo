package com.example.client;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@RegisterRestClient(configKey = "paypal")
@Path("/v1/catalogs/products")
public interface PayPalCatalogProductsClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> create(@HeaderParam("Authorization") String bearer,
                               Map<String, Object> request);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> get(@HeaderParam("Authorization") String bearer,
                            @PathParam("id") String id);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> list(@HeaderParam("Authorization") String bearer,
                             @QueryParam("page_size") Integer pageSize,
                             @QueryParam("page") Integer page,
                             @QueryParam("total_required") Boolean totalRequired);
}
