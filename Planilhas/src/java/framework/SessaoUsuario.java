/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author u0180759
 */
public class SessaoUsuario {

    HttpSession session;

    public SessaoUsuario(HttpSession session) {
        this.session = session;
    }

    public JSONObject getUsuarios() throws IOException {

        Conexao conexao = new Conexao();
        conexao.conectar();

        String sql = "select i_usuarios as codigo, i_clientes, nome, email, "
                + " admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha "
                + " from usuarios "
                + " where email='" + session.getAttribute("emailUsuario") + "'"
                + " order by 1, 2";

        RsJson rsJson = new RsJson();
        rsJson.setConexao(conexao);
        JSONArray dados = rsJson.getJsonBySQLStr(sql);

        JSONObject jsonRetorno = new JSONObject();

        if (dados != null && dados.length() > 0) {
            jsonRetorno.put("usuarioDados", (JSONObject) dados.get(0));
        }

        return jsonRetorno;
    }

    public boolean validaUsuarioSenha(String usuario, String senha) throws IOException {

        boolean retorno;
        Conexao conexao = new Conexao();
        conexao.conectar();

        String sql = "select 1 "
                + " from usuarios "
                + " where email='" + usuario + "'"
                + " and senha='" + senha + "'";

        RsJson rsJson = new RsJson();
        rsJson.setConexao(conexao);
        JSONArray dados = rsJson.getJsonBySQLStr(sql);

        retorno = (dados != null && dados.length() > 0);

        return retorno;
    }

    public static boolean isPermissao(HttpSession session, String permissao) throws IOException {
        JSONObject jsonRetorno = (new SessaoUsuario(session)).getUsuarios();
        jsonRetorno = jsonRetorno.getJSONObject("usuarioDados");
        return (jsonRetorno.optInt(permissao, 0) == 1);
    }

}
