/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import framework.Arquivo;
import framework.Conexao;
import framework.Tabela;
import org.json.JSONObject;
import treinos.EnviarTreinoEmail;

/**
 *
 * @author U0180759
 */
public class Teste {

    public static void main(String[] args) {

        Conexao conexao = new Conexao();
        
        conexao.conectar();
        
        conexao.executaComando("UPDATE eventosparticipacoes SET nome='Pedrão' WHERE i_eventosParticipacoes=5;");
        conexao.executaComando("UPDATE eventosparticipacoes SET nome='ãÃ-çÇ-éÉ-óÓ' WHERE i_eventosParticipacoes=7;");
        
        Tabela tabela = new Tabela("eventosparticipacoes");
        
        tabela.addColunaI("i_eventosParticipacoes", 6, true);
        tabela.addColunaI("i_eventos", 1);
        tabela.addColunaI("i_usuarios", 0);
        tabela.addColunaI("i_usuarios_convidador", 1);
        tabela.addColunaI("confirmado", 1);
        tabela.addColunaS("nome", "Maçã");
        tabela.addColunaS("controle", "");
        
        conexao.executaComando(tabela.getUpdate());
        
    }

}
