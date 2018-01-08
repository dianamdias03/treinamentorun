
package treinos;

import framework.Conexao;
import framework.RequestsParams;
import framework.Tabela;
import org.json.JSONObject;

public class MeusDados {

    public JSONObject gravar(RequestsParams requestsParams) {
        JSONObject retorno;
        if (requestsParams.getJsonRequest().optInt("tipo", 0) == 1) {
            retorno = gravarDados(requestsParams);
        } else {
            retorno = alterarSenha(requestsParams);
        }
        return retorno;
    }

    public JSONObject gravarDados(RequestsParams requestsParams) {

        Tabela tabela = new Tabela("usuarios");
        JSONObject jsonDados = requestsParams.getJsonRequest().getJSONObject("dados");

        tabela.addColunaI("i_usuarios", jsonDados.optInt("codigo", 0), true);
        tabela.addColunaI("i_clientes", jsonDados.optInt("i_clientes", 0));
        tabela.addColunaS("nome", jsonDados.optString("nome", ""));
        tabela.addColunaS("email", jsonDados.optString("email", ""));
//        tabela.addColunaS("senha", jsonDados.optString("senha", ""));
//        tabela.addColunaI("admin", jsonDados.optInt("admin", 0));
//        tabela.addColunaI("cria_planilhas", jsonDados.optInt("cria_planilhas", 0));
//        tabela.addColunaI("cria_usuarios", jsonDados.optInt("cria_usuarios", 0));
//        tabela.addColunaI("cria_eventos", jsonDados.optInt("cria_eventos", 0));
//        tabela.addColunaI("recebe_planilha", jsonDados.optInt("recebe_planilha", 0));
        tabela.addColunaS("cpf", jsonDados.optString("cpf", ""));
        tabela.addColunaS("rg", jsonDados.optString("rg", ""));
        tabela.addColunaS("endereco", jsonDados.optString("endereco", ""));
        tabela.addColunaS("cidade", jsonDados.optString("cidade", ""));
        tabela.addColunaS("estado", jsonDados.optString("estado", ""));
        tabela.addColunaS("cep", jsonDados.optString("cep", ""));
//        tabela.addColunaS("observacoes", jsonDados.optString("observacoes", ""));
        tabela.addColunaD("data_nascto", jsonDados.optString("data_nascto", ""));
        tabela.addColunaS("telefone_1", jsonDados.optString("telefone_1", ""));
        tabela.addColunaS("telefone_2", jsonDados.optString("telefone_2", ""));

        Conexao conexao = new Conexao();
        conexao.conectar();
        boolean sucesso = conexao.executaComando(tabela.getUpdate());
        conexao.desconectar();

        JSONObject retorno = new JSONObject();
        retorno.put("retorno", sucesso);

        return retorno;
    }

    public JSONObject alterarSenha(RequestsParams requestsParams) {

        boolean sucesso = false;
        JSONObject retorno = new JSONObject();
        JSONObject jsonDados = requestsParams.getJsonRequest().getJSONObject("dados");

        String senha1 = jsonDados.optString("senha1", "");
        String senha2 = jsonDados.optString("senha2", "");
        String senha3 = jsonDados.optString("senha3", "");

        if (senha1.equals("")) {
            retorno.put("erroMsg", "Senha atual não informada!!!");
        } else if (senha2.equals("")) {
            retorno.put("erroMsg", "Nova senha não informada!!!");
        } else if (senha3.equals("")) {
            retorno.put("erroMsg", "Confirmação da nova senha não informada!!!");
        } else if (!senha2.equals(senha3)) {
            retorno.put("erroMsg", "A nova senha e a senha de confirmação não são iguais!!!");
        } else {

            Tabela tabela = new Tabela("usuarios");
            tabela.addColunaI("i_usuarios", jsonDados.optInt("codigo", 0), true);
            tabela.addColunaS("senha", jsonDados.optString("senha2", ""));

            Conexao conexao = new Conexao();
            conexao.conectar();
            sucesso = conexao.executaComando(tabela.getUpdate());
            conexao.desconectar();

            if (!sucesso) {
                retorno.put("erroMsg", "Erro gravando alteração de senha!!!");
            }

        }

        retorno.put("retorno", sucesso);

        return retorno;
    }

}
