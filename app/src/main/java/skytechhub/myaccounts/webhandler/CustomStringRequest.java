package skytechhub.myaccounts.webhandler;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import skytechhub.myaccounts.app.AppController;

/**
 * Created by Asiya A Khatib on 1/20/2016.
 */
public class CustomStringRequest extends StringRequest {

    public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        if (AppController.preferences.getPreference_Boolean("isRegistered", false)) {
            headers.put("Authorization", "Bearer " + AppController.preferences.getPreference("api_key", ""));
        } else {
            headers.put("Authorization", "Basic MzQ4ODk0Mzc3ODk2Njk6c2RudmR2ODk0NXV0OWpmZ2lvZXJtZ2Zlcm1nZw==");
        }
        return headers;
    }


    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }
}
