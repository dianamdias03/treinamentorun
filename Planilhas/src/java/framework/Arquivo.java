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
            System.err.println("Erro: "+ex.toString());;
        }
    }
    
    public static void gravarLog(String conteudo){
        //gravaArquivo("c:\\temp\\log_t.txt", conteudo, true);
    }
}
