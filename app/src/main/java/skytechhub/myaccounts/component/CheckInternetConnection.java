package skytechhub.myaccounts.component;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.afollestad.materialdialogs.MaterialDialog;

import skytechhub.myaccounts.R;
import skytechhub.myaccounts.utils.AlertMessages;


public class CheckInternetConnection {

    private static ConnectivityManager connectivityManager;

    private static NetworkInfo wifiInfo, mobileInfo;
    public  static Boolean isNetAvailable(Context con) {

        try {
            connectivityManager = (ConnectivityManager)con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void showNetworkAlert(Activity activity)
    {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .content(AlertMessages.ALERT_NO_CONNECTIVITY)
                .positiveText("ok")
                .show();
    }
}
