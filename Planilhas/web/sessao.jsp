<%@page import="framework.SessaoUsuario"%>
<%@page import="org.json.JSONObject"%>
<%
    SessaoUsuario sessaoUsuario = new SessaoUsuario(session);
    JSONObject jsonRetorno = sessaoUsuario.getUsuarios();
    out.print(jsonRetorno.toString());
%>
