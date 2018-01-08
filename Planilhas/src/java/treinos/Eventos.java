/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

//import framework.Arquivo; 
import framework.GerenciaRequests;
import framework.RegistroJson;
import framework.Tabela;
import org.json.JSONObject;

public class Eventos extends framework.Gravar {

    @Override
    public void defineTabela() {

//        Arquivo.gravarLog(this.getGerenciaRequests().toString());
//        Arquivo.gravarLog(this.getGerenciaRequests().getNodeParams().toString());

        JSONObject jsonDados = this.getGerenciaRequests().getNodeParams().getJSONObject("dados");

        setTabela(new Tabela("eventos"));

        getTabela().addColunaI("i_eventos", jsonDados.optInt("codigo", 0), true);
        getTabela().addColunaI("i_clientes", jsonDados.optInt("i_clientes", 0));
        getTabela().addColunaS("nome", jsonDados.optString("nome", ""));
        getTabela().addColunaS("local", jsonDados.optString("local", ""));
        getTabela().addColunaS("distancias", jsonDados.optString("distancias", ""));
        getTabela().addColunaD("dia", jsonDados.optString("dia", "01/01/1900"));
        getTabela().addColunaS("descricao", jsonDados.optString("descricao", ""));
        getTabela().addColunaS("opcoesParticipacao", jsonDados.optString("opcoesParticipacao", ""));
    }

    public static void main(String[] args) {
        String params = "{\"params\":{\"tabela\":\"usuarios\",\"dados\":{\"recebe_planilha\":1,\"codigo\":0,\"cidade\":\"\",\"estado\":\"SC\",\"endereco\":\"\",\"telefone_2\":\"\",\"telefone_1\":\"\",\"i_clientes\":1,\"ctrl_status\":2,\"cria_planilhas\":0,\"admin\":\"\",\"nome\":\"Wagner\",\"cria_usuarios\":0,\"cep\":\"\",\"senha\":\"\",\"observacoes\":\"\",\"rg\":\"\",\"cpf\":\"\",\"data_nascto\":\"01/01/1980\",\"cria_eventos\":0,\"email\":\"\"}}}";
//        String params = "{\"params\":{\"tabela\":\"usuarios\",\"acao\":1}}";
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
        registro.setItem("local", "");
        registro.setItem("distancias", "");
        registro.setItem("dia", "");
        registro.setItem("descricao", "");
        registro.setItem("opcoesParticipacao", "Sim;Não");

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
