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
    public long lastID;

    public Gravar(JspWriter out) {
        this.out = out;
    }

    public void requestParams(HttpServletRequest request) {
        String params;
        Arquivo.gravarLog("setParams: " + "Inicio");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            params = br.readLine();
        } catch (IOException ex) {
            Arquivo.gravarLog("setParams: ex: " + "Erro");
            Logger.getLogger(Gravar.class.getName()).log(Level.SEVERE, null, ex);
            params = "{\"erro\":\"Erro lendo parametros\"}";
        }

        setParams(params);
        Arquivo.gravarLog("setParams: " + "Fim");
    }

    public void setParams(String params) {
        Arquivo.gravarLog("setParams: str=" + params);
        try {
            jsonRequestParam = new JSONObject(params);
        } catch (Exception e) {
            jsonRequestParam = new JSONObject();
            Arquivo.gravarLog("**Erro**" + e.toString());
        }
        if (!isCreateNewRecord()) {
            defineTabela();
        }
        Arquivo.gravarLog("setParams: ok=" + jsonRequestParam.toString());
    }

    public boolean isCreateNewRecord() {
        return this.getJsonRequestParam().getJSONObject("params").optInt("acao", 0) == 1;
    }

    public boolean isDeleteRecord() {
        return this.getJsonRequestParam().getJSONObject("params").optInt("acao", 0) == 2;
    }

    public boolean update() {

        String sql;
        boolean resultado;

        if (tabela.isNewRecord()) {
            sql = this.getTabela().getInsert();
        } else {
            if (this.isDeleteRecord()) {
                sql = this.getTabela().getDelete();
            } else {
                sql = this.getTabela().getUpdate();
            }
        }

        Arquivo.gravarLog(sql);

        Conexao conexao = new Conexao();
        conexao.conectar();
        resultado = conexao.executaComando(sql);
        this.lastID = conexao.lastID;

        return resultado;

//        Arquivo.gravarLog(this.getTabela().getDelete());
    }

    public void testGravar(String params) {

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
