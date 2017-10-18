<%@page import="framework.Arquivo"%>
<%@page import="framework.SessaoUsuario"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.json.JSONObject"%>
<%
    String usuario = "";
    String senha = "";
    String msg = "";
    String params;
    boolean sucesso = false;

    if (request.getParameter("acao") == null) {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        if (br != null) {
            params = br.readLine();
        } else {
            params = "";
        }

        try {
            JSONObject jsonObj = new JSONObject(params);
            usuario = jsonObj.optString("inputEmail");
            senha = jsonObj.optString("inputPassword");
        } catch (Exception e) {
            msg = "Erro parser JSON";
        }

        if (usuario == null) {
            usuario = "";
        }
        if (senha == null) {
            senha = "";
        }

        SessaoUsuario sessaoUsuario = new SessaoUsuario(session);

        if (sessaoUsuario.validaUsuarioSenha(usuario, senha)) {
            session.setAttribute("emailUsuario", usuario);
            Arquivo.gravarLog("Usuário logado: "+usuario);
            sucesso = true;
        } else {
            session.invalidate();
            msg = "Usuário ou senha inválido!";
            sucesso = false;
        }

        JSONObject jsonRetorno = new JSONObject();
        jsonRetorno.put("resultado", sucesso);
        jsonRetorno.put("usuario", usuario);
        jsonRetorno.put("msg", msg);
        jsonRetorno.put("get", request.getParameter("acao"));

        out.print(jsonRetorno.toString());
    } else {
        session.invalidate();
        out.print("<html>");
        out.print("<body>");
        out.print("<script>document.location.href = './login.html';</script>");
        out.print("</body>");
        out.print("</html>");
    }
%>