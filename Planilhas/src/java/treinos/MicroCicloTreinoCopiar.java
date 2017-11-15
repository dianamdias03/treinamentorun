package treinos;

import framework.Arquivo;
import framework.Conexao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;

public class MicroCicloTreinoCopiar {

    Conexao conexao;

    public static void main(String[] args) {
        MicroCicloTreinoCopiar microCicloTreinoCopiar = new MicroCicloTreinoCopiar();
        microCicloTreinoCopiar.copiar(4, "2017-09-18", 4, "2017-10-23");

    }

    public boolean copiar(int i_usuariosOrigem, String inicioOrigem, int i_usuariosDestino, String inicioDestino) {

        boolean retorno;
        conexao = new Conexao();
        conexao.conectar();

        int i_microCicloOrigem = getCodigoMicroCiclo(i_usuariosOrigem, inicioOrigem);
        int i_microCicloDestino = getCodigoMicroCiclo(i_usuariosDestino, inicioDestino);

        try {
            if (i_microCicloOrigem == 0 || i_microCicloDestino == 0) {
                System.out.println("Erro identificando codigos de micro ciclos");
                Arquivo.gravarLog("Erro identificando codigos de micro ciclos");
                throw new Exception();
            }

            if (!getExcluirMicroCiclo(i_microCicloDestino)) {
                throw new Exception();
            }

            copiaMicroCicloTreinos(i_microCicloOrigem, i_microCicloDestino, i_usuariosDestino, inicioOrigem, inicioDestino);
            retorno = true;
        } catch (Exception e) {
            System.out.println("Processo de copia abortado.");
            Arquivo.gravarLog("Processo de copia abortado.");
            retorno = false;
        }

        conexao.desconectar();

        return retorno;
    }

    public int getCodigoMicroCiclo(int i_usuarios, String inicio) {

        int i_microCiclo = 0;

        String sql = "select m.i_micro_ciclo from micro_ciclo m where m.inicio = date('" + inicio + "') and m.i_usuarios = " + i_usuarios + ";";

        ResultSet rs = conexao.executaSelect(sql);

        try {
            while (rs.next()) {
                i_microCiclo = rs.getInt("i_micro_ciclo");
            }
        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro lendo codigo do micro ciclo: " + ex.getMessage());
        }

        return i_microCiclo;
    }

    public boolean getExcluirMicroCiclo(int i_micro_ciclo) {

        boolean retorno = false;

        String sql = "delete from micro_ciclo_treinos where i_micro_ciclo = " + i_micro_ciclo + ";";
        
        if (conexao.executaComando(sql)) {
            retorno = true;
        } else {
            System.out.println("Erro excluindo micro ciclo " + sql);
            Arquivo.gravarLog("Erro excluindo micro ciclo " + sql);
        }

        return retorno;
    }

    public boolean copiaMicroCicloTreinos(int i_microCicloOrigem, int i_microCicloDestino, int i_usuarios, String inicioOrigem, String inicioDestino) {

        String sql;
        boolean retorno = false;

        sql = "insert into micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, \n"
                + "		i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, \n"
                + "		descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, \n"
                + "		fc_media, distancia, i_micro_ciclo_treinos_planejado, feedback)\n"
                + "select " + i_microCicloDestino + ", i_clientes, " + i_usuarios + ", tipo,\n"
                + "		date_add(dia, INTERVAL DATEDIFF( date('" + inicioDestino + "'), date('" + inicioOrigem + "')) DAY) as cp_dia, ordem, i_tipos_modalidades, \n"
                + "		i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, \n"
                + "		descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, \n"
                + "		fc_media, distancia, i_micro_ciclo_treinos_planejado, feedback\n"
                + "        from micro_ciclo_treinos\n"
                + "       where i_micro_ciclo = " + i_microCicloOrigem + "\n"
                + "order by dia;";

        if (conexao.executaComando(sql)) {
            retorno = true;
        } else {
            System.out.println("Erro copiando micro ciclo " + sql);
            Arquivo.gravarLog("Erro copiando micro ciclo " + sql);
        }

        return retorno;

    }

    public boolean updateMicroCiclo(JSONObject registro) {

        boolean retorno = false;
        int situacao = registro.optInt("situacao");
        int i_micro_ciclo = registro.optInt("i_micro_ciclo");
        String comentario_treinador = registro.optString("comentario_treinador");

        String sql = "update micro_ciclo_treinos "
                + " set situacao=" + situacao
                + "   , situacao=data_conclusao=(case when data_conclusao is null and situacao="+situacao+" then now() else null end)" 
                + "   , comentario_treinador='"+comentario_treinador+"'" 
                + " where i_micro_ciclo = " + i_micro_ciclo + ";";

   
        if (conexao.executaComando(sql)) {
            retorno = true;
        } else {
            System.out.println("Erro alterando micro ciclo " + sql);
            Arquivo.gravarLog("Erro alterando micro ciclo " + sql);
        }

        return retorno;
    }
}
