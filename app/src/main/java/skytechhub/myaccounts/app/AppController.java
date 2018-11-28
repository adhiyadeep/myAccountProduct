package skytechhub.myaccounts.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import skytechhub.myaccounts.AppController.ApplicationPreferences;
import skytechhub.myaccounts.webhandler.CustomVolleyRequestQueue;


public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    public static int imgNumber = 10;

    private static AppController mInstance;
    private static Context context;

    public static ApplicationPreferences preferences;
//    public static DataBaseHandler dbHandler;

    public static RequestQueue requestQueue;
    public static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this.getApplicationContext();
        preferences = new ApplicationPreferences(context);
//        dbHandler = new DataBaseHandler(context, "");
        requestQueue = CustomVolleyRequestQueue.getInstance(context)
                .getRequestQueue();
    }

    public static synchronized Context getContext() {
        return context;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
//            imageLoader = new ImageLoader(this.requestQueue, new LruBitmapCache());
        }
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null)
            requestQueue.cancelAll(tag);
    }

}
