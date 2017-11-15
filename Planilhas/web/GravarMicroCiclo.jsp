<%@page import="treinos.MicroCiclos"%>
<%@page import="framework.RequestsParams"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    
    RequestsParams requestsParams = new RequestsParams();
    requestsParams.setParams(request);
    
    MicroCiclos microCiclos = new MicroCiclos(requestsParams);
    out.write(microCiclos.doAction().toString());
    
%>