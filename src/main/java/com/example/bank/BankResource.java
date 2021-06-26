/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bank;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.util.function.Function;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BankResource {

    @Inject
    PgPool pgClient;

    @GET
    @Path("client/{id}/balance")
    public Uni<Response> getBalance(@PathParam Integer id) {
        return Bank.getBalance(pgClient, id)
                .onItem().transform(toHttpOk())
                .onItem().transform(ResponseBuilder::build);
    }

    @POST
    @Path("client/new/{balance}")
    public Uni<Response> createClient(@PathParam Integer balance) {
        return Bank.createClient(pgClient, new Client(0, "", "", ""))
                .onItem().call(client -> Bank.createTransaction(pgClient, new Transaction(0, 0, client.getId(), balance)))
                .onItem().transform(toHttpOk())
                .onItem().transform(ResponseBuilder::build);
    }


    @POST
    @Path("transaction")
    public Uni<Response> createClient(Transaction transaction) {
        return Bank.createTransaction(pgClient, transaction)
                .onItem().transform(toHttpOk())
                .onItem().transform(ResponseBuilder::build);
    }

    private <T> Function<T, ResponseBuilder> toHttpOk() {
        return r -> r != null ? Response.ok(r) : Response.status(Status.NOT_FOUND);
    }
}
