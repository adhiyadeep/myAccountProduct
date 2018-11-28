package skytechhub.myaccounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;

public class ChangePass extends AppCompatActivity {
    private EditText edt_newpass, edt_cpass;
    private Button btn_savepass;
    private CheckBox chk_box2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppController.preferences.getPreference("username", ""));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        edt_newpass = (EditText) findViewById(R.id.edt_newpass);
        edt_cpass = (EditText) findViewById(R.id.edt_cpass);
        btn_savepass = (Button) findViewById(R.id.btn_savepass);
        chk_box2 = (CheckBox) findViewById(R.id.chk_box1);


        chk_box2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {

                    // show password

                    edt_newpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_cpass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    Log.i("checker", "true");
                } else {
                    Log.i("checker", "false");

                    // hide password

                    edt_newpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_cpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });

        btn_savepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String new_pass = edt_newpass.getText().toString();
                final String c_pass = edt_cpass.getText().toString();
                if (edt_newpass.length() < 7)

                {
                    Toast.makeText(ChangePass.this, "Password length should be greater then 7 character!", Toast.LENGTH_SHORT).show();
                } else if (new_pass.equals("") || !new_pass.equals(c_pass)) {
                    Toast.makeText(ChangePass.this, "Password empty or miss match!", Toast.LENGTH_SHORT).show();
                } else {
                    change(new_pass);
                }
            }
        });
    }


    private void change(String new_pass) {
        String urlSuffix = "?emailid=" + AppController.preferences.getPreference("username", "") + "&password=" + new_pass;
        //String urlSuffix = URLEncoder.encode(urlSuffix1, "UTF-8");

        class bookingAppointment_membership extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ChangePass.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("onPostExecute: ", s);
                loading.dismiss();
                if (s.contains("Successfully updated.")) {                       //success
                    AlertDialog.Builder reorder = new AlertDialog.Builder(ChangePass.this);
                    reorder.setMessage("You successfully updated your login credential. Please login again.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    AppController.preferences.savePreference("isLogin", "false");
                                    AppController.preferences.savePreference("username", "");
                                    Intent intent = new Intent(ChangePass.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();
                } else {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(ChangePass.this);
                    reorder.setMessage("Failed. Please try again after sometime... ");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(URLClass.CHANGINGPASS_URL + s);
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
        bookingAppointment_membership ba = new bookingAppointment_membership();
        ba.execute(urlSuffix);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ChangePass.this, Admin.class);
        startActivity(intent);
        finish();
    }
}

