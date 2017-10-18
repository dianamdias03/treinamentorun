<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
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

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                System.out.println("Erro carregando a classe");
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLException: " + ex.toString());
            }

            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/treinos?user=root&password=FUOKs3BD_");

                String sql = "select descricao from treinosPreCadastrados order by 1;";

                Statement stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String descricao = rs.getString("descricao");
                    out.print(descricao);
                }

            } catch (SQLException ex) {
                out.println("Erro conectando");
                out.println("SQLException: x " + ex.getMessage());
                out.println("SQLState: " + ex.getSQLState());
                out.println("VendorError: " + ex.getErrorCode());
            }


        %>
    </body>
</html>
