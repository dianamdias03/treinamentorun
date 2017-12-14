package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

public class GerenciaRequests {
    
    private JSONObject jsonRequestParam;
    
    public String requestParams(HttpServletRequest request) {
        String params;
//        Arquivo.gravarLog("setParams: " + "Inicio");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            params = br.readLine();
        } catch (IOException ex) {
            Arquivo.gravarLog("setParams: ex: " + "Erro");
            Logger.getLogger(Gravar.class.getName()).log(Level.SEVERE, null, ex);
            params = "{\"erro\":\"Erro lendo parametros\"}";
        }
        
//        Arquivo.gravarLog("setParams: " + "Fim");
        
        return params;
    }
    
    public void setParams(String params) {
//        Arquivo.gravarLog("setParams: str=" + params);
        try {
            jsonRequestParam = new JSONObject(params);
        } catch (Exception e) {
            jsonRequestParam = new JSONObject();
            Arquivo.gravarLog("**Erro**" + e.toString());
        }
        
//        Arquivo.gravarLog("setParams: ok=" + jsonRequestParam.toString());
    }
    
    public boolean isCreateNewRecord() {
        return this.getJsonRequestParam().getJSONObject("params").optInt("acao", 0) == 1;
    }
    
    public boolean isDeleteRecord() {
        return this.getJsonRequestParam().getJSONObject("params").optInt("acao", 0) == 2;
    }
    
    public JSONObject getJsonRequestParam() {
        return jsonRequestParam;
    }
    
    public void setJsonRequestParam(JSONObject jsonObj) {
        this.jsonRequestParam = jsonObj;
    }
    
    public JSONObject getNodeParams() {
        return this.getJsonRequestParam().getJSONObject("params");
    }
    
    public Gravar getGravar() {
        Gravar gravar = null;
        String tabela = getNodeParams().optString("tabela", "");
        
        if (tabela.equals("usuarios")) {
            gravar = new treinos.Usuarios();
        }
        
        if (tabela.equals("criarEventos")) {
            gravar = new treinos.Eventos();
        }
        
        if (tabela.equals("micro_ciclo_treinos")) {
            gravar = new treinos.GravarMicroCicloTreinos();
        }
        
        if (tabela.equals("grupos")) {
            gravar = new treinos.Grupos();
        }
        
        if (tabela.equals("treinosPreCadastrados")) {
            gravar = new treinos.TreinosPreCadastrados();
        }
        
        if (gravar == null) {
            Arquivo.gravarLog("Tabela '" + tabela + "' não definida.");
        }
        
        return gravar;
    }
    
    public String doRequest(HttpServletRequest request) {
        return doRequest(requestParams(request));
    }
    
    public String doRequest(String request) {
        
        boolean retorno = false;
        boolean acaoRealizada = false;
        long lastID = 0;
        JSONObject jsonRetorno = new JSONObject();
        Gravar gravar=null;
        
        Arquivo.gravarLog("doRequest: " + request);
        this.setParams(request);
        
        if (getJsonRequestParam().has("params")) {
            JSONObject params = this.getJsonRequestParam().getJSONObject("params");
            if (params.has("enviarPlanilha")) {
                treinos.EnviarTreinoEmail enviarTreinoEmail = new treinos.EnviarTreinoEmail(params);
                jsonRetorno = enviarTreinoEmail.doAction();
                acaoRealizada = true;
                retorno = true;
            } else if (params.has("acao-gravar")) {
                
                if (params.optString("acao-gravar").equals("consultaSQL")) {
                    treinos.RequestConsultaSQL requestConsultaSQL = new treinos.RequestConsultaSQL(params);
                    jsonRetorno = requestConsultaSQL.doAction();
                    acaoRealizada = true;
                    retorno = true;
                }
                
            }
        }
        
        if (!acaoRealizada) {
            gravar = getGravar();
            if (gravar == null) {
                Arquivo.gravarLog("doRequest-retorno: tabela não indetificada!");
                retorno = false;
            } else {
                gravar.setGerenciaRequests(this);
                if (this.isCreateNewRecord()) {
                    jsonRetorno.put("registro", gravar.newRecord());
                    retorno = true;
                } else {
                    gravar.defineTabela();
                    retorno = gravar.update();
                    lastID = gravar.lastID;
                }
            }
            acaoRealizada = true;
        }
        
        jsonRetorno.put("resultado", retorno);
        jsonRetorno.put("params", lastID);
        jsonRetorno.put("novoCodigo", lastID);
        
        if(gravar!=null){
            gravar.triggerEndRequest(jsonRetorno);
        }
        
//        Arquivo.gravarLog("doRequest-retorno: " + jsonRetorno.toString());
        
        return jsonRetorno.toString();
    }
    
}
