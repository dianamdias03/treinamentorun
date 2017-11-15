<%-- 
    Document   : setup
    Created on : 17/10/2017, 22:37:40
    Author     : U0180759
--%>

<%@page import="treinos.MicroCicloSetup"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Gerando Micro Ciclos</h1>
        <%
            MicroCicloSetup microCicloSetup = new MicroCicloSetup();
            microCicloSetup.gerarPeriodos(out,0);
        %>

    </body>
</html>
