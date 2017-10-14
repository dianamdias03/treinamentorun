/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.FormatacaoDatas;
import framework.RequestsAction;
import java.sql.Date;
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

        Arquivo.gravarLog("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + retorno.toString());

        return retorno;
    }

}