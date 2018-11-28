package skytechhub.myaccounts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;

/**
 * Created by Deep on 13-10-2017.
 */
public class IcomeEmpEditReport extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    //Track
    private TextView select_date, add_date, select_income_billdate, add_income_billdate, txt_idincome;
    private EditText income_billNumber, income_amount, receivedFrom, income_chequeNumber, income_bankName, income_purpose, income_remarks;
    private Spinner spinner_income_paymentmode;
    private Button btn_incomeSubmit;


    private List<String> payment = new ArrayList<>();
    ProgressDialog loading;

    private boolean isSelectDate = false, isIncomeChequeDate = false, isExpenseDate = false, isExpenseBillDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.icomeedit);

        txt_idincome = (TextView) findViewById(R.id.txt_idincome);

        select_date = (TextView) findViewById(R.id.select_date_income2);
        add_date = (TextView) findViewById(R.id.add_date_income2);
        select_income_billdate = (TextView) findViewById(R.id.select_billdate_income2);
        add_income_billdate = (TextView) findViewById(R.id.add_income_billdate2);
        income_amount = (EditText) findViewById(R.id.income_amount2);
        income_billNumber = (EditText) findViewById(R.id.income_bill_number2);
        spinner_income_paymentmode = (Spinner) findViewById(R.id.spinner_paymentmode2);
        income_purpose = (EditText) findViewById(R.id.purpose2);
        receivedFrom = (EditText) findViewById(R.id.receivedFrom2);
        income_remarks = (EditText) findViewById(R.id.remarks2);
        income_bankName = (EditText) findViewById(R.id.income_cheque_bankName2);
        income_chequeNumber = (EditText) findViewById(R.id.income_cheque2);
        btn_incomeSubmit = (Button) findViewById(R.id.btn_income_submit2);

      add_date.setText(getIntent().getStringExtra("billdate"));
        //add_income_billdate.setText(getIntent().getStringExtra("billdate"));
        income_amount.setText(getIntent().getStringExtra("amount"));
        income_chequeNumber.setText(getIntent().getStringExtra("chequeno"));
        income_bankName.setText(getIntent().getStringExtra("bankname"));
        receivedFrom.setText(getIntent().getStringExtra("rfrom"));
        income_purpose.setText(getIntent().getStringExtra("purpose"));
        income_remarks.setText(getIntent().getStringExtra("remark"));
        income_billNumber.setText(getIntent().getStringExtra("billno"));
        txt_idincome.setText(getIntent().getStringExtra("id"));


        select_income_billdate.setOnClickListener(this);

        btn_incomeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        String name = getIntent().getStringExtra("paymentmode");

        select_date.setOnClickListener(this);
//        select_date.performClick();
        payment.add("<<select>>");
        payment.add("Cash");
        payment.add("Cheque");
        payment.add("Credit Card");
        payment.add("Debit Card");
        payment.add("Net Banking");
        payment.add("Others");
        payment.add("Paytm");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IcomeEmpEditReport.this, android.R.layout.simple_spinner_item, payment);
        spinner_income_paymentmode.setAdapter(adapter);

        spinner_income_paymentmode.setSelection(payment.indexOf(name));

        spinner_income_paymentmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner_income_paymentmode.getSelectedItem().toString();
                if (items.equals("Cheque")) {
                    income_chequeNumber.setVisibility(View.VISIBLE);
                    income_bankName.setVisibility(View.VISIBLE);
                } else {
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
        }
    }

    private void update() {
        String billNo = income_billNumber.getText().toString();
        String datebill = add_date.getText().toString();
        String amt = income_amount.getText().toString();
        String paymentMode = spinner_income_paymentmode.getSelectedItem().toString();
        String chequeNumber = income_chequeNumber.getText().toString();
        String bankName = income_bankName.getText().toString();
        String receivedfrom = receivedFrom.getText().toString();
        String purpose = income_purpose.getText().toString();
        String remarks = income_remarks.getText().toString();
        String editid = txt_idincome.getText().toString();


        sendToServer(billNo, datebill, amt, paymentMode, chequeNumber, bankName, receivedfrom, purpose, remarks,editid);

    }

    private void sendToServer(String billNo, String datebill, String amt, String paymentMode, String chequeNumber, String bankName, String receivedfrom, String purpose, String remarks,String editid) {

        DateFormat df = new SimpleDateFormat("dd / MM / yyyy, HH:mm");
        String android_date = df.format(Calendar.getInstance().getTime());

        String urlSuffix =  "?billno=" + billNo + "&billdate=" + datebill.replace(" ", "%20") + "&amount=" + amt + "&paymentmode=" + paymentMode.replace(" ", "%20") + "&chequeno=" + chequeNumber.replace(" ", "%20") + "&bankname=" + bankName.replace(" ", "%20") + "&rname=" + receivedfrom.replace(" ", "%20") + "&purpose=" + purpose.replace(" ", "%20") + "&remark=" + remarks.replace(" ", "%20") +"&id="+editid;


        class submit_income extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(IcomeEmpEditReport.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.e("onPostExecute: ", s);
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(IcomeEmpEditReport.this);
                    reorder.setMessage("Income updated successfully.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (AppController.preferences.getPreference("role", "").equals("Admin")) {
                                        Intent intent = new Intent(IcomeEmpEditReport.this,Track.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(IcomeEmpEditReport.this, EmployeeReport.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    dialog.dismiss();

                                }
                            });
                    AlertDialog orderError = reorder.create();
                    orderError.show();

                } else {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(IcomeEmpEditReport.this);
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
                    URL url = new URL(URLClass.EDIT_INCOME + s);
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
    public void onClick(View v) {
        if (v == select_date) {
            isSelectDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(IcomeEmpEditReport.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select bill date");
            pickerDialog.show(IcomeEmpEditReport.this.getFragmentManager(), "DatePicker");
        } else if (v == select_income_billdate) {
            isIncomeChequeDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(IcomeEmpEditReport.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select billing date");
            pickerDialog.show(IcomeEmpEditReport.this.getFragmentManager(), "DatePicker");
        }
    }
}

