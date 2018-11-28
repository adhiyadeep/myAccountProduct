package skytechhub.myaccounts.webhandler;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import skytechhub.myaccounts.app.AppController;

/**
 * Created by Dhaval on 10/16/2016.
 */
public class CustomJSONArrayRequest extends JsonArrayRequest {
    public CustomJSONArrayRequest(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public CustomJSONArrayRequest(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + AppController.preferences.getPreference("api_key", ""));
        return headers;
    }


}
