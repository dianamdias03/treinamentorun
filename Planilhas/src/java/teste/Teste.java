/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import framework.Arquivo;
import org.json.JSONObject;
import treinos.EnviarTreinoEmail;

/**
 *
 * @author U0180759
 */
public class Teste {

    public static void main(String[] args) {
        String texto = "Terça-feira";
        
        EnviarTreinoEmail enviarTreinoEmail = new EnviarTreinoEmail(new JSONObject());

//        texto = texto.replaceAll("ç", "x");

        texto = enviarTreinoEmail.text(texto);
        
        Arquivo.gravarLog(texto);
        System.out.println("texto: " + texto);
    }

}
