package skytechhub.myaccounts.webhandler;

import android.support.v4.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asiya A Khatib on 1/19/2016.
 */


public class CustomJSONObjectRequest extends JsonObjectRequest {

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        HashMap<String, String> headers = new HashMap<>();
        return headers;
    }


    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }
}
