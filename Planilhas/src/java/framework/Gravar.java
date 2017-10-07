/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import org.json.JSONObject;


public abstract class Gravar {

    private Tabela tabela;
    public long lastID;
    
    private GerenciaRequests gerenciaRequests;


    public boolean update() {

        String sql;
        boolean resultado;

        if (tabela.isNewRecord()) {
            sql = this.getTabela().getInsert();
        } else {
            if (getGerenciaRequests().isDeleteRecord()) {
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

    }

    public void testGravar(String params) {

        getGerenciaRequests().setParams(params);
        if (getGerenciaRequests().isCreateNewRecord()) {
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

    public Tabela getTabela() {
        return tabela;
    }

    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }

    public abstract void defineTabela();

    public abstract JSONObject newRecord();

    public GerenciaRequests getGerenciaRequests() {
        return gerenciaRequests;
    }

    public void setGerenciaRequests(GerenciaRequests gerenciaRequests) {
        this.gerenciaRequests = gerenciaRequests;
    }

}
