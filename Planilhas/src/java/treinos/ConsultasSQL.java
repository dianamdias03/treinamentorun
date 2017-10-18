/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.Conexao;
import framework.FormatacaoDatas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author U0180759
 */
public class ConsultasSQL {

    public JSONObject planilhaSemanaAtleta(int i_clientes, int i_usuarios, String inicio) {

        JSONObject retorno = new JSONObject();
        retorno.put("resultado", false);

        String sql = "select m.i_micro_ciclo, m.i_clientes, m.i_usuarios, m.inicio, m.fim, "
                + "          m.situacao, m.comentario_treinador, m.comentario_atleta,\n"
                + "	     u.nome, u.email\n"
                + "     from micro_ciclo m,\n"
                + "          usuarios u \n"
                + "    where u.i_clientes = " + i_clientes + "\n"
                + "      and u.i_usuarios = " + i_usuarios + "\n"
                + "      and u.i_clientes = m.i_clientes\n"
                + "      and u.i_usuarios = m.i_usuarios\n"
                + "      and m.inicio = date('" + inicio + "');";

        Conexao conexao = new Conexao();
        conexao.conectar();

        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {

                FormatacaoDatas formatacaoDatas;

                retorno.put("i_micro_ciclo", rs.getInt("i_micro_ciclo"));
                retorno.put("i_clientes", rs.getInt("i_clientes"));
                retorno.put("i_usuarios", rs.getInt("i_usuarios"));
                retorno.put("situacao", rs.getInt("situacao"));
                retorno.put("inicio", rs.getDate("inicio"));
                retorno.put("fim", rs.getDate("fim"));
                retorno.put("comentario_treinador", rs.getString("comentario_treinador"));
                retorno.put("comentario_atleta", rs.getString("comentario_atleta"));
                retorno.put("nome", rs.getString("nome"));
                retorno.put("email", rs.getString("email"));

                formatacaoDatas = new FormatacaoDatas(rs.getDate("inicio"));
                retorno.put("inicioFormatado", formatacaoDatas.diaMes());

                formatacaoDatas = new FormatacaoDatas(rs.getDate("fim"));
                retorno.put("fimFormatado", formatacaoDatas.diaMes());

                retorno.put("nomePlanilha", "Treinos " + retorno.optString("inicioFormatado") + " a " + retorno.optString("fimFormatado"));
            }

            GravarMicroCicloTreinos gravarMicroCicloTreinos = new GravarMicroCicloTreinos();

            JSONArray lista = gravarMicroCicloTreinos.get(
                    retorno.optInt("i_clientes", 0),
                    retorno.optInt("i_usuarios", 0),
                    retorno.optInt("i_micro_ciclo", 0),
                    retorno.optString("inicio", "2017-09-18"));

            retorno.put("semana", lista);
            retorno.put("resultado", true);
            
            conexao.desconectar();

        } catch (SQLException ex) {
            Logger.getLogger(ConsultasSQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retorno;

    }

    public JSONObject treinosPreCadastrados() {

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        String sql = "select descricao from treinosPreCadastrados order by 1;";

        Conexao conexao = new Conexao();
        conexao.conectar();

        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                JSONObject item = new JSONObject();
                String descricao = rs.getString("descricao");
                item.put("descricao", descricao );
                item.put("descricaoF", descricao.replaceAll("\n", "<br>"));
                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);
            
            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo treinos pre cadastrados: " + ex.getMessage());
        }

        Arquivo.gravarLog(retorno.toString());

        return retorno;

    }

    public static void main(String[] args) {
        ConsultasSQL consultasSQL = new ConsultasSQL();
        System.out.println("Resultado:\n" + consultasSQL.planilhaSemanaAtleta(1, 4, "2017-10-09").toString());
    }

}
