package com.example.food24h.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food24h.R;
import com.example.food24h.SPOrd;
import com.example.food24h.User;
import com.example.food24h.adapter.OrdAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrdFrag extends Fragment {
    RecyclerView recyclerView;
    TextView textView;
    Button btn_all,btn_hn;
    OrdAdapter ordAdapter;
    DatabaseReference databaseReference;
    String key_user="",time="";
    String admin = "";

    Calendar calendar = Calendar.getInstance();
    int hour =calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ord_frag,container,false);

        recyclerView = view.findViewById(R.id.recycler_list_ord);
        textView = (TextView) view.findViewById(R.id.text_myord);

        btn_all = (Button) view.findViewById(R.id.all);
        btn_hn = (Button) view.findViewById(R.id.hn);

        Bundle bundle = getActivity().getIntent().getExtras();
        key_user = bundle.getString("key_user");


        btn_hn.setVisibility(View.GONE);
        btn_all.setVisibility(View.GONE);

        load_data("0");
        req_time();

        btn_hn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data("1");
            }
        });
        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data("0");
            }
        });


        return view;
    }

    private void load_data(String all) {
        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.position.equals("admin"))
                {
                    admin="0";
                    textView.setText("Quản lý đơn hàng");
                }
                else {
                    admin ="1";
                    textView.setText(""+user.fullname);
                }
                load_list(admin,all);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void req_time() {
        android.icu.util.Calendar mcurrentTime = android.icu.util.Calendar.getInstance();
        int hour_now = mcurrentTime.get(android.icu.util.Calendar.HOUR_OF_DAY);
        int minute_now = mcurrentTime.get(android.icu.util.Calendar.MINUTE);
        time = ""+day+"/"+month+"/"+year;
    }

    private void load_list(String admin, String all) {
        //Toast.makeText(getActivity(),""+all+"apk "+ admin,Toast.LENGTH_LONG).show();
        ordAdapter = new OrdAdapter(getContext());
        if (ordAdapter == null){    ordAdapter = new OrdAdapter(getContext()); } else{    ordAdapter.notifyDataSetChanged(); }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ordAdapter.setData(getListOrd(admin,all));
        recyclerView.setAdapter(ordAdapter);

    }

    private List<SPOrd> getListOrd(String admin, String all) {
        List<SPOrd> spOrdList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    if (admin.equals("0"))
                    {
                        if (all.equals("0"))
                        {
                            final SPOrd spOrd = dataSnapshot.getValue(SPOrd.class);
                            if (spOrd.status.length()==1)
                            {
                                spOrd.temp=dataSnapshot.getKey();
                                spOrd.key_user_ord = key_user;
                                spOrdList.add(spOrd);
                            }

                        }else {
                            final SPOrd spOrd = dataSnapshot.getValue(SPOrd.class);
                            if (spOrd.status.length()==1)
                            {
                                spOrd.temp=dataSnapshot.getKey();
                                spOrd.key_user_ord = key_user;
                                if (spOrd.day_ord.equals(time))
                                {
                                    spOrdList.add(spOrd);
                                }
                            }


                        }
                    }
                    else {
                        final SPOrd spOrd = dataSnapshot.getValue(SPOrd.class);
                        if (spOrd.status.length()==1)
                        {
                            if (spOrd.key_user_ord.equals(key_user))
                            {
                                spOrd.temp=dataSnapshot.getKey();
                                spOrd.key_user_ord = key_user;
                                spOrdList.add(spOrd);
                            }
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return spOrdList;
    };


}