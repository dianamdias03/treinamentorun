<%@page import="framework.GerenciaRequests"%>
<%
    GerenciaRequests gerenciaRequests = new GerenciaRequests();
    out.print(gerenciaRequests.doRequest(request));
%>