<%-- 
    Document   : TempEnvioEmails
    Created on : 23/10/2017, 11:42:40
    Author     : U0180759
--%>

<%@page import="treinos.ConsultasSQL"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ConsultasSQL consultasSQL = new ConsultasSQL();
    int lista = 0;

    if (request.getParameter("lista") != null) {
        lista = Integer.parseInt(request.getParameter("lista"));
    }

    if (lista == 1) {
        out.println(consultasSQL.treinosConcluidos().toString());
    } else if (lista == 2) {
        
        String i_clientes=request.getParameter("i_clientes");
        String i_usuarios=request.getParameter("i_usuarios");
        String inicio=request.getParameter("inicio");
        
        if(i_clientes!=null && i_usuarios!=null && inicio!=null){
            out.println(
                    consultasSQL.planilhaSemanaAtleta(
                            Integer.parseInt(i_clientes), 
                            Integer.parseInt(i_usuarios), 
                            inicio
                    ).toString()
            );
            
        }
        
        
    } else {
        out.println("{\"Resultado\":\"ok\"}");
    }
%>

