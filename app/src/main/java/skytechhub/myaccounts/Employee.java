package skytechhub.myaccounts;

import android.Manifest;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import skytechhub.myaccounts.POJO.EmployeeDetails;
import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;

public class Employee extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, Response.ErrorListener, Response.Listener<JSONObject> {

    //Track
    private TextView select_date, add_date, select_income_billdate, add_income_billdate;
    private EditText income_billNumber, income_amount, receivedFrom, income_chequeNumber, income_bankName, income_purpose, income_remarks;
    private Spinner spinner_income_paymentmode;
    private Button btn_incomeSubmit;

    private Button btn_skytechhub_income, btn_skytechhub_expense, btn_skytechhub_report;
    private List<String> payment = new ArrayList<>();
    ProgressDialog loading;

    //Expense
    private TextView select_date_expense, add_date_expense, select_billdate_expense, add_expense_billdate;
    private EditText expense_amount, expense_cheque, expense_cheque_bankName, expense_bill_number, givento, exp_purpose, exp_remarks;
    private Spinner spinner_exp_paymentmode;
    private Button btn_expense_submit;

    private boolean isSelectDate = false, isIncomeChequeDate = false, isExpenseDate = false, isExpenseBillDate = false, isCheque = false;
    private Dialog confirmDialog, changePassword;
    //exit app
    private Boolean exit = false;

    private CheckBox chk_passbox;

    //ausers changepassword
    private EditText newPassword, confirmPassword;
    private Button btn_save_password;

    public static String id, ownerid, name, phone, companyname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppController.preferences.getPreference("name", ""));
        setSupportActionBar(toolbar);
        getClientDetails();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn_skytechhub_income = (Button) findViewById(R.id.btn_skytechhub_income);
        btn_skytechhub_expense = (Button) findViewById(R.id.btn_skytechhub_expense);
        btn_skytechhub_report = (Button) findViewById(R.id.btnsky_report);


        btn_skytechhub_income.setOnClickListener(this);
        btn_skytechhub_expense.setOnClickListener(this);
        btn_skytechhub_report.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == btn_skytechhub_income) {

            showDialogIncome();
        } else if (v == select_date) {
            isSelectDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(Employee.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select income date");
            pickerDialog.show(Employee.this.getFragmentManager(), "DatePicker");
        } else if (v == select_income_billdate) {
            isIncomeChequeDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(Employee.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select billing date");
            pickerDialog.show(Employee.this.getFragmentManager(), "DatePicker");
        } else if (v == btn_skytechhub_expense) {
            showDialogExpense();
        } else if (v == select_date_expense) {
            isExpenseDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(Employee.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select expense date ");
            pickerDialog.show(Employee.this.getFragmentManager(), "DatePicker");
        } else if (v == select_billdate_expense) {
            isExpenseBillDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(Employee.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select expense billing date ");
            pickerDialog.show(Employee.this.getFragmentManager(), "DatePicker");
        } else if (v == btn_expense_submit) {
            expense();
        } else if (v == btn_skytechhub_report) {
            Intent intent = new Intent(Employee.this, EmployeeReport.class);
            startActivity(intent);
            finish();
        }
    }


    // GET Request of Admin Details Owner id ,Owner name etc
    private void getClientDetails() {
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/gettingresponse/emplogindetails.php?emailid=" + AppController.preferences.getPreference("username", ""), null, Employee.this, Employee.this);
        Log.e("getClientid OTP: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        android.app.AlertDialog.Builder reorder = new android.app.AlertDialog.Builder(Employee.this);
        reorder.setTitle("Getting Detail");
        reorder.setMessage("No detail found.");
        reorder.setCancelable(true);
        reorder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getClientDetails();
                    }
                });

        android.app.AlertDialog orderError = reorder.create();
        orderError.show();
    }

    @Override
    public void onResponse(JSONObject response) {


        Log.e("Response", String.valueOf(response));
        try {
            JSONArray jsonArray = response.getJSONArray("Details");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                EmployeeDetails employeename_pojo = new EmployeeDetails();

                id = object.getString("id");
                ownerid = object.getString("ownerid");
                name = object.getString("name");
                phone = object.getString("phone");
                companyname = object.getString("companyname");


                employeename_pojo.setId(id);
                employeename_pojo.setOwnerid(ownerid);
                employeename_pojo.setName(name);
                employeename_pojo.setPhone(phone);
                employeename_pojo.setCompanyname(companyname);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Expense
    private void showDialogExpense() {
        payment.clear();
        confirmDialog = new Dialog(Employee.this);
        confirmDialog.setContentView(R.layout.expense);
        confirmDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = confirmDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        confirmDialog.show();

        select_date_expense = (TextView) confirmDialog.findViewById(R.id.select_date_expense);
        add_date_expense = (TextView) confirmDialog.findViewById(R.id.add_date_expense);
        select_billdate_expense = (TextView) confirmDialog.findViewById(R.id.select_billdate_expense);
        add_expense_billdate = (TextView) confirmDialog.findViewById(R.id.add_expense_billdate);
        expense_amount = (EditText) confirmDialog.findViewById(R.id.expense_amount);
        expense_cheque = (EditText) confirmDialog.findViewById(R.id.expense_cheque);
        expense_cheque_bankName = (EditText) confirmDialog.findViewById(R.id.expense_cheque_bankName);
        expense_bill_number = (EditText) confirmDialog.findViewById(R.id.expense_bill_number);
        givento = (EditText) confirmDialog.findViewById(R.id.givento);
        exp_purpose = (EditText) confirmDialog.findViewById(R.id.exp_purpose);
        exp_remarks = (EditText) confirmDialog.findViewById(R.id.exp_remarks);

        spinner_exp_paymentmode = (Spinner) confirmDialog.findViewById(R.id.spinner_exp_paymentmode);

        btn_expense_submit = (Button) confirmDialog.findViewById(R.id.btn_expense_submit);
        btn_expense_submit.setOnClickListener(this);

        select_billdate_expense.setOnClickListener(this);
        select_date_expense.setOnClickListener(this);
        // select_date_expense.performClick();
        payment.add("<<select>>");
        payment.add("Cash");  //cheque
        payment.add("Cheque"); //credit card
        payment.add("Credit Card");    //debit card
        payment.add("Debit Card");   //
        payment.add("Net Banking");
        payment.add("Others");
        payment.add("Paytm");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Employee.this, android.R.layout.simple_spinner_item, payment);
        spinner_exp_paymentmode.setAdapter(adapter);

        spinner_exp_paymentmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner_exp_paymentmode.getSelectedItem().toString();
                if (items == "Cheque") {
                    isCheque = true;
                    expense_cheque.setText("");
                    expense_cheque_bankName.setText("");
                    expense_cheque.setVisibility(View.VISIBLE);
                    expense_cheque_bankName.setVisibility(View.VISIBLE);

                } else {
                    isCheque = false;
                    expense_cheque.setText("");
                    expense_cheque_bankName.setText("");
                    expense_cheque.setVisibility(View.GONE);
                    expense_cheque_bankName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void showDialogIncome() {
        payment.clear();
        confirmDialog = new Dialog(Employee.this);
        confirmDialog.setContentView(R.layout.income);
        confirmDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = confirmDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        confirmDialog.show();

        select_date = (TextView) confirmDialog.findViewById(R.id.select_date_income);
        add_date = (TextView) confirmDialog.findViewById(R.id.add_date_income);
        select_income_billdate = (TextView) confirmDialog.findViewById(R.id.select_billdate_income);
        add_income_billdate = (TextView) confirmDialog.findViewById(R.id.add_income_billdate);
        income_amount = (EditText) confirmDialog.findViewById(R.id.income_amount);
        income_billNumber = (EditText) confirmDialog.findViewById(R.id.income_bill_number);
        spinner_income_paymentmode = (Spinner) confirmDialog.findViewById(R.id.spinner_paymentmode);
        income_purpose = (EditText) confirmDialog.findViewById(R.id.purpose);
        receivedFrom = (EditText) confirmDialog.findViewById(R.id.receivedFrom);
        income_remarks = (EditText) confirmDialog.findViewById(R.id.remarks);
        income_bankName = (EditText) confirmDialog.findViewById(R.id.income_cheque_bankName);
        income_chequeNumber = (EditText) confirmDialog.findViewById(R.id.income_cheque);
        btn_incomeSubmit = (Button) confirmDialog.findViewById(R.id.btn_income_submit);

        select_income_billdate.setOnClickListener(this);

        btn_incomeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income();
            }
        });

        select_date.setOnClickListener(this);
        //  select_date.performClick();
        payment.add("<<select>>");
        payment.add("Cash");  //cheque
        payment.add("Cheque"); //credit card
        payment.add("Credit Card");    //debit card
        payment.add("Debit Card");   //
        payment.add("Net Banking");
        payment.add("Others");
        payment.add("Paytm");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Employee.this, android.R.layout.simple_spinner_item, payment);
        spinner_income_paymentmode.setAdapter(adapter);

        spinner_income_paymentmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner_income_paymentmode.getSelectedItem().toString();
                if (items.equals("Cheque")) {
                    isCheque = true;
                    income_chequeNumber.setText("");
                    income_bankName.setText("");
                    income_chequeNumber.setVisibility(View.VISIBLE);
                    income_bankName.setVisibility(View.VISIBLE);
                } else {
                    isCheque = false;
                    income_chequeNumber.setText("");
                    income_bankName.setText("");

                    income_chequeNumber.setVisibility(View.GONE);
                    income_bankName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = ++monthOfYear;
        String strMonth;
        if (month >= 1 && month <= 9) {
            strMonth = "0" + month;
        } else {
            strMonth = String.valueOf(month);
        }
        final String date = year + "-" + strMonth + "-" + dayOfMonth;

        final String date1 = dayOfMonth + "-" + strMonth + "-" + year;

        if (isSelectDate) {
            add_date.setText(date1);
            isSelectDate = false;
        } else if (isIncomeChequeDate) {
            add_income_billdate.setText(date1);
            isIncomeChequeDate = false;
            receivedFrom.setFocusableInTouchMode(true);
            receivedFrom.requestFocus();
        } else if (isExpenseDate) {
            add_date_expense.setText(date1);
            isExpenseDate = false;

        } else {
            add_expense_billdate.setText(date1);
            isExpenseBillDate = false;
            givento.setFocusableInTouchMode(true);
            givento.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skytechhub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            AppController.preferences.savePreference("isLogin", "false");
            AppController.preferences.savePreference("username", "");
            AppController.preferences.savePreference("role", "");
            AppController.preferences.savePreference("editreport", "");

            final ProgressDialog progress = new ProgressDialog(Employee.this);
            progress.setTitle("Please wait");
            progress.setMessage("We are signing out your profile");
            progress.show();

            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progress.cancel();
                    Intent intent = new Intent(Employee.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);

        } else if (id == R.id.action_changePassword_users) {
            changePassword = new Dialog(Employee.this);
            changePassword.setContentView(R.layout.dialog_changepass);
            changePassword.setCanceledOnTouchOutside(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = changePassword.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.BOTTOM;
            lp.copyFrom(window.getAttributes());
            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            changePassword.show();

            newPassword = (EditText) changePassword.findViewById(R.id.changePassword);
            confirmPassword = (EditText) changePassword.findViewById(R.id.confirmPassword);
            btn_save_password = (Button) changePassword.findViewById(R.id.btn_changePassword);

            btn_save_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newPassword.length() < 7) {
                        Toast.makeText(Employee.this, "Password length should be greater then 7 character!", Toast.LENGTH_SHORT).show();
                    } else if (newPassword.getText().toString().equals("") || newPassword.getText().toString().equals(confirmPassword)) {
                        Toast.makeText(Employee.this, "Password empty or miss match!", Toast.LENGTH_SHORT).show();
                    } else if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                        change();
                    } else {
                        AlertDialog.Builder reorder = new AlertDialog.Builder(Employee.this);
                        reorder.setTitle("Password");
                        reorder.setMessage("Entered password not matched. Please try again.");
                        reorder.setCancelable(true);
                        reorder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        newPassword.setText("");
                                        confirmPassword.setText("");
                                        dialog.dismiss();

                                    }
                                });

                        AlertDialog orderError = reorder.create();
                        orderError.show();
                    }
                }
            });

            chk_passbox = (CheckBox) changePassword.findViewById(R.id.chk_passbox);


            chk_passbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {

                        // show password

                        newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        Log.i("checker", "true");
                    } else {
                        Log.i("checker", "false");

                        // hide password

                        newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void change() {

        String newPass = newPassword.getText().toString();

        changingPassword(newPass);

    }

    private void changingPassword(String newPass) {

        String urlSuffix = "?emailid=" + AppController.preferences.getPreference("username", "") + "&password=" + newPass;
        //String urlSuffix = URLEncoder.encode(urlSuffix1, "UTF-8");

        class addingBusiness extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Employee.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s.equals("Successfully updated.")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Employee.this);
                    reorder.setTitle("Updated");
                    reorder.setMessage("You successfully updated your login credential. Please login again.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    AppController.preferences.savePreference("isLogin", "false");
                                    AppController.preferences.savePreference("username", "");
                                    Intent intent = new Intent(Employee.this, LoginActivity.class);
                                    startActivity(intent);
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
                    return new String("Exception: " + e);
                }

            }
        }
        addingBusiness ba = new addingBusiness();
        ba.execute(urlSuffix);
    }

    private void income() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        String date = DateToStr;
        String billNo = income_billNumber.getText().toString();
        String billDate = add_income_billdate.getText().toString();
        String amt = income_amount.getText().toString();
        String paymentMode = spinner_income_paymentmode.getSelectedItem().toString();
        String chequeNumber = income_chequeNumber.getText().toString();
        String bankName = income_bankName.getText().toString();
        String receivedfrom = receivedFrom.getText().toString();
        String purpose = income_purpose.getText().toString();
        String remarks = income_remarks.getText().toString();

        if (date.equals("") || amt.equals("") || billDate.equals("") || paymentMode.equals("<<select>>") || receivedfrom.equals("") || purpose.equals("")) {
            Toast.makeText(Employee.this, "Please fill all details", Toast.LENGTH_SHORT).show();
        }
        else {
            if (isCheque) {
                if (income_chequeNumber.getText().toString().equals("")) {
                    income_chequeNumber.setError("Insert cheque number");

                }
                else if (income_bankName.getText().toString().equals("")) {
                    income_bankName.setError("Enter bank name");
                }
                else {
                    sendToServer(date, billNo, billDate, amt, paymentMode, chequeNumber, bankName, receivedfrom, purpose, remarks);
                }
            }
            else
            {
                sendToServer(date, billNo, billDate, amt, paymentMode, chequeNumber, bankName, receivedfrom, purpose, remarks);
            }



        }

    }

    private void sendToServer(String date, String billNo, String billDate, String amt, String paymentMode, String chequeNumber, String bankName, String receivedfrom, String purpose, String remarks) {

        DateFormat df = new SimpleDateFormat("dd / MM / yyyy, HH:mm");
        String android_date = df.format(Calendar.getInstance().getTime());

        String urlSuffix = "?date=" + date.replace(" ", "%20") + "&billno=" + billNo + "&billdate=" + billDate.replace(" ", "%20") + "&amount=" + amt + "&modeofpayment=" + paymentMode.replace(" ", "%20") + "&chequeno=" + chequeNumber.replace(" ", "%20") + "&bankname=" + bankName.replace(" ", "%20") + "&receivedfrom=" + receivedfrom.replace(" ", "%20") + "&purpose=" + purpose.replace(" ", "%20") + "&remarks=" + remarks.replace(" ", "%20") + "&ownerid=" + ownerid.toString() + "&companyname=" + companyname.toString().replace(" ", "%20");


        class submit_income extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Employee.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.e("onPostExecute: ", s);
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Employee.this);
                    reorder.setMessage("Income Details submitted.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    confirmDialog.cancel();
                                }
                            });
                    AlertDialog orderError = reorder.create();
                    orderError.show();

                } else {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Employee.this);
                    reorder.setMessage("failed. Please try again/after sometime... ");
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
                    URL url = new URL(URLClass.income + s);
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
        submit_income ba = new submit_income();
        ba.execute(urlSuffix);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    private void expense() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        String selected_add_date_expense = DateToStr;
        String selected_add_expense_billdate = add_expense_billdate.getText().toString();
        String selected_Spinner = spinner_exp_paymentmode.getSelectedItem().toString();
        String selected_expense_amount = expense_amount.getText().toString();
        String selected_expense_cheque = expense_cheque.getText().toString();
        String selected_expense_cheque_bankName = expense_cheque_bankName.getText().toString();
        String selected_expense_bill_number = expense_bill_number.getText().toString();
        String selected_givento = givento.getText().toString();
        String purpose = exp_purpose.getText().toString();
        String selected_exp_remarks = exp_remarks.getText().toString();

        if (selected_add_date_expense.equals("") || selected_add_expense_billdate.equals("") || selected_Spinner.equals("<<select>>") || expense_amount.equals("") || givento.equals("") || purpose.equals("")) {
            Toast.makeText(Employee.this, "Please fill all details", Toast.LENGTH_SHORT).show();
        } else {
            if (isCheque) {
                if (expense_cheque.getText().toString().equals("")) {
                    expense_cheque.setError("Insert cheque number");
                } else if (expense_cheque_bankName.getText().toString().equals("")) {
                    expense_cheque_bankName.setError("Enter bank name");
                } else {
                    SendData(selected_add_date_expense, selected_add_expense_billdate, selected_Spinner, selected_expense_amount, selected_expense_cheque, selected_expense_cheque_bankName, selected_expense_bill_number, selected_givento, purpose, selected_exp_remarks);
                }

            }
            else
            {
                SendData(selected_add_date_expense, selected_add_expense_billdate, selected_Spinner, selected_expense_amount, selected_expense_cheque, selected_expense_cheque_bankName, selected_expense_bill_number, selected_givento, purpose, selected_exp_remarks);
            }

        }

    }

    private void SendData(String selected_date_expense, String selected_billdate_expense, String selected_spinner, String selected_expense_amount, String selected_expense_cheque, String selected_expense_cheque_bankName, String selected_expense_bill_number, String selected_givento, String exp_purpose, String selected_exp_remarks) {


        String urlSuffix = "?date=" + selected_date_expense.replace(" ", "%20") + "&billno=" + selected_expense_bill_number + "&billdate=" + selected_billdate_expense + "&amount=" + selected_expense_amount + "&modeofpayment=" + selected_spinner.replace(" ", "%20") + "&chequeno=" + selected_expense_cheque + "&bankname=" + selected_expense_cheque_bankName.replace(" ", "%20") + "&givento=" + selected_givento.replace(" ", "%20") + "&purpose=" + exp_purpose.replace(" ", "%20") + "&remarks=" + selected_exp_remarks.replace(" ", "%20") + "&companyname=" + companyname.toString().replace(" ", "%20") + "&ownerid=" + ownerid.toString();

        class submit_expense extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Employee.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.e("onPostExecute: ", s);
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Employee.this);
                    reorder.setMessage("Expense Details submitted.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    confirmDialog.cancel();
                                }
                            });
                    AlertDialog orderError = reorder.create();
                    orderError.show();

                } else {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(Employee.this);
                    reorder.setMessage("failed. Please try again/after sometime... ");
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
                    URL url = new URL(URLClass.expense + s);
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
        submit_expense ba = new submit_expense();
        ba.execute(urlSuffix);
    }


}