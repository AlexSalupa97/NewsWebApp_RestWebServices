<%-- 
    Document   : index
    Created on : Mar 17, 2018, 2:02:09 AM
    Author     : oracle
--%>





<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="domain.PozeStiri"%>
<%@page import="service.PozeStiriRESTFacade"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="javax.xml.bind.DatatypeConverter"%>

<%@page import="com.sun.jersey.core.util.Base64"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="java.io.OutputStream"%>
<%@page import="domain.Poze"%>
<%@page import="service.PozeRESTFacade"%>
<%@page import="javax.swing.ImageIcon"%>
<%@page import="service.CategoriiRESTFacade"%>
<%@page import="domain.Categorii"%>
<%@page import="domain.Stiri"%>
<%@page import="service.StiriRESTFacade"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>

<%@page import="java.net.URL"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.IOException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>stiri.grupa4.ro</title>
    </head>

    <%-- design --%>
    <style>
        .wrap {
            width: 100%;
            overflow:auto;
            display: flex;

            margin-top: 1px;
        }

        .fleft {
            float:left; 
            background: beige;
            width: 9%;
            position: relative;

            border-right: 10px solid #333;

        }

        .fright {
            float: right;
            background:none;
            position: relative;

            width: 90%;
        }

        .navbar {
            overflow:hidden;
            background-color: #333;
            position: relative;
            top: 0;
            width: 100%;
        }

        .navbar a {
            float: left;
            display: block;
            color: #f2f2f2;
            text-align: center;
            padding: 14px 16px;
            text-decoration: none;
            font-size: 17px;
        }

        .navbar a:hover {

            color: red;
        }

        .main {
            padding: 16px;
            margin-top: 30px;
            height: 1500px; /* Used in this example to enable scrolling */
        }
        body {margin:0;}

        p{
            margin: 35px;
        }

    </style>

    <%-- body --%>
    <body>

        <%-- <form action="tester.jsp" method="GET">
             <input type="hidden" name="inputName" value="afisare">
             <input type="submit">  

        </form>
        --%>




        <div class="navbar">
            <a href="index.jsp">Home</a>

            <a href="#contact">Contact</a>
            <a href="#login">Login</a>
            <a href="signupjsp.jsp">Signup</a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a></a>
            <a style="text-align:right">current user</a>
        </div>

        <div class="wrap">
            <div class="fleft">
                <%!  String afisareCategorii() {
                        CategoriiRESTFacade crfGET = new CategoriiRESTFacade();
                        List< Categorii> categorii = crfGET.getJpaController().findCategoriiEntities();
                        StringBuilder sb = new StringBuilder();
                        for (Categorii c : categorii) {

                            //sb.append("<div>");
                            sb.append("&emsp;");
                            sb.append("&emsp;");
                            sb.append("<a href=\"categoriijsp.jsp?idCategorie=" + c.getIdCategorie() + "\"style=\"text-decoration: none;color:black\"");
                            sb.append("<b>" + c.getNumeCategorie() + "</b>");
                            sb.append("</a>");
                            sb.append("<hr>");

                            //sb.append("</div>");
                        }
                        //System.out.println(sb.toString());
                        return sb.toString();

                    }
                %>

                <%=afisareCategorii()%>


            </div>
            <div class="fright">
                <%!  String afisareTitluStiri() throws IOException {

                        PozeStiriRESTFacade psrfGET = new PozeStiriRESTFacade();
                        List<PozeStiri> pozeStiri = psrfGET.getJpaController().findPozeStiriEntities();

                        short max = 0;

                        for (PozeStiri ps : pozeStiri) {
                            if (ps.getIdStire().getIdStire() > max) {
                                max = ps.getIdStire().getIdStire();
                            }

                        }
                        int[] stireDejaAfisata = new int[max];
                        for (PozeStiri ps : pozeStiri) {
                            stireDejaAfisata[ps.getIdStire().getIdStire() - 1] = 0;

                        }
                        for (int i = 0; i < max; i++) {
                            //System.out.print(stireDejaAfisata[i] + " ");
                        }

                        StringBuilder sb = new StringBuilder();
                        for (PozeStiri ps : pozeStiri) {

                            if (stireDejaAfisata[ps.getIdStire().getIdStire() - 1] == 0) {

                                String encoded = DatatypeConverter.printBase64Binary(ps.getIdPoza().getLinkPoza());

                                sb.append("<p>");

                                sb.append("<font size=\"6\">");
//                                sb.append("<form action=\"stiri.jsp\" method=\"GET\">");
//                                sb.append("<input type=\"hidden\" name=\"idStire\" value=\"ps.getIdStire().getidStire()\">");
//                                sb.append("<input type=\"submit\">");
//                                sb.append("</form>");
                                sb.append("<a href=\"stirijsp.jsp?idStire=" + ps.getIdStire().getIdStire() + "\" style=\"text-decoration: none;color:black\"");
                                sb.append("<b>" + ps.getIdStire().getTitlu() + "</b>");
                                sb.append("</a>");
                                sb.append("</font>");
                                sb.append("<br><br><br>");

                                OutputStream out = null;

                                try {
                                    out = new BufferedOutputStream(new FileOutputStream("/home/oracle/Desktop/proiect/ProiectJavaWeb_bun_2/web/" + "img" + ps.getIdPoza().getIdPoza() + ".jpeg"));
                                    out.write(ps.getIdPoza().getLinkPoza());
                                    //System.out.println("creata");
                                } finally {
                                    if (out != null) {
                                        out.close();
                                    }
                                }
                                sb.append("<img src=\"img" + ps.getIdPoza().getIdPoza() + ".jpeg\"style=\"width:350px;height:200px;\"/>");

                                sb.append("<br><br><br>");

                                sb.append(ps.getIdStire().getContinutStire().substring(0, ps.getIdStire().getContinutStire().length() / 5) + "[...]");
                                sb.append("<hr>");
                                sb.append("</p>");

                                stireDejaAfisata[ps.getIdStire().getIdStire() - 1] = 1;
                            }
                        }

                        //System.out.println(sb.toString());
                        return sb.toString();

                    }
                %>

                <%=afisareTitluStiri()%>
            </div>

        </div>




    </body>
</html>


