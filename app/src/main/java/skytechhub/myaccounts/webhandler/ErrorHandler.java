package skytechhub.myaccounts.webhandler;

import android.app.Activity;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.component.CustomProgressBar;
import skytechhub.myaccounts.component.CustomViews;
import skytechhub.myaccounts.component.StringValidation;
import skytechhub.myaccounts.utils.AlertMessages;

/**
 * Created by Asiya A Khatib on 1/20/2016.
 */

// Unexpected response code 401 for http://52.76.82.24:8080/wowperksbackend-0.0.1-SNAPSHOT/api/wishlist/
/// class to handle volley errors
public class ErrorHandler {

    public static void handleVolleyError(Activity activity, VolleyError error) {
        try {
            Log.v("***Error", "-----" + error.toString());

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                if (error instanceof NoConnectionError) {
                    CustomViews.showAlert(activity, AlertMessages.ALERT_NO_CONNECTIVITY, "");
                } else {
                    CustomViews.showAlert(activity, AlertMessages.ALERT_TIMEOUT, "");
                }

            } else {
                NetworkResponse response = error.networkResponse;

                Log.v("***Error-code", "-----" + response.statusCode);

                if (StringValidation.validateString(response.toString()) && StringValidation.validateString(response.data.toString())) {
                    String rs = new String(response.data);
                    Log.v("***Error-Response", "-----" + response.toString() + "---data---" + rs);
                    JSONObject jsonObject = new JSONObject(rs);
                    String errorMessage = jsonObject.getString("Msg");

                    if (errorMessage.equals("Username Already Exist")) {
                        String currentOTP = jsonObject.getString("OTP");
                        Log.v("****** Current-OTP", "" + currentOTP);
                        AppController.preferences.savePreference("OTP", currentOTP);
                        AppController.preferences.savePreference_Boolean("go_to_OTP", true);
                    } else if (errorMessage.equals("Invalid otp")) {
                        CustomViews.showAlert(activity, "Invalid OTP, please check and try again.", "finish");
                    } else {
                        CustomViews.showAlert(activity, errorMessage, "finish");
                    }
                } else {
                    CustomViews.showAlert(activity, AlertMessages.ALERT_SERVER_MAINTAINCE, "finish");
                }
            }
        } catch (Exception e) {
            CustomViews.showAlert(activity, AlertMessages.ALERT_SERVER_MAINTAINCE, "finish");
        } finally {
            CustomProgressBar.dismissProgressDialog();
        }

//        try {
//            if (error instanceof NoConnectionError) {
//                Log.d("NoConnectionError>>>>>>", "NoConnectionError.......");
//                Toast.makeText(activity,"no connection error...!", Toast.LENGTH_SHORT).show();
//
////                            ErrorMessage.logErrorMessage(getString(R.string.connection_error_msg), context);
//            } else if (error instanceof AuthFailureError) {
//                Log.d("AuthFailureError>>>>>>", "AuthFailureError.......");
//                Toast.makeText(activity,"Authentication failure error...!", Toast.LENGTH_SHORT).show();
//
////                            ErrorMessage.logErrorMessage(getString(R.string.authFailure_error_msg), context);
//            } else if (error instanceof ServerError) {
//                Log.d("ServerError>>>>>>>>>", "ServerError.......");
//                Toast.makeText(activity,"Server error...!", Toast.LENGTH_SHORT).show();
////                            ErrorMessage.logErrorMessage(getString(R.string.server_connection_error_msg), context);
//            } else if (error instanceof NetworkError) {
//                Log.d("NetworkError>>>>>>>>>", "NetworkError.......");
////                            ErrorMessage.logErrorMessage(getString(R.string.network_error_msg), context);
//            } else if (error instanceof ParseError) {
//                Log.d("ParseError>>>>>>>>>", "ParseError.......");
//                Toast.makeText(activity,"Parser error...!", Toast.LENGTH_SHORT).show();
//
////                            ErrorMessage.logErrorMessage(getString(R.string.parse_error_msg), context);
//            } else if (error instanceof TimeoutError) {
//                Log.d("TimeoutError>>>>>>>>>", "TimeoutError.......");
//                Toast.makeText(activity,"Timeout error...!", Toast.LENGTH_SHORT).show();
////                            ErrorMessage.logErrorMessage(getString(R.string.timeout_error_msg), context);
//            }
//            CustomProgressBar.dismissProgressDialog();
//        } catch (Exception e) {
//            Log.e("Exception : ", "Exception in class - JsonErrorsCheck " + " :: " + e.getMessage());
//            e.printStackTrace();
//            CustomProgressBar.dismissProgressDialog();
//        }

    }
}
