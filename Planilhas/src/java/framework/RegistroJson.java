/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import org.json.JSONObject;

/**
 *
 * @author u0180759
 */
public class RegistroJson {

    private JSONObject registro;

    public void create() {
        registro = new JSONObject();
    }

    public void setItem(String nome, long codigo, String descricao) {
        JSONObject jsonItem = new JSONObject();
        jsonItem.put("codigo", codigo);
        jsonItem.put("descricao", descricao );
        registro.put(nome, jsonItem);
    }

    public void setItem(String nome, String valor) {
        registro.put(nome, valor);
    }

    public void setItem(String nome, long valor) {
        registro.put(nome, valor);
    }

    public void setItem(String nome, JSONObject valor) {
        registro.put(nome, valor);
    }

    public JSONObject getRegistro() {
        return registro;
    }

    public void setRegistro(JSONObject registro) {
        this.registro = registro;
    }

}
