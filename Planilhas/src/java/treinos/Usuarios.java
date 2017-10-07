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

public class Usuarios extends framework.Gravar {

    @Override
    public void defineTabela() {

        Arquivo.gravarLog(this.getGerenciaRequests().toString());
        Arquivo.gravarLog(this.getGerenciaRequests().getNodeParams().toString());

        JSONObject jsonDados = this.getGerenciaRequests().getNodeParams().getJSONObject("dados");

        setTabela(new Tabela("usuarios"));

        getTabela().addColunaI("i_usuarios", jsonDados.optInt("codigo", 0), true);
        getTabela().addColunaI("i_clientes", jsonDados.optInt("i_clientes", 0));
        getTabela().addColunaS("nome", jsonDados.optString("nome", ""));
        getTabela().addColunaS("email", jsonDados.optString("email", ""));
        getTabela().addColunaS("senha", jsonDados.optString("senha", ""));
        getTabela().addColunaI("admin", jsonDados.optInt("admin",0));
        getTabela().addColunaI("cria_planilhas", jsonDados.optInt("cria_planilhas", 0));
        getTabela().addColunaI("cria_usuarios", jsonDados.optInt("cria_usuarios", 0));
        getTabela().addColunaI("cria_eventos", jsonDados.optInt("cria_eventos", 0));
        getTabela().addColunaI("recebe_planilha", jsonDados.optInt("recebe_planilha", 0));
        getTabela().addColunaS("cpf", jsonDados.optString("cpf", ""));
        getTabela().addColunaS("rg", jsonDados.optString("rg", ""));
        getTabela().addColunaS("endereco", jsonDados.optString("endereco", ""));
        getTabela().addColunaS("cidade", jsonDados.optString("cidade", ""));
        getTabela().addColunaS("estado", jsonDados.optString("estado", ""));
        getTabela().addColunaS("cep", jsonDados.optString("cep", ""));
        getTabela().addColunaS("observacoes", jsonDados.optString("observacoes", ""));
        getTabela().addColunaD("data_nascto", jsonDados.optString("data_nascto", ""));
        getTabela().addColunaS("telefone_1", jsonDados.optString("telefone_1", ""));
        getTabela().addColunaS("telefone_2", jsonDados.optString("telefone_2", ""));
    }

    public static void main(String[] args) {
        String params = "{\"params\":{\"tabela\":\"usuarios\",\"dados\":{\"recebe_planilha\":1,\"codigo\":0,\"cidade\":\"\",\"estado\":\"SC\",\"endereco\":\"\",\"telefone_2\":\"\",\"telefone_1\":\"\",\"i_clientes\":1,\"ctrl_status\":2,\"cria_planilhas\":0,\"admin\":\"\",\"nome\":\"Wagner\",\"cria_usuarios\":0,\"cep\":\"\",\"senha\":\"\",\"observacoes\":\"\",\"rg\":\"\",\"cpf\":\"\",\"data_nascto\":\"01/01/1980\",\"cria_eventos\":0,\"email\":\"\"}}}";
//        String params = "{\"params\":{\"tabela\":\"usuarios\",\"acao\":1}}";
        GerenciaRequests gerenciaRequests = new GerenciaRequests();
        String lsRetorno = gerenciaRequests.doRequest(params);
        Arquivo.gravarLog(lsRetorno);
    }

    @Override
    public JSONObject newRecord() {
        
        int i_clientes=1;

        RegistroJson registro = new RegistroJson();
        registro.create();
        
        registro.setItem("ctrl_status", 2);
        registro.setItem("codigo", 0);
        registro.setItem("i_clientes", i_clientes);
        registro.setItem("nome", "");
        registro.setItem("email", "");
        registro.setItem("senha", "");
        registro.setItem("admin", "");
        registro.setItem("cria_planilhas", 0);
        registro.setItem("cria_usuarios", 0);
        registro.setItem("cria_eventos", 0);
        registro.setItem("recebe_planilha", 0);
        registro.setItem("cpf", "");
        registro.setItem("rg", "");
        registro.setItem("endereco", "");
        registro.setItem("cidade", "");
        registro.setItem("estado", "SC");
        registro.setItem("cep", "");
        registro.setItem("observacoes", "");
        registro.setItem("data_nascto", "");
        registro.setItem("telefone_1", "");
        registro.setItem("telefone_2", "");

        return registro.getRegistro();
    }

}
