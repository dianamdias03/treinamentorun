/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.Conexao;
import framework.FormatacaoDatas;
import framework.RegistroJson;
import framework.RsJson;
import framework.Tabela;
import java.io.IOException;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author u0180759
 */
public class GravarMicroCicloTreinos extends framework.Gravar {

    @Override
    public void defineTabela() {

        Arquivo.gravarLog(this.getGerenciaRequests().getNodeParams().toString());

        JSONObject jsonDados = this.getGerenciaRequests().getNodeParams().getJSONObject("dados");

        setTabela(new Tabela("micro_ciclo_treinos"));

        getTabela().addColunaI("i_micro_ciclo_treinos", jsonDados.optInt("codigo", 0), true);
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
        getTabela().addColunaS("feedback", jsonDados.optString("feedback", ""));
        getTabela().addColunaI("tempo_treino_minimo", jsonDados.optInt("tempo_treino_minimo", 0));
        getTabela().addColunaI("tempo_treino_maximo", jsonDados.optInt("tempo_treino_maximo", 0));
        getTabela().addColunaI("tempo_treino_realizado", jsonDados.optInt("tempo_treino_realizado", 0));
        getTabela().addColunaI("fc_media", jsonDados.optInt("fc_media", 0));
        getTabela().addColunaI("distancia", jsonDados.optInt("distancia", 0));
        getTabela().addColuna("i_micro_ciclo_treinos_planejado", "0");
    }

    public RegistroJson newTreino() {

        Arquivo.gravarLog("->" + this.getGerenciaRequests().getNodeParams().toString());

        RegistroJson registro = new RegistroJson();
        registro.create();
        registro.setItem("ctrl_status", 2);
        registro.setItem("codigo", 0);
        registro.setItem("tipo", 1);
        registro.setItem("dia", this.getGerenciaRequests().getNodeParams().optString("dia", ""));
        registro.setItem("tipos_modalidades", 1, "Não informado");
        registro.setItem("tipos_intensidades", 1, "Não informado");
        registro.setItem("tipos_treinos", 1, "Não informado");
        registro.setItem("tipos_distancias", 1, "km");
        registro.setItem("tipos_percursos", 1, "Não informado");
        registro.setItem("descricao", "");
        registro.setItem("feedback", "");
        registro.setItem("i_clientes", this.getGerenciaRequests().getNodeParams().optInt("i_clientes", 0));
        registro.setItem("i_usuarios", this.getGerenciaRequests().getNodeParams().optInt("i_usuarios", 0));
        registro.setItem("i_micro_ciclo", this.getGerenciaRequests().getNodeParams().optInt("i_micro_ciclo", 0));

        return registro;
    }

    @Override
    public JSONObject newRecord() {
        Arquivo.gravarLog("->" + this.getGerenciaRequests().getNodeParams().toString());
        RegistroJson registro = newTreino();
        return registro.getRegistro();
    }

    public JSONObject newDiaDeFolga() {
        Arquivo.gravarLog("Dia de folga->" + this.getGerenciaRequests().getNodeParams().toString());
        RegistroJson registro = newTreino();
        registro.setItem("tipos_modalidades", 1, "Não informado");
        registro.setItem("ctrl_status", 1);
        Arquivo.gravarLog("Dia de folga Registro:" + registro.toString());
        return registro.getRegistro();
    }

    public JSONArray get(int i_clientes, int i_usuarios, int i_micro_ciclo, String asDia) {
        String sql;
        sql = "select i_micro_ciclo_treinos as codigo, i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, "
                + "i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, "
                + "tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, "
                + "i_micro_ciclo_treinos_planejado, feedback,"
                + "(select s.descricao from tipos_modalidades s where s.i_clientes=p.i_clientes and s.i_tipos_modalidades=p.i_tipos_modalidades) as s_tipos_modalidades, "
                + "(select s.descricao from tipos_treinos s where s.i_clientes=p.i_clientes and s.i_tipos_treinos=p.i_tipos_treinos) as s_tipos_treinos, "
                + "(select s.descricao from tipos_intensidades s where s.i_clientes=p.i_clientes and s.i_tipos_intensidades=p.i_tipos_intensidades) as s_tipos_intensidades, "
                + "(select s.descricao from tipos_percursos s where s.i_clientes=p.i_clientes and s.i_tipos_percursos=p.i_tipos_percursos) as s_tipos_percursos, "
                + "(select s.descricao from tipos_distancias s where s.i_clientes=p.i_clientes and s.i_tipos_distancias=p.i_tipos_distancias) as s_tipos_distancias, "
                + "1 as ctrl_status "
                + "from micro_ciclo_treinos p "
                + "where i_clientes = " + i_clientes + " and i_usuarios = " + i_usuarios + " and i_micro_ciclo=" + i_micro_ciclo
                + " order by dia, ordem";

        JSONArray dados = null;

        if (sql.equals("")) {
            dados = new JSONArray();
        } else {
            Conexao conexao = new Conexao();
            conexao.conectar();

            RsJson rsJson = new RsJson();
            rsJson.setConexao(conexao);
            try {
                dados = rsJson.getJsonBySQLStr(sql);
            } catch (IOException ex) {
                Arquivo.gravarLog("Erro executando comando SQL na listagem de micro_ciclo_treinos: " + ex.getMessage());
            }
        }

        FormatacaoDatas formatacaoDatas;
        JSONArray dados2 = new JSONArray();
        JSONObject jsonItem;
        JSONArray jsonLinha = new JSONArray();
        Date dia = new Date();
        for (int i = 0; dados != null && i < dados.length(); i++) {
            jsonItem = dados.getJSONObject(i);
            JSONObject jsonNew = new JSONObject();

            JSONObject jsonTiposModalidades = new JSONObject();
            jsonTiposModalidades.put("codigo", jsonItem.getInt("i_tipos_modalidades"));
            jsonTiposModalidades.put("descricao", jsonItem.get("s_tipos_modalidades"));
            jsonItem.put("tipos_modalidades", jsonTiposModalidades);

            JSONObject jsonTipoTreinos = new JSONObject();
            jsonTipoTreinos.put("codigo", jsonItem.getInt("i_tipos_treinos"));
            jsonTipoTreinos.put("descricao", jsonItem.get("s_tipos_treinos"));
            jsonItem.put("tipos_treinos", jsonTipoTreinos);

            JSONObject jsonTiposIntensidades = new JSONObject();
            jsonTiposIntensidades.put("codigo", jsonItem.getInt("i_tipos_intensidades"));
            jsonTiposIntensidades.put("descricao", jsonItem.get("s_tipos_intensidades"));
            jsonItem.put("tipos_intensidades", jsonTiposIntensidades);

            JSONObject jsonTiposPercursos = new JSONObject();
            jsonTiposPercursos.put("codigo", jsonItem.getInt("i_tipos_percursos"));
            jsonTiposPercursos.put("descricao", jsonItem.get("s_tipos_percursos"));
            jsonItem.put("tipos_percursos", jsonTiposPercursos);

            JSONObject jsonTipoDistancia = new JSONObject();
            jsonTipoDistancia.put("codigo", jsonItem.getInt("i_tipos_distancias"));
            jsonTipoDistancia.put("descricao", jsonItem.get("s_tipos_distancias"));
            jsonItem.put("tipos_distancias", jsonTipoDistancia);

            Date diaJsonItem = FormatacaoDatas.ymdToDate(jsonItem.get("dia").toString());
            formatacaoDatas = new FormatacaoDatas(diaJsonItem);
            Arquivo.gravarLog(jsonItem.get("dia").toString() + " " + formatacaoDatas.getDia().toString() + " " + formatacaoDatas.getDataDMY() + " " + diaJsonItem.toString());
            jsonItem.put("dia", formatacaoDatas.getDataDMY());

            if (!dia.equals(formatacaoDatas.getDia())) {
                jsonLinha = new JSONArray();
                jsonNew.put("dia", jsonItem.get("dia"));
                jsonNew.put("diaF", formatacaoDatas.diaMes());
                jsonNew.put("diaS", formatacaoDatas.diaSemana());
                jsonNew.put("Itens", jsonLinha);
                dados2.put(jsonNew);
                dia = formatacaoDatas.getDia();
            }
            jsonLinha.put(jsonItem);
        }

        dados2 = new JSONArray();
        String lsDia = asDia;
        formatacaoDatas = new FormatacaoDatas(FormatacaoDatas.ymdToDate(lsDia));
        for (int i = 0; i < 7; i++) {
            JSONObject jsonNew = new JSONObject();
            jsonLinha = new JSONArray();
            jsonNew.put("dia", formatacaoDatas.getDataDMY());
            jsonNew.put("diaF", formatacaoDatas.diaMes());
            jsonNew.put("diaS", formatacaoDatas.diaSemana());

            for (int j = 0; dados != null && j < dados.length(); j++) {
                jsonItem = dados.getJSONObject(j);
                Date diaJsonItem = FormatacaoDatas.dmyToDate(jsonItem.get("dia").toString());
                if (diaJsonItem.equals(formatacaoDatas.getDia())) {
                    jsonLinha.put(jsonItem);
                }
            }

            jsonNew.put("Itens", jsonLinha);
            dados2.put(jsonNew);
            formatacaoDatas.addDia(1);
        }

        dados = dados2;

        return dados;

    }

    public static void main(String[] args) {
        /*
        GravarMicroCicloTreinos gravar = new GravarMicroCicloTreinos();
//        String params = "{\"params\":{\"dados\":{\"i_micro_ciclo_treinos\":4,\"tipo\":4,\"i_tipos_modalidades\":1,\"s_tipos_intensidades\":\"Não informado\",\"i_tipos_treinos\":1,\"s_tipos_treinos\":\"Não informado\",\"tempo_treino_minimo\":0,\"i_tipos_intensidades\":1,\"i_micro_ciclo\":1,\"i_usuarios\":4,\"tempo_treino_realizado\":0,\"ordem\":1,\"s_tipos_distancias\":\"km\",\"s_tipos_modalidades\":\"Corrida\",\"i_clientes\":1,\"s_tipos_percursos\":\"Não informado\",\"tipos_modalidades\":{\"i_tipos_modalidades\":1,\"descricao\":\"Corrida\"},\"ctrl_status\":2,\"tempo_treino_maximo\":0,\"i_micro_ciclo_treinos_planejado\":0,\"tipos_intensidades\":{\"i_tipos_intensidades\":1,\"descricao\":\"Não informado\"},\"tipos_percursos\":{\"i_tipos_percursos\":1,\"descricao\":\"Não informado\"},\"fc_media\":150,\"descricao\":\"testetetete\",\"tipos_distancias\":{\"i_tipos_distancias\":1,\"descricao\":\"km\"},\"i_tipos_percursos\":1,\"distancia\":15,\"i_tipos_distancias\":1,\"tipos_treinos\":{\"i_tipos_treinos\":1,\"descricao\":\"Não informado\"},\"dia\":\"21/09/2017\"}}}";
        String params = "{\"params\":{\"acao\":1}}";
        gravar.testGravar(params);
*/
        GravarMicroCicloTreinos gravarMicroCicloTreinos = new GravarMicroCicloTreinos();
        JSONArray lista = gravarMicroCicloTreinos.get(1, 4, 1, "2017-09-18");
        Arquivo.gravarLog("lista: " + lista.toString());
    }

}
