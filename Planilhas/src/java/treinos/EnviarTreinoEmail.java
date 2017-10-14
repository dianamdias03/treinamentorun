package treinos;

import framework.Arquivo;
import framework.RequestsAction;
import org.json.JSONArray;
import org.json.JSONObject;
import framework.EnviarEmail;

public class EnviarTreinoEmail extends RequestsAction {

    public EnviarTreinoEmail(JSONObject params) {
        super(params);
    }

    @Override
    public JSONObject doAction() {

        JSONObject retorno = new JSONObject();

        JSONObject emailConteudo = getHtmlTreino(
                getParams().getJSONObject("enviarPlanilha").optInt("i_clientes", 0),
                getParams().getJSONObject("enviarPlanilha").optInt("i_usuarios", 0),
                getParams().getJSONObject("enviarPlanilha").optString("inicio", "2017-09-18"));

        EnviarEmail enviarEmail = new EnviarEmail();
        enviarEmail.enviarHtml(
                emailConteudo.optString("email"),
                emailConteudo.optString("titulo"),
                emailConteudo.optString("conteudo")
        );
        retorno.put("Email", "Enviado");

        return retorno;
    }

    public JSONObject getHtmlTreino(int i_clientes, int i_usuarios, String inicio) {

        JSONObject retorno = new JSONObject();
        JSONObject dados;
        StringBuilder sb = new StringBuilder();

        ConsultasSQL consultasSQL = new ConsultasSQL();
        dados = consultasSQL.planilhaSemanaAtleta(i_clientes, i_usuarios, inicio);

        Arquivo.gravarLog(dados.toString());

        sb.append(tag("b", "<span style=\"font-size:x-large\">Atleta: " + dados.optString("nome") + "</span>"));
        sb.append("<br>");
        sb.append(
                tag("b", "<span style=\"font-size:x-large\">"
                        + "Periodo: "
                        + dados.optString("inicioFormatado", "") + " a "
                        + dados.optString("fimFormatado", "")
                        + "</span>")
        );
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<br>");

        JSONArray semana = dados.getJSONArray("semana");

        for (int i = 0; i < semana.length(); i++) {
            JSONObject dia = semana.getJSONObject(i);

            String diaSemana = tag("h3", text(dia.optString("diaS") + " - " + dia.optString("diaF")));

            diaSemana = "<table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\">" + diaSemana + "</td></tr>";

            sb.append(diaSemana);
            JSONArray itens = dia.getJSONArray("Itens");
            for (int j = 0; j < itens.length(); j++) {
                JSONObject treino = itens.getJSONObject(j);
                sb.append("<tr><td style=\"padding: 15px;\">");
                if (treino.optInt("i_tipos_modalidades", 0) == 4) {
                    sb.append(tag("b", text("Folga") + "<br>"));
                } else {
                    sb.append(tag("b", text(treino.optString("s_tipos_modalidades") + ":")));
                    sb.append("<br>");
                    sb.append(text(treino.optString("descricao")));
                    sb.append("<br>");
                }
                sb.append("</td></tr>");
            }
            sb.append("</table>");
            sb.append("<br>");
        }
        sb.append("<br>");
        sb.append("</p>");
        sb = new StringBuilder("<html><body>" + sb.toString() + "</body></html>");

        retorno.put("email", dados.optString("email", ""));
        retorno.put("titulo", dados.optString("nomePlanilha", ""));
        retorno.put("conteudo", sb.toString());

        return retorno;
    }

    public String tag(String tag, String texto) {
        return "<" + tag + ">" + texto + "</" + tag + ">";
    }

    public String text(String texto) {

//        texto = StringEscapeUtils.escapeHtml4(texto);
        texto = texto.replaceAll(";", "&#x3B;");
        texto = texto.replaceAll(":", "&#x3A;");
        texto = texto.replaceAll("ç", "&ccedil;");
        texto = texto.replaceAll("Ç", "&Ccedil;");
        texto = texto.replaceAll("õ", "&otilde;");
        texto = texto.replaceAll("Õ", "&Otilde;");
        texto = texto.replaceAll("-", "&#45;");
        texto = texto.replaceAll("ã", "&atilde;");
        texto = texto.replaceAll("Ã", "&Atilde;");
        texto = texto.replaceAll("\\*", "&#x2A;");
        texto = texto.replaceAll("/", "&#x2F;");
        texto = texto.replaceAll("'", "&lsquo;");
        texto = texto.replaceAll("\"", "&quot;");
        texto = texto.replaceAll("\\(", "&#x28;");
        texto = texto.replaceAll("\\)", "&#x29;");
        texto = texto.replaceAll(" ", "&nbsp;");

        texto = texto.replaceAll("\n", "<br>\n");
        return texto;
    }

    public static void main(String[] args) {
        EnviarTreinoEmail enviarTreinoEmail = new EnviarTreinoEmail(null);
        JSONObject conteudo = enviarTreinoEmail.getHtmlTreino(1, 4, "2017-10-09");
        Arquivo.gravarLog(conteudo.toString());
    }

}
