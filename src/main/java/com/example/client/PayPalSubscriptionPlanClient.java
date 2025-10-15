package com.example.client;

import com.example.dto.plan.CreateSubscriptionPlanRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@RegisterRestClient(configKey = "paypal")
@Path("/v1/billing/plans")
public interface PayPalSubscriptionPlanClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> create(@HeaderParam("Authorization") String bearer,
                               CreateSubscriptionPlanRequest request);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Object> getAll(@HeaderParam("Authorization") String bearer);
}
