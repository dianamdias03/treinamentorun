<%-- 
    Document   : teste_json
    Created on : 17/10/2017, 08:49:06
    Author     : U0180759
--%>

<%@page import="org.json.JSONObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
            JSONObject obj = new JSONObject();
            obj.put("teste", true);
            out.print(obj.toString());
        %>
    </body>
</html>
