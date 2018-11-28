package skytechhub.myaccounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skytechhub.myaccounts.POJO.companyname_pojo;
import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;


public class AddCompany extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
    private EditText edt_companyname;
    private EditText edt_phone;
    private EditText edt_email;
    private EditText edt_website;
    private EditText edt_address;
    private EditText edt_city;
    private EditText edt_sector;
    private RadioGroup radiogrp;
    private RadioButton radio_public,radio_private;
    private Button btn_register;
    ProgressDialog loading;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_company,null);

        //Edit Text
        edt_companyname= (EditText) view.findViewById(R.id.edt_companyname);
        edt_phone= (EditText) view.findViewById(R.id.edt_phone);
        edt_email= (EditText) view.findViewById(R.id.edt_email);
        edt_website= (EditText) view.findViewById(R.id.edt_website);
        edt_address=(EditText) view.findViewById(R.id.edt_address);
        edt_city= (EditText)view.findViewById(R.id.edt_city);
        edt_sector=(EditText) view.findViewById(R.id.edt_sector);

        //Radio Group
        radiogrp=(RadioGroup)view.findViewById(R.id.radiogrp);

        //Radio Button
        radio_public=(RadioButton)view.findViewById(R.id.radio_public);
        radio_private=(RadioButton)view.findViewById(R.id.radio_private);

        //Button
        btn_register=(Button)view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        getCompanyList();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view==btn_register)
        {
                addcompany();
            }
    }



    //Get Company Details
    private void getCompanyList() {

        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/companydetails.php?ownerid=" + Admin.ownerid, null, AddCompany.this, AddCompany.this);
        Log.e("getRequestofCompany: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }


    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            edt_email.setError("Not Valid Email");
        }
        return check;
    }
    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {

            if(phone.length() != 10) {
                check = false;
                edt_phone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    public static boolean validateLetters(String txt) {

        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();

    }

    private void addcompany() {
        String companyname = edt_companyname.getText().toString().trim();

        String phone = edt_phone.getText().toString().trim();
        String email = edt_email.getText().toString().trim();

        String website = edt_website.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String city = edt_city.getText().toString().trim();
        String sector = edt_sector.getText().toString().trim();

        String publicprivate = ((RadioButton)getView().findViewById(radiogrp.getCheckedRadioButtonId())).getText().toString();

       if (companyname.equals(""))
       {
           edt_companyname.setError("please enter company name");
       }

       else if (email.equals(""))
       {
           edt_email.setError("please enter email id");
       }
       else if (address.equals(""))
       {
           edt_address.setError("please enter address");
       }
       else if (city.equals(""))
       {
           edt_city.setError("please enter city");
       }
      else if (sector.equals(""))
       {
           edt_sector.setError("please eneter sector");
       }
       else if (!isValidMail(email))
       {
           edt_email.setError("please enter valid email id");
       }
       else if (!isValidMobile(phone))
       {
           edt_phone.setError("please enter 10 digit mobile number");
       }
           else
        {
            sendToServer(companyname,phone,email,website,address,city,sector,publicprivate);
        }

    }

    private void sendToServer(String companyname, String phone, String email, String website, String address, String city, String sector, String publicprivate) {
        String urlSuffix = "?companyname=" + companyname.replace(" ", "%20")+"&companyphone="+phone+"&companyemailid="+email.replace(" ", "%20")+"&companywebsite="+website.replace(" ","%20")+"&companyaddress="+address.replace(" ", "%20")+"&companycity="+city.replace(" ", "%20")+"&sector="+sector.replace(" ", "%20")+"&pvtpub="+publicprivate.replace(" ", "%20")+"&ownerid="+Admin.ownerid;
        class Companyregister extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                if (s.equals("success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(getActivity());
                    reorder.setMessage("Successfully registered as company.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                    Intent intent = new Intent(getActivity(),Admin.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();

                }
                else
                {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(getActivity());
                    reorder.setMessage("Registration failed. Please try again/after sometime... ");
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
                    URL url = new URL(URLClass.REGISTER_COMPANY + s);
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
        Companyregister ba = new Companyregister();
        ba.execute(urlSuffix);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        getCompanyList();
    }

    @Override
    public void onResponse(JSONObject response) {

        Admin.deactivecompanyList.clear();
        Admin.companyList.clear();

        Log.e("Response", String.valueOf(response));

        try {
            JSONArray jsonArray = response.getJSONArray("Details");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                companyname_pojo companyname = new companyname_pojo();

                Admin.companyid = object.getString("id");
                String companyname1 = object.getString("companyname");
                String companyphone = object.getString("companyphone");
                String companyemail = object.getString("companyemailid");
                String companyaddress = object.getString("companyaddress");
                String companycity = object.getString("companycity");
                String discard = object.getString("discard");


                companyname.setCompanyname(companyname1);
                companyname.setCompanyphone(companyphone);
                companyname.setCompanyemailid(companyemail);
                companyname.setCompanyaddress(companyaddress);
                companyname.setCompanycity(companycity);
                companyname.setDiscard(discard);
                companyname.setCompanyid(Admin.companyid);


                if (discard.contains("No")) {
                    Admin.companyList.add(companyname);
                }
                if (discard.contains("Yes")) {
                    Admin.deactivecompanyList.add(companyname);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

