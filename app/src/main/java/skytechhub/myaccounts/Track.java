package skytechhub.myaccounts;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import skytechhub.myaccounts.POJO.CustomerWiseInExReport;
import skytechhub.myaccounts.POJO.Expense;
import skytechhub.myaccounts.POJO.Income;
import skytechhub.myaccounts.POJO.ProfitLossPojo;
import skytechhub.myaccounts.POJO.companyname_pojo;
import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;


public class Track extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, Response.ErrorListener, Response.Listener<JSONObject> {

    private TextView txt_pl,selectfromdate, txtfromdate, selecttodate, txttodate, txt_profit, txt_modeofpayment, txt_modeofpaymentexpense, txt_adminincomecostomer, txt_adminexpensecostomer;
    private boolean isExpenseDate = false, isExpenseBillDate = false, isCompany, isIncome, isExpense;

    private Spinner spinner_companyreport;
    private Button btn_track, btn_clear;
    private ImageButton img_refresh;
    private WebView wv;
       private Dialog confirmDialog;
    ProgressBar progressBar;
    List<String> custName = new ArrayList<>();
    private static List<ProfitLossPojo> profitLossPojoList = new ArrayList<>();
    private ArrayList<String> companyname = new ArrayList<>();

    public static List<companyname_pojo> companyList = new ArrayList<>();

    public static List<Expense> expenseList = new ArrayList<>();
    public static List<Income> incomeList = new ArrayList<>();

    public static List<Income> AdmindetailedIncome = new ArrayList<>();    // For Reports
    public static List<Expense> AdmindetailedExpense = new ArrayList<>();   // For Reports

    private double incomeCount = 0, expenseCount = 0;
    BarChart barChart;
    private Date mindate, maxdate;
    private String phpFromDate, phpToDate;
    private FloatingActionButton fab, downloadicon;
    private ProgressDialog progressDialog, progressDialogincome, progressDialogcompany;


    public static int incomevalue, expensevalue;     // for sending value to other page and show income and expense
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    List<String> givento = new ArrayList<>();
    List<CustomerWiseInExReport> report = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        getCompanyList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_pl=(TextView)findViewById(R.id.txt_pl);
        txt_profit = (TextView) findViewById(R.id.txt_profit);

        img_refresh=(ImageButton)findViewById(R.id.img_refresh);
        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeList.clear();
                expenseList.clear();
                profitLossPojoList.clear();
                Intent intent = new Intent(Track.this,Track.class);
                startActivity(intent);
                finish();
            }
        });

        txt_modeofpayment = (TextView) findViewById(R.id.txt_modeofpayment);
        txt_modeofpaymentexpense = (TextView) findViewById(R.id.txt_modeofpaymentexpense);

        downloadicon = (FloatingActionButton) findViewById(R.id.downloadreport);

        txt_adminexpensecostomer = (TextView) findViewById(R.id.txt_adminexpensecostomer);
        txt_adminincomecostomer = (TextView) findViewById(R.id.txt_adminincomecostomer);

txt_pl.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        profitloss();
    }
});
        fab = (FloatingActionButton) findViewById(R.id.floating);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss a", new Date());

                Intent intent = new Intent(Track.this, AdminDetailedReport.class);
                intent.putExtra("fromdate", txtfromdate.getText().toString());
                intent.putExtra("todate", txttodate.getText().toString());
                startActivity(intent);
                finish();
            }
        });


        downloadicon.setVisibility(View.GONE);
        downloadicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                requestStoragePermission();
                /** Instantiating PopupMenu class */
                PopupMenu popup = new PopupMenu(getBaseContext(), view);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());


                /** Defining menu item click listener for the popup menu */
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if (item.getTitle().equals("Income Report")) {
                            String min = phpFromDate;
                            String max = phpToDate;
                            String com = spinner_companyreport.getSelectedItem().toString();
                            String id = Admin.ownerid;

                            final Dialog confirmDialog = new Dialog(Track.this);
                            confirmDialog.setContentView(R.layout.activity_webviewactivity);
                            confirmDialog.setCanceledOnTouchOutside(false);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = confirmDialog.getWindow();
                            lp.copyFrom(window.getAttributes());

                            //This makes the dialog take up the full width
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            window.setAttributes(lp);
                            confirmDialog.show();


                            wv = (WebView) confirmDialog.findViewById(R.id.datawebview);
                            String temp = "http://newmyaccounts.skytechhub.com/GettingResponse/incomeexcelreport.php?fromdate=" + min + "&todate=" + max + "&compnyname=" + com.replace(" ", "%20") + "&ownerid=" + id;
                            wv.loadUrl(temp);
                            Log.e("Income", temp);
                            wv.setWebViewClient(new MyClient());
                            wv.setWebChromeClient(new GoogleClient());
                            WebSettings webSettings = wv.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            wv.clearCache(true);
                            wv.clearHistory();
                            wv.setDownloadListener(new DownloadListener() {

                                @Override


                                public void onDownloadStart(String url, String userAgent,
                                                            String contentDisposition, String mimeType,
                                                            long contentLength) {

                                    DownloadManager.Request request = new DownloadManager.Request(
                                            Uri.parse(url));
                                    request.setMimeType(mimeType);
                                    String cookies = CookieManager.getInstance().getCookie(url);
                                    request.addRequestHeader("cookie", cookies);
                                    request.addRequestHeader("User-Agent", userAgent);
                                    request.setDescription("Downloading file...");
                                    request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                                            mimeType));
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalFilesDir(Track.this,
                                            Environment.DIRECTORY_DOWNLOADS, ".xls");
                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    dm.enqueue(request);
                                    Toast.makeText(getApplicationContext(), "Downloading File",
                                            Toast.LENGTH_LONG).show();
                                }
                            });


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    confirmDialog.cancel();
                                }
                            }, 0001);


                        } else {

                            String min = phpFromDate;
                            String max = phpToDate;
                            String com = spinner_companyreport.getSelectedItem().toString();
                            String id = Admin.ownerid;

                            final Dialog confirmDialog = new Dialog(Track.this);
                            confirmDialog.setContentView(R.layout.activity_webviewactivity);
                            confirmDialog.setCanceledOnTouchOutside(false);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = confirmDialog.getWindow();
                            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                            window.setAttributes(lp);
                            confirmDialog.show();


                            wv = (WebView) confirmDialog.findViewById(R.id.datawebview);
                            String temp = "http://newmyaccounts.skytechhub.com/GettingResponse/expenseexcelreport.php?fromdate=" + min + "&todate=" + max + "&compnyname=" + com.replace(" ", "%20") + "&ownerid=" + id;
                            Log.e("Expense", temp);
                            wv.loadUrl(temp);
                            wv.setWebViewClient(new MyClient());
                            wv.setWebChromeClient(new GoogleClient());
                            WebSettings webSettings = wv.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            wv.clearCache(true);
                            wv.clearHistory();
                            wv.setDownloadListener(new DownloadListener() {

                                @Override


                                public void onDownloadStart(String url, String userAgent,
                                                            String contentDisposition, String mimeType,
                                                            long contentLength) {

                                    DownloadManager.Request request = new DownloadManager.Request(
                                            Uri.parse(url));
                                    request.setMimeType(mimeType);
                                    String cookies = CookieManager.getInstance().getCookie(url);
                                    request.addRequestHeader("cookie", cookies);
                                    request.addRequestHeader("User-Agent", userAgent);
                                    request.setDescription("Downloading file...");
                                    request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                                            mimeType));
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalFilesDir(Track.this,
                                            Environment.DIRECTORY_DOWNLOADS, ".xls");
                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    dm.enqueue(request);
                                    Toast.makeText(getApplicationContext(), "Downloading File",
                                            Toast.LENGTH_LONG).show();

                                }
                            });


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    confirmDialog.cancel();
                                }
                            }, 0001);


                        }
                        return true;
                    }
                });

                /** Showing the popup menu */
                popup.show();

            }
        });

        selectfromdate = (TextView) findViewById(R.id.selectfromdate);
        txtfromdate = (TextView) findViewById(R.id.txtfromdate);
        selecttodate = (TextView) findViewById(R.id.selecttodate);
        txttodate = (TextView) findViewById(R.id.txttodate);

        selecttodate.setOnClickListener(this);
        selectfromdate.setOnClickListener(this);

      //  selecttodate.performClick();
     //   selectfromdate.performClick();

        spinner_companyreport = (Spinner) findViewById(R.id.spinner_companyreport);

        btn_track = (Button) findViewById(R.id.btn_track);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        barChart = (BarChart) findViewById(R.id.barchart);
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txttodate.getText().toString().equals("") || txtfromdate.getText().toString().equals("")) {
                    Toast.makeText(Track.this, "please select date", Toast.LENGTH_SHORT).show();
                } else if (!maxdate.after(mindate)  && !maxdate.equals(mindate) ) {
                    Toast.makeText(Track.this, "Please select proper dates", Toast.LENGTH_SHORT).show();
                } else {
                    if (spinner_companyreport.getSelectedItem().equals("<<Select>>")) {
                        Toast.makeText(Track.this, "Please select company", Toast.LENGTH_LONG).show();
                        spinner_companyreport.performClick();
                        btn_track.setVisibility(View.VISIBLE);
                        btn_clear.setVisibility(View.GONE);
                        txt_profit.setVisibility(View.GONE);
                        txt_pl.setVisibility(View.GONE);
                        downloadicon.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        txt_modeofpayment.setVisibility(View.VISIBLE);
                        txt_modeofpaymentexpense.setVisibility(View.VISIBLE);

                    } else {
                        txt_adminexpensecostomer.setVisibility(View.GONE);
                        txt_adminincomecostomer.setVisibility(View.GONE);
                        txt_modeofpayment.setVisibility(View.GONE);
                        txt_modeofpaymentexpense.setVisibility(View.GONE);
                        barChart.setVisibility(View.VISIBLE);
                        txt_pl.setVisibility(View.GONE);
                        txt_profit.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        downloadicon.setVisibility(View.VISIBLE);
                        btn_track.setVisibility(View.GONE);
                        btn_clear.setVisibility(View.VISIBLE);
                        if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                            String company = spinner_companyreport.getSelectedItem().toString();

                            for (int i = 0; i < incomeList.size(); i++) {
                                Date date = incomeList.get(i).getDate();

                                if (incomeList.get(i).getCompanyname().equals(company)) {
                                    if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                                        incomeCount = incomeCount + Integer.parseInt(incomeList.get(i).getAmount());
                                        AdmindetailedIncome.add(incomeList.get(i));
                                    }
                                }
                            }
                            incomeList.clear();


                            for (int i = 0; i < expenseList.size(); i++) {
                                Date date = expenseList.get(i).getDate();

                                if (expenseList.get(i).getCompanyname().equals(company)) {
                                    if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                                        expenseCount = expenseCount + Integer.parseInt(expenseList.get(i).getAmount());
                                        AdmindetailedExpense.add(expenseList.get(i));
                                    }
                                }
                            }
                            expenseList.clear();


                            Log.e("onClick: ", String.valueOf(incomeCount));
                            Log.e("onClick: ", String.valueOf(expenseCount));

                            incomevalue = (int) incomeCount;
                            expensevalue = (int) expenseCount;

                            if (incomeCount > expenseCount || incomeCount == expenseCount) {
                                String Profit = String.valueOf(incomeCount - expenseCount);

                                Log.e("profit", Profit);

                                double pro = Double.parseDouble(Profit);
                                DecimalFormat formatter = new DecimalFormat("#,##,###");
                                String formatted = formatter.format(pro);
                                txt_profit.setText(Html.fromHtml("<font color='#FF4081'>Total Profit is :</font>" + formatted));

                            } else {
                                String loss = String.valueOf(expenseCount - incomeCount);

                                Log.e("loss", loss);
                                double losss = Double.parseDouble(loss);
                                DecimalFormat formatter = new DecimalFormat("#,##,###");
                                String formatted = formatter.format(losss);
                                txt_profit.setText(Html.fromHtml("<font color='#FF4081'>Total Loss is :</font>" + formatted));

                            }


                            ArrayList<BarEntry> entries = new ArrayList<>();
                            entries.add(new BarEntry((float) incomeCount, 0));
                            entries.add(new BarEntry((float) expenseCount, 1));

                            BarDataSet bardataset = new BarDataSet(entries, "Cells");

                            ArrayList<String> labels = new ArrayList<String>();
                            labels.add("Income");
                            labels.add("Expense");

                            BarData data = new BarData(labels, bardataset);
                            barChart.setData(data); // set the data and list of lables into chart

                            barChart.setDescription("See Detail");  // set the description
                            bardataset.setColors(new int[]{R.color.income, R.color.expence}, Track.this);
                            //   bardataset.setColors(ColorTemplate.PASTEL_COLORS);

                            barChart.animateY(2000);
                            incomeList.clear();
                            expenseList.clear();

                        }


                    }
                }
            }

        });

        txt_adminexpensecostomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                costomerwiseexpense();
            }
        });

        txt_adminincomecostomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                costomerwiseincome();
            }
        });


        txt_modeofpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeofpaymentdialoug();
            }
        });

        txt_modeofpaymentexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeofpaymentexpensedialoug();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void profitloss() {
        if (txtfromdate.getText().toString().equals("") || txttodate.getText().toString().equals("")) {
            Toast.makeText(Track.this, "Please select date", Toast.LENGTH_SHORT).show();
        } else if (!maxdate.after(mindate) && !maxdate.equals(mindate)) {
            Toast.makeText(Track.this, "Please select proper dates", Toast.LENGTH_SHORT).show();
        } else {
            if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                confirmDialog = new Dialog(Track.this);
                confirmDialog.setContentView(R.layout.dailoug_profitloss);
                confirmDialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = confirmDialog.getWindow();
                lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                confirmDialog.show();


                TextView txt_fromdt = (TextView) confirmDialog.findViewById(R.id.txt_fromdt);
                TextView txt_to = (TextView) confirmDialog.findViewById(R.id.txt_to);
                TextView txt_cmpyname = (TextView) confirmDialog.findViewById(R.id.txt_cmpyname);


                txt_cmpyname.setText("Name of company:  " + spinner_companyreport.getSelectedItem().toString());
                txt_fromdt.setText("From:  " + phpFromDate);
                txt_to.setText("To:  " + phpToDate);


                //Dhaval's code
                custName.clear();



                for (int i = 0; i < incomeList.size(); i++) {
                    incomeList.clear();
                    expenseList.clear();
                    String company = spinner_companyreport.getSelectedItem().toString();;
                    Date date = incomeList.get(i).getDate();
                    if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                        if (incomeList.get(i).getCompanyname().equals(company)) {
                            if (!custName.contains(incomeList.get(i).getRname())) {
                                custName.add(incomeList.get(i).getRname());
                            }
                        }
                    }
                }
                for (int i = 0; i < expenseList.size(); i++) {
                    String company =  spinner_companyreport.getSelectedItem().toString();;
                    Date date = expenseList.get(i).getDate();
                    if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                        if (expenseList.get(i).getCompanyname().equals(company)) {
                            if (!custName.contains(expenseList.get(i).getGivento())) {
                                custName.add(expenseList.get(i).getGivento());
                            }
                        }
                    }
                }

                for (int i = 0; i < custName.size(); i++) {
                    ProfitLossPojo pojo = new ProfitLossPojo();
                    int incomeamount = 0;
                    int expenseamount = 0;
                    String company = spinner_companyreport.getSelectedItem().toString();;


                    for (int j = 0; j < incomeList.size(); j++) {
                        Date date = incomeList.get(j).getDate();
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            if (incomeList.get(j).getCompanyname().equals(company)) {
                                if (incomeList.get(j).getRname().equals(custName.get(i))) {
                                    incomeamount = incomeamount + Integer.parseInt(incomeList.get(j).getAmount());
                                }
                            }
                        }

                    }
                    for (int j = 0; j < expenseList.size(); j++) {
                        Date date = expenseList.get(j).getDate();
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            if (expenseList.get(j).getCompanyname().equals(company)) {

                                if (expenseList.get(j).getGivento().equals(custName.get(i))) {
                                    expenseamount = expenseamount + Integer.parseInt(expenseList.get(j).getAmount());
                                }
                            }

                        }
                    }
                    int pandl = 0;
                    if (incomeamount > expenseamount) {
                        pandl = incomeamount - expenseamount;
                    } else {
                        pandl = incomeamount - expenseamount;
                    }
                    pojo.setName(custName.get(i));
                    pojo.setIncome(incomeamount);
                    pojo.setExpense(expenseamount);
                    pojo.setPl(pandl);

                    profitLossPojoList.add(pojo);

                }

                //DHAVAL'S CODE'
                TableLayout table = (TableLayout) confirmDialog.findViewById(R.id.tablelayout_pandl);

                //heading
                TableRow row = new TableRow(this);
                // create a new TextView
                TextView name = new TextView(this);
                // set the text to "text xx"
                name.setText(" Name ");
                name.setGravity(Gravity.CENTER);
                name.setTypeface(null, Typeface.BOLD);
                name.setBackgroundResource(R.drawable.tableformat);
                row.addView(name);

                TextView income = new TextView(this);
                income.setText(" Income ");
                income.setTypeface(null, Typeface.BOLD);
                income.setBackgroundResource(R.drawable.tableformat);
                row.addView(income);

                final TextView expense = new TextView(this);
                expense.setText(" Expense ");
                expense.setTypeface(null, Typeface.BOLD);
                expense.setBackgroundResource(R.drawable.tableformat);
                row.addView(expense);

                TextView pl = new TextView(this);
                pl.setText(" Profit and loss ");
                pl.setTypeface(null, Typeface.BOLD);
                pl.setBackgroundResource(R.drawable.tableformat);
                row.addView(pl);

                table.addView(row);

                for (int j = 0; j < profitLossPojoList.size(); j++) {
//            Log.e("name: ",profitLossPojoList.get(i).getName() );
//            Log.e("income: ", String.valueOf(profitLossPojoList.get(i).getIncome()));
//            Log.e("expense: ", String.valueOf(profitLossPojoList.get(i).getExpense()));
//            Log.e("p and l: ", String.valueOf(profitLossPojoList.get(i).getPl()));

                    if (j % 2 == 0) {
                        incomeList.clear();
                        expenseList.clear();
                        TableRow tableRow = new TableRow(this);
                        TextView cname = new TextView(this);
                        cname.setText("  " + profitLossPojoList.get(j).getName());
                        cname.setBackgroundResource(R.drawable.tableformat);
                        cname.setGravity(Gravity.LEFT);
                        tableRow.addView(cname);

                        TextView cincome = new TextView(this);
                        DecimalFormat formatter = new DecimalFormat("#,##,###");
                        String formatted = formatter.format(profitLossPojoList.get(j).getIncome());
                        cincome.setText("" + formatted);
                        cincome.setBackgroundResource(R.drawable.tableformat);
                        cincome.setGravity(Gravity.END);
                        tableRow.addView(cincome);

                        TextView cexpense = new TextView(this);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                        String formatted1 = formatter1.format(profitLossPojoList.get(j).getExpense());
                        cexpense.setText("" + formatted1);
                        cexpense.setBackgroundResource(R.drawable.tableformat);
                        cexpense.setGravity(Gravity.END);
                        tableRow.addView(cexpense);

                        TextView cpl = new TextView(this);
                        DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                        String formatted2 = formatter2.format(profitLossPojoList.get(j).getPl());
                        cpl.setText("" + formatted2);
                        cpl.setBackgroundResource(R.drawable.tableformat);
                        cpl.setGravity(Gravity.END);
                        tableRow.addView(cpl);

                        table.addView(tableRow);
                    } else {
                        incomeList.clear();
                        expenseList.clear();
                        TableRow tableRow = new TableRow(this);
                        TextView cname = new TextView(this);
                        cname.setText("  " + profitLossPojoList.get(j).getName());
                        cname.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cname.setGravity(Gravity.LEFT);
                        tableRow.addView(cname);

                        TextView ccash = new TextView(this);
                        DecimalFormat formatter = new DecimalFormat("#,##,###");
                        String formatted = formatter.format(profitLossPojoList.get(j).getIncome());
                        ccash.setText("" + formatted);
                        ccash.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccash.setGravity(Gravity.END);
                        tableRow.addView(ccash);

                        TextView ccheque = new TextView(this);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                        String formatted1 = formatter.format(profitLossPojoList.get(j).getExpense());
                        ccheque.setText("" + formatted1);
                        ccheque.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccheque.setGravity(Gravity.END);
                        tableRow.addView(ccheque);

                        TextView ccredit = new TextView(this);
                        DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                        String formatted2 = formatter.format(profitLossPojoList.get(j).getPl());
                        ccredit.setText("" + formatted2);
                        ccredit.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccredit.setGravity(Gravity.END);
                        tableRow.addView(ccredit);

                        table.addView(tableRow);

                    }
                }
                incomeList.clear();
                expenseList.clear();

                confirmDialog.setCancelable(false);
                confirmDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            confirmDialog.dismiss();
                            profitLossPojoList.clear();
                            incomeList.clear();
                            expenseList.clear();
                            Intent intent = new Intent(Track.this,Track.class);
                            startActivity(intent);
                            finish();

                        }
                        return true;
                    }
                });
            }
            else
            {
                Toast.makeText(Track.this,"Please select company",Toast.LENGTH_LONG).show();
                spinner_companyreport.performClick();
            }

        }





    }


    private void costomerwiseexpense() {

        if (txttodate.getText().toString().equals("") || txtfromdate.getText().toString().equals("")) {
            Toast.makeText(Track.this, "please select date", Toast.LENGTH_SHORT).show();
        } else if (!maxdate.after(mindate) && !maxdate.after(mindate) ) {
            Toast.makeText(Track.this, "Please select proper dates", Toast.LENGTH_SHORT).show();
        } else {
            if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                // if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                String company = spinner_companyreport.getSelectedItem().toString();

                confirmDialog = new Dialog(Track.this);
                confirmDialog.setContentView(R.layout.dailoug_costincomeexpense);
                confirmDialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = confirmDialog.getWindow();
                lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                confirmDialog.show();
//
//                LayoutInflater li = LayoutInflater.from(Track.this);
//                final View promptsView = li.inflate(R.layout.dailoug_costincomeexpense, null);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Track.this);
//                alertDialogBuilder.setTitle("Customer wise Expense Details");
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);

                final TextView from = (TextView) confirmDialog.findViewById(R.id.txt_fromdt);
                final TextView to = (TextView) confirmDialog.findViewById(R.id.txt_to);
                final TextView copnyname = (TextView) confirmDialog.findViewById(R.id.txt_cmpyname);

                copnyname.setText("Name of company:  " + company);
                from.setText("From:  " + phpFromDate);
                to.setText("To:  " + phpToDate);

//            DHAVAL'S CODE'
                TableLayout table = (TableLayout) confirmDialog.findViewById(R.id.tableLayout01);

                //heading
                TableRow row = new TableRow(this);
                // create a new TextView
                TextView name = new TextView(this);
                // set the text to "text xx"
                name.setText(" Name ");
                name.setGravity(Gravity.CENTER);
                name.setTypeface(null, Typeface.BOLD);
                name.setBackgroundResource(R.drawable.tableformat);
                row.addView(name);

                TextView cash = new TextView(this);

                cash.setText(" Cash ");
                cash.setTypeface(null, Typeface.BOLD);
                cash.setBackgroundResource(R.drawable.tableformat);
                row.addView(cash);

                TextView cheque = new TextView(this);
                cheque.setText(" Cheque ");
                cheque.setTypeface(null, Typeface.BOLD);
                cheque.setBackgroundResource(R.drawable.tableformat);
                row.addView(cheque);

                TextView credit = new TextView(this);
                credit.setText(" Credit Card");
                credit.setTypeface(null, Typeface.BOLD);
                credit.setBackgroundResource(R.drawable.tableformat);
                row.addView(credit);

                TextView debit = new TextView(this);
                debit.setText(" Debit Card");
                debit.setTypeface(null, Typeface.BOLD);
                debit.setBackgroundResource(R.drawable.tableformat);
                row.addView(debit);

                TextView netbanking = new TextView(this);
                netbanking.setText(" Net Banking");
                netbanking.setTypeface(null, Typeface.BOLD);
                netbanking.setBackgroundResource(R.drawable.tableformat);
                row.addView(netbanking);

                TextView paytm = new TextView(this);
                paytm.setText(" PayTM ");
                paytm.setTypeface(null, Typeface.BOLD);
                paytm.setBackgroundResource(R.drawable.tableformat);
                row.addView(paytm);

                TextView others = new TextView(this);
                others.setText(" Others ");
                others.setTypeface(null, Typeface.BOLD);
                others.setBackgroundResource(R.drawable.tableformat);
                row.addView(others);

                TextView total = new TextView(this);
                total.setText(" Total ");
                total.setTypeface(null, Typeface.BOLD);
                total.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                row.addView(total);

                table.addView(row);

                report.clear();
                givento.clear();

                //generating report
                for (int i = 0; i < expenseList.size(); i++) {
                    int cashamt = 0;
                    int creditamt = 0;
                    int chequeamt = 0;
                    int debitamt = 0;
                    int netbankingamt = 0;
                    int paytmamt = 0;
                    int othersamt = 0;
                    CustomerWiseInExReport inex = new CustomerWiseInExReport();
                    Date date = expenseList.get(i).getDate();
                    if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                        String co = spinner_companyreport.getSelectedItem().toString();
                        if (expenseList.get(i).getCompanyname().equals(co)) {
                            if (!givento.contains(expenseList.get(i).getGivento())) {
                                //adding new to list
                                givento.add(expenseList.get(i).getGivento());
                                if (expenseList.get(i).getPaymentmode().equals("Cash")) {
                                    cashamt = cashamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                if (expenseList.get(i).getPaymentmode().equals("Debit Card")) {
                                    debitamt = debitamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                if (expenseList.get(i).getPaymentmode().equals("Credit Card")) {
                                    creditamt = creditamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                if (expenseList.get(i).getPaymentmode().equals("Cheque")) {
                                    chequeamt = chequeamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                if (expenseList.get(i).getPaymentmode().equals("Net Banking")) {
                                    netbankingamt = netbankingamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                if (expenseList.get(i).getPaymentmode().equals("Paytm")) {
                                    paytmamt = paytmamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                if (expenseList.get(i).getPaymentmode().equals("Others")) {
                                    othersamt = othersamt + Integer.parseInt(expenseList.get(i).getAmount());
                                }
                                int tot = cashamt + debitamt + creditamt + chequeamt + netbankingamt + paytmamt + othersamt;
                                inex.setName(expenseList.get(i).getGivento());
                                inex.setCash(cashamt);
                                inex.setDebit(debitamt);
                                inex.setCredit(creditamt);
                                inex.setCheque(chequeamt);
                                inex.setNet(netbankingamt);
                                inex.setPaytm(paytmamt);
                                inex.setOthers(othersamt);
                                inex.setTotal(tot);

                                report.add(inex);
                            } else {
                                String nm = expenseList.get(i).getGivento();
                                getDataExpense(nm, i);
                            }
                        }
                    }

                }
                for (int j = 0; j < report.size(); j++) {

                    //showing data in color
                    if (j % 2 == 0) {
                        TableRow tableRow = new TableRow(this);
                        TextView cname = new TextView(this);
                        cname.setText("  " + report.get(j).getName());
                        cname.setBackgroundResource(R.drawable.tableformat);
                        cname.setGravity(Gravity.LEFT);
                        tableRow.addView(cname);

                        TextView ccash = new TextView(this);
                        DecimalFormat formatter = new DecimalFormat("#,##,###");
                        String formatted = formatter.format( report.get(j).getCash());
                        ccash.setText("" +formatted);
                        ccash.setBackgroundResource(R.drawable.tableformat);
                        ccash.setGravity(Gravity.END);
                        tableRow.addView(ccash);

                        TextView ccheque = new TextView(this);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                        String formatted1 = formatter.format( report.get(j).getCheque());
                        ccheque.setText("" +formatted1 );
                        ccheque.setBackgroundResource(R.drawable.tableformat);
                        ccheque.setGravity(Gravity.END);
                        tableRow.addView(ccheque);

                        TextView ccredit = new TextView(this);
                        DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                        String formatted2 = formatter.format( report.get(j).getCredit());
                        ccredit.setText("" + formatted2);
                        ccredit.setBackgroundResource(R.drawable.tableformat);
                        ccredit.setGravity(Gravity.END);
                        tableRow.addView(ccredit);

                        TextView cdebit = new TextView(this);
                        DecimalFormat formatter3 = new DecimalFormat("#,##,###");
                        String formatted3 = formatter.format( report.get(j).getDebit());
                        cdebit.setText("" + formatted3);
                        cdebit.setBackgroundResource(R.drawable.tableformat);
                        cdebit.setGravity(Gravity.END);
                        tableRow.addView(cdebit);

                        TextView cnet = new TextView(this);
                        DecimalFormat formatter4 = new DecimalFormat("#,##,###");
                        String formatted4 = formatter.format(  report.get(j).getNet());
                        cnet.setText("" +formatted4);
                        cnet.setBackgroundResource(R.drawable.tableformat);
                        cnet.setGravity(Gravity.END);
                        tableRow.addView(cnet);

                        TextView cpaytm = new TextView(this);
                        DecimalFormat formatter5 = new DecimalFormat("#,##,###");
                        String formatted5 = formatter.format(  report.get(j).getPaytm());
                        cpaytm.setText("" +formatted5 );
                        cpaytm.setBackgroundResource(R.drawable.tableformat);
                        cpaytm.setGravity(Gravity.END);
                        tableRow.addView(cpaytm);

                        TextView cothers = new TextView(this);
                        DecimalFormat formatter6 = new DecimalFormat("#,##,###");
                        String formatted6 = formatter.format(   report.get(j).getOthers());
                        cothers.setText("" +formatted6);
                        cothers.setBackgroundResource(R.drawable.tableformat);
                        cothers.setGravity(Gravity.END);
                        tableRow.addView(cothers);

                        TextView ctotal = new TextView(this);
                        DecimalFormat formatter7 = new DecimalFormat("#,##,###");
                        String formatted7 = formatter.format(   report.get(j).getTotal());
                        ctotal.setText("" +formatted7 );
                        ctotal.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                        ctotal.setGravity(Gravity.END);
                        tableRow.addView(ctotal);

                        table.addView(tableRow);
                    } else {
                        TableRow tableRow = new TableRow(this);
                        TextView cname = new TextView(this);
                        cname.setText("  " + report.get(j).getName());
                        cname.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cname.setGravity(Gravity.LEFT);
                        tableRow.addView(cname);

                        TextView ccash = new TextView(this);
                        DecimalFormat formatter = new DecimalFormat("#,##,###");
                        String formatted = formatter.format(   report.get(j).getCash());
                        ccash.setText("" + formatted);
                        ccash.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccash.setGravity(Gravity.END);
                        tableRow.addView(ccash);

                        TextView ccheque = new TextView(this);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                        String formatted1 = formatter.format(   report.get(j).getCheque());
                        ccheque.setText("" + formatted1);
                        ccheque.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccheque.setGravity(Gravity.END);
                        tableRow.addView(ccheque);

                        TextView ccredit = new TextView(this);
                        DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                        String formatted2 = formatter.format(   report.get(j).getCredit());
                        ccredit.setText("" +formatted2);
                        ccredit.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccredit.setGravity(Gravity.END);
                        tableRow.addView(ccredit);

                        TextView cdebit = new TextView(this);
                        DecimalFormat formatter3 = new DecimalFormat("#,##,###");
                        String formatted3 = formatter.format(    report.get(j).getDebit());
                        cdebit.setText("" +formatted3);
                        cdebit.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cdebit.setGravity(Gravity.END);
                        tableRow.addView(cdebit);

                        TextView cnet = new TextView(this);
                        DecimalFormat formatter4 = new DecimalFormat("#,##,###");
                        String formatted4 = formatter.format(    report.get(j).getNet());
                        cnet.setText("" + formatted4);
                        cnet.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cnet.setGravity(Gravity.END);
                        tableRow.addView(cnet);

                        TextView cpaytm = new TextView(this);
                        DecimalFormat formatter5 = new DecimalFormat("#,##,###");
                        String formatted5 = formatter.format(    report.get(j).getPaytm());
                        cpaytm.setText("" +formatted5);
                        cpaytm.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cpaytm.setGravity(Gravity.END);
                        tableRow.addView(cpaytm);

                        TextView cothers = new TextView(this);
                        DecimalFormat formatter6 = new DecimalFormat("#,##,###");
                        String formatted6 = formatter.format(   report.get(j).getOthers());
                        cothers.setText("" +formatted6 );
                        cothers.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cothers.setGravity(Gravity.END);
                        tableRow.addView(cothers);

                        TextView ctotal = new TextView(this);
                        DecimalFormat formatter7 = new DecimalFormat("#,##,###");
                        String formatted7 = formatter.format(    report.get(j).getTotal());
                        ctotal.setText("" +formatted7);
                        ctotal.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                        ctotal.setGravity(Gravity.END);
                        tableRow.addView(ctotal);

                        table.addView(tableRow);
                    }


                }

                //calculating last coloumn totals
                int cashtotal = 0;
                int chequetotal = 0;
                int debittotal = 0;
                int credittotal = 0;
                int netbankingtotal = 0;
                int paytmtotal = 0;
                int othertotal = 0;
                int tottot = 0;
                for (int a = 0; a < report.size(); a++) {
                    cashtotal = cashtotal + report.get(a).getCash();
                    chequetotal = chequetotal + report.get(a).getCheque();
                    debittotal = debittotal + report.get(a).getDebit();
                    credittotal = credittotal + report.get(a).getCredit();
                    netbankingtotal = netbankingtotal + report.get(a).getNet();
                    paytmtotal = paytmtotal + report.get(a).getPaytm();
                    othertotal = othertotal + report.get(a).getOthers();
                }

                //total of orange color
                tottot = cashtotal + debittotal + chequetotal + credittotal + netbankingtotal + paytmtotal + othertotal;

                TableRow row1 = new TableRow(this);

                TextView distot = new TextView(this);
                distot.setText("Total");
                distot.setTypeface(null, Typeface.BOLD);
                distot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                distot.setGravity(Gravity.CENTER);
                row1.addView(distot);

                TextView cashtot = new TextView(this);
                DecimalFormat formatter = new DecimalFormat("#,##,###");
                String formatted = formatter.format(cashtotal);
                cashtot.setText("" +formatted);
                cashtot.setTypeface(null, Typeface.BOLD);
                cashtot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                cashtot.setGravity(Gravity.END);
                row1.addView(cashtot);

                TextView cheqtot = new TextView(this);
                DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                String formatted1 = formatter.format(chequetotal);
                cheqtot.setText("" +formatted1 );
                cheqtot.setTypeface(null, Typeface.BOLD);
                cheqtot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                cheqtot.setGravity(Gravity.END);
                row1.addView(cheqtot);

                TextView credtot = new TextView(this);
                DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                String formatted2 = formatter.format(credittotal);
                credtot.setText("" + formatted2);
                credtot.setTypeface(null, Typeface.BOLD);
                credtot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                credtot.setGravity(Gravity.END);
                row1.addView(credtot);

                TextView debitot = new TextView(this);
                DecimalFormat formatter3 = new DecimalFormat("#,##,###");
                String formatted3 = formatter.format(debittotal);
                debitot.setText("" +formatted3 );
                debitot.setTypeface(null, Typeface.BOLD);
                debitot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                debitot.setGravity(Gravity.END);
                row1.addView(debitot);

                TextView nettot = new TextView(this);
                DecimalFormat formatter4 = new DecimalFormat("#,##,###");
                String formatted4 = formatter.format(netbankingtotal);
                nettot.setText("" +formatted4 );
                nettot.setTypeface(null, Typeface.BOLD);
                nettot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                nettot.setGravity(Gravity.END);
                row1.addView(nettot);

                TextView paytot = new TextView(this);
                DecimalFormat formatter5 = new DecimalFormat("#,##,###");
                String formatted5 = formatter.format(paytmtotal);
                paytot.setText("" + formatted5);
                paytot.setTypeface(null, Typeface.BOLD);
                paytot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                paytot.setGravity(Gravity.END);
                row1.addView(paytot);

                TextView othertot = new TextView(this);
                DecimalFormat formatter6 = new DecimalFormat("#,##,###");
                String formatted6 = formatter.format(othertotal);
                othertot.setText("" + formatted6);
                othertot.setTypeface(null, Typeface.BOLD);
                othertot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                othertot.setGravity(Gravity.END);
                row1.addView(othertot);

                TextView totoftot = new TextView(this);
                DecimalFormat formatter7 = new DecimalFormat("#,##,###");
                String formatted7 = formatter.format(tottot);
                totoftot.setText("" +formatted7 );
                totoftot.setTypeface(null, Typeface.BOLD);
                totoftot.setBackgroundResource(R.drawable.tableformatwithredcolor);
                totoftot.setGravity(Gravity.END);
                row1.addView(totoftot);

                table.addView(row1);


//                final AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();
//                alertDialog.setCancelable(false);

                confirmDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            confirmDialog.dismiss();
                            Intent intent = new Intent(Track.this, Track.class);
                            startActivity(intent);
                            expenseList.clear();
                            incomeList.clear();
                            Track.this.finish();

                        }
                        return true;
                    }
                });

            } else {
                Toast.makeText(Track.this, "Please select company", Toast.LENGTH_SHORT).show();
                spinner_companyreport.performClick();
            }
        }


    }
    private void getDataExpense(String nm, int j) {

        for (int i = 0; i < report.size(); i++) {
            if (report.get(i).getName().equals(nm)) {
                int cashactual = report.get(i).getCash();
                int debitactual = report.get(i).getDebit();
                int creditactual = report.get(i).getCredit();
                int chequeactual = report.get(i).getCheque();
                int netactual = report.get(i).getNet();
                int paytmactual = report.get(i).getPaytm();
                int othersactual = report.get(i).getOthers();

                if (expenseList.get(j).getPaymentmode().equals("Cash")) {
                    cashactual = cashactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                if (expenseList.get(j).getPaymentmode().equals("Debit Card")) {
                    debitactual = debitactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                if (expenseList.get(j).getPaymentmode().equals("Credit Card")) {
                    creditactual = creditactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                if (expenseList.get(j).getPaymentmode().equals("Cheque")) {
                    chequeactual = chequeactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                if (expenseList.get(j).getPaymentmode().equals("Net Banking")) {
                    netactual = netactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                if (expenseList.get(j).getPaymentmode().equals("Paytm")) {
                    paytmactual = paytmactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                if (expenseList.get(j).getPaymentmode().equals("Others")) {
                    othersactual = othersactual + Integer.parseInt(expenseList.get(j).getAmount());
                }
                int tot = cashactual + chequeactual + debitactual + creditactual + netactual + paytmactual + othersactual;
                report.get(i).setCash(cashactual);
                report.get(i).setCheque(chequeactual);
                report.get(i).setDebit(debitactual);
                report.get(i).setCredit(creditactual);
                report.get(i).setNet(netactual);
                report.get(i).setPaytm(paytmactual);
                report.get(i).setOthers(othersactual);
                report.get(i).setTotal(tot);
            }
        }
    }

    private void getData(String nm, int j) {

        for (int i = 0; i < report.size(); i++) {
            if (report.get(i).getName().equals(nm)) {
                int cashactual = report.get(i).getCash();
                int debitactual = report.get(i).getDebit();
                int creditactual = report.get(i).getCredit();
                int chequeactual = report.get(i).getCheque();
                int netactual = report.get(i).getNet();
                int paytmactual = report.get(i).getPaytm();
                int othersactual = report.get(i).getOthers();

                if (incomeList.get(j).getPaymentmode().equals("Cash")) {
                    cashactual = cashactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                if (incomeList.get(j).getPaymentmode().equals("Debit Card")) {
                    debitactual = debitactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                if (incomeList.get(j).getPaymentmode().equals("Credit Card")) {
                    creditactual = creditactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                if (incomeList.get(j).getPaymentmode().equals("Cheque")) {
                    chequeactual = chequeactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                if (incomeList.get(j).getPaymentmode().equals("Net Banking")) {
                    netactual = netactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                if (incomeList.get(j).getPaymentmode().equals("Paytm")) {
                    paytmactual = paytmactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                if (incomeList.get(j).getPaymentmode().equals("Others")) {
                    othersactual = othersactual + Integer.parseInt(incomeList.get(j).getAmount());
                }
                int tot = cashactual + chequeactual + debitactual + creditactual + netactual + paytmactual + othersactual;
                report.get(i).setCash(cashactual);
                report.get(i).setCheque(chequeactual);
                report.get(i).setDebit(debitactual);
                report.get(i).setCredit(creditactual);
                report.get(i).setNet(netactual);
                report.get(i).setPaytm(paytmactual);
                report.get(i).setOthers(othersactual);
                report.get(i).setTotal(tot);
            }
        }
    }

    private void costomerwiseincome() {

        if (txtfromdate.getText().toString().equals("") || txttodate.getText().toString().equals("")) {
            Toast.makeText(Track.this, "Please select date", Toast.LENGTH_SHORT).show();
        }
        else if (!maxdate.after(mindate) && !maxdate.equals(mindate)) {
            Toast.makeText(Track.this, "Please select proper dates", Toast.LENGTH_SHORT).show();
        } else {
            if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                String company = spinner_companyreport.getSelectedItem().toString();
                //  if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                confirmDialog = new Dialog(Track.this);
                confirmDialog.setContentView(R.layout.dailoug_profitloss);
                confirmDialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = confirmDialog.getWindow();
                lp.copyFrom(window.getAttributes());

                    //This makes the dialog take up the full width

                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                confirmDialog.show();

                final TextView from = (TextView) confirmDialog.findViewById(R.id.txt_fromdt);
                final TextView to = (TextView) confirmDialog.findViewById(R.id.txt_to);
                final TextView copnyname = (TextView) confirmDialog.findViewById(R.id.txt_cmpyname);



                progressBar = (ProgressBar) confirmDialog.findViewById(R.id.progress1);
                final WebView webView = (WebView) confirmDialog.findViewById(R.id.webwiew_incomexp);

                webView.setWebViewClient(new webview());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("http://www.technotalkative.com");

                copnyname.setText("Name of company:  " + company);
                from.setText("From:  " + phpFromDate);
                to.setText("To:  " + phpToDate);


                //DHAVAL'S CODE'
                TableLayout table = (TableLayout) confirmDialog.findViewById(R.id.tableLayout01);

                //heading
                TableRow row = new TableRow(this);
                // create a new TextView
                TextView name = new TextView(this);
                // set the text to "text xx"
                name.setText(" Name ");
                name.setGravity(Gravity.CENTER);
                name.setTypeface(null, Typeface.BOLD);
                name.setBackgroundResource(R.drawable.tableformat);
                row.addView(name);

                TextView cash = new TextView(this);
                cash.setText(" Cash ");
                cash.setTypeface(null, Typeface.BOLD);
                cash.setBackgroundResource(R.drawable.tableformat);
                row.addView(cash);

                TextView cheque = new TextView(this);
                cheque.setText(" Cheque ");
                cheque.setTypeface(null, Typeface.BOLD);
                cheque.setBackgroundResource(R.drawable.tableformat);
                row.addView(cheque);

                TextView credit = new TextView(this);
                credit.setText(" Credit Card");
                credit.setTypeface(null, Typeface.BOLD);
                credit.setBackgroundResource(R.drawable.tableformat);
                row.addView(credit);

                TextView debit = new TextView(this);
                debit.setText(" Debit Card");
                debit.setTypeface(null, Typeface.BOLD);
                debit.setBackgroundResource(R.drawable.tableformat);
                row.addView(debit);

                TextView netbanking = new TextView(this);
                netbanking.setText(" Net Banking");
                netbanking.setTypeface(null, Typeface.BOLD);
                netbanking.setBackgroundResource(R.drawable.tableformat);
                row.addView(netbanking);

                TextView paytm = new TextView(this);
                paytm.setText(" PayTM ");
                paytm.setTypeface(null, Typeface.BOLD);
                paytm.setBackgroundResource(R.drawable.tableformat);
                row.addView(paytm);

                TextView others = new TextView(this);
                others.setText(" Others ");
                others.setTypeface(null, Typeface.BOLD);
                others.setBackgroundResource(R.drawable.tableformat);
                row.addView(others);

                TextView total = new TextView(this);
                total.setText(" Total ");
                total.setTypeface(null, Typeface.BOLD);
                total.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                row.addView(total);

                table.addView(row);

                report.clear();
                givento.clear();

                //generating report
                for (int i = 0; i < incomeList.size(); i++) {
                    int cashamt = 0;
                    int creditamt = 0;
                    int chequeamt = 0;
                    int debitamt = 0;
                    int netbankingamt = 0;
                    int paytmamt = 0;
                    int othersamt = 0;
                    CustomerWiseInExReport inex = new CustomerWiseInExReport();
                    Date date = incomeList.get(i).getDate();
                    if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                        String co = spinner_companyreport.getSelectedItem().toString();
                        if (incomeList.get(i).getCompanyname().equals(co)) {
                            if (!givento.contains(incomeList.get(i).getRname())) {
                                //adding new to list
                                givento.add(incomeList.get(i).getRname());
                                if (incomeList.get(i).getPaymentmode().equals("Cash")) {
                                    cashamt = cashamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                if (incomeList.get(i).getPaymentmode().equals("Debit Card")) {
                                    debitamt = debitamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                if (incomeList.get(i).getPaymentmode().equals("Credit Card")) {
                                    creditamt = creditamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                if (incomeList.get(i).getPaymentmode().equals("Cheque")) {
                                    chequeamt = chequeamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                if (incomeList.get(i).getPaymentmode().equals("Net Banking")) {
                                    netbankingamt = netbankingamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                if (incomeList.get(i).getPaymentmode().equals("Paytm")) {
                                    paytmamt = paytmamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                if (incomeList.get(i).getPaymentmode().equals("Others")) {
                                    othersamt = othersamt + Integer.parseInt(incomeList.get(i).getAmount());
                                }
                                int tot = cashamt + debitamt + creditamt + chequeamt + netbankingamt + paytmamt + othersamt;
                                inex.setName(incomeList.get(i).getRname());
                                inex.setCash(cashamt);
                                inex.setDebit(debitamt);
                                inex.setCredit(creditamt);
                                inex.setCheque(chequeamt);
                                inex.setNet(netbankingamt);
                                inex.setPaytm(paytmamt);
                                inex.setPaytm(paytmamt);
                                inex.setOthers(othersamt);
                                inex.setTotal(tot);

                                report.add(inex);
                            } else {
                                String nm = incomeList.get(i).getRname();
                                getData(nm, i);
                            }
                        }
                    }

                }
                for (int j = 0; j < report.size(); j++) {

                    //showing data in color
                    if (j % 2 == 0) {
                        TableRow tableRow = new TableRow(this);
                        TextView cname = new TextView(this);
                        cname.setText("  " + report.get(j).getName());
                        cname.setBackgroundResource(R.drawable.tableformat);
                        cname.setGravity(Gravity.LEFT);
                        tableRow.addView(cname);

                        TextView ccash = new TextView(this);
                        DecimalFormat formatter = new DecimalFormat("#,##,###");
                        String formatted = formatter.format(report.get(j).getCash());
                        ccash.setText("" +formatted );
                        ccash.setBackgroundResource(R.drawable.tableformat);
                        ccash.setGravity(Gravity.END);
                        tableRow.addView(ccash);

                        TextView ccheque = new TextView(this);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                        String formatted1 = formatter.format(report.get(j).getCheque());
                        ccheque.setText("" + formatted1);
                        ccheque.setBackgroundResource(R.drawable.tableformat);
                        ccheque.setGravity(Gravity.END);
                        tableRow.addView(ccheque);

                        TextView ccredit = new TextView(this);
                        DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                        String formatted2 = formatter.format(report.get(j).getCredit());
                        ccredit.setText("" +formatted2 );
                        ccredit.setBackgroundResource(R.drawable.tableformat);
                        ccredit.setGravity(Gravity.END);
                        tableRow.addView(ccredit);

                        TextView cdebit = new TextView(this);
                        DecimalFormat formatter3 = new DecimalFormat("#,##,###");
                        String formatted3 = formatter.format(report.get(j).getDebit());
                        cdebit.setText("" + formatted3);
                        cdebit.setBackgroundResource(R.drawable.tableformat);
                        cdebit.setGravity(Gravity.END);
                        tableRow.addView(cdebit);

                        TextView cnet = new TextView(this);
                        DecimalFormat formatter4 = new DecimalFormat("#,##,###");
                        String formatted4 = formatter.format( report.get(j).getNet());
                        cnet.setText("" +formatted4);
                        cnet.setBackgroundResource(R.drawable.tableformat);
                        cnet.setGravity(Gravity.END);
                        tableRow.addView(cnet);

                        TextView cpaytm = new TextView(this);
                        DecimalFormat formatter5 = new DecimalFormat("#,##,###");
                        String formatted5 = formatter.format( report.get(j).getPaytm());
                        cpaytm.setText("" +formatted5 );
                        cpaytm.setBackgroundResource(R.drawable.tableformat);
                        cpaytm.setGravity(Gravity.END);
                        tableRow.addView(cpaytm);

                        TextView cothers = new TextView(this);
                        DecimalFormat formatter6 = new DecimalFormat("#,##,###");
                        String formatted6 = formatter.format(  report.get(j).getOthers());
                        cothers.setText("" +formatted6);
                        cothers.setBackgroundResource(R.drawable.tableformat);
                        cothers.setGravity(Gravity.END);
                        tableRow.addView(cothers);

                        TextView ctotal = new TextView(this);
                        DecimalFormat formatter7 = new DecimalFormat("#,##,###");
                        String formatted7 = formatter.format( report.get(j).getTotal());
                        ctotal.setText("" +formatted7 );
                        ctotal.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                        ctotal.setGravity(Gravity.END);
                        tableRow.addView(ctotal);

                        table.addView(tableRow);
                    } else {
                        TableRow tableRow = new TableRow(this);
                        TextView cname = new TextView(this);
                        cname.setText("  " + report.get(j).getName());
                        cname.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cname.setGravity(Gravity.LEFT);
                        tableRow.addView(cname);

                        TextView ccash = new TextView(this);
                        DecimalFormat formatter = new DecimalFormat("#,##,###");
                        String formatted = formatter.format(report.get(j).getCash());
                        ccash.setText("" +formatted );
                        ccash.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccash.setGravity(Gravity.END);
                        tableRow.addView(ccash);

                        TextView ccheque = new TextView(this);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###");
                        String formatted1 = formatter.format(report.get(j).getCheque());
                        ccheque.setText("" +formatted1 );
                        ccheque.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccheque.setGravity(Gravity.END);
                        tableRow.addView(ccheque);

                        TextView ccredit = new TextView(this);
                        DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                        String formatted2 = formatter.format(report.get(j).getCredit());
                        ccredit.setText("" +formatted2 );
                        ccredit.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        ccredit.setGravity(Gravity.END);
                        tableRow.addView(ccredit);

                        TextView cdebit = new TextView(this);
                        DecimalFormat formatter3 = new DecimalFormat("#,##,###");
                        String formatted3 = formatter.format( report.get(j).getDebit());
                        cdebit.setText("" +formatted3);
                        cdebit.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cdebit.setGravity(Gravity.END);
                        tableRow.addView(cdebit);

                        TextView cnet = new TextView(this);
                        DecimalFormat formatter4 = new DecimalFormat("#,##,###");
                        String formatted4 = formatter.format(  report.get(j).getNet());
                        cnet.setText("" +formatted4);
                        cnet.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cnet.setGravity(Gravity.END);
                        tableRow.addView(cnet);

                        TextView cpaytm = new TextView(this);
                        DecimalFormat formatter5 = new DecimalFormat("#,##,###");
                        String formatted5 = formatter.format(  report.get(j).getPaytm());
                        cpaytm.setText("" +formatted5 );
                        cpaytm.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cpaytm.setGravity(Gravity.END);
                        tableRow.addView(cpaytm);

                        TextView cothers = new TextView(this);
                        DecimalFormat formatter6 = new DecimalFormat("#,##,###");
                        String formatted6 = formatter.format(  report.get(j).getOthers());
                        cothers.setText("" +formatted6 );
                        cothers.setBackgroundResource(R.drawable.tableformatwithgreycolor);
                        cothers.setGravity(Gravity.END);
                        tableRow.addView(cothers);

                        TextView ctotal = new TextView(this);
                        DecimalFormat formatter7 = new DecimalFormat("#,##,###");
                        String formatted7 = formatter.format(  report.get(j).getTotal());
                        ctotal.setText("" + formatted7);
                        ctotal.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                        ctotal.setGravity(Gravity.END);
                        tableRow.addView(ctotal);

                        table.addView(tableRow);
                    }



                }

                //calculating last coloumn totals
                int cashtotal = 0;
                int chequetotal = 0;
                int debittotal = 0;
                int credittotal = 0;
                int netbankingtotal = 0;
                int paytmtotal = 0;
                int othertotal = 0;
                int tottot = 0;
                for (int a = 0; a < report.size(); a++) {
                    cashtotal = cashtotal + report.get(a).getCash();
                    chequetotal = chequetotal + report.get(a).getCheque();
                    debittotal = debittotal + report.get(a).getDebit();
                    credittotal = credittotal + report.get(a).getCredit();
                    netbankingtotal = netbankingtotal + report.get(a).getNet();
                    paytmtotal = paytmtotal + report.get(a).getPaytm();
                    othertotal = othertotal + report.get(a).getOthers();
                }

                //total of orange color
                tottot = cashtotal + debittotal + chequetotal + credittotal + netbankingtotal + paytmtotal + othertotal;

                TableRow row1 = new TableRow(this);

                TextView distot = new TextView(this);

                distot.setText("Total");
                distot.setTypeface(null, Typeface.BOLD);
                distot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                distot.setGravity(Gravity.CENTER);
                row1.addView(distot);

                TextView cashtot = new TextView(this);

                DecimalFormat formatter = new DecimalFormat("#,##,###");
                String formatted = formatter.format(cashtotal);

                cashtot.setText("" + formatted);
                cashtot.setTypeface(null, Typeface.BOLD);
                cashtot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                cashtot.setGravity(Gravity.END);
                row1.addView(cashtot);

                TextView cheqtot = new TextView(this);

                DecimalFormat formatter2 = new DecimalFormat("#,##,###");
                String formatted2 = formatter.format(chequetotal);
                cheqtot.setText("" + formatted2);
                cheqtot.setTypeface(null, Typeface.BOLD);
                cheqtot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                cheqtot.setGravity(Gravity.END);
                row1.addView(cheqtot);

                TextView credtot = new TextView(this);
                DecimalFormat formatter3 = new DecimalFormat("#,##,###");
                String formatted3 = formatter.format(credittotal);
                credtot.setText("" +formatted3 );
                credtot.setTypeface(null, Typeface.BOLD);
                credtot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                credtot.setGravity(Gravity.END);
                row1.addView(credtot);

                TextView debitot = new TextView(this);
                DecimalFormat formatter4 = new DecimalFormat("#,##,###");
                String formatted4 = formatter.format(debittotal);
                debitot.setText("" + formatted4);
                debitot.setTypeface(null, Typeface.BOLD);
                debitot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                debitot.setGravity(Gravity.END);
                row1.addView(debitot);

                TextView nettot = new TextView(this);
                DecimalFormat formatter5 = new DecimalFormat("#,##,###");
                String formatted5 = formatter.format(netbankingtotal);
                nettot.setText("" +formatted5 );
                nettot.setTypeface(null, Typeface.BOLD);
                nettot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                nettot.setGravity(Gravity.END);
                row1.addView(nettot);

                TextView paytot = new TextView(this);
                DecimalFormat formatter6 = new DecimalFormat("#,##,###");
                String formatted6 = formatter.format(paytmtotal);
                paytot.setText("" +formatted6 );
                paytot.setTypeface(null, Typeface.BOLD);
                paytot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                paytot.setGravity(Gravity.END);
                row1.addView(paytot);

                TextView othertot = new TextView(this);
                DecimalFormat formatter7 = new DecimalFormat("#,##,###");
                String formatted7 = formatter.format(othertotal);
                othertot.setText("" + formatted7);
                othertot.setTypeface(null, Typeface.BOLD);
                othertot.setBackgroundResource(R.drawable.tableformatwithyellowcolor);
                othertot.setGravity(Gravity.END);
                row1.addView(othertot);

                TextView totoftot = new TextView(this);
                DecimalFormat formatter8 = new DecimalFormat("#,##,###");
                String formatted8 = formatter.format(tottot);
                totoftot.setText("" + formatted8);
                totoftot.setTypeface(null, Typeface.BOLD);
                totoftot.setBackgroundResource(R.drawable.tableformatwithredcolor);
                totoftot.setGravity(Gravity.END);
                row1.addView(totoftot);

                table.addView(row1);

//                // create alert dialog
//                final AlertDialog alertDialog = alertDialogBuilder.create();
//                // show it
//                alertDialog.show();
                confirmDialog.setCancelable(false);
                confirmDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            confirmDialog.dismiss();
                            Intent intent = new Intent(Track.this, Track.class);
                            startActivity(intent);
                            expenseList.clear();
                            incomeList.clear();
                            Track.this.finish();

                        }
                        return true;
                    }
                });
            } else {
                Toast.makeText(Track.this, "Please select company name", Toast.LENGTH_SHORT).show();
                spinner_companyreport.performClick();
            }
        }


    }

    private void modeofpaymentexpensedialoug() {
        if (txtfromdate.getText().toString().equals("") || txttodate.getText().toString().equals("")) {
            Toast.makeText(Track.this, "Please select date", Toast.LENGTH_SHORT).show();
        } else if (!maxdate.after(mindate) && !maxdate.equals(mindate)) {
            Toast.makeText(Track.this, "Please select proper dates", Toast.LENGTH_SHORT).show();
        } else {
            if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {
                //  if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {

                String company = spinner_companyreport.getSelectedItem().toString();

                LayoutInflater li = LayoutInflater.from(Track.this);

                final View promptsView = li.inflate(R.layout.dialoug_modeofpayment, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Track.this);

                alertDialogBuilder.setTitle("Mode of Payments For Expenses");
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setView(promptsView);

                final TextView from = (TextView) promptsView.findViewById(R.id.txtfrom);
                final TextView to = (TextView) promptsView.findViewById(R.id.txtto);
                final TextView copnyname = (TextView) promptsView.findViewById(R.id.txtnameofcompany);

                final TextView cash = (TextView) promptsView.findViewById(R.id.txt_cash);
                final TextView cheque = (TextView) promptsView.findViewById(R.id.txt_cheque);
                final TextView creditcard = (TextView) promptsView.findViewById(R.id.txt_creditcard);
                final TextView debitcard = (TextView) promptsView.findViewById(R.id.txt_debitcard);
                final TextView netbanking = (TextView) promptsView.findViewById(R.id.txt_netbanking);
                final TextView others = (TextView) promptsView.findViewById(R.id.txt_others);
                final TextView paytm = (TextView) promptsView.findViewById(R.id.txt_paytm);
                final TextView total = (TextView) promptsView.findViewById(R.id.txt_total);

                int cash2 = 0;
                int cheque2 = 0;
                int creditcard2 = 0;
                int debitcard2 = 0;
                int netbanking2 = 0;
                int others2 = 0;
                int paytm2 = 0;
                int total2 = 0;


                for (int i = 0; i < expenseList.size(); i++) {

                    Date date = expenseList.get(i).getDate();

                    copnyname.setText("Name of company:  " + company);
                    from.setText("From:  " + phpFromDate);
                    to.setText("To:  " + phpToDate);

                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Cash")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            cash2 = cash2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(cash2);
                            String l = String.valueOf(cash.length());
                            System.out.print(l);
                            cash.setText("" + formatted);
                        }
                        Log.e("cash", String.valueOf(cash2));
                    }
                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Cheque")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            cheque2 = cheque2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(cheque2);
                            cheque.setText("" + formatted);
                        }
                        Log.e("cheque", String.valueOf(cheque2));
                    }
                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Credit Card")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            creditcard2 = creditcard2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(creditcard2);
                            creditcard.setText("" + formatted);
                        }
                    }

                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Debit Card")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            debitcard2 = debitcard2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(debitcard2);
                            debitcard.setText("" + formatted);
                        }

                    }

                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Net Banking")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            netbanking2 = netbanking2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(netbanking2);
                            netbanking.setText("" + formatted);
                        }
                    }
                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Others")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            others2 = others2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(others2);
                            others.setText("" + formatted);
                        }
                    }
                    if (expenseList.get(i).getCompanyname().equals(company) && expenseList.get(i).getPaymentmode().equals("Paytm")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            paytm2 = paytm2 + Integer.parseInt(expenseList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(paytm2);
                            paytm.setText("" + formatted);
                        }
                    }

                    total2 = cash2 + cheque2 + creditcard2 + debitcard2 + netbanking2 + others2 + paytm2;
                    DecimalFormat formatter = new DecimalFormat("#,##,###");
                    String formatted = formatter.format(total2);
                    total.setText("Total Expense is  " + formatted);

                }

                //     incomeList.clear();

                final AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                alertDialog.setCancelable(false);

                alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(Track.this, Track.class);
                            startActivity(intent);
                            expenseList.clear();
                            incomeList.clear();
                            Track.this.finish();

                        }
                        return true;
                    }
                });

        } else {
                Toast.makeText(this, "Please select company", Toast.LENGTH_SHORT).show();
                spinner_companyreport.performClick();
            }

        }


    }

    private void modeofpaymentdialoug() {
        if (txtfromdate.getText().toString().equals("") || txttodate.getText().toString().equals("")) {
            Toast.makeText(Track.this, "Please select date", Toast.LENGTH_SHORT).show();
        } else if (!maxdate.after(mindate) && !maxdate.equals(mindate)) {
            Toast.makeText(Track.this, "Please select proper dates", Toast.LENGTH_SHORT).show();
        } else {
            if (!spinner_companyreport.getSelectedItem().toString().equals("<<Select>>")) {

                LayoutInflater li = LayoutInflater.from(Track.this);
                final View promptsView = li.inflate(R.layout.dialoug_modeofpayment, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Track.this);

                alertDialogBuilder.setTitle("Mode of Payments For Incomes");
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextView from = (TextView) promptsView.findViewById(R.id.txtfrom);
                final TextView to = (TextView) promptsView.findViewById(R.id.txtto);
                final TextView copnyname = (TextView) promptsView.findViewById(R.id.txtnameofcompany);


                final TextView cash = (TextView) promptsView.findViewById(R.id.txt_cash);
                final TextView cheque = (TextView) promptsView.findViewById(R.id.txt_cheque);
                final TextView creditcard = (TextView) promptsView.findViewById(R.id.txt_creditcard);
                final TextView debitcard = (TextView) promptsView.findViewById(R.id.txt_debitcard);
                final TextView netbanking = (TextView) promptsView.findViewById(R.id.txt_netbanking);
                final TextView others = (TextView) promptsView.findViewById(R.id.txt_others);
                final TextView paytm = (TextView) promptsView.findViewById(R.id.txt_paytm);
                final TextView total = (TextView) promptsView.findViewById(R.id.txt_total);

                int cash1 = 0;
                int cheque1 = 0;
                int creditcard1 = 0;
                int debitcard1 = 0;
                int netbanking1 = 0;
                int others1 = 0;
                int paytm1 = 0;
                int total1 = 0;


                for (int i = 0; i < incomeList.size(); i++) {

                    Date date = incomeList.get(i).getDate();
                    String company = spinner_companyreport.getSelectedItem().toString();
                    copnyname.setText("Name of company:  " + company);
                    from.setText("From:  " + phpFromDate);
                    to.setText("To:  " + phpToDate);
                    //   String company = spinner_companyreport.getSelectedItem().toString();
                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Cash")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            cash1 = cash1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(cash1);
                            cash.setText("" + formatted);
                        }
                        Log.e("cash", String.valueOf(cash1));
                    }
                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Cheque")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            cheque1 = cheque1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(cheque1);
                            cheque.setText("" + formatted);
                        }
                        Log.e("cheque", String.valueOf(cheque1));
                    }
                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Credit Card")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            creditcard1 = creditcard1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(creditcard1);
                            creditcard.setText("" + formatted);
                        }
                    }

                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Debit Card")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            debitcard1 = debitcard1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(debitcard1);
                            debitcard.setText("" + formatted);
                        }

                    }

                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Net Banking")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            netbanking1 = netbanking1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(netbanking1);
                            netbanking.setText("" + formatted);
                        }
                    }
                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Others")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            others1 = others1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(others1);
                            others.setText("" + formatted);
                        }
                    }
                    if (incomeList.get(i).getCompanyname().equals(company) && incomeList.get(i).getPaymentmode().equals("Paytm")) {
                        if ((date.after(mindate) || date.equals(mindate)) && (date.before(maxdate) || date.equals(maxdate))) {
                            paytm1 = paytm1 + Integer.parseInt(incomeList.get(i).getAmount());
                            DecimalFormat formatter = new DecimalFormat("#,##,###");
                            String formatted = formatter.format(paytm1);
                            paytm.setText("" + formatted);
                        }
                    }

                    total1 = cash1 + cheque1 + creditcard1 + debitcard1 + netbanking1 + others1 + paytm1;
                    DecimalFormat formatter = new DecimalFormat("#,##,###");
                    String formatted = formatter.format(total1);
                    total.setText("Total Income is  " + formatted);

                }


                //     incomeList.clear();

                final AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                alertDialog.setCancelable(false);

                alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(Track.this, Track.class);
                            startActivity(intent);
                            expenseList.clear();
                            incomeList.clear();
                            Track.this.finish();

                        }
                        return true;
                    }
                });
            }
            else {
                Toast.makeText(this, "Please select company", Toast.LENGTH_SHORT).show();
                spinner_companyreport.performClick();
            }
        }

    }


    @Override
    public void onClick(View v) {
        if (v == selectfromdate) {
            isExpenseDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select From date ");
            pickerDialog.show(this.getFragmentManager(), "DatePicker");
        }
        if (v == selecttodate) {
            isExpenseBillDate = true;
            Calendar now = Calendar.getInstance();

            DatePickerDialog pickerDialog = DatePickerDialog.newInstance(this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            pickerDialog.setThemeDark(true);
            pickerDialog.setAccentColor(Color.parseColor("#ecbe8d"));
            pickerDialog.setTitle("Select to date ");
            pickerDialog.show(this.getFragmentManager(), "DatePicker");

        }

        if (v == btn_clear) {
            downloadicon.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            btn_track.setVisibility(View.VISIBLE);
            btn_clear.setVisibility(View.GONE);

            Intent intent = new Intent(Track.this, Admin.class);
            startActivity(intent);
            Track.this.finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        incomeList.clear();
        expenseList.clear();
        profitLossPojoList.clear();
        Intent intent = new Intent(Track.this, Admin.class);
        startActivity(intent);
        Track.this.finish();
    }

    //Get Company Details
    private void getCompanyList() {
        progressDialogcompany = new ProgressDialog(Track.this);
        progressDialogcompany.setTitle("Processing...");
        progressDialogcompany.setMessage("Please wait...");
        progressDialogcompany.setCancelable(false);
        progressDialogcompany.show();
        isCompany = true;
        isIncome = false;
        isExpense = false;
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/companydetails.php?ownerid=" + Admin.ownerid, null, this, this);
        Log.e("getRequestofCompany: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }


    private void getIncome() {
        isCompany = false;
        isIncome = true;
        isExpense = false;
        progressDialogincome = new ProgressDialog(Track.this);
        progressDialogincome.setTitle("Processing...");
        progressDialogincome.setMessage("Please wait...");

        progressDialogincome.setCancelable(false);
        progressDialogincome.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialogincome.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 15000);

        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/income.php?ownerid=" + Admin.ownerid, null, this, this);
        Log.e("getRequestIncome: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }

    private void getExpense() {
        isCompany = false;
        isIncome = false;
        isExpense = true;
        progressDialog = new ProgressDialog(Track.this);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 15000);

        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/expense.php?ownerid=" + Admin.ownerid, null, this, this);
        Log.e("getRequestExpense: ", String.valueOf(request));
        AppController.requestQueue.add(request);
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


        if (isExpenseDate) {
            phpFromDate = dayOfMonth + "-" + strMonth + "-" + year;
            final String date1 = dayOfMonth + "-" + strMonth + "-" + year;
            Calendar now = Calendar.getInstance();


//            expenseList.clear();
//            incomeList.clear();
//            profitLossPojoList.clear();
            expenseList.clear();
            txtfromdate.setText(date1);

//            if (txtfromdate.getText().toString().equals(""))
//            {
//                selectfromdate.setEnabled(true);
//            }
//            else
//            {
//                selectfromdate.setEnabled(true);
//            }
            String dtStart = date1;
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                mindate = format.parse(dtStart);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isExpenseDate = false;
            getExpense();


        } else {
            phpToDate = dayOfMonth + "-" + strMonth + "-" + year;
            final String date1 = dayOfMonth + "-" + strMonth + "-" + year;
            incomeList.clear();
            txttodate.setText(date1);

//            if (txttodate.getText().toString().equals(""))
//            {
//                selecttodate.setEnabled(true);
//            }
//            else
//            {
//                selecttodate.setEnabled(false);
//            }

            String dtStart = date1;
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                maxdate = format.parse(dtStart);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isExpenseBillDate = false;

            getIncome();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        android.app.AlertDialog.Builder reorder = new android.app.AlertDialog.Builder(Track.this);
        reorder.setTitle("Getting Detail");
        reorder.setMessage("Internet issue/failed to load data.");
        reorder.setCancelable(true);
        reorder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getCompanyList();

                    }
                });

        android.app.AlertDialog orderError = reorder.create();
        orderError.show();

    }

    @Override
    public void onResponse(JSONObject response) {


        if (isCompany) {
            companyList.clear();

            Log.e("Response", String.valueOf(response));
            try {

                JSONArray jsonArray = response.getJSONArray("Details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    companyname_pojo companyname = new companyname_pojo();

                    String cmpynm = object.getString("companyname");

                    companyname.setCompanyname(cmpynm);

                    companyList.add(companyname);

                }
                //From Spinner
                companyname.clear();
                companyname.add("<<Select>>");
                for (int i = 0; i < Track.companyList.size(); i++) {

                    companyname.add(companyList.get(i).getCompanyname());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, companyname);
                spinner_companyreport.setAdapter(adapter);//Till Spinner
//getIncome();
                progressDialogcompany.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        if (isIncome) {
            AdmindetailedIncome.clear();
            incomeList.clear();
            Log.e("Response", String.valueOf(response));
            try {

                JSONArray jsonArray = response.getJSONArray("Details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    Income income = new Income();
                    //  AdmindetailedIncome.clear();

                    String incomeid = object.getString("id");
                    String incomedate = object.getString("date");
                    String incomebillno = object.getString("billno");
                    String incomebilldate = object.getString("billdate");
                    String incomeamount = object.getString("amount");
                    String incomepaymentmode = object.getString("paymentmode");
                    String incomechequeno = object.getString("chequeno");
                    String incomebankname = object.getString("bankname");
                    String incomername = object.getString("rname");
                    String incomepurpose = object.getString("purpose");
                    String incomeremark = object.getString("remark");
                    String incomecompanyname = object.getString("companyname");
                    String incomeDMLDateTime = object.getString("DMLDateTime");

                    income.setId(incomeid);
                    income.setBillno(incomebillno);
                    income.setBilldate(incomebilldate);
                    income.setAmount(incomeamount);
                    income.setPaymentmode(incomepaymentmode);
                    income.setChequeno(incomechequeno);
                    income.setBankname(incomebankname);
                    income.setRname(incomername);
                    income.setPurpose(incomepurpose);
                    income.setRemark(incomeremark);
                    income.setCompanyname(incomecompanyname);
                    income.setDMLDateTime(incomeDMLDateTime);

                    incomeList.add(income);

                    //  getExpense();

                    String dtStart = incomedate;
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    try {
                        date = format.parse(dtStart);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    income.setDate(date);

                    progressDialogincome.dismiss();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        if (isExpense) {
            expenseList.clear();
            AdmindetailedExpense.clear();
            Log.e("Response", String.valueOf(response));
            try {
                JSONArray jsonArray = response.getJSONArray("exp");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    Expense expense = new Expense();
                    // AdmindetailedExpense.clear();

                    String expenseid = object.getString("id");
                    String expensedate = object.getString("date");
                    String expensebillno = object.getString("billno");
                    String expensebilldate = object.getString("billdate");
                    String expenseamount = object.getString("amount");
                    String expensepaymentmode = object.getString("paymentmode");
                    String expensechequeno = object.getString("chequeno");
                    String expensebankname = object.getString("bankname");
                    String expensegivento = object.getString("givento");
                    String expensepurpose = object.getString("purpose");
                    String expenseremark = object.getString("remarks");
                    String expensecompanyname = object.getString("companyname");
                    String expenseDMLDateTime = object.getString("DMLDateTime");

                    expense.setId(expenseid);
                    //   expense.setDate(expensedate);
                    expense.setBillno(expensebillno);
                    expense.setBilldate(expensebilldate);
                    expense.setAmount(expenseamount);
                    expense.setPaymentmode(expensepaymentmode);
                    expense.setChequeno(expensechequeno);
                    expense.setBankname(expensebankname);
                    expense.setGivento(expensegivento);
                    expense.setPurpose(expensepurpose);
                    expense.setRemark(expenseremark);
                    expense.setCompanyname(expensecompanyname);
                    expense.setDMLDateTime(expenseDMLDateTime);

                    expenseList.add(expense);

                    String dtStartexp = expensedate;
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    try {
                        date = format.parse(dtStartexp);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    expense.setDate(date);
                    progressDialog.dismiss();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class MyClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            view.loadUrl(Url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

    class GoogleClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

        }

    }

    private class webview extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(Url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);

        }
    }

    private void requestStoragePermission() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }
}





