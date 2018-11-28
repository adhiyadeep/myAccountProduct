package skytechhub.myaccounts.component;


import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;


public class CustomProgressBar {

//    public static AlertDialog alertDialog;


    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity context, String message, boolean isCancelable) {
        Log.e("Progress bar ....", "Progress bar showing..........");
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
//        progressDialog.setTitle("Loading");
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        try {


            Log.e("Progress bar ....", "Progress bar dismissed..........");
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            Log.v("dismiss progress bar",""+e.getMessage());
        }
    }

//    public static void showProgressBar(Activity activity, String message) {
//        try {
//
//            // AlertDialog.Builder builder = new AlertDialog.Builder(activity);
////            AlertDialog.Builder builder = new AlertDialog.Builder(activity,
////                    R.style.MyTheme);
////
////            LayoutInflater inflater = (LayoutInflater) activity
////                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////            View layout = inflater.inflate(R.layout.custom_progressbar,
////                    (ViewGroup) activity.findViewById(R.id.linear_progress));
////
////            TextView text = (TextView) layout.findViewById(R.id.message);
////            text.setText(message);
////            // builder.setView(layout);
////            alertDialog = builder.create();
////            alertDialog.setView(layout, 0, 0, 0, 0);
////            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////            // alertDialog.getWindow().setBackgroundDrawable(
////            // new ColorDrawable(android.graphics.Color.TRANSPARENT));
////            // alertDialog.setInverseBackgroundForced(true);
////            alertDialog.setCancelable(false);
////            alertDialog.show();
//        } catch (Exception e) {
//            Log.v("exeption", e.toString());
//        }
//
//    }
//
//    public static void dismissProgressBar() {
//        try {
//            alertDialog.dismiss();
//        } catch (Exception e) {
//
//        }
//
//
//    }

}
