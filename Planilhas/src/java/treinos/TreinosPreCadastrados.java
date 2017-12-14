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

public class TreinosPreCadastrados extends framework.Gravar {

    public int getCodigoItemJson(JSONObject jsonDados, String nome) {

        int codigo = 0;

        try {
            JSONObject jsonItem = jsonDados.getJSONObject(nome);
            codigo = jsonItem.optInt("codigo", 0);
        } catch (Exception e) {
            Arquivo.gravarLog("Erro lendo '" + nome + "'. Exception: " + e.getMessage());
        }

        return codigo;
    }

    @Override
    public void defineTabela() {

//        Arquivo.gravarLog(this.getGerenciaRequests().toString());
//        Arquivo.gravarLog(this.getGerenciaRequests().getNodeParams().toString());
        JSONObject jsonDados = this.getGerenciaRequests().getNodeParams().getJSONObject("dados");

        setTabela(new Tabela("treinosPreCadastrados"));

        getTabela().addColunaI("i_treinosPreCadastrados", jsonDados.optInt("codigo", 0), true);
        getTabela().addColunaS("descricao", jsonDados.optString("descricao", ""));

        getTabela().addColunaI("i_tipos_modalidades", getCodigoItemJson(jsonDados, "tipos_modalidades"));
        getTabela().addColunaI("i_tipos_intensidades", getCodigoItemJson(jsonDados, "tipos_intensidades"));
        getTabela().addColunaI("i_tipos_treinos", getCodigoItemJson(jsonDados, "tipos_treinos"));
        getTabela().addColunaI("i_tipos_percursos", getCodigoItemJson(jsonDados, "tipos_percursos"));
        getTabela().addColunaI("i_grupos_atletas", getCodigoItemJson(jsonDados, "grupos_atletas"));
        getTabela().addColunaI("i_clientes", jsonDados.optInt("i_clientes", 0));

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

        jsonParam.put("params", jsonObject);

        String params = jsonParam.toString();
        System.out.println("params:" + params);
        System.out.println("jsonParam:" + jsonParam.toString());

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

        try {
            JSONObject jsonDados = this.getGerenciaRequests().getNodeParams().getJSONObject("dados");
            String descricao = jsonDados.optString("descricao", "");
            descricao = descricao.replaceAll("\n", "<br>");
            jsonRetorno.put("descricaoF", descricao);
        } catch (Exception e) {
            //-- Proteção quando o json não estiver na estrutura correta.
        }

    }

}
