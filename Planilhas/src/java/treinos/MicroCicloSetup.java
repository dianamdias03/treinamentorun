/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treinos;

import framework.Conexao;
import framework.FormatacaoDatas;
import java.util.Date;


/**
 *
 * @author u0180759
 */
public class MicroCicloSetup {

    public boolean gerarMicroCiclos(String lsInicioPeriodo) {
        String sql;
        boolean resultado;

        Conexao conexao = new Conexao();
        conexao.conectar();

        sql = "insert into micro_ciclo (i_clientes, i_usuarios, inicio, fim, Situacao, comentario_treinador, comentario_atleta)\n"
                + "select i_clientes, i_usuarios, cast('" + lsInicioPeriodo + "' as date) as inicio, DATE_ADD(cast('" + lsInicioPeriodo + "' as date), INTERVAL 6 DAY) as fim,\n"
                + "       0 as Situacao, '' as comentario_treinador, '' comentario_atleta\n"
                + "from usuarios\n"
                + "where not exists(select 1 from micro_ciclo where micro_ciclo.i_usuarios = usuarios.i_usuarios and micro_ciclo.inicio = cast('" + lsInicioPeriodo + "' as date));";

        resultado = conexao.executaComando(sql);
        if (!resultado) {
            return resultado;
        }

        /*
        sql = "insert into micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado)\n"
                + "select i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado\n"
                + "from (\n"
                + "select m.i_micro_ciclo, m.i_clientes, m.i_usuarios, 0 as tipo, date_add(m.inicio, interval i_dia -1 day) as dia, 1 as ordem, \n"
                + "	  1 i_tipos_modalidades, 1 i_tipos_intensidades, 4 as i_tipos_treinos, 1 i_tipos_distancias, 1 i_tipos_percursos, '' descricao, \n"
                + "      0 tempo_treino_minimo, 0 tempo_treino_maximo, 0 tempo_treino_realizado, 0 fc_media, 0 distancia, 0 i_micro_ciclo_treinos_planejado\n"
                + "from micro_ciclo m, semana s\n"
                + "where m.inicio = cast('" + lsInicioPeriodo + "' as date)\n"
                + ") as tab\n"
                + "where not exists( select 1 from micro_ciclo_treinos t where tab.dia = t.dia and tab.i_usuarios=t.i_usuarios )\n"
                + "order by tab.i_usuarios, tab.dia;";

        resultado = conexao.executaComando(sql);
        */

        return resultado;
    }

    public static void main(String[] args) {

        String lsDiaInicio = "04/09/2017";
        String lsDiaFim = "31/12/2017";
        FormatacaoDatas formatacaoDatas;
        MicroCicloSetup microCicloSetup = new MicroCicloSetup();

        Date diaInicio;
        Date diaFim;

        diaInicio = FormatacaoDatas.dmyToDate(lsDiaInicio);
        diaFim = FormatacaoDatas.dmyToDate(lsDiaFim);

        formatacaoDatas = new FormatacaoDatas(diaInicio);

        while (diaFim.after(formatacaoDatas.getDia())) {

            System.out.println(
                    formatacaoDatas.getDataYMD()
                    + " " + formatacaoDatas.getDataDMY()
                    + " " + formatacaoDatas.siglaMes()
                    + " " + formatacaoDatas.siglaSemana()
                    + " " + formatacaoDatas.diaMes());

            formatacaoDatas.addDia(7);
            microCicloSetup.gerarMicroCiclos(formatacaoDatas.getDataYMD());

        }

    }
}
