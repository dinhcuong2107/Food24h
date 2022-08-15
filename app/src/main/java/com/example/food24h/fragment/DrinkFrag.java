package com.example.food24h.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.food24h.AddDrinkActivity;
import com.example.food24h.AddFoodActivity;
import com.example.food24h.Drink;
import com.example.food24h.Food;
import com.example.food24h.R;
import com.example.food24h.User;
import com.example.food24h.adapter.ListDrinkAdapter;
import com.example.food24h.adapter.ListFoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrinkFrag extends Fragment {
    String key_user="";
    Button btn_add_drink,btn_find_drink;
    EditText editText_find_drink;
    ListView listView_drink;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_drink_frag,container,false);

        btn_add_drink = (Button) view.findViewById(R.id.add_drink);
        btn_find_drink = (Button) view.findViewById(R.id.btn_find_dink);
        editText_find_drink = (EditText) view.findViewById(R.id.edt_find_drink);
        listView_drink = (ListView) view.findViewById(R.id.list_drink);

        Bundle bundle = getActivity().getIntent().getExtras();
        key_user = bundle.getString("key_user");

        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.position.equals("admin"))
                {
                    btn_add_drink.setVisibility(View.VISIBLE);
                }
                else {
                    btn_add_drink.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        load_list_drink();

        btn_add_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDrinkActivity.class);
                intent.putExtra("key_user",key_user);
                startActivity(intent);
            }
        });
        return view;
    }

    private void load_list_drink() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Drink");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Drink> arrayList=new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Drink drink = dataSnapshot.getValue(Drink.class);
                    if (Integer.parseInt(drink.sl)>=1)
                    {
                        drink.sl=key_user;
                        drink.text = dataSnapshot.getKey();
                        arrayList.add(drink);
                        ListDrinkAdapter listDrinkAdapter = new ListDrinkAdapter(getActivity(),R.layout.item_kos1,arrayList);
                        listView_drink.setAdapter(listDrinkAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}