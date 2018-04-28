<%@page import="java.util.Random"%>
<%@page import="domain.Guests"%>
<%@page import="service.GuestsRESTFacade"%>
<%@page import="domain.Autori"%>
<%@page import="service.AutoriRESTFacade"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>

<%@page import="java.util.Calendar"%>
<%@page import="javax.persistence.TemporalType"%>
<%@page import="java.sql.Connection"%>
<%@page import="service.UseriRESTFacade"%>
<%@page import="java.net.URI"%>
<%@page import="javax.ws.rs.core.Response"%>
<%@page import="com.sun.jersey.api.client.ClientResponse"%>
<%@page import="com.sun.jersey.core.util.MultivaluedMapImpl"%>
<%@page import="javax.ws.rs.core.MultivaluedMap"%>
<%@page import="java.sql.*" %>
<%@page import="java.util.Date" %>
<%@page import="java.sql.Statement" %>
<%@page import="com.sun.jersey.api.client.Client" %>
<%@page import="com.sun.jersey.api.client.WebResource" %>
<%@page import="controller.UseriJpaController" %>
<%@page import="domain.Useri" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inserting Data into DB</title>


    </head>
    <body>
        <h1>Inserting Data into DB!</h1>
        <form name="myForm" action="index.jsp" method="POST">
            <table border="0">

                <tbody>

                    <tr>
                        <td>USERNAME:</td>
                        <td><input type="text" name="nume_user" value="" size="50" /></td>
                    </tr>
                    <tr>
                        <td>PASSWORD:</td>
                        <td><input type="text" name="parola_user" value="" size="50" /></td>
                    </tr>

                    <tr>
                        <td>E-MAIL:</td>
                        <td><input type="text" name="email_user" value="" size="50" /></td>
                    </tr>


                </tbody>

            </table>



            <input type="submit" value="Submit" name="submit_user" />
            <input type="reset" value="Clear" name="clear" />

            <table border="0">
                <tbody>

                <br />
                <br />
                <br />
                <br />
                <br />


                <tr>
                    <td>NUME AUTOR: </td>
                    <td><input type="text" name="nume_autor" value="" size="48" /></td>
                </tr>
                <tr>
                    <td>PRENUME AUTOR: </td>
                    <td><input type="text" name="prenume_autor" value="" size="48" /></td>
                </tr>

                <tr>
                    <td>E-MAIL AUTOR:</td>
                    <td><input type="text" name="email_autor" value="" size="48" /></td>
                </tr>
                <tr>
                    <td>PAROLA AUTOR:</td>
                    <td><input type="text" name="parola_autor" value="" size="48" /></td>
                </tr>
                <tr>
                    <td>ADRESA AUTOR:</td>
                    <td><input type="text" name="adresa_autor" value="" size="48" /></td>
                </tr>




                </tbody>

            </table>
            <input type="submit" value="Submit" name="submit_autor" />
            <input type="reset" value="Clear" name="clear_autor" />

            <table border="0">

                <tbody>
                <br />
                <br />
                <br />
                <br />
                <br />
                <br />


                </tbody>

            </table>



            <input type="submit" value="Continue as guest" name="submit_guest" />




        </form>

    </body>
</html>

<%

    if (request.getParameter("submit_user") != null) {

        short id = 1;

        UseriRESTFacade urfGET = new UseriRESTFacade();
        List<Useri> useri = urfGET.getJpaController().findUseriEntities();

        for (Useri u : useri) {
            if (u.getIdUser() == id) {
                id++;
            }
        }

        short iTip = 1;
        short stare = 1;

        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:7001/ProiectJavaWeb_bun_1/webresources/domain.useri");

        Useri u = new Useri();

        String nUser = new String();
        String nPass = new String();
        String nEmail = new String();
        
            nUser = request.getParameter("nume_user");
        
        if (request.getParameter("parola_user") != null) {
            nPass = request.getParameter("parola_user");
        }
        if (request.getParameter("email_user") != null) {
            nEmail = request.getParameter("email_user");
        }

        try {
            u.setIdUser(id);
            u.setNumeUser(nUser);
            u.setParolaUser(nPass);
            u.setEmailUser(nEmail);
            u.setTipUser(iTip);
            u.setStareUseri(stare);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            u.setDataCreare(date);
            u.setUltimaActivitate(date);

            System.out.println("set done");
        } catch (Exception ex) {
            System.out.println("set failed");
        }

        UseriRESTFacade urfPOST = new UseriRESTFacade();

        try {
            urfPOST.getJpaController().create(u);
            Response r = Response.created(URI.create(u.getIdUser().toString())).build();

        } catch (Exception ex) {

            System.out.println("nu");
            Response r = Response.notModified(ex.getMessage()).build();
        }

    }
%>

<%
    if (request.getParameter("submit_autor") != null) {

        short id = 1;

        AutoriRESTFacade arfGET = new AutoriRESTFacade();
        List<Autori> autori = arfGET.getJpaController().findAutoriEntities();

        for (Autori a : autori) {
            if (a.getIdAutor() == id) {
                id++;
            }
        }

        short stare = 1;

        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:7001/ProiectJavaWeb_bun_1/webresources/domain.useri");

        Autori a = new Autori();

        String nNume = new String();
        String nPrenume = new String();
        String nEmail = new String();
        String nPass = new String();
        String nAdresa = new String();
        if (request.getParameter("nume_autor") != null) {
            nNume = request.getParameter("nume_autor");
        }
        if (request.getParameter("prenume_autor") != null) {
            nPrenume = request.getParameter("prenume_autor");
        }
        if (request.getParameter("email_autor") != null) {
            nEmail = request.getParameter("email_autor");
        }
        if (request.getParameter("parola_autor") != null) {
            nPass = request.getParameter("parola_autor");
        }
        if (request.getParameter("adresa_autor") != null) {
            nAdresa = request.getParameter("adresa_autor");
        }

        try {
            a.setIdAutor(id);
            a.setNumeAutor(nNume);
            a.setPrenumeAutor(nPrenume);
            a.setEmailAutor(nEmail);
            a.setParolaAutor(nPass);
            a.setAdresaAutor(nAdresa);
            a.setStareAutor(stare);

            System.out.println("set done");
        } catch (Exception ex) {
            System.out.println("set failed");
        }

        AutoriRESTFacade arfPOST = new AutoriRESTFacade();

        try {
            arfPOST.getJpaController().create(a);
            Response r = Response.created(URI.create(a.getIdAutor().toString())).build();
            System.out.println("da");

        } catch (Exception ex) {

            System.out.println("nu");
            Response r = Response.notModified(ex.getMessage()).build();
        }

    }
%>

<%
    if (request.getParameter("submit_guest") != null) {

        String id = "";

        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        id = sb.toString();
        System.out.println(id);

        GuestsRESTFacade grfGET = new GuestsRESTFacade();
        List<Guests> guests = grfGET.getJpaController().findGuestsEntities();

        for (Guests g : guests) {
            if (g.getIdGuest() == id) {

                for (int i = 0; i < 10; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                id = sb.toString();
                System.out.println(id);

            }
        }

        short stare = 1;

        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:7001/ProiectJavaWeb_bun_1/webresources/domain.useri");

        Guests g = new Guests();

        try {
            g.setIdGuest(id);
            g.setStareGuest(stare);

            System.out.println("set done");
        } catch (Exception ex) {
            System.out.println("set failed");
        }

        GuestsRESTFacade grfPOST = new GuestsRESTFacade();

        try {
            grfPOST.getJpaController().create(g);
            Response r = Response.created(URI.create(g.getIdGuest().toString())).build();
            System.out.println("da");

        } catch (Exception ex) {

            System.out.println("nu");
            Response r = Response.notModified(ex.getMessage()).build();
        }

    }
%>