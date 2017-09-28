/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.RegistroJson;
import framework.Tabela;
import javax.servlet.jsp.JspWriter;
import org.apache.jasper.runtime.JspWriterImpl;
import org.json.JSONObject;

/**
 *
 * @author u0180759
 */
public class GravarMicroCicloTreinos extends framework.Gravar {

    public GravarMicroCicloTreinos(JspWriter out) {
        super(out);
    }

    @Override
    public void defineTabela() {

        Arquivo.gravarLog(this.getJsonRequestParam().toString());

        JSONObject jsonDados = this.getJsonRequestParam().getJSONObject("params").getJSONObject("dados");

        setTabela(new Tabela("micro_ciclo_treinos"));

        getTabela().addColunaI("i_micro_ciclo_treinos", jsonDados.optInt("i_micro_ciclo_treinos", 0), true);
        getTabela().addColunaI("i_micro_ciclo", jsonDados.optInt("i_micro_ciclo", 0));
        getTabela().addColunaI("i_clientes", jsonDados.optInt("i_clientes", 0));
        getTabela().addColunaI("i_usuarios", jsonDados.optInt("i_usuarios", 0));
        getTabela().addColunaI("tipo", jsonDados.optInt("tipo", 0));
        getTabela().addColunaD("dia", jsonDados.optString("dia", ""));
        getTabela().addColunaI("ordem", jsonDados.optInt("ordem", 0));
        getTabela().addColuna("i_tipos_modalidades", jsonDados.getJSONObject("tipos_modalidades").optString("codigo", ""));
        getTabela().addColuna("i_tipos_intensidades", jsonDados.getJSONObject("tipos_intensidades").optString("codigo", ""));
        getTabela().addColuna("i_tipos_treinos", jsonDados.getJSONObject("tipos_treinos").optString("codigo", ""));
        getTabela().addColuna("i_tipos_distancias", jsonDados.getJSONObject("tipos_distancias").optString("codigo", ""));
        getTabela().addColuna("i_tipos_percursos", jsonDados.getJSONObject("tipos_percursos").optString("codigo", ""));
        getTabela().addColunaS("descricao", jsonDados.optString("descricao", ""));
        getTabela().addColunaI("tempo_treino_minimo", jsonDados.optInt("tempo_treino_minimo", 0));
        getTabela().addColunaI("tempo_treino_maximo", jsonDados.optInt("tempo_treino_maximo", 0));
        getTabela().addColunaI("tempo_treino_realizado", jsonDados.optInt("tempo_treino_realizado", 0));
        getTabela().addColunaI("fc_media", jsonDados.optInt("fc_media", 0));
        getTabela().addColunaI("distancia", jsonDados.optInt("distancia", 0));
        getTabela().addColuna("i_micro_ciclo_treinos_planejado", "0");
    }

    public static void main(String[] args) {
        GravarMicroCicloTreinos gravar = new GravarMicroCicloTreinos(new JspWriterImpl());
//        String params = "{\"params\":{\"dados\":{\"i_micro_ciclo_treinos\":4,\"tipo\":4,\"i_tipos_modalidades\":1,\"s_tipos_intensidades\":\"Não informado\",\"i_tipos_treinos\":1,\"s_tipos_treinos\":\"Não informado\",\"tempo_treino_minimo\":0,\"i_tipos_intensidades\":1,\"i_micro_ciclo\":1,\"i_usuarios\":4,\"tempo_treino_realizado\":0,\"ordem\":1,\"s_tipos_distancias\":\"km\",\"s_tipos_modalidades\":\"Corrida\",\"i_clientes\":1,\"s_tipos_percursos\":\"Não informado\",\"tipos_modalidades\":{\"i_tipos_modalidades\":1,\"descricao\":\"Corrida\"},\"ctrl_status\":2,\"tempo_treino_maximo\":0,\"i_micro_ciclo_treinos_planejado\":0,\"tipos_intensidades\":{\"i_tipos_intensidades\":1,\"descricao\":\"Não informado\"},\"tipos_percursos\":{\"i_tipos_percursos\":1,\"descricao\":\"Não informado\"},\"fc_media\":150,\"descricao\":\"testetetete\",\"tipos_distancias\":{\"i_tipos_distancias\":1,\"descricao\":\"km\"},\"i_tipos_percursos\":1,\"distancia\":15,\"i_tipos_distancias\":1,\"tipos_treinos\":{\"i_tipos_treinos\":1,\"descricao\":\"Não informado\"},\"dia\":\"21/09/2017\"}}}";
        String params = "{\"params\":{\"acao\":1}}";
        gravar.testGravar(params);
    }

    @Override
    public JSONObject newRecord() {
        RegistroJson registro = new RegistroJson();
        registro.create();
        registro.setItem("ctrl_status", 1);
        registro.setItem("i_micro_ciclo_treinos", 0);
        registro.setItem("tipo", 1);
        registro.setItem("i_tipos_modalidades", 1, "Não informado");
        registro.setItem("descricao", "Teste Teste");
        
        return registro.getRegistro();
    }

}
