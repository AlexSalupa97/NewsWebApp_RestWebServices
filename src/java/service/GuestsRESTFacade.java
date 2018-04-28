/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.GuestsJpaController;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.naming.InitialContext;
import domain.Guests;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author oracle
 */
@Path("domain.guests")
public class GuestsRESTFacade {

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return (EntityManagerFactory) new InitialContext().lookup("java:comp/env/persistence-factory");
    }

    public GuestsJpaController getJpaController() {
        try {
            UserTransaction utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            return new GuestsJpaController(utx, getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GuestsRESTFacade() {
    }

    @POST
    @Consumes({"application/xml"})
    public Response create(Guests entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getIdGuest().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml"})
    public Response edit(Guests entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id) {
        try {
            getJpaController().destroy(id);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }
    
//    @PUT
//    @Path("{id}")
//    public Response edit(@PathParam("id") String id) {
//        try {
//            getJpaController().edit();
//            return Response.ok().build();
//        } catch (Exception ex) {
//            return Response.notModified(ex.getMessage()).build();
//        }
//    }

    @GET
    @Path("{id}")
    @Produces({"application/xml"})
    public Guests find(@PathParam("id") String id) {
        return getJpaController().findGuests(id);
    }

    @GET
    @Produces({"application/xml"})
    public List<Guests> findAll() {
        return getJpaController().findGuestsEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml"})
    public List<Guests> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findGuestsEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getGuestsCount());
    }
    
}
