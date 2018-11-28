package skytechhub.myaccounts.Adaptor;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import skytechhub.myaccounts.AddEmp;
import skytechhub.myaccounts.AddEmployee;
import skytechhub.myaccounts.Admin;
import skytechhub.myaccounts.POJO.EmployeeList;
import skytechhub.myaccounts.R;
import skytechhub.myaccounts.URLclass.URLClass;

/**
 * Created by Deep on 05-07-2017.
 */


public class EmployeeList_Adapter extends RecyclerView.Adapter<EmployeeList_Adapter.ViewHolder> {

    private List<EmployeeList> eList;
    private LayoutInflater inflater;
    private TextView txt_empname, txt_empnumber, txt_emailid, txt_companyname, status;
    private Button btn_deactive, btn_active;

    public EmployeeList_Adapter(Context context, List<EmployeeList> e_list) {
        this.eList = e_list;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.emplyeename_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EmployeeList request = eList.get(position);

        txt_empname.setText("" + request.getEmpname());
        txt_empnumber.setText("" + request.getEmpphone());
        txt_emailid.setText("" + request.getEmpemail());
        txt_companyname.setText("" + request.getEmpcompanyname());
        status.setText("" + request.getReportedit());

        if (request.getReportedit().equals("") || request.getReportedit().equals(null)  ) {
            status.setVisibility(View.GONE);
        }

        if (request.getEmpdiscard().toString().equals("No")) {
            btn_active.setVisibility(View.GONE);
            btn_deactive.setVisibility(View.VISIBLE);
        }
        if (request.getEmpdiscard().toString().equals("Yes")) {
            btn_deactive.setVisibility(View.GONE);
            btn_active.setVisibility(View.VISIBLE);
        }




        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                reorder.setTitle("Confirm");
                reorder.setMessage("Do you want to add employee again?");
                reorder.setCancelable(true);
                reorder.setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                reorder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                        String id = request.getEmpid().toString();
                        activeemployee(id);
                    }
                });
                AlertDialog orderError = reorder.create();
                orderError.show();

            }
        });
        btn_deactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                reorder.setTitle("Confirm");
                reorder.setMessage("Do you want to remove employee?");
                reorder.setCancelable(true);
                reorder.setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                reorder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                        String id = request.getEmpid().toString();
                        activeemployee(id);
                    }
                });
                AlertDialog orderError = reorder.create();
                orderError.show();

        }
        });

    }


    @Override
    public int getItemCount() {
        return eList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            txt_empname = (TextView) itemView.findViewById(R.id.txt_empname);
            txt_empnumber = (TextView) itemView.findViewById(R.id.txt_empnumber);
            txt_emailid = (TextView) itemView.findViewById(R.id.txt_emailid);
            txt_companyname = (TextView) itemView.findViewById(R.id.txt_empcompanyname);
            btn_deactive = (Button) itemView.findViewById(R.id.btn_deactive);
            btn_active = (Button) itemView.findViewById(R.id.btn_active);
            status = (TextView) itemView.findViewById(R.id.status);

        }
    }



    private void activeemployee(String id) {

        String urlSuffix = "?id=" + id;

        class activatecompany extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(inflater.getContext(), "Please Wait...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("Success")) {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                    reorder.setMessage("Done");
                    reorder.setCancelable(true);
                    reorder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    //   Admin.deactivecompanyList = true;
                                    Intent intent = new Intent(inflater.getContext(), AddEmployee.class);
                                    inflater.getContext().startActivity(intent);

                                }
                            });

                    AlertDialog orderError = reorder.create();
                    orderError.show();

                } else {
                    AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                    reorder.setMessage("Failed. Please try again/after sometime... ");
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
                    URL url = new URL(URLClass.ACTIVEEMPLIST + s);
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
        activatecompany ac = new activatecompany();
        ac.execute(urlSuffix);
    }
}