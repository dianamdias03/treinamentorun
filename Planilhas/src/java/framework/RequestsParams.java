package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

public class RequestsParams {
    
    private JSONObject jsonRequest;
    
    public void setParams(HttpServletRequest request) {
        String params;
//        Arquivo.gravarLog("setParams: " + "Inicio");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            params = br.readLine();
//            Arquivo.gravarLog("readLine="+params);
        } catch (IOException ex) {
            Arquivo.gravarLog("setParams: ex: " + "Erro");
            Logger.getLogger(Gravar.class.getName()).log(Level.SEVERE, null, ex);
            params = "{\"erro\":\"Erro lendo parametros\"}";
        }
        
//        Arquivo.gravarLog("setParams: " + "Fim");
        
        setParams(params);
        
        return;
    }
    
    public void setParams(String params) {
//        Arquivo.gravarLog("setParams: str=" + params);
        try {
            jsonRequest = new JSONObject(params);
        } catch (Exception e) {
            jsonRequest = new JSONObject();
            Arquivo.gravarLog("**Erro**" + e.toString());
        }
        
//        Arquivo.gravarLog("setParams: ok=" + jsonRequest.toString());
    }
    
    public JSONObject getJsonRequest() {
        return jsonRequest;
    }
    
    public void setJsonRequest(JSONObject jsonObj) {
        this.jsonRequest = jsonObj;
    }

        
}
