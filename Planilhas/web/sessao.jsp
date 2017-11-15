<%@page import="framework.FormatacaoDatas"%>
<%@page import="java.util.Enumeration"%>
<%@page import="framework.SessaoUsuario"%>
<%@page import="org.json.JSONObject"%>



<%
    SessaoUsuario sessaoUsuario = new SessaoUsuario(session);
    if (request.getAttribute("dados") == null) {

        JSONObject dadosSessao = new JSONObject();
        
        FormatacaoDatas formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setCurrentDate();
        
//        Enumeration keys = session.getAttributeNames();
//        while (keys.hasMoreElements()) {
//            String key = (String) keys.nextElement();
//            dadosSessao.put(key, session.getAttribute(key).toString());
//        }
        if (sessaoUsuario.isLogado()) {
            dadosSessao.put("recebe_planilha", sessaoUsuario.isRecebePlanilhas());
            dadosSessao.put("i_usuarios", sessaoUsuario.getCodigoUsuario());
            dadosSessao.put("i_clientes", sessaoUsuario.getCodigoCliente());
            dadosSessao.put("cria_planilhas", sessaoUsuario.isCriaPlanilhas());
            dadosSessao.put("admin", sessaoUsuario.isAdm());
            dadosSessao.put("nome", sessaoUsuario.getNome());
            dadosSessao.put("cria_usuarios", sessaoUsuario.isCriaUsuarios());
            dadosSessao.put("cria_eventos", sessaoUsuario.isCriaEventos());
            dadosSessao.put("email", sessaoUsuario.getEmail());
            dadosSessao.put("atualizacao", formatacaoDatas.getDataDMY_HMS());
        }
        dadosSessao.put("logado", sessaoUsuario.isLogado());
        out.print(dadosSessao.toString());

    } else {
        out.print("<html><body>");
        out.print("Dados do objeto de sessão:");
        out.print("<lu>");
        if (sessaoUsuario.isLogado()) {
            out.print("<li>i_clientes: " + sessaoUsuario.getCodigoCliente());
            out.print("<li>i_usuarios: " + sessaoUsuario.getCodigoUsuario());
            out.print("<li>nome: " + sessaoUsuario.getNome());
            out.print("<li>e-mail: " + sessaoUsuario.getEmail());
            out.print("<li>adm: " + sessaoUsuario.isAdm());
            out.print("<li>cria_eventos: " + sessaoUsuario.isCriaEventos());
            out.print("<li>cria_planilhas: " + sessaoUsuario.isCriaPlanilhas());
            out.print("<li>cria_usuarios: " + sessaoUsuario.isCriaUsuarios());
            out.print("<li>recebe_planilhas: " + sessaoUsuario.isRecebePlanilhas());
        }
        out.print("<li>Logado: " + sessaoUsuario.isLogado());
        out.print("</lu>");
        out.print("<br><br><br>");
        out.print("Todas as propriedades da sessão:");
        out.print("<lu>");
        Enumeration keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            out.print("<li>" + key + "=" + session.getAttribute(key).toString());
        }
        out.print("</lu>");
        out.print("</body></html>");
    }
%>


