package treinos;

import framework.Conexao;
import framework.FormatacaoDatas;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;

public class MicroCicloSetup {

    public boolean gerarMicroCiclos(String lsInicioPeriodo, int i_usuarios) {
        String sql;
        String whereUsuario;
        boolean resultado;

        Conexao conexao = new Conexao();
        conexao.conectar();

        if (i_usuarios > 0) {
            whereUsuario = " and i_usuarios=" + i_usuarios;
        } else {
            whereUsuario = "";
        }

        sql = "insert into micro_ciclo (i_clientes, i_usuarios, inicio, fim, Situacao, comentario_treinador, comentario_atleta)\n"
                + "select i_clientes, i_usuarios, cast('" + lsInicioPeriodo + "' as date) as inicio, DATE_ADD(cast('" + lsInicioPeriodo + "' as date), INTERVAL 6 DAY) as fim,\n"
                + "       0 as Situacao, '' as comentario_treinador, '' comentario_atleta\n"
                + "from usuarios\n"
                + "where not exists(select 1 from micro_ciclo where micro_ciclo.i_usuarios = usuarios.i_usuarios and micro_ciclo.inicio = cast('" + lsInicioPeriodo + "' as date))"
                + whereUsuario
                + ";";

        resultado = conexao.executaComando(sql);
        if (!resultado) {
            return resultado;
        }

        conexao.desconectar();

        return resultado;
    }

    public static void main(String[] args) {
        JspWriter out = null;
        MicroCicloSetup microCicloSetup = new MicroCicloSetup();
        try {
            microCicloSetup.gerarPeriodos(out, 0);
        } catch (IOException ex) {
            Logger.getLogger(MicroCicloSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gerarPeriodos(JspWriter out, int i_usuarios) throws IOException {

        String lsDiaInicio = "08/01/2018";
        String lsDiaFim = "31/12/2018";
        FormatacaoDatas formatacaoDatas;
        MicroCicloSetup microCicloSetup = new MicroCicloSetup();

        Date diaInicio;
        Date diaFim;

        diaInicio = FormatacaoDatas.dmyToDate(lsDiaInicio);
        diaFim = FormatacaoDatas.dmyToDate(lsDiaFim);

        formatacaoDatas = new FormatacaoDatas(diaInicio);

        while (diaFim.after(formatacaoDatas.getDia())) {

            String dados = formatacaoDatas.getDataYMD()
                    + " " + formatacaoDatas.getDataDMY()
                    + " " + formatacaoDatas.siglaMes()
                    + " " + formatacaoDatas.siglaSemana()
                    + " " + formatacaoDatas.diaMes();

            if (out == null) {
                System.out.println(dados);
            } else {
                out.write("<br>" + dados);
            }

            formatacaoDatas.addDia(7);
            microCicloSetup.gerarMicroCiclos(formatacaoDatas.getDataYMD(),i_usuarios);

        }

    }
}
