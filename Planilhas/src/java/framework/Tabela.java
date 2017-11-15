
package framework;

public class Tabela {

    public String insertColunas = "";
    public String insertValores = "";
    public String updateLista = "";
    public String whereLista = "";
    public String nomeTabela;

    private int codigo;

    public String valor_pk;

    public Tabela(String tabela) {
        this.nomeTabela = tabela;
        this.codigo = 0;
    }

    public void addColuna(String coluna, String valor, boolean chave) {

        boolean autoIncrement;

        autoIncrement = (valor.equals("") || valor.equals("0")) && chave;

        if (!autoIncrement) {
            if (insertColunas.equals("")) {
                insertColunas = coluna;
                insertValores = valor;
            } else {
                insertColunas += "," + coluna;
                insertValores += "," + valor;
            }
        }
        
        if (!chave) {
            if (updateLista.equals("")) {
                updateLista = coluna + "=" + valor;
            } else {
                updateLista += "," + coluna + "=" + valor;
            }
        } else {
            if (whereLista.equals("")) {
                whereLista = coluna + "=" + valor;
            } else {
                whereLista = whereLista + " and " + coluna + "=" + valor;
            }
            valor_pk = valor;
        }
    }

    public void addColuna(String coluna, String valor) {
        addColuna(coluna, valor, false);
    }

    public String getInsert() {
        return "insert into " + nomeTabela + "(" + insertColunas + ") values(" + insertValores + ");";
    }

    public String getUpdate() {
        return "update " + nomeTabela + " set " + updateLista + " where " + whereLista + ";";
    }

    public String getDelete() {
        return "delete from " + nomeTabela + " where " + whereLista + ";";
    }

    public void addColunaS(String coluna, String valor) {
        valor = valor.replaceAll("'", "''");
        addColuna(coluna, "'" + valor + "'");
    }

    public void addColunaD(String coluna, String valor) {

        if (valor.length() == 10) {
            String lsData = valor.substring(6, 10) + "-"
                    + valor.substring(3, 5) + "-"
                    + valor.substring(0, 2);
            addColuna(coluna, "date('" + lsData + "')");
        }
    }

    public void addColunaExp(String coluna, String valor) {
        addColuna(coluna, valor);
    }

    public void addColunaI(String coluna, int valor) {

        if (coluna.equals("codigo")) {
            setCodigo(valor);
        }

        addColuna(coluna, String.valueOf(valor));
    }

    public void addColunaI(String coluna, int valor, boolean chave) {
        addColuna(coluna, String.valueOf(valor), chave);
    }

    public boolean isNewRecord() {
        return valor_pk.equals("") || valor_pk.equals("0");
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

}
