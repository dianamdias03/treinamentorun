<%@page import="org.json.JSONObject"%>
<%@page import="treinos.EnviarTreinoEmail"%>
<%
    
    EnviarTreinoEmail enviarTreinoEmail = new EnviarTreinoEmail(null);
    JSONObject conteudo = enviarTreinoEmail.getHtmlTreino(1, 4, "2017-10-09");
    out.write(conteudo.optString("conteudo"));
%>
