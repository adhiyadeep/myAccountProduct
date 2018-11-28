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
import skytechhub.myaccounts.LoginActivity;
import skytechhub.myaccounts.POJO.Expense;
import skytechhub.myaccounts.POJO.Income;
import skytechhub.myaccounts.R;
import skytechhub.myaccounts.app.AppController;

/**
 * Created by Deep on 11-10-2017.
 */
public class EmployeeReport_expenseAdaptor  extends RecyclerView.Adapter<EmployeeReport_expenseAdaptor.ViewHolder>{

    private List<Expense> eList;    // For Expense
    private LayoutInflater inflater;
    private TextView txt_billno, txt_billdate, txt_amount, txt_paymentmode, txt_chequeno, txt_bankname, txt_rname, txt_purpose, txt_remark,txtid;

    private ImageButton empreportedit;
    public EmployeeReport_expenseAdaptor(Context context, List<Expense> e_list) {
        this.eList = e_list;
        this.inflater = LayoutInflater.from(context);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.employeeincome_list, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Expense requestlist = eList.get(position);// For Income Set in Text
        if (EmployeeDetailedReport.isExpense)
        {
            txt_billno.setText("Bill No                 : " + requestlist.getBillno());
            txt_billdate.setText("Bill Date              : " + requestlist.getBilldate());

            DecimalFormat formatter = new DecimalFormat("#,##,###");
            String formatted = formatter.format(Integer.parseInt(requestlist.getAmount()));
            txt_amount.setText("Amount              : " + formatted);

            txt_paymentmode.setText("Payment Mode  : " + requestlist.getPaymentmode());
            txt_chequeno.setText("Cheque No         : " + requestlist.getChequeno());
            txt_bankname.setText("Bank Name        : " + requestlist.getBankname());
            txt_rname.setText(" Given To            : " + requestlist.getGivento());
            txt_purpose.setText("Purpose              : " + requestlist.getPurpose());
            txt_remark.setText("Remarks               : " + requestlist.getRemark());

            if (requestlist.getBillno().equals("") || requestlist.getBillno().equals(null))
            {
                txt_billno.setVisibility(View.GONE);
            }
            if (requestlist.getBilldate().equals("") || requestlist.getBilldate().equals(null))
            {
                txt_billdate.setVisibility(View.GONE);
            }
            if (requestlist.getBankname().equals("") || requestlist.getBankname().equals(null))
            {
                txt_bankname.setVisibility(View.GONE);
            }
            if ((requestlist.getChequeno().equals("") || requestlist.getChequeno().equals(null)))
            {
                txt_chequeno.setVisibility(View.GONE);
            }
            if ((requestlist.getRemark().equals("") || requestlist.getRemark().equals(null)))
            {
                txt_remark.setVisibility(View.GONE);
            }

            if (AppController.preferences.getPreference("editreport","").contains("Yes"))
            {
                empreportedit.setVisibility(View.VISIBLE);
            }
            else
            {
                empreportedit.setVisibility(View.GONE);
            }
        }
        empreportedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inflater.getContext(), AdminReportEdit.class);
                intent.putExtra("id",requestlist.getId());
                intent.putExtra("billno",requestlist.getBillno());
                intent.putExtra("billdate",requestlist.getBilldate());
                intent.putExtra("amount",requestlist.getAmount());
                intent.putExtra("paymentmode",requestlist.getPaymentmode());
                intent.putExtra("chequeno",requestlist.getChequeno());
                intent.putExtra("bankname",requestlist.getBankname());
                intent.putExtra("givento",requestlist.getGivento());
                intent.putExtra("purpose",requestlist.getPurpose());
                intent.putExtra("remark",requestlist.getRemark());
                intent.putExtra("billdt",requestlist.getDate());
                inflater.getContext().startActivity(intent);
            }
        });

        }

    @Override
    public int getItemCount()
    {
        return eList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            txt_billno=(TextView)itemView.findViewById(R.id.txt_billno);
            txt_billdate = (TextView)itemView.findViewById(R.id.txt_billdate);
            txt_amount = (TextView)itemView.findViewById(R.id.txt_amount);
            txt_paymentmode = (TextView)itemView.findViewById(R.id.txt_paymentmode);
            txt_chequeno = (TextView)itemView.findViewById(R.id.txt_chequeno);
            txt_bankname = (TextView)itemView.findViewById(R.id.txt_bankname);
            txt_rname = (TextView)itemView.findViewById(R.id.txt_rname);
            txt_purpose = (TextView)itemView.findViewById(R.id.txt_purpose);
            txt_remark = (TextView)itemView.findViewById(R.id.txt_remark);
            txtid=(TextView)itemView.findViewById(R.id.txtid);
            empreportedit=(ImageButton)itemView.findViewById(R.id.empreportedit);

        }
    }

}
