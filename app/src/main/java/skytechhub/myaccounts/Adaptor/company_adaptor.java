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
import java.util.ArrayList;
import java.util.List;

import skytechhub.myaccounts.AddCompany;
import skytechhub.myaccounts.Admin;
import skytechhub.myaccounts.Company;
import skytechhub.myaccounts.POJO.companyname_pojo;
import skytechhub.myaccounts.R;
import skytechhub.myaccounts.URLclass.URLClass;

/**
 * Created by Deep on 19-09-2017.
 */
public class company_adaptor extends RecyclerView.Adapter<company_adaptor.ViewHolder>  {

    private List<companyname_pojo>
            cList = new ArrayList<>();
    private LayoutInflater inflater;
    private TextView txt_companyname, txt_companyphone, txt_emailid, txt_companyaddress, txt_city;
    private Button btn_active, btn_deactive;

    public company_adaptor(Context context, List<companyname_pojo> c_list) {
        this.cList = c_list;

        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_companydata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final companyname_pojo request = cList.get(position);
        txt_companyname.setText(" " + request.getCompanyname());
        txt_companyphone.setText("" + request.getCompanyphone());
        txt_emailid.setText("" + request.getCompanyemailid());
        txt_companyaddress.setText("" + request.getCompanyaddress());
        txt_city.setText("" + request.getCompanycity());

        if (request.getDiscard().toString().equals("No")) {
            btn_active.setVisibility(View.GONE);
            btn_deactive.setVisibility(View.VISIBLE);
        }
        if (request.getDiscard().toString().equals("Yes")) {
            btn_deactive.setVisibility(View.GONE);
            btn_active.setVisibility(View.VISIBLE);
        }
        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder reorder = new AlertDialog.Builder(inflater.getContext());
                reorder.setTitle("Confirm");
                reorder.setMessage("Do you want to add company again?");
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


                        String id = request.getCompanyid().toString();
                        activecompany(id);
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
                reorder.setMessage("Do you want to remove company?");
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


                        String id = request.getCompanyid().toString();
                        activecompany(id);
                    }
                });
                AlertDialog orderError = reorder.create();
                orderError.show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return cList.size();


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            txt_companyname = (TextView) itemView.findViewById(R.id.txt_companyname);
            txt_companyphone = (TextView) itemView.findViewById(R.id.txt_companyphone);
            txt_emailid = (TextView) itemView.findViewById(R.id.txt_emailid);
            txt_companyaddress = (TextView) itemView.findViewById(R.id.companyaddress);
            txt_city = (TextView) itemView.findViewById(R.id.txt_city);


            btn_deactive = (Button) itemView.findViewById(R.id.btn_deactive);
            btn_active = (Button) itemView.findViewById(R.id.btn_active);

        }

    }

    private void activecompany(String id) {

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
                                    Intent intent = new Intent(inflater.getContext(), Company.class);
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
                    URL url = new URL(URLClass.activecompany + s);
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
