package skytechhub.myaccounts.component;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import skytechhub.myaccounts.R;


/**
 * Created by Asiya A Khatib on 12/7/2015.
 */
public class CustomViews {
    public static MaterialDialog.Builder dialog;
    private static int currentPage = 0;
    private static Handler handler = new Handler();

    public static void showToastMessage(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static void getscreenDimens(Activity activity) {
        // ScreenDimensions dimensions = new ScreenDimensions();

        int width, height;
        DisplayMetrics displayMetrics;
        displayMetrics = activity.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int dp = (int) (200 / displayMetrics.density);
        float px = dp * (displayMetrics.densityDpi / 160f);
        height = Math.round(px);
        float px1 = dpWidth * (displayMetrics.densityDpi / 160f);
        width = Math.round(px1);
        //dimensions.setHeight(height);
        //dimensions.setWidth(width);
        //return dimensions;

    }


//    public static void addDotToLayouts(int size, LinearLayout layout, Activity activity) {
//        try {
//
//            if (layout.getChildCount() > 0) {
//                layout.removeAllViews();
//            }
//
//            for (int i = 0; i < size; i++) {
//                ImageButton imgDot = new ImageButton(activity);
//                imgDot.setTag(i);
//                imgDot.setImageResource(R.drawable.dot_selector);
//                imgDot.setBackgroundResource(0);
//                imgDot.setPadding(5, 5, 5, 5);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
//                imgDot.setLayoutParams(params);
//                if (i == 0)
//                    imgDot.setSelected(true);
//
//                layout.addView(imgDot);
//            }
//        } catch (Exception e) {
//            Log.v("Exception in Home views", "" + e.getMessage());
//        }
//
//    }


    public static void viewPagerDetails(int size, int pos, LinearLayout layout) {
        try {
            for (int i = 0; i < size; i++) {
                if (i != pos) {
                    ((ImageView) layout.findViewWithTag(i)).setSelected(false);
                }
            }
            ((ImageView) layout.findViewWithTag(pos)).setSelected(true);
        } catch (Exception e) {
            Log.v("Exception in Home views", "" + e.getMessage());
        }


    }

    public static void showAlert(final Activity activity, String msg, String type) {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .content(msg)
                .positiveText("ok")
                .show();
    }

    public static void showProgress(Activity activity) {
        dialog = new MaterialDialog.Builder(activity);
        dialog.content("Checking pin").progress(true, 0).progressIndeterminateStyle(true).show();
    }

    public static void dismissProgress(Activity activity) {
        dialog.dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });


    }

//    public static void setColorToListLogo(ImageView imageview, String name) {
//        String firstLetter;
//        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
//        // generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getRandomColor();
//        // declare the builder object once.
//        TextDrawable.IBuilder builder = TextDrawable.builder()
//                .beginConfig()
//                .endConfig()
//                .round();
//
//        if (name != null) {
//            firstLetter = String.valueOf(name.charAt(0));
//
//        } else {
//            firstLetter = "T";
//        }
//
//        // reuse the builder specs to create multiple drawables
//        TextDrawable ic1 = builder.build(firstLetter, color2);
//        imageview.setImageDrawable(ic1);
//    }


}
