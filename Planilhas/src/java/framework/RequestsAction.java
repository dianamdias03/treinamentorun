
package framework;

import org.json.JSONObject;

public abstract class RequestsAction {
    
    private final JSONObject params;

    public abstract JSONObject doAction();

    public JSONObject getParams() {
        return params;
    }
    
    public RequestsAction(JSONObject params){
        this.params = params;
    }
    
}
