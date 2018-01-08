/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author u0180759
 */
public class Arquivo {

    public static void gravaArquivo(String nomeArquivo, String conteudo, boolean append) {
        File arquivo = new File(nomeArquivo + ".txt");
        try {
            FileWriter grava = new FileWriter(arquivo, append);
            PrintWriter escreve = new PrintWriter(grava);
            escreve.println(conteudo);
            escreve.close();
            grava.close();
        } catch (IOException ex) {
            System.err.println("Erro: " + ex.toString());;
        }
    }

    public static void gravarLog(String conteudo) {
        FormatacaoDatas formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setCurrentDate();
        gravaArquivo("/temp/log_t", formatacaoDatas.getDataDMY_HMS() + " - " + conteudo, true);
    }

    public static void gravarSQL(String sql) {
        FormatacaoDatas formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setCurrentDate();
        gravaArquivo("/temp/t_sql", formatacaoDatas.getDataDMY_HMS() + " - " + sql, true);
    }

    public static void gravarSQLChanges(String sql) {
        FormatacaoDatas formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setCurrentDate();
        gravaArquivo("/temp/t_sql_changes", formatacaoDatas.getDataDMY_HMS() + " - " + sql, true);
    }
}
