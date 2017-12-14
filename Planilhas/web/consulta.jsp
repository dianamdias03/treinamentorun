<%@page import="treinos.GravarMicroCicloTreinos"%>
<%@page import="treinos.Usuarios"%>
<%@page import="treinos.Grupos"%>
<%@page import="treinos.MeusDados"%>
<%@page import="treinos.EventosParticipantes"%>
<%@page import="org.json.JSONObject"%>
<%@page import="treinos.ConsultasSQL"%>
<%@page import="framework.Arquivo"%>
<%@page import="framework.RequestsParams"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%
    JSONObject listaRetorno = null;
    RequestsParams requestsParams = new RequestsParams();

    requestsParams.setParams(request);

//    Arquivo.gravarLog("consulta.jsp: "+requestsParams.getJsonRequest().toString());

    String consulta = requestsParams.getJsonRequest().optString("consulta", "");
    if (consulta.equals("participantes")) {
        ConsultasSQL consultaSQL = new ConsultasSQL();
        listaRetorno = consultaSQL.participantesEvento(
                requestsParams.getJsonRequest().optInt("i_eventos", 0),
                0/*requestsParams.getJsonRequest().optInt("i_usuarios", 0)*/
        );
    }

    if (consulta.equals("participantesGravar")) {
        EventosParticipantes eventosParticipantes = new EventosParticipantes();
        listaRetorno = eventosParticipantes.Gravar(requestsParams);
    }

    if (consulta.equals("usuarios")) {
        ConsultasSQL consultaSQL = new ConsultasSQL();
        listaRetorno = consultaSQL.dadosUsuario(requestsParams);
    }

    if (consulta.equals("meusdados-gravar")) {
        MeusDados meusDados = new MeusDados();
        listaRetorno = meusDados.gravar(requestsParams);
    }

    if (consulta.equals("grupos")) {
        ConsultasSQL consultaSQL = new ConsultasSQL();
        listaRetorno = consultaSQL.grupos(requestsParams);
    }

    if (consulta.equals("grupos-gravar-atleta")) {
        Usuarios usuarios = new Usuarios();
        listaRetorno = usuarios.gravarGrupo(requestsParams);
    }

    if (consulta.equals("treinosPreCadastradosUsuario")) {
        ConsultasSQL consultaSQL = new ConsultasSQL();
        listaRetorno = consultaSQL.treinosPreCadastradosUsuario(requestsParams);
    }

    if (consulta.equals("treinosPreCadastrados")) {
        ConsultasSQL consultaSQL = new ConsultasSQL();
        listaRetorno = consultaSQL.treinosPreCadastrados(requestsParams);
    }

    if (consulta.equals("treino-fixos")) {
        GravarMicroCicloTreinos gravarMicroCicloTreinos = new GravarMicroCicloTreinos();
        listaRetorno = gravarMicroCicloTreinos.newTreinoFixo(requestsParams);
    }

    if (listaRetorno == null) {
        listaRetorno = new JSONObject();
        listaRetorno.put("resultado", false);
        listaRetorno.put("msgErro", "Consulta '"+consulta+"' inválida!");
        Arquivo.gravarLog("Consulta '"+consulta+"' inválida! "+requestsParams.getJsonRequest().toString());
    }

    out.print(listaRetorno.toString());

%>