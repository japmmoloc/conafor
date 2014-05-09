<%-- 
    Document   : Asesores
    Created on : 29-abr-2014, 14:21:30
    Author     : jose
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="con.aplic.modelo.Asesor" %>   
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="arrAser" type="Asesor[]" scope="request" /> 

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        Hola
       <c:forEach items="${arrAser}" var="oAser">
            ${oAser.nomAsesor}
            
        </c:forEach> 
    </body>
</html>
