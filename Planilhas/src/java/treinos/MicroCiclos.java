/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.Conexao;
import framework.GerenciaRequests;
import framework.RegistroJson;
import framework.RequestsParams;
import framework.Tabela;
import org.json.JSONObject;

public class MicroCiclos /*extends framework.Gravar*/ {

    private Tabela tabela;
    private RequestsParams requestsParams;

    public MicroCiclos(RequestsParams requestsParams) {
        this.requestsParams = requestsParams;
    }

    public void defineTabela() {

        int situacao;

//        Arquivo.gravarLog("MicroCiclos.getRequestsParams" + this.getRequestsParams().toString());

        JSONObject jsonDados = this.getRequestsParams().getJsonRequest().getJSONObject("registro");

        setTabela(new Tabela("micro_ciclo"));

        situacao = jsonDados.getJSONObject("situacaoObj").optInt("codigo", 0);

        getTabela().addColunaI("i_micro_ciclo", jsonDados.optInt("codigo", 0), true);
        getTabela().addColunaI("situacao", situacao);
        getTabela().addColunaS("comentario_treinador", jsonDados.optString("comentario_treinador", ""));
        getTabela().addColunaS("comentario_atleta", jsonDados.optString("comentario_atleta", ""));
        getTabela().addColunaExp("data_conclusao", "(case when data_conclusao is null and 2=" + situacao + " then now() else data_conclusao end)");

    }

    public static void main(String[] args) {
        String params = "{\"params\":{\"tabela\":\"usuarios\",\"dados\":{\"recebe_planilha\":1,\"codigo\":0,\"cidade\":\"\",\"estado\":\"SC\",\"endereco\":\"\",\"telefone_2\":\"\",\"telefone_1\":\"\",\"i_clientes\":1,\"ctrl_status\":2,\"cria_planilhas\":0,\"admin\":\"\",\"nome\":\"Wagner\",\"cria_usuarios\":0,\"cep\":\"\",\"senha\":\"\",\"observacoes\":\"\",\"rg\":\"\",\"cpf\":\"\",\"data_nascto\":\"01/01/1980\",\"cria_eventos\":0,\"email\":\"\"}}}";
//        String params = "{\"params\":{\"tabela\":\"usuarios\",\"acao\":1}}";
        GerenciaRequests gerenciaRequests = new GerenciaRequests();
        String lsRetorno = gerenciaRequests.doRequest(params);
        Arquivo.gravarLog(lsRetorno);
    }

    public JSONObject newRecord() {
        RegistroJson registro = new RegistroJson();
        registro.create();
        return registro.getRegistro();
    }

    public String getComandoSQL() {
        String sql;
        int acao = this.getRequestsParams().getJsonRequest().optInt("acao", 0);

        if (acao == 3) {
            sql = getTabela().getDelete();
        } else {
            if (getTabela().getCodigo() != 0) {
                sql = getTabela().getInsert();
            } else {
                sql = getTabela().getUpdate();
            }
        }

        return sql;
    }

    public JSONObject doAction() {

        String sql;
        boolean sqlExecutado;
        
        this.defineTabela();
        
        sql = this.getComandoSQL();

        Conexao conexao = new Conexao();

        conexao.conectar();

//        sqlExecutado=true;
        sqlExecutado = conexao.executaComando(sql);

        conexao.desconectar();

        JSONObject retorno = new JSONObject();
        retorno.put("sql", sql);
        retorno.put("resultado", sqlExecutado);
        retorno.put("request", getRequestsParams().getJsonRequest());

        return retorno;
    }

    /**
     * @return the tabela
     */
    public Tabela getTabela() {
        return tabela;
    }

    /**
     * @param tabela the tabela to set
     */
    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }

    /**
     * @return the requestsParams
     */
    public RequestsParams getRequestsParams() {
        return requestsParams;
    }

    /**
     * @param requestsParams the requestsParams to set
     */
    public void setRequestsParams(RequestsParams requestsParams) {
        this.requestsParams = requestsParams;
    }

}
