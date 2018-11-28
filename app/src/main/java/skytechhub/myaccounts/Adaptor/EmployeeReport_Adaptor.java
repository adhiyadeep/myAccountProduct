package skytechhub.myaccounts.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import skytechhub.myaccounts.AdminReportEdit;
import skytechhub.myaccounts.EmployeeDetailedReport;
import skytechhub.myaccounts.IcomeEmpEditReport;
import skytechhub.myaccounts.LoginActivity;
import skytechhub.myaccounts.POJO.Expense;
import skytechhub.myaccounts.POJO.Income;
import skytechhub.myaccounts.R;
import skytechhub.myaccounts.app.AppController;


/**
 * Created by Deep on 04-10-2017.
 */
public class EmployeeReport_Adaptor extends RecyclerView.Adapter<EmployeeReport_Adaptor.ViewHolder> {
    private List<Income> iList;   // For Income
    private LayoutInflater inflater;
    private TextView txt_billno, txt_billdate, txt_amount, txt_paymentmode, txt_chequeno, txt_bankname, txt_rname, txt_purpose, txt_remark, txtid;
    private ImageButton empreportedit;


    public EmployeeReport_Adaptor(Context context, List<Income> i_list) {
        this.iList = i_list;  // For Income
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.employeeincome_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Income request = iList.get(position);


        if (EmployeeDetailedReport.isIncome) {
            txt_billno.setText("Bill No                 : " + request.getBillno());
            txt_billdate.setText("Bill Date              : " + request.getBilldate());

            DecimalFormat formatter = new DecimalFormat("#,##,###");
            String formatted = formatter.format(Integer.parseInt(request.getAmount()));
            txt_amount.setText("Amount               : " + formatted);

            txt_paymentmode.setText("Payment Mode   : " + request.getPaymentmode());
            txt_chequeno.setText("Cheque No          : " + request.getChequeno());
            txt_bankname.setText("Bank Name          : " + request.getBankname());
            txt_rname.setText("Received From   : " + request.getRname());
            txt_purpose.setText("Purpose               : " + request.getPurpose());
            txt_remark.setText("Remarks                   : " + request.getRemark());
            txtid.setText(request.getId());


            if (request.getChequeno().equals("") || request.getChequeno().equals(null) || request.getBankname().equals("")) {
                txt_chequeno.setVisibility(View.GONE);
                txt_bankname.setVisibility(View.GONE);

            }
            if (request.getBilldate().equals(null) || request.getBilldate().equals("")) {
                txt_billdate.setVisibility(View.GONE);

            }
            if (request.getBillno().equals(null) || request.getBillno().equals("")) {
                txt_billno.setVisibility(View.GONE);
            }
            if (request.getRemark().equals("") || request.getRemark().equals(null)) {
                txt_remark.setVisibility(View.GONE);
            } else {
                txt_chequeno.setText("Cheque Number     : " + request.getChequeno());
                txt_bankname.setText("Bank Name          : " + request.getBankname());
                txt_remark.setText("Remark                : " + request.getRemark());
                txt_billno.setText("Bill No                 : " + request.getBillno());
                txt_billdate.setText("Bill Date              : " + request.getBilldate());
            }

            if (AppController.preferences.getPreference("editreport", "").contains("Yes")) {
                empreportedit.setVisibility(View.VISIBLE);
            }
            else {
                empreportedit.setVisibility(View.GONE);
            }
        }
        empreportedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inflater.getContext(), IcomeEmpEditReport.class);
                intent.putExtra("id", request.getId());
                intent.putExtra("billno", request.getBillno());
                intent.putExtra("billdate", request.getBilldate());
                intent.putExtra("amount", request.getAmount());
                intent.putExtra("paymentmode", request.getPaymentmode());
                intent.putExtra("chequeno", request.getChequeno());
                intent.putExtra("bankname", request.getBankname());
                intent.putExtra("rfrom", request.getRname());
                intent.putExtra("purpose", request.getPurpose());
                intent.putExtra("remark", request.getRemark());
                intent.putExtra("billdt", request.getDate());
                intent.putExtra("id", request.getId());
                inflater.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return iList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            txt_billno = (TextView) itemView.findViewById(R.id.txt_billno);
            txt_billdate = (TextView) itemView.findViewById(R.id.txt_billdate);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
            txt_paymentmode = (TextView) itemView.findViewById(R.id.txt_paymentmode);
            txt_chequeno = (TextView) itemView.findViewById(R.id.txt_chequeno);
            txt_bankname = (TextView) itemView.findViewById(R.id.txt_bankname);
            txt_rname = (TextView) itemView.findViewById(R.id.txt_rname);
            txt_purpose = (TextView) itemView.findViewById(R.id.txt_purpose);
            txt_remark = (TextView) itemView.findViewById(R.id.txt_remark);
            empreportedit = (ImageButton) itemView.findViewById(R.id.empreportedit);
            txtid = (TextView) itemView.findViewById(R.id.txtid);

        }
    }


}
