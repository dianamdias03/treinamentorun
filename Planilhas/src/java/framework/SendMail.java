package framework;

import java.util.Base64;

public class SendMail {

    public boolean gravarDB(String nome,
            String email,
            String titulo,
            String conteudo) {

        byte[] conteudoBytes = conteudo.getBytes();
        String encoded = Base64.getEncoder().encodeToString(conteudoBytes);

        //        byte[] barr = Base64.getDecoder().decode(encoded);
        //        String barr2 = new String(barr);
        String sql = String.format("INSERT INTO treinos.emailsend(nome,email,titulo,conteudo,"
                + "entrada,envio_primeiro,envio_ultimo,estado,qtdeEnvios,prioridade)\n"
                + "VALUES(\n"
                + "'%s', '%s', '%s', '%s', now(),\n"
                + "null, null, 0, 0, 10);",
                nome, email, titulo, encoded);

        Conexao conexao = new Conexao();
        conexao.conectar();
        boolean retorno = conexao.executaComando(sql);
        conexao.desconectar();

        return retorno;
    }

}
