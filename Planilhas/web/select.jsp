<%@page import="framework.Arquivo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="framework.RsJson"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="framework.Conexao"%>
<%

    String tabela = "";
    String params;
    String sql;
    JSONObject jsonObj;

    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    if (br != null) {
        params = br.readLine();
    } else {
        params = "";
    }

    if (params == null) {
        JSONObject jsonObjParams = new JSONObject();
        jsonObjParams.put("tab", "atleta_micro_ciclo_treinos");
        jsonObjParams.put("i_usuarios", 4);
        jsonObjParams.put("i_micro_ciclo", 1);
        jsonObj = new JSONObject();
        jsonObj.put("params", jsonObjParams);
        params = jsonObj.toString();
    }

    try {
        jsonObj = new JSONObject(params);
        tabela = jsonObj.getJSONObject("params").optString("tab", "");
    } catch (Exception e) {
        jsonObj = new JSONObject();
        out.print("**Erro**" + e.toString());
    }

    Arquivo.gravarLog(params);

    Conexao conexao = new Conexao();
    conexao.conectar();

    RsJson rsJson = new RsJson();
    rsJson.setConexao(conexao);

    sql = "";
    int i_clientes = 1;

    if (tabela.equals("atletas")) {
        sql = "select i_usuarios, i_clientes, nome from usuarios order by 1, 2";
    }
    if (tabela.equals("atleta_micro_ciclo")) {
        int i_usuarios = jsonObj.getJSONObject("params").optInt("i_usuarios", 0);
        String dia = jsonObj.getJSONObject("params").optString("dia", "2017-09-25");
        dia = "cast('" + dia + "' as date)";
        if (jsonObj.getJSONObject("params").optInt("navegacao", 0) == 1) {
            dia = "date_add("+ dia + ", interval 7 day )";
        }
        if (jsonObj.getJSONObject("params").optInt("navegacao", 0) == -1) {
            dia = "date_add("+ dia + ", interval -7 day )";
        }
        sql = "select i_micro_ciclo, i_clientes, i_usuarios, inicio, fim, situacao, comentario_treinador, comentario_atleta from micro_ciclo where i_usuarios = " + i_usuarios + " and inicio=" + dia;
    }
    if (tabela.equals("atleta_micro_ciclo_treinos")) {
        int i_usuarios = jsonObj.getJSONObject("params").optInt("i_usuarios", 0);
        int i_micro_ciclo = jsonObj.getJSONObject("params").optInt("i_micro_ciclo", 0);
        sql = "select i_micro_ciclo_treinos as codigo, i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, "
                + "i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, "
                + "tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, "
                + "i_micro_ciclo_treinos_planejado,"
                + "(select s.descricao from tipos_modalidades s where s.i_clientes=p.i_clientes and s.i_tipos_modalidades=p.i_tipos_modalidades) as s_tipos_modalidades, "
                + "(select s.descricao from tipos_treinos s where s.i_clientes=p.i_clientes and s.i_tipos_treinos=p.i_tipos_treinos) as s_tipos_treinos, "
                + "(select s.descricao from tipos_intensidades s where s.i_clientes=p.i_clientes and s.i_tipos_intensidades=p.i_tipos_intensidades) as s_tipos_intensidades, "
                + "(select s.descricao from tipos_percursos s where s.i_clientes=p.i_clientes and s.i_tipos_percursos=p.i_tipos_percursos) as s_tipos_percursos, "
                + "(select s.descricao from tipos_distancias s where s.i_clientes=p.i_clientes and s.i_tipos_distancias=p.i_tipos_distancias) as s_tipos_distancias, "
                + "1 as ctrl_status "
                + "from micro_ciclo_treinos p "
                + "where i_clientes = " + i_clientes + " and i_usuarios = " + i_usuarios + " and i_micro_ciclo=" + i_micro_ciclo
                + " order by dia, ordem";
    }
    if (tabela.equals("tipos_modalidades")) {
        sql = "select i_tipos_modalidades as codigo, i_clientes, descricao from tipos_modalidades where i_clientes = " + i_clientes + " order by 1, 2";
    }
    if (tabela.equals("tipos_treinos")) {
        sql = "select i_tipos_treinos as codigo, i_clientes, descricao from tipos_treinos where i_clientes = " + i_clientes + " order by 1, 2";
    }
    if (tabela.equals("tipos_intensidades")) {
        sql = "select i_tipos_intensidades as codigo, i_clientes, descricao from tipos_intensidades where i_clientes = " + i_clientes + " order by 1, 2";
    }
    if (tabela.equals("tipos_percursos")) {
        sql = "select i_tipos_percursos as codigo, i_clientes, descricao from tipos_percursos where i_clientes = " + i_clientes + " order by 1, 2";
    }
    if (tabela.equals("tipos_distancias")) {
        sql = "select i_tipos_distancias as codigo, i_clientes, descricao from tipos_distancias where i_clientes = " + i_clientes + " order by 1, 2";
    }

//    out.print(sql);
    Arquivo.gravarLog("sql: " + sql);
    JSONArray dados = rsJson.getJsonBySQLStr(sql);

    if (tabela.equals("atleta_micro_ciclo_treinos")) {
        JSONArray dados2 = new JSONArray();
        JSONObject jsonItem;
        JSONArray jsonLinha = new JSONArray();
        String dia = "";
        for (int i = 0; i < dados.length(); i++) {
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

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String lsDia = format.format((Date) jsonItem.get("dia"));
            jsonItem.put("dia", lsDia);

            if (!dia.equals(jsonItem.get("dia").toString())) {
                jsonLinha = new JSONArray();
                jsonNew.put("dia", jsonItem.get("dia"));
                jsonNew.put("diaF", jsonItem.get("dia").toString().substring(0, 5));
                jsonNew.put("Itens", jsonLinha);
                dados2.put(jsonNew);
                dia = jsonItem.get("dia").toString();
            }
            jsonLinha.put(jsonItem);
        }

        if (dados.length() == 0) {
            jsonLinha = new JSONArray();
            JSONObject jsonNew = new JSONObject();
            jsonNew.put("dia", "18/09/2017");
            jsonNew.put("diaF", "18/09/2017".substring(0, 5));
            jsonNew.put("Itens", jsonLinha);
            dados2.put(jsonNew);
        }

        dados = dados2;
    }

    JSONObject jsonRetorno = new JSONObject();
    jsonRetorno.put("registros", dados);
    jsonRetorno.put("params", jsonObj);
    jsonRetorno.put("resultado", dados.length()>0);

    out.print(jsonRetorno.toString());

%>
