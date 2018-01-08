package treinos;

import framework.Arquivo;
import framework.Conexao;
import framework.RequestsParams;
import framework.Tabela;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventosParticipantes {

    Conexao conexao = new Conexao();

    public JSONObject Gravar(RequestsParams requestsParams) {

        JSONObject retorno = new JSONObject();
        int i_eventosParticipacoes;
        int i_usuarios;
        int i_eventos;
        int minhaParticipacao;
        int distancia;

        i_eventosParticipacoes = requestsParams.getJsonRequest().optInt("i_eventosParticipacoes", 0);
        i_eventos = requestsParams.getJsonRequest().optInt("i_eventos", 0);
        i_usuarios = requestsParams.getJsonRequest().optInt("i_usuarios", 0);
        minhaParticipacao = 1;//requestsParams.getJsonRequest().optInt("minhaParticipacao", 0);
        distancia = requestsParams.getJsonRequest().optInt("distancia", 0);

        JSONArray listaParticipantes = requestsParams.getJsonRequest().getJSONArray("convidados");

        conexao = new Conexao();
        conexao.conectar();
        
        Arquivo.gravarLog(requestsParams.getJsonRequest().toString());

        gravarTabela(i_eventosParticipacoes,
                i_eventos,
                i_usuarios,
                i_usuarios,
                requestsParams.getJsonRequest().optString("nomeConvidador", ""),
                minhaParticipacao,
                distancia,
                requestsParams.getJsonRequest().optString("opcaoAtleta", ""));

        for (int i = 0; i < listaParticipantes.length(); i++) {
            JSONObject participante = listaParticipantes.getJSONObject(i);
            gravarTabela(
                    participante.optInt("i_eventosParticipacoes", 0), 
                    i_eventos, 
                    0, 
                    i_usuarios, 
                    participante.optString("nome", ""), 
                    1, 
                    participante.optInt("distancia", 0),
                    "Convidados"
            );
        }

        retorno.put("resultado", true);

        conexao.desconectar();

        return retorno;
    }

    public boolean gravarTabela(int i_eventosParticipacoes, int i_eventos, int i_usuarios, int i_usuarios_convidador, String nome, int confirmado, int distancia, String opcao) {
        Tabela tabela = new Tabela("eventosParticipacoes");

        tabela.addColunaI("i_eventosParticipacoes", i_eventosParticipacoes, true);
        tabela.addColunaI("i_eventos", i_eventos);
        tabela.addColunaI("i_usuarios", i_usuarios);
        tabela.addColunaI("i_usuarios_convidador", i_usuarios_convidador);
        tabela.addColunaI("confirmado", confirmado);
        tabela.addColunaI("distancia", distancia);
        tabela.addColunaS("nome", nome);
        tabela.addColunaS("controle", "");
        tabela.addColunaS("opcao", opcao);

        if (i_eventosParticipacoes == 0) {
            tabela.addColunaExp("entrada", "now()");
        }

        if (nome.equals("")) {
            if (i_eventosParticipacoes > 0) {
                conexao.executaComando(tabela.getDelete());
            }
        } else {
            if (i_eventosParticipacoes > 0) {
                conexao.executaComando(tabela.getUpdate());
            } else {
                conexao.executaComando(tabela.getInsert());
            }
        }

        return true;
    }

}
