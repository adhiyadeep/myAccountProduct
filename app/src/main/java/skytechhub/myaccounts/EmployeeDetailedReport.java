package skytechhub.myaccounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import skytechhub.myaccounts.Adaptor.EmployeeReport_Adaptor;
import skytechhub.myaccounts.Adaptor.EmployeeReport_expenseAdaptor;
import skytechhub.myaccounts.POJO.Expense;
import skytechhub.myaccounts.POJO.Income;

public class EmployeeDetailedReport extends AppCompatActivity {
    private RecyclerView income_recyclerview, expense_recyclerview;
    private Button btn_seeincome, btn_seeexpense;
    private List<Income> iList;
    private List<Expense> elist;



    private EmployeeReport_Adaptor mAdapter;
    private EmployeeReport_expenseAdaptor expAdapter;

    private List<Income> detailedIncomeList = new ArrayList<>();
    private List<Expense> detailedExpenseList = new ArrayList<>();
    public static boolean isIncome, isExpense;
    private TextView txt_fromdate, txt_todate,record,txt_incomeval;  // txt_incomeval is to show income and expense

    private String mindate, maxdate;
    private TextView txt_incometotal;
    String min, max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detailed_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        txt_incomeval=(TextView)findViewById(R.id.txt_incomeval);
        txt_incometotal=(TextView)findViewById(R.id.txt_incometotal);

        record=(TextView)findViewById(R.id.record);
        txt_fromdate = (TextView) findViewById(R.id.txt_fromdate);
        txt_todate = (TextView) findViewById(R.id.txt_todate);

        min = getIntent().getExtras().getString("fromdate");
        max = getIntent().getExtras().getString("todate");

        txt_fromdate.setText(Html.fromHtml("<font color='#FF4081'>From : </font>" + min));
        txt_todate.setText(Html.fromHtml("<font color='#FF4081'>  To : </font>" + max));


        detailedIncomeList.clear();
        detailedExpenseList.clear();

        detailedIncomeList = EmployeeReport.detailedIncome;
        detailedExpenseList = EmployeeReport.detailedExpense;


        btn_seeincome = (Button) findViewById(R.id.btn_seeincome);
        btn_seeexpense = (Button) findViewById(R.id.btn_seeexpense);


        iList = EmployeeReport.incomeList;

        income_recyclerview = (RecyclerView) findViewById(R.id.income_recyclerview);
        income_recyclerview.setAdapter(mAdapter);

        expense_recyclerview = (RecyclerView) findViewById(R.id.expense_recyclerview);


        btn_seeincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable d = getResources().getDrawable(R.drawable.mybuttons);
                btn_seeexpense.setBackground(d);
                btn_seeexpense.setTextColor(Color.parseColor("#000000"));

                btn_seeincome.setBackgroundColor(Color.parseColor("#003B46"));
                btn_seeincome.setTextColor(Color.parseColor("#ffffff"));

                income_recyclerview.setVisibility(View.VISIBLE);
                expense_recyclerview.setVisibility(View.GONE);

                isIncome = true;
                isExpense = false;
                if (detailedIncomeList.size() == 0) {
                    txt_incomeval.setText("Total Income Amount is  "+EmployeeReport.incval);
                    record.setVisibility(View.VISIBLE);
                    income_recyclerview.setLayoutManager(new GridLayoutManager(EmployeeDetailedReport.this, 1));
                    mAdapter = new EmployeeReport_Adaptor(EmployeeDetailedReport.this, detailedIncomeList);
                    income_recyclerview.setAdapter(mAdapter);
                    income_recyclerview.setHasFixedSize(true);
                    mAdapter.getItemCount();
                } else {

                    DecimalFormat formatter = new DecimalFormat("#,##,###");
                    String formatted = formatter.format(EmployeeReport.incval);

                    txt_incomeval.setText("Total Income Amount is  "+formatted);

                    record.setVisibility(View.GONE);

                    income_recyclerview.setLayoutManager(new GridLayoutManager(EmployeeDetailedReport.this, 1));
                    mAdapter = new EmployeeReport_Adaptor(EmployeeDetailedReport.this, detailedIncomeList);
                    income_recyclerview.setAdapter(mAdapter);
                    income_recyclerview.setHasFixedSize(true);
                    mAdapter.getItemCount();
                }

            }

        });
        btn_seeincome.performClick();

        btn_seeexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable d = getResources().getDrawable(R.drawable.mybuttons);
                btn_seeincome.setBackground(d);
                btn_seeincome.setTextColor(Color.parseColor("#000000"));

                btn_seeexpense.setBackgroundColor(Color.parseColor("#003B46"));
                btn_seeexpense.setTextColor(Color.parseColor("#ffffff"));

                income_recyclerview.setVisibility(View.GONE);
                expense_recyclerview.setVisibility(View.VISIBLE);


                isExpense = true;
                isIncome = false;


                if (detailedExpenseList.size() == 0) {
                    txt_incomeval.setText("Total Expense Amount is "+EmployeeReport.expval);
                    record.setVisibility(View.VISIBLE);
                    expense_recyclerview.setLayoutManager(new GridLayoutManager(EmployeeDetailedReport.this, 1));
                    expAdapter = new EmployeeReport_expenseAdaptor(EmployeeDetailedReport.this, detailedExpenseList);
                    expense_recyclerview.setAdapter(expAdapter);
                    expense_recyclerview.setHasFixedSize(true);
                    mAdapter.getItemCount();
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,##,###");
                    String formatted = formatter.format(EmployeeReport.expval);
                    txt_incomeval.setText("Total  Expense Amount is "+formatted);

                    record.setVisibility(View.GONE);
                    expense_recyclerview.setLayoutManager(new GridLayoutManager(EmployeeDetailedReport.this, 1));
                    expAdapter = new EmployeeReport_expenseAdaptor(EmployeeDetailedReport.this, detailedExpenseList);
                    expense_recyclerview.setAdapter(expAdapter);
                    expense_recyclerview.setHasFixedSize(true);
                    mAdapter.getItemCount();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, EmployeeReport.class);
        startActivity(intent);
        finish();
    }
}
