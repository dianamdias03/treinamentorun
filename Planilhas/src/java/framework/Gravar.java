/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import org.json.JSONObject;

/**
 *
 * @author u0180759
 */
public abstract class Gravar {

    private JspWriter out;
    private JSONObject jsonRequestParam;
    private Tabela tabela;

    public Gravar(JspWriter out) {
        this.out = out;
    }

    public void requestParams(HttpServletRequest request) {
        String params = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (br != null) {
                params = br.readLine();
            } else {
                params = "";
            }
            setParams(params);
        } catch (IOException ex) {
            Logger.getLogger(Gravar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setParams(String params) throws IOException {
        try {
            jsonRequestParam = new JSONObject(params);
        } catch (Exception e) {
            jsonRequestParam = new JSONObject();
            out.print("**Erro**" + e.toString());
        }
        if (!isCreateNewRecord()) {
            defineTabela();
        }
    }

    public boolean isCreateNewRecord() {
        return this.getJsonRequestParam().getJSONObject("params").optInt("acao", 0) == 1;
    }

    public boolean update() {

        String sql;

        if (tabela.isNewRecord()) {
            sql = this.getTabela().getInsert();
        } else {
            sql = this.getTabela().getUpdate();
        }

        Arquivo.gravarLog(sql);

        Conexao conexao = new Conexao();
        conexao.conectar();
        return conexao.executaComando(sql);

//        Arquivo.gravarLog(this.getTabela().getDelete());
    }

    public void testGravar(String params) {

        try {
            this.setParams(params);

            if (isCreateNewRecord()) {
                System.out.println(this.newRecord().toString());
                Arquivo.gravarLog(this.newRecord().toString());
            } else {
                System.out.println(this.getTabela().getInsert());
                System.out.println(this.getTabela().getUpdate());
                System.out.println(this.getTabela().getDelete());
                Arquivo.gravarLog(this.getTabela().getInsert());
                Arquivo.gravarLog(this.getTabela().getUpdate());
                Arquivo.gravarLog(this.getTabela().getDelete());
            }

        } catch (IOException ex) {
            Logger.getLogger(Gravar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public JSONObject getJsonRequestParam() {
        return jsonRequestParam;
    }

    public void setJsonRequestParam(JSONObject jsonObj) {
        this.jsonRequestParam = jsonObj;
    }

    public Tabela getTabela() {
        return tabela;
    }

    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }

    public abstract void defineTabela();

    public abstract JSONObject newRecord();

}
