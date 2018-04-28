<%-- 
    Document   : tester
    Created on : Apr 23, 2018, 3:34:31 PM
    Author     : oracle
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% String hidden = (String) request.getParameter("inputName");%>
        <%=hidden%>
    </body>
</html>
