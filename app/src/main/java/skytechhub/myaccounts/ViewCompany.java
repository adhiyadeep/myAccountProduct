package skytechhub.myaccounts;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import skytechhub.myaccounts.Adaptor.company_adaptor;
import skytechhub.myaccounts.POJO.companyname_pojo;


public class ViewCompany extends Fragment {
    private List<companyname_pojo> c_list, d_list;
    private RecyclerView recyclerview;
    private Button btn_activecompany, btn_deactivecompany;
    private TextView txt_totalcompanies;
    private company_adaptor mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_company, null);

        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerview.setAdapter(mAdapter);

        txt_totalcompanies = (TextView)view.findViewById(R.id.txt_totalcompanies);

        btn_activecompany =(Button) view.findViewById(R.id.btn_activecompany);
        btn_deactivecompany = (Button)view.findViewById(R.id.btn_deactivecompany);

        c_list = Admin.companyList;
        d_list = Admin.deactivecompanyList;


        btn_activecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable d = getResources().getDrawable(R.drawable.mybuttons);
                btn_deactivecompany.setBackground(d);
                btn_deactivecompany.setTextColor(Color.parseColor("#000000"));

                btn_activecompany.setBackgroundColor(Color.parseColor("#003B46"));
                btn_activecompany.setTextColor(Color.parseColor("#ffffff"));
                String temp;
                temp = String.valueOf(Admin.companyList.size());
                txt_totalcompanies.setText("Total Companies : " + temp);

                recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                mAdapter = new company_adaptor(getActivity(), c_list);
                recyclerview.setAdapter(mAdapter);
                recyclerview.setHasFixedSize(true);
                mAdapter.getItemCount();
            }
        });


        btn_deactivecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable d = getResources().getDrawable(R.drawable.mybuttons);
                btn_activecompany.setBackground(d);
                btn_activecompany.setTextColor(Color.parseColor("#000000"));

                btn_deactivecompany.setBackgroundColor(Color.parseColor("#003B46"));
                btn_deactivecompany.setTextColor(Color.parseColor("#ffffff"));

                String temp;
                temp = String.valueOf(Admin.deactivecompanyList.size());
                txt_totalcompanies.setText("Total Deactivated Companies : " + temp);
                recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                mAdapter = new company_adaptor(getActivity(), d_list);
                recyclerview.setAdapter(mAdapter);
                recyclerview.setHasFixedSize(true);
                mAdapter.getItemCount();
            }
        });
        return view;
    }


}
