<%@page import="treinos.GravarMicroCicloTreinos"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="framework.RsJson"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="framework.Conexao"%>
<%

    GravarMicroCicloTreinos gravar = new GravarMicroCicloTreinos(out);

    gravar.requestParams(request);

    boolean retorno = gravar.update();

    JSONObject jsonRetorno = new JSONObject();
    jsonRetorno.put("resultado", retorno);
    jsonRetorno.put("params", "");
    
    out.print(jsonRetorno.toString());

%>