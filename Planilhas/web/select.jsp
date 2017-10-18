<%@page import="framework.SessaoUsuario"%>
<%@page import="framework.FormatacaoDatas"%>
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
    JSONArray dados = new JSONArray();

    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    if (br != null) {
        params = br.readLine();
    } else {
        params = "";
    }

    if (params == null) {
//        JSONObject jsonObjParams = new JSONObject();
//        jsonObjParams.put("tabela", "atleta_micro_ciclo_treinos");
//        jsonObjParams.put("i_usuarios", 4);
//        jsonObjParams.put("i_micro_ciclo", 1);
//        jsonObj = new JSONObject();
//        jsonObj.put("params", jsonObjParams);
//        params = jsonObj.toString();
//        params = "{\"params\":{\"tabela\":\"atleta_micro_ciclo_treinos\",\"i_usuarios\":4,\"i_micro_ciclo\":19}}";
//        params = "{\"params\":{\"tabela\":\"tipos_modalidades\",\"i_usuarios\":4,\"i_micro_ciclo\":19}}";
        params = "{\"params\":{\"tabela\":\"usuarios\"}}";
    }

    try {
        jsonObj = new JSONObject(params);
        tabela = jsonObj.getJSONObject("params").optString("tabela", "");
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

    SessaoUsuario sessaoUsuario = new SessaoUsuario(session);

    if (sessaoUsuario.isLogado()) {

        if (tabela.equals("usuarios")) {
            if (sessaoUsuario.isCriaUsuarios()) {
                sql = "select i_usuarios as codigo, i_clientes, nome, email, senha, data_nascto, "
                        + "admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha, "
                        + "cpf, rg, endereco, cidade, estado, cep, "
                        + "observacoes, telefone_1, telefone_2 "
                        + "from usuarios "
                        + "order by 1, 2";
            }
        }
        if (tabela.equals("atletas")) {
            String dia = jsonObj.getJSONObject("params").optString("dia", "2017-09-25");
            String lsWhere = "";
            dia = "cast('" + dia + "' as date)";

            if (!sessaoUsuario.isCriaPlanilhas()) {
                lsWhere = " where i_usuarios=" + sessaoUsuario.getCodigoUsuario() + " ";
            }

            sql = "select i_usuarios as codigo, i_clientes, nome, email,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 0 day ) ),-1 ) as Dia2,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 1 day ) ),-1 ) as Dia3,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 2 day ) ),-1 ) as Dia4,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 3 day ) ),-1 ) as Dia5,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 4 day ) ),-1 ) as Dia6,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 5 day ) ),-1 ) as Dia7,"
                    + "       coalesce( (select min(i_tipos_modalidades) from micro_ciclo_treinos t where t.i_usuarios = u.i_usuarios and dia = date_add( " + dia + ", interval 6 day ) ),-1 ) as Dia8"
                    + "  from usuarios u "
                    + " " + lsWhere
                    + " order by 1, 2";
        }
        if (tabela.equals("atleta_micro_ciclo")) {
            int i_usuarios = jsonObj.getJSONObject("params").optInt("i_usuarios", 0);
            String dia = jsonObj.getJSONObject("params").optString("dia", "2017-09-25");
            dia = "cast('" + dia + "' as date)";
            if (jsonObj.getJSONObject("params").optInt("navegacao", 0) == 1) {
                dia = "date_add(" + dia + ", interval 7 day )";
            }
            if (jsonObj.getJSONObject("params").optInt("navegacao", 0) == -1) {
                dia = "date_add(" + dia + ", interval -7 day )";
            }
            sql = "select i_micro_ciclo, i_clientes, i_usuarios, inicio, fim, situacao, comentario_treinador, comentario_atleta from micro_ciclo where i_usuarios = " + i_usuarios + " and inicio=" + dia;
        }
        if (tabela.equals("atleta_micro_ciclo_treinos")) {
            int i_usuarios = jsonObj.getJSONObject("params").optInt("i_usuarios", 0);
            int i_micro_ciclo = jsonObj.getJSONObject("params").optInt("i_micro_ciclo", 0);
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

//        out.print(sql);
        Arquivo.gravarLog("sql: " + sql);

        if (!sql.equals("")) {
            dados = rsJson.getJsonBySQLStr(sql);
        }

        if (tabela.equals("atleta_micro_ciclo_treinos")) {
            FormatacaoDatas formatacaoDatas;
            JSONArray dados2 = new JSONArray();
            JSONObject jsonItem;
            JSONArray jsonLinha = new JSONArray();
            Date dia = new Date();
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
            String lsDia = jsonObj.getJSONObject("params").optString("dia", "2017-09-25");
            formatacaoDatas = new FormatacaoDatas(FormatacaoDatas.ymdToDate(lsDia));
            for (int i = 0; i < 7; i++) {
                JSONObject jsonNew = new JSONObject();
                jsonLinha = new JSONArray();
                jsonNew.put("dia", formatacaoDatas.getDataDMY());
                jsonNew.put("diaF", formatacaoDatas.diaMes());
                jsonNew.put("diaS", formatacaoDatas.diaSemana());

                for (int j = 0; j < dados.length(); j++) {
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
        }

        if (tabela.equals("usuarios") && dados != null) {
            JSONObject jsonItem;
            for (int i = 0; i < dados.length(); i++) {
                jsonItem = dados.getJSONObject(i);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (jsonItem.has("data_nascto")) {
                    String lsDia = format.format((Date) jsonItem.get("data_nascto"));
                    jsonItem.put("data_nascto", lsDia);
                }
            }
        }

        if (tabela.equals("atleta_micro_ciclo")) {
            JSONObject jsonItem;
            FormatacaoDatas formatacaoDatas;

            for (int i = 0; i < dados.length(); i++) {
                jsonItem = dados.getJSONObject(i);
                formatacaoDatas = new FormatacaoDatas(FormatacaoDatas.ymdToDate(jsonItem.get("inicio").toString()));
                jsonItem.put("inicioF", formatacaoDatas.diaMes());
                formatacaoDatas = new FormatacaoDatas(FormatacaoDatas.ymdToDate(jsonItem.get("fim").toString()));
                jsonItem.put("fimF", formatacaoDatas.diaMes());
            }
        }

    }
    
    conexao.desconectar();

    JSONObject jsonRetorno = new JSONObject();
    jsonRetorno.put("registros", dados);
    jsonRetorno.put("params", jsonObj);
    jsonRetorno.put("resultado", (dados != null && dados.length() > 0));

    out.print(jsonRetorno.toString());

%>
