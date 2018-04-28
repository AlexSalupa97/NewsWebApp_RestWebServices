/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.RandCuvinteCheieJpaController;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.naming.InitialContext;
import domain.RandCuvinteCheie;
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
@Path("domain.randcuvintecheie")
public class RandCuvinteCheieRESTFacade {

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return (EntityManagerFactory) new InitialContext().lookup("java:comp/env/persistence-factory");
    }

    private RandCuvinteCheieJpaController getJpaController() {
        try {
            UserTransaction utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            return new RandCuvinteCheieJpaController(utx, getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public RandCuvinteCheieRESTFacade() {
    }

    @POST
    @Consumes({"application/xml"})
    public Response create(RandCuvinteCheie entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getIdRandCuvantCheie().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/xml"})
    public Response edit(RandCuvinteCheie entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Short id) {
        try {
            getJpaController().destroy(id);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml"})
    public RandCuvinteCheie find(@PathParam("id") Short id) {
        return getJpaController().findRandCuvinteCheie(id);
    }

    @GET
    @Produces({"application/xml"})
    public List<RandCuvinteCheie> findAll() {
        return getJpaController().findRandCuvinteCheieEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/xml"})
    public List<RandCuvinteCheie> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findRandCuvinteCheieEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getRandCuvinteCheieCount());
    }
    
}
