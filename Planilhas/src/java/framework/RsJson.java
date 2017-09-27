/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RsJson {

    private Conexao conexao;

    public JSONArray getJsonBySQL(String sql) throws IOException {

        JSONArray lista = null;
        try {
            if (conexao == null) {
                setConexao(new Conexao());
                conexao.conectar();
            }
            java.sql.ResultSet rs = conexao.executaSelect(sql);
            lista = convert(rs);
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
        }
        return lista;
    }

    public JSONArray getJsonBySQLStr(String sql) throws IOException {
        JSONArray json = getJsonBySQL(sql);
        return json;
    }

    public static JSONArray convert(ResultSet rs)
            throws SQLException, JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();

        int numColumns = rsmd.getColumnCount();

        while (rs.next()) {
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnLabel(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }

        return json;
    }

    public void setConexao(Conexao conexao) {
        this.conexao = conexao;
    }
}
