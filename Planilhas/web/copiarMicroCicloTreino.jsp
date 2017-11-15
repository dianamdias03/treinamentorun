<%@page import="treinos.MicroCicloTreinoCopiar"%>
<%@page import="org.json.JSONObject"%>
<%@page import="framework.RequestsParams"%>
<%

    boolean retorno;
    RequestsParams requestsParams = new RequestsParams();

    requestsParams.setParams(request);

    JSONObject copiar = requestsParams.getJsonRequest().getJSONObject("copiar");
    JSONObject colar = requestsParams.getJsonRequest().getJSONObject("colar");

    MicroCicloTreinoCopiar microCicloTreinoCopiar = new MicroCicloTreinoCopiar();
    retorno = microCicloTreinoCopiar.copiar(
            copiar.optInt("i_usuarios"),
            copiar.optString("inicio"),
            colar.optInt("i_usuarios"),
            colar.optString("inicio")
    );
    
    JSONObject dadosRetorno = new JSONObject();
    
    dadosRetorno.put("retorno", retorno);

    out.write(dadosRetorno.toString());
%>
