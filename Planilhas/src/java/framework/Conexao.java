package framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    Connection conn = null;
    public long lastID;

    public boolean conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("Erro carregando a classe");
            System.out.println("SQLException: " + ex.getMessage());
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/treinos?user=root&password=a1b2c3");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
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
            //stmt.execute(sql);
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                lastID = rs.getInt(1);
            }
            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
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

}
