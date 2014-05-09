<%-- 
    Document   : error
    Created on : 25/09/2013, 01:50:25 PM
    Author     : Any
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
          <h1> ERROR:</h1> <br> <br>
        <%=request.getParameter("sError")%>
        <br/>
        <a href="index.jsp">Regresar</a>
    </body>
</html>