<%@page import="framework.RsJson"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="framework.Conexao"%>
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
            out.println("Your IP address is " + request.getRemoteAddr());

            Conexao conexao = new Conexao();
            conexao.conectar();

            RsJson rsJson = new RsJson();
            rsJson.setConexao(conexao);
            String str = rsJson.getJsonBySQLStr("select i_tipos_treinos, i_clientes, descricao from tipos_treinos order by 1, 2");
            
            out.print(str);

        %>
    </body>
</html>
