package skytechhub.myaccounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

import skytechhub.myaccounts.POJO.EmployeeList;
import skytechhub.myaccounts.POJO.companyname_pojo;
import skytechhub.myaccounts.URLclass.URLClass;
import skytechhub.myaccounts.app.AppController;
import skytechhub.myaccounts.webhandler.CustomJSONObjectRequest;


public class AddEmp extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    private EditText edt_personname, edt_personnumber, edt_reg_email;
    private Spinner spinner_company;
    ProgressDialog loading;
    private Button btn_reg;
    private CheckBox check_edit;
    private String isEdit;

    private List<String> companyname = new ArrayList<>();
    public static List<EmployeeList> employeeList = new ArrayList<>();
    public static List<EmployeeList> deactiveempList = new ArrayList<>();
    public static List<companyname_pojo> companyList = new ArrayList<>();

    public Boolean getEmployee, getCompanyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_employee, null);


        getEmployee();


        edt_personname =(EditText) view.findViewById(R.id.edt_personname);
        edt_personnumber =(EditText) view.findViewById(R.id.edt_personnumber);
        edt_reg_email =(EditText) view.findViewById(R.id.edt_reg_email);
        check_edit = (CheckBox) view.findViewById(R.id.check_edit);
        check_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (check_edit.isChecked()) {
                    isEdit = "Yes";
                } else {
                    isEdit = "No";
                }
            }
        });


        spinner_company = (Spinner) view.findViewById(R.id.spinner_company);
//        companyname.clear();
//        companyname.add("<<Select>>");
//
//        for (int i=0;i<Admin.companyList.size();i++)
//        {
//            companyname.add(Admin.companyList.get(i).getCompanyname().toString());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, companyname);
//        spinner_company.setAdapter(adapter);


        btn_reg = (Button) view.findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    register();
                }


            }
        });


        return view;


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

        if (!check) {
            edt_reg_email.setError("Not Valid Email");
        }
        return check;
    }

    public boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {

            if (phone.length() != 10) {
                check = false;
                edt_personnumber.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private void register() {

        String personname = edt_personname.getText().toString().trim();
        String personnumber = edt_personnumber.getText().toString().trim();
        isValidMobile(personnumber);
        String c_name = spinner_company.getSelectedItem().toString();
        String email = edt_reg_email.getText().toString().trim();
        isValidMail(email);
        String report = isEdit;
        String id = Admin.ownerid;
        if (personname.equals("")) {
            edt_personname.setError("Please enter employee name");
        } else if (personnumber.equals("")) {
            edt_personnumber.setError("Please enter employee mobile number");
        } else if (c_name.equals("<<Select>>")) {
            Toast.makeText(getActivity(), "Please select coompany", Toast.LENGTH_SHORT).show();
            spinner_company.performClick();
        } else if (email.equals("")) {
            edt_reg_email.setError("Please eter employee email");
        } else if (!isValidMobile(personnumber)) {
            edt_personnumber.setError("Please enter 10 digit mobile number");
        } else if (!isValidMail(email)) {
            edt_reg_email.setError("Please enter proper email address");
        } else {
            sendToServer(personname, personnumber, c_name, email, id, report);
        }
    }


    private void sendToServer(String personname, String personnumber, String c_name, String email, String id, String report) {


        String urlSuffix = "?name=" + personname.replace(" ", "%20") + "&phone=" + personnumber + "&emailid=" + email.replace(" ", "%20") + "&companyname=" + c_name.replace(" ", "%20") + "&ownerid=" + id + "&editreport=" + report;
        class bookingAppointment_membership extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //  Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(getActivity());
                    reorder.setMessage("Employee added successfully.");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                    Intent intent = new Intent(getActivity(), Admin.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();

                } else {
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
                    URL url = new URL(URLClass.ADD_EMPLOYEE + s);
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

    //Employee Active or Deactive
    private void getEmployee() {
        getEmployee = true;
        getCompanyList = false;
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/employeedetails.php?ownerid=" + Admin.ownerid, null, this, this);
        Log.e("getRequestofEmployee: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }


    //Get Company Details
    private void getCompanyList() {
        getCompanyList = true;
        getEmployee = false;
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, "http://newmyaccounts.skytechhub.com/GettingResponse/companydetails.php?ownerid=" + Admin.ownerid, null, this, this);
        Log.e("getRequestofCompany: ", String.valueOf(request));
        AppController.requestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        getCompanyList();
    }


    @Override
    public void onResponse(JSONObject response) {
        if (getEmployee) {
            Log.e("Response", String.valueOf(response));
            employeeList.clear();
            deactiveempList.clear();
            getCompanyList();

            try {
                JSONArray jsonArray = response.getJSONArray("getData");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    EmployeeList employeename_pojo = new EmployeeList();

                    String id = object.getString("id");
                    String ownerid1 = object.getString("ownerid");
                    String name = object.getString("name");
                    String phone = object.getString("phone");
                    String emailid = object.getString("emailid");
                    String companyname = object.getString("companyname");
                    String role = object.getString("role");
                    String discard = object.getString("discard");
                    String status = object.getString("editreport");


                    employeename_pojo.setEmpid(id);
                    employeename_pojo.setEmpownerid(ownerid1);
                    employeename_pojo.setEmpname(name);
                    employeename_pojo.setEmpphone(phone);
                    employeename_pojo.setEmpemail(emailid);
                    employeename_pojo.setEmpcompanyname(companyname);
                    employeename_pojo.setEmpdiscard(discard);
                    employeename_pojo.setEmprole(role);
                    employeename_pojo.setReportedit(status);

                    if (discard.contains("No")) {
                        employeeList.add(employeename_pojo);
                    }
                    if (discard.contains("Yes")) {
                        deactiveempList.add(employeename_pojo);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (getCompanyList) {
            companyList.clear();
            Log.e("Response", String.valueOf(response));
            try {

                JSONArray jsonArray = response.getJSONArray("Details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    companyname_pojo companyname = new companyname_pojo();

                    String cmpynm = object.getString("companyname");
                    String dis = object.getString("discard");

                    companyname.setCompanyname(cmpynm);
                    companyname.setDiscard(dis);

                    companyList.add(companyname);

                }
                //From Spinner
                companyname.clear();

                companyname.add("<<Select>>");

                for (int i = 0; i < companyList.size(); i++) {
                    if (companyList.get(i).getDiscard().equals("No")) {
                        companyname.add(companyList.get(i).getCompanyname().toString());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, companyname);
                spinner_company.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
