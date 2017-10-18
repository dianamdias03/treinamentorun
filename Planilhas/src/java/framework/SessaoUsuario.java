package framework;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;

public class SessaoUsuario {

    HttpSession session;

    public SessaoUsuario(HttpSession session) {
        this.session = session;
    }

    public boolean carregaUsuario(Conexao conexao, String email) {

        String sql = "select i_usuarios, i_clientes, nome, email, "
                + " admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha "
                + " from usuarios "
                + " where email='" + email + "'"
                + " order by 1, 2";

        ResultSet rs = conexao.executaSelect(sql);

        try {
            while (rs.next()) {
                session.setAttribute("i_usuarios", rs.getInt("i_usuarios"));
                session.setAttribute("i_clientes", rs.getInt("i_clientes"));
                session.setAttribute("nome", rs.getString("nome"));
                session.setAttribute("email", rs.getString("email"));
                session.setAttribute("admin", rs.getInt("admin"));
                session.setAttribute("cria_planilhas", rs.getInt("cria_planilhas"));
                session.setAttribute("cria_usuarios", rs.getInt("cria_usuarios"));
                session.setAttribute("cria_eventos", rs.getInt("cria_eventos"));
                session.setAttribute("recebe_planilha", rs.getInt("recebe_planilha"));
            }
        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo dados do usuário no controle de sessao: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean validaUsuarioSenha(String usuario, String senha) throws IOException {

        boolean retorno;
        Conexao conexao = new Conexao();
        conexao.conectar();

        usuario = usuario.toLowerCase();

        String sql = "select 1 "
                + " from usuarios "
                + " where email='" + usuario + "'"
                + " and senha='" + senha + "'";

        ResultSet rs = conexao.executaSelect(sql);

        try {
            retorno = rs.next();
            Arquivo.gravarLog(usuario + " - validado.");
            if (retorno) {
                carregaUsuario(conexao, usuario);
            }

        } catch (SQLException ex) {
            retorno = false;
        }
        
        conexao.desconectar();

        return retorno;
    }

    public boolean isLogado() {
        return session.getAttribute("email") != null;
    }

    public boolean isAdm() {
        return session.getAttribute("admin").toString().equals("1");
    }

    public boolean isCriaPlanilhas() {
        return session.getAttribute("cria_planilhas").toString().equals("1");
    }

    public boolean isCriaUsuarios() {
        return session.getAttribute("cria_usuarios").toString().equals("1");
    }

    public boolean isCriaEventos() {
        return session.getAttribute("cria_eventos").toString().equals("1");
    }

    public boolean isRecebePlanilhas() {
        return session.getAttribute("recebe_planilha").toString().equals("1");
    }

    public String getNome() {
        return session.getAttribute("nome").toString();
    }

    public String getEmail() {
        return session.getAttribute("email").toString();
    }

    public int getCodigoUsuario() {
        return Integer.parseInt(session.getAttribute("i_usuarios").toString());
    }

    public int getCodigoCliente() {
        return Integer.parseInt(session.getAttribute("i_clientes").toString());
    }

}
