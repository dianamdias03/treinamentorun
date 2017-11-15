/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.GerenciaRequests;
import framework.RegistroJson;
import framework.Tabela;
import org.json.JSONObject;

public class Grupos extends framework.Gravar {

    @Override
    public void defineTabela() {

//        Arquivo.gravarLog(this.getGerenciaRequests().toString());
//        Arquivo.gravarLog(this.getGerenciaRequests().getNodeParams().toString());

        JSONObject jsonDados = this.getGerenciaRequests().getNodeParams().getJSONObject("dados");

        setTabela(new Tabela("grupos_atletas"));

        getTabela().addColunaI("i_grupos_atletas", jsonDados.optInt("codigo", 0), true);
        getTabela().addColunaI("i_clientes", jsonDados.optInt("i_clientes", 0));
        getTabela().addColunaS("nome", jsonDados.optString("nome", ""));
    }

    public static void main(String[] args) {
        
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonDados = new JSONObject();
        JSONObject jsonParam = new JSONObject();
        
        jsonDados.put("codigo", 0);
        jsonDados.put("i_clientes", 1);
        jsonDados.put("nome", "Avancados");
        
        jsonObject.put("tabela", "grupos");
        jsonObject.put("dados", jsonDados);
        
        jsonParam.put("params",jsonObject);
        
        String params = jsonParam.toString();
        System.out.println("params:"+params);
        System.out.println("jsonParam:"+jsonParam.toString());
        
        GerenciaRequests gerenciaRequests = new GerenciaRequests();
        String lsRetorno = gerenciaRequests.doRequest(params);
//        Arquivo.gravarLog(lsRetorno);
    }

    @Override
    public JSONObject newRecord() {

        int i_clientes = 1;

        RegistroJson registro = new RegistroJson();
        registro.create();

        registro.setItem("ctrl_status", 2);
        registro.setItem("codigo", 0);
        registro.setItem("i_clientes", i_clientes);
        registro.setItem("nome", "");

        return registro.getRegistro();
    }

    @Override
    public boolean triggerAposGravar(long codigo) {
        return true;
    }

    @Override
    public void triggerEndRequest(JSONObject jsonRetorno) {
        return;
    }

}
