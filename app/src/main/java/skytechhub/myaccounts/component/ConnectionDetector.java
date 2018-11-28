package skytechhub.myaccounts.component;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionDetector {

    public static boolean isConnectingToInternet(Activity context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null) {

                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

                if (networkInfo != null) {
                    for (NetworkInfo aNetworkInfo : networkInfo) {
                        if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED)
                            return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            Log.e("Connection Exception...", " Check connection tester class..." + e.getMessage());
        }
        return false;
    }
}
