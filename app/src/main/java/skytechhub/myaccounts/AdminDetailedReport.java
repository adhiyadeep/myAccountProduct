package skytechhub.myaccounts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import skytechhub.myaccounts.Adaptor.AdminReport_Adaptor;
import skytechhub.myaccounts.Adaptor.AminReport_expenseAdaptor;
import skytechhub.myaccounts.POJO.Expense;
import skytechhub.myaccounts.POJO.Income;

public class AdminDetailedReport extends AppCompatActivity {
    private Button btn_adminseeincome, btn_adminseeexpense;

    private RecyclerView income_recycler, expense_recycler;
    private List<Income> iList = new ArrayList<>();

    private TextView txt_norecord, txt_incometotal;
    private AdminReport_Adaptor mAdapter;
    private AminReport_expenseAdaptor eAdapter;


    private List<Income> AdmindetailedIncome = new ArrayList<>();
    private List<Expense> AdmindetailedExpense = new ArrayList<>();



    public static boolean isIncome, isExpense;

    private TextView fromdate, todate;
    private String min, max;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detailed_report);
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

        AdmindetailedIncome.clear();
        AdmindetailedExpense.clear();

        txt_norecord = (TextView) findViewById(R.id.txt_norecord);
        txt_incometotal = (TextView) findViewById(R.id.txt_incometotal);

        fromdate = (TextView) findViewById(R.id.fromdate);
        todate = (TextView) findViewById(R.id.todate);

        min = getIntent().getExtras().getString("fromdate");
        max = getIntent().getExtras().getString("todate");

        fromdate.setText(Html.fromHtml("<font color='#FF4081'>From : </font>" + min));
        todate.setText(Html.fromHtml("<font color='#FF4081'>  To     : </font>    " + max));



        AdmindetailedIncome = Track.AdmindetailedIncome;
        AdmindetailedExpense = Track.AdmindetailedExpense;

        btn_adminseeincome = (Button) findViewById(R.id.btn_adminseeincome);
        btn_adminseeexpense = (Button) findViewById(R.id.btn_adminseeexpense);


        iList.clear();
        iList = Track.AdmindetailedIncome;



        income_recycler = (RecyclerView) findViewById(R.id.income_adminrecyclerview);
        income_recycler.setAdapter(mAdapter);

        expense_recycler = (RecyclerView) findViewById(R.id.expense_adminrecyclerview);

        btn_adminseeincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable d = getResources().getDrawable(R.drawable.mybuttons);
                btn_adminseeexpense.setBackground(d);
                btn_adminseeexpense.setTextColor(Color.parseColor("#000000"));

                btn_adminseeincome.setBackgroundColor(Color.parseColor("#003B46"));
                btn_adminseeincome.setTextColor(Color.parseColor("#ffffff"));


                income_recycler.setVisibility(View.VISIBLE);
                expense_recycler.setVisibility(View.GONE);

                isIncome = true;
                isExpense = false;

                if (AdmindetailedIncome.size() == 0) {
                    txt_incometotal.setText("Total Income Amount is " + Track.incomevalue);
                    txt_norecord.setVisibility(View.VISIBLE);

                    income_recycler.setLayoutManager(new GridLayoutManager(AdminDetailedReport.this, 1));
                    mAdapter = new AdminReport_Adaptor(AdminDetailedReport.this, AdmindetailedIncome);
                    income_recycler.setAdapter(mAdapter);
                    income_recycler.setHasFixedSize(true);
                    mAdapter.getItemCount();


                } else {
                    DecimalFormat formatter = new DecimalFormat("#,##,###");
                    String formatted = formatter.format(Track.incomevalue);

                    txt_incometotal.setText("Total Income Amount is  " + formatted);
                    txt_norecord.setVisibility(View.GONE);


                    income_recycler.setLayoutManager(new GridLayoutManager(AdminDetailedReport.this, 1));
                    mAdapter = new AdminReport_Adaptor(AdminDetailedReport.this, AdmindetailedIncome);
                    income_recycler.setAdapter(mAdapter);
                    income_recycler.setHasFixedSize(true);
                    mAdapter.getItemCount();

                }
            }

        });
        btn_adminseeincome.performClick();

        btn_adminseeexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable d = getResources().getDrawable(R.drawable.mybuttons);
                btn_adminseeincome.setBackground(d);
                btn_adminseeincome.setTextColor(Color.parseColor("#000000"));

                btn_adminseeexpense.setBackgroundColor(Color.parseColor("#003B46"));
                btn_adminseeexpense.setTextColor(Color.parseColor("#ffffff"));

                expense_recycler.setVisibility(View.VISIBLE);
                income_recycler.setVisibility(View.GONE);

                isExpense = true;
                isIncome = false;

                if (AdmindetailedExpense.size() == 0) {
                    txt_incometotal.setText("Total Expense Amount is  " + Track.expensevalue);
                    txt_norecord.setVisibility(View.VISIBLE);
                    expense_recycler.setLayoutManager(new GridLayoutManager(AdminDetailedReport.this, 1));
                    eAdapter = new AminReport_expenseAdaptor(AdminDetailedReport.this, AdmindetailedExpense);
                    expense_recycler.setAdapter(eAdapter);
                    expense_recycler.setHasFixedSize(true);
                    eAdapter.getItemCount();
                } else {

                    DecimalFormat formatter = new DecimalFormat("#,##,###");
                    String formatted = formatter.format(Track.expensevalue);

                    txt_incometotal.setText("Total Expense Amount is " + formatted);

                    txt_norecord.setVisibility(View.GONE);
                    expense_recycler.setLayoutManager(new GridLayoutManager(AdminDetailedReport.this, 1));
                    eAdapter = new AminReport_expenseAdaptor(AdminDetailedReport.this, AdmindetailedExpense);
                    expense_recycler.setAdapter(eAdapter);
                    expense_recycler.setHasFixedSize(true);
                    eAdapter.getItemCount();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Track.class);
        startActivity(intent);
        finish();
    }
}
