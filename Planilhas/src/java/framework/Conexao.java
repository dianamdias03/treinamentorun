package framework;

import java.sql.*;
import java.util.Properties;


public class Conexao {

    Connection conn = null;
    public long lastID;

    public boolean conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.out.println("Erro carregando a classe");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLException: " + ex.toString());
        }

        try {

            Properties prop = new Properties();
            prop.put("charSet", "latin1");
            prop.put("character_set_server", "latin1");
            prop.put("user", "root");
            prop.put("password", "FUOKs3BD_");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/treinos", prop);

        } catch (SQLException ex) {
            System.out.println("Erro conectando");
            System.out.println("SQLException: x " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }

        return true;
    }

    public ResultSet executaSelect(String sql) {

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            Arquivo.gravarSQL(sql);
            rs = stmt.executeQuery(sql);
            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            Arquivo.gravarLog("SQLException: " + ex.getMessage());
            Arquivo.gravarLog("SQLState: " + ex.getSQLState());
            Arquivo.gravarLog("VendorError: " + ex.getErrorCode());
        }

        return rs;
    }

    public boolean executaComando(String sql) {

        Statement stmt = null;

        try {
            stmt = conn.createStatement();

            Arquivo.gravarSQLChanges(sql);
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                lastID = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Arquivo.gravarLog("SQLException: " + ex.getMessage());
            Arquivo.gravarLog("SQLState: " + ex.getSQLState());
            Arquivo.gravarLog("VendorError: " + ex.getErrorCode());
            return false;
        }

        return true;
    }

    public String getTeste() {
        return "Texto de teste";
    }

    public void desconectar() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Arquivo.gravarLog("Erro desconectando - " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        if (conexao.conectar()) {
            conexao.executaSelect("select 1 from dummy");
        } else {
            System.out.println("Não conectou");
        }
    }

}
