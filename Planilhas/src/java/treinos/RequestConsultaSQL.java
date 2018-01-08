/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.FormatacaoDatas;
import framework.RequestsAction;
import org.json.JSONObject;

public class RequestConsultaSQL extends RequestsAction {

    public RequestConsultaSQL(JSONObject params) {
        super(params);
    }

    @Override
    public JSONObject doAction() {

        JSONObject retorno = new JSONObject();

        if (getParams().optString("consulta").equals("planilhaSemanal")) {
            retorno = consultaPlanilhaSemanal();
//        } else if (getParams().optString("consulta").equals("treinosPreCadastrados")) {
//            ConsultasSQL consultasSQL = new ConsultasSQL();
//            retorno = consultasSQL.treinosPreCadastrados();
        } else if (getParams().optString("consulta").equals("criarEventos")) {
            ConsultasSQL consultasSQL = new ConsultasSQL();
            retorno = consultasSQL.criarEventos();
        }
        return retorno;
    }

    public JSONObject consultaPlanilhaSemanal() {

        JSONObject retorno = new JSONObject();
        ConsultasSQL consultasSQL = new ConsultasSQL();
        int i_clientes = getParams().optInt("i_clientes");
        int i_usuarios = getParams().optInt("i_usuarios");
        int navegacao = getParams().optInt("navegacao");
        String dia = getParams().optString("dia");

        if (dia.equals("")) {
            FormatacaoDatas formatacaoDatas = new FormatacaoDatas();
            formatacaoDatas.setCurrentDate();
            formatacaoDatas.addDia(-formatacaoDatas.getDiaSemanaN() + 2);
            dia = formatacaoDatas.getDataYMD();
        }

        FormatacaoDatas formatacaoDatas = new FormatacaoDatas(FormatacaoDatas.ymdToDate(dia));

        if (navegacao == -1) {
            formatacaoDatas.addDia(-7);
        } else if (navegacao == 1) {
            formatacaoDatas.addDia(7);
        }

        JSONObject dados = consultasSQL.planilhaSemanaAtleta(i_clientes, i_usuarios, formatacaoDatas.getDataYMD());

        retorno.put("dados", dados);
        retorno.put("dia", formatacaoDatas.getDataYMD());
        retorno.put("getParams", getParams());
        retorno.put("Acao", "Consulta SQL");

        return retorno;
    }

}
