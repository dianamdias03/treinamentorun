<%@page import="org.json.JSONObject"%>
<%@page import="treinos.GravarMicroCicloTreinos"%>
<%

    boolean retorno;
    long lastID=0;
    JSONObject jsonRetorno = new JSONObject();

    GravarMicroCicloTreinos gravar = new GravarMicroCicloTreinos(out);

    gravar.requestParams(request);

    if (gravar.isCreateNewRecord()) {
        jsonRetorno.put("registro", gravar.newRecord());
        retorno = true;
    } else {
        retorno = gravar.update();
        lastID = gravar.lastID;
    }

    jsonRetorno.put("resultado", retorno);
    jsonRetorno.put("params", lastID);
    jsonRetorno.put("novoCodigo", lastID);

    out.print(jsonRetorno.toString());

%>