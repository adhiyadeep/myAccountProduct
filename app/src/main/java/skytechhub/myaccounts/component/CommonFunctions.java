package skytechhub.myaccounts.component;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import skytechhub.myaccounts.app.AppController;

public class CommonFunctions {
    public static float getDiscountValue(float price, int discount) {
        float d = (discount / 100) * price;
        return d;

    }


    public static int randomBox() {
        HashMap<Integer, Integer> mRandomNumStorage = new HashMap<Integer, Integer>();
        Random rand = new Random();
        int pickedNumber = rand.nextInt(10000);
        if (mRandomNumStorage.isEmpty()
                || !mRandomNumStorage.containsKey(pickedNumber)) {
            mRandomNumStorage.put(pickedNumber, pickedNumber);
        } else {
            randomBox();
        }
        return pickedNumber;

    }

    public static void vibratephone(Context context1) {
        try {
            Vibrator v = (Vibrator) context1
                    .getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void setNotificationTone(Context context) {
//        try {
//            int status = CheckDeviceProfile.getDeviceProfile(context);
//            if (status == 2) {
//                vibratephone(context);
//            } else if (status == 3) {
//                vibratephone(context);
//                MediaPlayer m = new MediaPlayer();
//                Uri uri;
//                // check settings database
//                boolean flagSound = AppController.preferences.getPreference_Boolean("notisound", false);
//                boolean flagCustomtone = AppController.preferences.getPreference_Boolean("customsound", false);
//                if (flagSound && flagCustomtone) {
//                    //  String path = AppController.preferences.getPreference("sounduri", null);
//
//                    String path = getOurCartInstance().getLocalPath();
//                    if (StringValidation.validateString(path)) {
//                        uri = Uri.parse(path);
//                    } else {
//                        uri = CheckDeviceProfile.getDefaultNotificationSound();
//                    }
//                    Log.v("uri in noti", "" + uri.toString());
//
////                Log.e("notification uri",uri.toString());
////                m.setDataSource(context1, uri);
////                m.prepare();
////                m.setVolume(10f, 10f);
////                m.setLooping(true);
////                m.start();
//
//
//                    Ringtone r = RingtoneManager.getRingtone(context, uri);
//                    r.play();
//                    Thread.sleep(300);
//                    r.stop();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void userOnlineStatus(boolean status) {
        AppController.preferences.savePreference_Boolean("online", status);
    }

    public static void cancelNotifications(Activity activity, int id) {
        NotificationManager notificationManager = (NotificationManager) activity
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (id != 0)
            notificationManager.cancel(id);
    }

    public static String getCurrentDate() {
        String dateValue;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        dateValue = dateformat.format(c.getTime());
        return dateValue;
    }

    public static String getCurrentTime() {
        String timeValue;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm aa");
        timeValue = dateformat.format(c.getTime());
        return timeValue;
    }

    public static void setTypeFaceAssets(Activity activity, String text, TextView textview) {
        Typeface tf2 = Typeface.createFromAsset(activity.getAssets(), "tapshop_icon_file1.ttf");
        textview.setText(text);
        textview.setTypeface(tf2);
    }

    public static void setTypeFaceFontAwesome(Activity activity, TextView textview) {
        Typeface tf2 = Typeface.createFromAsset(activity.getAssets(), "fontawesome-webfont.ttf");
        textview.setTypeface(tf2);
    }

    public static void setOpensansRegular(Activity activity, TextView textView) {
        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "open_sans_all/OpenSans-Regular.ttf");
        textView.setTypeface(tf1);
    }

    public static void setOpensansBold(Activity activity, TextView textView) {
        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "open_sans_all/OpenSans-Bold.ttf");
        textView.setTypeface(tf1);
    }

    public static void setRalewayBold(Activity activity, TextView textView) {
        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "Raleway/Raleway-Bold.ttf");
        textView.setTypeface(tf1);
    }

    public static void setRalewayLight(Activity activity, TextView textView) {
        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "Raleway/Raleway-Light.ttf");
        textView.setTypeface(tf1);
    }

    public static void setRalewayRegular(Activity activity, TextView textView) {
        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "Raleway/Raleway-Regular.ttf");
        textView.setTypeface(tf1);
    }

    public static void createPhoneAndMail(Activity activity, TextView textView, String s) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "phone_mail.ttf");
        textView.setText(s);
        textView.setTypeface(typeface);
    }

    public static void setQuickSandBold(Activity activity, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "quick_Sand/Quicksand-Bold.otf");
        textView.setTypeface(typeface);
    }

    public static void setOpenSansRegForET(Activity activity, EditText editText) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "open_sans_all/OpenSans-Regular.ttf");
        editText.setTypeface(typeface);
    }

    public static void setOpensansRegularForBT(Activity activity, Button button) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "open_sans_all/OpenSans-Regular.ttf");
        button.setTypeface(typeface);
    }

    public static void setHelveticaRegularForTxT(Activity activity, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "Helvetica-Regular.ttf");
        textView.setTypeface(typeface);
    }

    public static void setHelveticaRegularForEdt(Activity activity, EditText textView) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "Helvetica-Regular.ttf");
        textView.setTypeface(typeface);
    }

    public static void setHelveticaRegularForBtn(Activity activity, Button textView) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "Helvetica-Regular.ttf");
        textView.setTypeface(typeface);
    }

    //    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    public static String encodeToBase64(Activity activity, Intent data) throws FileNotFoundException {
//        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
//        image.compress(compressFormat, quality, byteArrayOS);
//        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        final Uri imageUri = data.getData();
        final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        String encodedImage = encodeImage(selectedImage);
        return encodedImage;

    }

    private static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

//    public static void logOut(Activity activity) {
//        AppController.preferences.savePreference_Boolean("isLoggedIn", false);
//        Intent loginIntent = new Intent(activity, LogInActivity.class);
//        activity.startActivity(loginIntent);
//        activity.finish();
//    }

    public static void handleVolleyError(VolleyError error, final Activity activity, String TAG) {

//        try {
//            int statusCode = error.networkResponse.statusCode;
//            String strData = new String(error.networkResponse.data);
//            Log.e("Status Code", String.valueOf(statusCode));
//            Log.e("Data ", strData);
//            JSONObject object = new JSONObject(strData);
//
//            if (statusCode == 401) {
//                JSONObject jsonObject = object.getJSONObject("error");
//                CustomDialogAdapter.showOkDialog(activity, StringConstants.INVALID_TOKEN, new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
////                        logOut(activity);
//                    }
//                });
//            } else if (statusCode == 400) {
//                JSONObject jsonObject = object.getJSONObject("error");
//                CustomDialogAdapter.showOkDialog(activity, jsonObject.getString("error_message"), null);
//                return;
//            } else {
//                ErrorHandler.handleVolleyError(activity, error);
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
    }

}