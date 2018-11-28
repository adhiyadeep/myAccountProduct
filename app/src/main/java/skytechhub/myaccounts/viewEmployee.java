package skytechhub.myaccounts;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import skytechhub.myaccounts.Adaptor.EmployeeList_Adapter;
import skytechhub.myaccounts.POJO.EmployeeList;

import static skytechhub.myaccounts.R.*;


public class viewEmployee extends Fragment  {

    private List<EmployeeList> emp_list;
    private List<EmployeeList>deactive_list;
    private RecyclerView recycler_employee;
    private Button btn_activeemp,btn_empdec;
    private TextView txt_totalemoloyees;
    private EmployeeList_Adapter mAdapterlist;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_employee,null);


        recycler_employee = (RecyclerView)view.findViewById(R.id.recycler_employee);

        txt_totalemoloyees= (TextView)view.findViewById(id.txt_totalemployees);

        btn_activeemp= (Button)view.findViewById(R.id.btn_activeemp);
        btn_empdec= (Button)view.findViewById(R.id.btn_empdec);

        deactive_list=AddEmp.deactiveempList;
        emp_list=AddEmp.employeeList;


        btn_activeemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Active List
                Drawable d = getResources().getDrawable(drawable.mybuttons);
                btn_empdec.setBackground(d);
                btn_empdec.setTextColor(Color.parseColor("#000000"));

                btn_activeemp.setBackgroundColor(Color.parseColor("#003B46"));
                btn_activeemp.setTextColor(Color.parseColor("#ffffff"));

                String temp;
                temp = String.valueOf(emp_list.size());
                txt_totalemoloyees.setText("Total Employees: " + temp);
                recycler_employee.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                mAdapterlist = new EmployeeList_Adapter(getActivity(), emp_list);
                recycler_employee.setAdapter(mAdapterlist);
                recycler_employee.setHasFixedSize(true);
                mAdapterlist.getItemCount();
            }
        });

        btn_empdec.setOnClickListener(new View.OnClickListener() {   //Deactivated List
            @Override
            public void onClick(View view) {
                Drawable d = getResources().getDrawable(drawable.mybuttons);
                btn_activeemp.setBackground(d);
                btn_activeemp.setTextColor(Color.parseColor("#000000"));

                btn_empdec.setBackgroundColor(Color.parseColor("#003B46"));
                btn_empdec.setTextColor(Color.parseColor("#ffffff"));
                String temp;
                temp = String.valueOf(AddEmp.deactiveempList.size());
                txt_totalemoloyees.setText("Total Deactivated Employees : " + temp);
                recycler_employee.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                mAdapterlist = new EmployeeList_Adapter(getActivity(), deactive_list);
                recycler_employee.setAdapter(mAdapterlist);
                recycler_employee.setHasFixedSize(true);
                mAdapterlist.getItemCount();
            }
        });

      //  btn_activeemp.performClick();

        return view;
    }



}