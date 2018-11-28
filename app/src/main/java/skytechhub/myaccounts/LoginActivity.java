package skytechhub.myaccounts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, Response.ErrorListener, Response.Listener<JSONObject>, OnClickListener {

    ProgressDialog loading;
    public static boolean isLogin = false;
    private Button btn_register;
    private TextView txt_forgotpass;
    private CheckBox chk_box;

    private static final String LOGIN_URL = "http://newmyaccounts.skytechhub.com/android/login.php";
    private static final int REQUEST_READ_CONTACTS = 0;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  ProgressDialog progressdialog5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (!AppController.preferences.getPreference("isLogin", "").contains("true")) {
            setContentView(R.layout.activity_login);
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });
            chk_box=(CheckBox)findViewById(R.id.chk_box);
            chk_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {

                        // show password

                        mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        Log.i("checker", "true");
                    } else {
                        Log.i("checker", "false");

                        // hide password

                        mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }

                }
            });


            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    attemptLogin();
                }
            });
            txt_forgotpass = (TextView) findViewById(R.id.txt_forgotpass);
            txt_forgotpass.setOnClickListener(this);

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        } else {
            if (AppController.preferences.getPreference("role", "").equals("Admin")) {
                if (isOnline())
                {
                    getClientDetails();
                }
            }
            else if (AppController.preferences.getPreference("role", "").equals("Admin") )
            {
                if (isOnline())
                {
                    getClientDetails();
                }
            }

            else if (AppController.preferences.getPreference("role", "").equals("Employee")  ) {
                if (isOnline())
                {
                    getClientDetails();
                }

            }
            else if (AppController.preferences.getPreference("role", "").equals("Employee"))
            {
                if (isOnline())
                {
                    getClientDetails();
                }
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("No Internet connection or poor network");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return false;
        }
        return true;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//false

            showProgress(false);
            login();
        }
    }

    private void login() {

        String email = mEmailView.getText().toString();
        AppController.preferences.savePreference("username", email);
        String password = mPasswordView.getText().toString();

        goLogin(email, password);
    }

    private void goLogin(String email, String password) {
        String urlsuffix = "?emailid=" + email + "&password=" + password;
        class UserLoginTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                // TODO: attempt authentication against a network service.

                try {
                    URL url = new URL(LOGIN_URL + s);
                    Log.e("doInBackground: ", String.valueOf(url));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                    // Simulate network access.
                } catch (Exception e) {
                    return "Exception: " + e.getMessage();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(final String success) {
                showProgress(false);
                Log.e("onPostExecute: ", success);
                if (success.equals("Login Successful")) {
                    loading.dismiss();
                    getClientDetails();
                    AppController.preferences.savePreference("isLogin", "true");

                } else {
                    AppController.preferences.savePreference("isLogin", "false");
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    loading.dismiss();
                }
            }

            @Override
            protected void onCancelled() {
                showProgress(false);
            }
        }
        UserLoginTask srf = new UserLoginTask();
        srf.execute(urlsuffix);


    }

    private void getClientDetails() {
         progressdialog5 = new ProgressDialog(LoginActivity.this);
        progressdialog5.setMessage("Please Wait....");
        progressdialog5.show();
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/android/details.php?emailid=" + AppController.preferences.getPreference("username", ""), null, LoginActivity.this, LoginActivity.this);
        Log.e("getClientid OTP: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }



     void Showdialog() {
            LayoutInflater li = LayoutInflater.from(LoginActivity.this);
            final View promptsView = li.inflate(R.layout.forgotpass, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setTitle("Forgot Password!");
            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText edt_forgot_email = (EditText) promptsView.findViewById(R.id.edt_forgot_email);
            final Button btn_submit = (Button) promptsView.findViewById(R.id.btn_submit);
            final Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);





            edt_forgot_email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!isValidMail((edt_forgot_email))) {
                        edt_forgot_email.setError("Email not valid");
                    } else {
                        edt_forgot_email.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();
         btn_back.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {
                 alertDialog.dismiss();
             }
         });
            // show it
            alertDialog.show();


            btn_submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    newpassword();
                }

                private void newpassword() {
                    if (edt_forgot_email.getError() != null) {
                      //  Toast.makeText(LoginActivity.this, "Filed is Required", Toast.LENGTH_SHORT).show();
                    } else {
                        String email = edt_forgot_email.getText().toString().trim();
                        if (TextUtils.isEmpty(email)) {
                            edt_forgot_email.setError("Field is empty");
                            return;
                        }

                        sendToServer(email);
                    }


                }

                private void sendToServer(String email) {

                    String urlSuffix = "?emailid=" + email.replace(" ", "%20");

                    class forgotpassword extends AsyncTask<String, Void, String> {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(LoginActivity.this, "Please Wait...", null, true, true);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                          //  Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                            if (s.equals("Success")) {
                                android.app.AlertDialog.Builder reorder = new android.app.AlertDialog.Builder(LoginActivity.this);
                                reorder.setMessage("New password sent by SMS to your registered mobile number!.");
                                reorder.setCancelable(true);
                                reorder.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                dialog.dismiss();
                                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                android.app.AlertDialog orderError = reorder.create();
                                orderError.show();
                            } else {
                                android.app.AlertDialog.Builder reorder = new android.app.AlertDialog.Builder(LoginActivity.this);
                                reorder.setMessage("failed. Please try again/after sometime... ");
                                reorder.setCancelable(true);
                                reorder.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                            }
                                        });
                                android.app.AlertDialog orderError = reorder.create();
                                orderError.show();
                            }
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            String s = params[0];
                            BufferedReader bufferedReader = null;
                            try {
                                URL url = new URL(URLClass.FORGOTPASS_URL + s);
                                Log.e("doInBackground: ", String.valueOf(url));
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                                String result;
                                result = bufferedReader.readLine();
                                return result;

                            } catch (Exception e) {
                                return String.valueOf(e);
                            }
                        }
                    }
                    forgotpassword ba = new forgotpassword();
                    ba.execute(urlSuffix);
                }
            });
        }

    private boolean isValidMail(EditText email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email.getText());
        check = m.matches();
        return check;
    }

    @Override
    public void onClick(View view) {
        if (view==txt_forgotpass)
        {
            Showdialog();
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        login();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("Response", String.valueOf(response));
        try {
            JSONArray jsonArray = response.getJSONArray("Details");
//            loading.dismiss();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                AppController.preferences.savePreference("role", object.getString("role"));
                AppController.preferences.savePreference("discard", object.getString("discard"));
                AppController.preferences.savePreference("name", object.getString("name"));
                AppController.preferences.savePreference("phone", object.getString("phone"));
                AppController.preferences.savePreference("companyname", object.getString("companyname"));
                AppController.preferences.savePreference("editreport",object.getString("editreport"));


                progressdialog5.dismiss();
            }

            if (AppController.preferences.getPreference("role", "").equals("Admin") && (AppController.preferences.getPreference("discard", "").equals("No")))
            {
                Intent intent = new Intent(LoginActivity.this, Admin.class);
                startActivity(intent);
                finish();
            }
            else if (AppController.preferences.getPreference("role", "").equals("Admin") && (AppController.preferences.getPreference("discard", "").contains("Yes")))
            {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                }
                builder.setTitle("Deactivated")
                        .setMessage("You are deactivated my MyAccounts Admin.Please contact admin for further queries.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }


            else if (AppController.preferences.getPreference("role", "").equals("Employee")&& (AppController.preferences.getPreference("discard", "").contains("No"))) {
                Intent intent = new Intent(LoginActivity.this, Employee.class);
                startActivity(intent);
                finish();
            }
            else if (AppController.preferences.getPreference("role", "").equals("Employee") && (AppController.preferences.getPreference("discard", "").contains("Yes"))) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(LoginActivity.this);
                }
                builder.setTitle("Deactivated")
                        .setMessage("You are deactivated my MyAccounts Admin.Please contact admin for further queries.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

}




