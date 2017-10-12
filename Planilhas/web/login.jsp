<%@page import="framework.SessaoUsuario"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.json.JSONObject"%>
<%
    String usuario = "";
    String senha = "";
    String msg = "";
    String params;

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
            JSONObject jsonRetorno = sessaoUsuario.getUsuarios();
            session.setAttribute("i_clientes", jsonRetorno.optInt("i_clientes", 0));
            session.setAttribute("i_usuarios", jsonRetorno.optInt("codigo", 0));
            session.setAttribute("cria_planilhas", jsonRetorno.optInt("cria_planilhas", 0));
        } else {
            session.removeAttribute("emailUsuario");
            msg = "Usuário ou senha inválido!";
        }

        JSONObject jsonRetorno = new JSONObject();
        jsonRetorno.put("resultado", (session.getAttribute("emailUsuario") != null));
        jsonRetorno.put("usuario", usuario);
//        jsonRetorno.put("senha", senha);
//        jsonRetorno.put("params", params);
        jsonRetorno.put("msg", msg);
        jsonRetorno.put("get", request.getParameter("acao"));

        out.print(jsonRetorno.toString());
    } else {
        out.print("<html>");
        out.print("<body>");
        out.print("<script>document.location.href = './login.html';</script>");
        out.print("</body>");
        out.print("</html>");
    }
%>