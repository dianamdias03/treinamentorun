/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Arquivo;
import framework.Conexao;
import framework.FormatacaoDatas;
import framework.RequestsParams;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

    public JSONObject addCodigoDescricao(int codigo, String descricao) {
        JSONObject json = new JSONObject();
        json.put("codigo", codigo);
        json.put("descricao", descricao);
        return json;
    }

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

    public JSONObject treinosPreCadastradosUsuario(RequestsParams requestsParams) {

        /*
        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        int i_usuarios = requestsParams.getJsonRequest().optInt("i_usuarios", 0);

        String sql = "select descricao, \n"
                + "       i_treinosPreCadastrados, "
                + "\n     i_tipos_intensidades, "
                + "	   (select count(*) from micro_ciclo_treinos  "
                + "          where i_usuarios = " + i_usuarios + " "
                + "            and micro_ciclo_treinos.i_treinosPreCadastrados=treinosPreCadastrados.i_treinosPreCadastrados) as qtde\n"
                + " from treinosPreCadastrados \n"
                + " order by 1;";

        Conexao conexao = new Conexao();
        conexao.conectar();

        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                JSONObject item = new JSONObject();
                int qtde = rs.getInt("qtde");
                String descricao = rs.getString("descricao");
                item.put("i_treinosPreCadastrados", rs.getInt("i_treinosPreCadastrados"));
                item.put("qtde", qtde);
                if (qtde == 1) {
                    item.put("qtdeF", qtde + " vez");
                } else {
                    item.put("qtdeF", qtde + " vezes");
                }
                item.put("i_tipos_intensidades", rs.getInt("i_tipos_intensidades"));
                item.put("descricao", descricao);
                item.put("descricaoF", descricao.replaceAll("\n", "<br>"));
                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo treinos pre cadastrados: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;
        */
        
        requestsParams.getJsonRequest().put("qtde",1);
        
        return treinosPreCadastrados(requestsParams);

    }

    public JSONObject treinosPreCadastrados(RequestsParams requestsParams) {

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        String colunaQtde;

        if (requestsParams.getJsonRequest().optInt("qtde", 0) == 1) {
            colunaQtde = "	   (select count(*) from micro_ciclo_treinos c "
                    + "          where c.i_usuarios = " + requestsParams.getJsonRequest().optInt("i_usuarios", 0) + " "
                    + "            and c.i_treinosPreCadastrados=t.i_treinosPreCadastrados) as qtde\n";
        } else {
            colunaQtde = "0 as qtde";
        }

        String sql = "    select i_treinosPreCadastrados, descricao, i_grupos_atletas, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_percursos, i_tipos_percursos, i_clientes, \n"
                + "		   (select g.nome from grupos_atletas g where g.i_clientes = t.i_clientes and g.i_grupos_atletas = t.i_grupos_atletas) as s_grupos_atletas,\n"
                + "           (select s.descricao from tipos_modalidades s where s.i_clientes=t.i_clientes and s.i_tipos_modalidades=t.i_tipos_modalidades) as s_tipos_modalidades,\n"
                + "           (select s.descricao from tipos_treinos s where s.i_clientes=t.i_clientes and s.i_tipos_treinos=t.i_tipos_treinos) as s_tipos_treinos,\n"
                + "           (select s.descricao from tipos_intensidades s where s.i_clientes=t.i_clientes and s.i_tipos_intensidades=t.i_tipos_intensidades) as s_tipos_intensidades, \n"
                + "           (select s.descricao from tipos_percursos s where s.i_clientes=t.i_clientes and s.i_tipos_percursos=t.i_tipos_percursos) as s_tipos_percursos,\n"
                + "           " + colunaQtde + " \n"
                + "    from treinosPreCadastrados t\n"
                + "    where i_clientes = 1\n"
                + "    order by s_grupos_atletas, i_treinosPreCadastrados;";

        Arquivo.gravarLog(sql);
        Conexao conexao = new Conexao();
        conexao.conectar();

        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                JSONObject item = new JSONObject();
                String descricao = rs.getString("descricao");
                item.put("codigo", rs.getInt("i_treinosPreCadastrados"));
                item.put("i_clientes", rs.getInt("i_clientes"));
                item.put("descricao", descricao);
                item.put("descricaoF", descricao.replaceAll("\n", "<br>"));
                item.put("grupos_atletas", addCodigoDescricao(rs.getInt("i_grupos_atletas"), rs.getString("s_grupos_atletas")));
                item.put("tipos_modalidades", addCodigoDescricao(rs.getInt("i_tipos_modalidades"), rs.getString("s_tipos_modalidades")));
                item.put("tipos_treinos", addCodigoDescricao(rs.getInt("i_tipos_treinos"), rs.getString("s_tipos_treinos")));
                item.put("tipos_intensidades", addCodigoDescricao(rs.getInt("i_tipos_intensidades"), rs.getString("s_tipos_intensidades")));
                item.put("tipos_percursos", addCodigoDescricao(rs.getInt("i_tipos_percursos"), rs.getString("s_tipos_percursos")));

                int qtde = rs.getInt("qtde");
                item.put("qtde", qtde);
                if (qtde == 1) {
                    item.put("qtdeF", qtde + " vez");
                } else {
                    item.put("qtdeF", qtde + " vezes");
                }
                lista.put(item);

            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo treinos pre cadastrados: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public JSONObject treinosConcluidos() {

        FormatacaoDatas formatacaoDatas;
        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        String sql = "select i_clientes, i_usuarios, inicio, data_conclusao, data_email from micro_ciclo where situacao=2 order by data_conclusao;";

        Conexao conexao = new Conexao();
        conexao.conectar();

        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                JSONObject item = new JSONObject();
                item.put("i_clientes", rs.getString("i_clientes"));
                item.put("i_usuarios", rs.getString("i_usuarios"));

                formatacaoDatas = new FormatacaoDatas(rs.getDate("inicio"));
                item.put("inicio", formatacaoDatas.getDataYMD());

                formatacaoDatas.setDia(rs.getTimestamp("data_conclusao").toString());
                item.put("data_conclusao", formatacaoDatas.getDataDMY_HMS());

                formatacaoDatas.setDia(rs.getTimestamp("data_email").toString());
                item.put("data_email", formatacaoDatas.getDataDMY_HMS());

                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo treinos concluidos: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public JSONObject criarEventos() {

        int i_clientes = 1;

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        String sql = "select i_eventos as codigo, i_clientes, nome, dia, descricao, distancias, local, opcoesParticipacao "
                + " from eventos "
                + " where i_clientes=" + i_clientes
                + " order by dia, nome;";

        Conexao conexao = new Conexao();
        conexao.conectar();

//        Arquivo.gravarLog(sql);
        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                FormatacaoDatas formatacaoDatas = new FormatacaoDatas(rs.getDate("dia"));
                String descricao = rs.getString("descricao");

                JSONObject item = new JSONObject();
                item.put("codigo", rs.getInt("codigo"));
                item.put("i_clientes", rs.getInt("i_clientes"));
                item.put("nome", rs.getString("nome"));
                item.put("distancias", rs.getString("distancias"));
                item.put("local", rs.getString("local"));
                item.put("dia", formatacaoDatas.getDataDMY());
                item.put("opcoesParticipacao", rs.getString("opcoesParticipacao"));
                item.put("descricao", descricao);
                item.put("descricaoF", descricao.replaceAll("\n", "<br>"));
                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo eventos: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public JSONObject participantesEvento(int i_eventos, int i_usuarios) {

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        String whereUsuarios = "";

        if (i_usuarios > 0) {
            whereUsuarios = "\n   and i_usuarios_convidador=" + i_usuarios;
        }

        retorno.put("resultado", false);

        String sql = "select i_eventosParticipacoes, i_eventos, i_usuarios, nome, entrada, controle, i_usuarios_convidador, confirmado, distancia, opcao,"
                + "\n       (select s.nome from usuarios s where s.i_usuarios = eventosParticipacoes.i_usuarios_convidador) as nomeUsuario,"
                + "\n       (case i_usuarios when i_usuarios_convidador then (select s.nome from usuarios s where s.i_usuarios = eventosParticipacoes.i_usuarios_convidador) else nome end) as nomeUsuarioConvidado"
                + "\n from eventosParticipacoes "
                + "\n where i_eventos=" + i_eventos
                + whereUsuarios
                + "\n order by nomeUsuario, i_usuarios desc, nomeUsuarioConvidado;";

        Conexao conexao = new Conexao();
        conexao.conectar();

//        Arquivo.gravarLog(sql);
        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                JSONObject item = new JSONObject();
                item.put("i_eventosParticipacoes", rs.getInt("i_eventosParticipacoes"));
                item.put("i_eventos", rs.getInt("i_eventos"));
                item.put("i_usuarios", rs.getInt("i_usuarios"));
                item.put("nome", rs.getString("nome"));
                item.put("entrada", rs.getDate("entrada"));
                item.put("controle", rs.getString("controle"));
                item.put("i_usuarios_convidador", rs.getInt("i_usuarios_convidador"));
                item.put("confirmado", rs.getInt("confirmado"));
                item.put("distancia", rs.getInt("distancia"));
                item.put("nomeUsuario", rs.getString("nomeUsuario"));
                item.put("opcao", rs.getString("opcao"));
                item.put("nomeUsuarioConvidado", rs.getString("nomeUsuarioConvidado"));
                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo eventos: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public JSONObject dadosUsuario(RequestsParams requestsParams) {

        int i_clientes = 1;
        int i_usuarios = requestsParams.getJsonRequest().optInt("i_usuarios", 0);

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        String sql = "select i_usuarios, i_clientes, nome, email, senha, \n"
                + "cpf, rg, endereco, cidade, estado, cep, \n"
                + "data_nascto, telefone_1, telefone_2 "
                + " from usuarios "
                + " where i_clientes=" + i_clientes
                + "   and i_usuarios=" + i_usuarios
                + " order by nome;";

        Conexao conexao = new Conexao();
        conexao.conectar();

//        Arquivo.gravarLog(sql);
        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                FormatacaoDatas formatacaoDatas = new FormatacaoDatas(rs.getDate("data_nascto"));

                JSONObject item = new JSONObject();
                item.put("i_usuarios", rs.getInt("i_usuarios"));
                item.put("codigo", rs.getInt("i_usuarios"));
                item.put("i_clientes", rs.getInt("i_clientes"));
                item.put("nome", rs.getString("nome"));
                item.put("email", rs.getString("email"));
                item.put("telefone_1", rs.getString("telefone_1"));
                item.put("telefone_2", rs.getString("telefone_2"));
                item.put("cpf", rs.getString("cpf"));
                item.put("rg", rs.getString("rg"));
                item.put("endereco", rs.getString("endereco"));
                item.put("cidade", rs.getString("cidade"));
                item.put("estado", rs.getString("estado"));
                item.put("cep", rs.getString("cep"));
                item.put("data_nascto", formatacaoDatas.getDataDMY());
                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo eventos: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public JSONObject grupos(RequestsParams requestsParams) {

        int i_clientes = 1;

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        String sql = "select i_grupos_atletas as codigo, i_clientes, nome "
                + " from grupos_atletas "
                + " where i_clientes=" + i_clientes
                + " order by nome;";

        Conexao conexao = new Conexao();
        conexao.conectar();

//        Arquivo.gravarLog(sql);
        ResultSet rs = conexao.executaSelect(sql);

        try {

            while (rs.next()) {
                JSONObject item = new JSONObject();
                item.put("codigo", rs.getInt("codigo"));
                item.put("i_clientes", rs.getInt("i_clientes"));
                item.put("nome", rs.getString("nome"));
                lista.put(item);
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo eventos: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public JSONObject charSet(String sql) {

        JSONObject retorno = new JSONObject();
        JSONArray lista = new JSONArray();
        retorno.put("resultado", false);

        Conexao conexao = new Conexao();
        conexao.conectar();

        ResultSet rs = conexao.executaSelect(sql);

        try {

            ResultSetMetaData rsmd = rs.getMetaData();

            int numColumns = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i < numColumns + 1; i++) {
                    String column_name = rsmd.getColumnLabel(i);
                    if (i > 1) {
                        System.out.print(" | ");
                    }
                    System.out.print(rs.getObject(column_name));
                }
                System.out.print("\n");
            }

            retorno.put("dados", lista);
            retorno.put("resultado", true);

            conexao.desconectar();

        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo eventos: " + ex.getMessage());
        }

//        Arquivo.gravarLog(retorno.toString());
        return retorno;

    }

    public static void main(String[] args) {
        ConsultasSQL consultasSQL = new ConsultasSQL();
//        System.out.println("Resultado:\n" + consultasSQL.planilhaSemanaAtleta(1, 4, "2017-10-09").toString());
//        System.out.println("Resultado:\n" + consultasSQL.treinosConcluidos().toString());

        /*
         JSONObject teste = new JSONObject();
         RequestsParams requestsParams = new RequestsParams();
         teste.put("i_usuarios", 1);
         requestsParams.setParams(teste.toString());
         System.out.println("Resultado:\n" + consultasSQL.dadosUsuario(requestsParams));
         */
        JSONObject resultado = new JSONObject();
        resultado = consultasSQL.charSet("SHOW VARIABLES LIKE 'character_set%';");
        resultado = consultasSQL.charSet("SHOW VARIABLES LIKE 'collat%';");
//        System.out.println("Resultado: " + resultado.toString());
    }

}
