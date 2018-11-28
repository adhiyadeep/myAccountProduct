package skytechhub.myaccounts.component;

import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;


public class CheckDeviceProfile {

    public static int getDeviceProfile(Context context) {


        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode())

        {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp", "Silent mode");
                return 1;


            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i("MyApp", "Vibrate mode");
                return 2;

            case AudioManager.RINGER_MODE_NORMAL:
                Log.i("MyApp", "Normal mode");
                return 3;

        }
        return 1;
    }


    public static Uri getDefaultNotificationSound() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return uri;
    }

}

