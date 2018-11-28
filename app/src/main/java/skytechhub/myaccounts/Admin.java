package skytechhub.myaccounts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import skytechhub.myaccounts.POJO.companyname_pojo;
import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Response.ErrorListener, Response.Listener<JSONObject> {

    public static String ownerid, ownername, ownerphone, owneremailid;

    public static String companyid, companyname;

    public static List<companyname_pojo> companyList = new ArrayList<>();
    public static List<companyname_pojo> deactivecompanyList = new ArrayList<>();


    private TextView txt_companycount, edt_employeecoount;
    private boolean isCompanyList, isAdminDetails, isCounts;     // isCounts is for Total Employee and company count

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppController.preferences.getPreference("name", ""));
        setSupportActionBar(toolbar);



        getClientDetails();


        txt_companycount = (TextView) findViewById(R.id.txt_companycount);
        edt_employeecoount = (TextView) findViewById(R.id.edt_employeecoount);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        }  else {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Admin.this.finish();

                        }
                    }).setNegativeButton("No", null).show();
            }
        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



         if (id == R.id.nav_emp) {

            Intent intent = new Intent(Admin.this, AddEmployee.class);
            startActivity(intent);


        }else if(id==R.id.nav_income)
        {
            Intent intent = new Intent(Admin.this, Track.class);
            startActivity(intent);
            finish();
        }


        else if (id == R.id.nav_addcompany) {

            Intent intent = new Intent(Admin.this, Company.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_changepass) {
            Intent intent = new Intent(Admin.this, ChangePass.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_logout) {
            AppController.preferences.savePreference("isLogin", "false");
            AppController.preferences.savePreference("role", "");
             AppController.preferences.savePreference("username", "");




            final ProgressDialog progress = new ProgressDialog(Admin.this);
            progress.setTitle("Please wait");
            progress.setMessage("We are signing out your profile");
            progress.show();

            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    progress.cancel();
                    Intent intent = new Intent(Admin.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 3000);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // GET Request of Admin Details Owner id ,Owner name etc
    private void getClientDetails() {
        isAdminDetails = true;
        isCounts = false;
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/android/ownerdetail.php?emailid=" + AppController.preferences.getPreference("username", ""), null, Admin.this, Admin.this);
        Log.e("getClientid OTP: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }


    private void getCount() {   // this method is for getting total count of company and employee
        isAdminDetails = false;
        isCounts = true;
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/companycount.php?ownerid=" + ownerid, null, Admin.this, Admin.this);
        Log.e("getRequestofCount: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        android.app.AlertDialog.Builder reorder = new android.app.AlertDialog.Builder(Admin.this);
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


        if (isAdminDetails) {
            Log.e("Response", String.valueOf(response));
            try {
                JSONArray jsonArray = response.getJSONArray("Details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    ownerid = object.getString("id");
                    ownername = object.getString("name");
                    ownerphone = object.getString("phone");
                    owneremailid = object.getString("emailid");
                }
                getCount();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (isCounts) {
            Log.e("Response", String.valueOf(response));
            try {
                JSONArray jsonArray = response.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);


                    String count = object.getString("count");
                    String company = object.getString("company");

                   edt_employeecoount .setText("Total Active Employees: "+count);
                    txt_companycount.setText("Total Active Companies: "+company);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    }




