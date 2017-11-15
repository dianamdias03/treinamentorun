/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import framework.Arquivo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author U0180759
 */
public class PegarConteudoWeb {

    private String content_http() {
        URL url;
        try {

//            url = new URL("http://104.236.202.162/Planilhas/testeemail.html");
            url = new URL("http://104.236.202.162/Planilhas/TempEnvioEmails.jsp?lista=2&i_clientes=1&i_usuarios=6&inicio=2017-10-16");
            URLConnection con = url.openConnection();
            con.setConnectTimeout(2000);
            InputStream is = con.getInputStream();
            StringBuilder sb = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            reader.close();

            return sb.toString();
        } catch (MalformedURLException ex) {
            Arquivo.gravarLog("MalformedURLException: " + ex.getMessage());
        } catch (IOException ex) {
            Arquivo.gravarLog("IOException: " + ex.getMessage());
        }

        return "";
    }

    public static void main(String[] args) {
        PegarConteudoWeb pegarConteudoWeb = new PegarConteudoWeb();

        String conteudo = pegarConteudoWeb.content_http();
//        conteudo = conteudo.replaceAll("<\\/", "\n<\\/");

        System.out.println("Resultado: " + conteudo);
    }

}
