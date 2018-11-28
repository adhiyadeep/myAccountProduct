package skytechhub.myaccounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import skytechhub.myaccounts.Adaptor.AdminReport_Adaptor;
import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;

/**
 * Created by Deep on 12-10-2017.
 */
public class AdminReportEdit extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private TextView select_date_expense, add_date_expense, select_billdate_expense, add_expense_billdate, id;
    private EditText expense_amount, expense_cheque, expense_cheque_bankName, expense_bill_number, givento, exp_purpose, exp_remarks;
    private Spinner spinner_exp_paymentmode;
    private Button btn_update;
    ProgressDialog loading;
    private boolean isSelectDate = false, isIncomeChequeDate = false, isExpenseDate = false, isExpenseBillDate = false;

    private List<String> payment = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_edit_admindata);


        select_date_expense = (TextView) findViewById(R.id.select_date_expense1);
        add_date_expense = (TextView) findViewById(R.id.add_date_expense1);
        select_billdate_expense = (TextView) findViewById(R.id.select_billdate_expense1);
        add_expense_billdate = (TextView) findViewById(R.id.add_expense_billdate1);
        expense_amount = (EditText) findViewById(R.id.expense_amount1);
        expense_cheque = (EditText) findViewById(R.id.expense_cheque1);
        expense_cheque_bankName = (EditText) findViewById(R.id.expense_cheque_bankName1);
        expense_bill_number = (EditText) findViewById(R.id.expense_bill_number1);
        givento = (EditText) findViewById(R.id.givento1);
        exp_purpose = (EditText) findViewById(R.id.exp_purpose1);
        exp_remarks = (EditText) findViewById(R.id.exp_remarks1);
        id = (TextView) findViewById(R.id.edit_id);

        spinner_exp_paymentmode = (Spinner) findViewById(R.id.spinner_exp_paymentmode1);

        btn_update = (Button) findViewById(R.id.btn_update);

        btn_update.setOnClickListener(this);

        add_date_expense.setText(getIntent().getStringExtra("billdate"));
        expense_amount.setText(getIntent().getStringExtra("amount"));
        expense_cheque.setText(getIntent().getStringExtra("chequeno"));
        expense_cheque_bankName.setText(getIntent().getStringExtra("bankname"));
        givento.setText(getIntent().getStringExtra("givento"));
        exp_purpose.setText(getIntent().getStringExtra("purpose"));
        exp_remarks.setText(getIntent().getStringExtra("remark"));
        expense_bill_number.setText(getIntent().getStringExtra("billno"));
        id.setText(getIntent().getStringExtra("id"));


//        select_billdate_expense.setOnClickListener(this);

        String name = getIntent().getStringExtra("paymentmode");


        select_date_expense.setOnClickListener(this);
        payment.add("<<select>>");
        payment.add("Cash");
        payment.add("Cheque");
        payment.add("Credit Card");
        payment.add("Debit Card");
        payment.add("Net Banking");
        payment.add("Others");
        payment.add("Paytm");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminReportEdit.this, android.R.layout.simple_spinner_item, payment);
        spinner_exp_paymentmode.setAdapter(adapter);

        spinner_exp_paymentmode.setSelection(payment.indexOf(name));

        spinner_exp_paymentmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner_exp_paymentmode.getSelectedItem().toString();
                if (items == "Cheque") {
                    expense_cheque.setVisibility(View.VISIBLE);
                    expense_cheque_bankName.setVisibility(View.VISIBLE);

                } else {
                    expense_cheque.setVisibility(View.GONE);
                    expense_cheque_bankName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == select_date_expense) {
            isExpenseDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(AdminReportEdit.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)

            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select Bill Date");
            pickerDialog.show(AdminReportEdit.this.getFragmentManager(), "DatePicker");
        } else if (v == select_billdate_expense) {
            isExpenseBillDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(AdminReportEdit.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select expense billing date ");
            pickerDialog.show(AdminReportEdit.this.getFragmentManager(), "DatePicker");
        } else if (v == btn_update) {
            update();
        }
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

        if (isExpenseDate) {
            add_date_expense.setText(date1);
            isExpenseDate = false;
        } else {
            add_expense_billdate.setText(date1);
            isExpenseBillDate = false;
        }
    }

    void update() {
//      String selected_add_date_expense = add_date_expense.getText().toString();
        String billdt = add_date_expense.getText().toString();
        String spinnerpayment = spinner_exp_paymentmode.getSelectedItem().toString();
        String amt = expense_amount.getText().toString();
        String cheque = expense_cheque.getText().toString();
        String bankname = expense_cheque_bankName.getText().toString();
        String billno = expense_bill_number.getText().toString();
        String givn = givento.getText().toString();
        String prps = exp_purpose.getText().toString();
        String remrk = exp_remarks.getText().toString();
        String companyname = Admin.companyname;
        String ownerid = Admin.ownerid;
        String editid = id.getText().toString();


        SendData(billdt, spinnerpayment, amt, cheque, bankname, billno, givn, prps, remrk, companyname, ownerid, editid);

    }

    void SendData(String billdt, String spinnerpayment, String amt, String cheque, String bankname, String billno, String givn, String prps, String remrk, String companyname, String ownerid, String editid) {
        String url = "?billdate=" + billdt + "&paymentmode=" + spinnerpayment + "&amount=" + amt + "&chequeno=" + cheque + "&bankname=" + bankname + "&billno=" + billno + "&givento=" + givn.replace(" ", "%20") + "&purpose=" + prps.replace(" ", "%20") + "&remarks=" + remrk.replace(" ", "%20") + "&companyname=" + Admin.companyname + "&ownerid=" + ownerid + "&id=" + editid;

        class update extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminReportEdit.this, "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.e("onPostExecute: ", s);
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(AdminReportEdit.this);
                    reorder.setMessage("Updated details submitted.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (AppController.preferences.getPreference("role", "").equals("Admin")) {
                                        Intent intent = new Intent(AdminReportEdit.this,Track.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(AdminReportEdit.this, EmployeeReport.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    dialog.dismiss();

                                }
                            });
                    AlertDialog orderError = reorder.create();
                    orderError.show();

                } else {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(AdminReportEdit.this);
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
                    URL url = new URL(URLClass.Edit_AdminExpense + s);
                    Log.e("EditReport: ", String.valueOf(url));
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
        update ba = new update();
        ba.execute(url);
    }
}

