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
import android.widget.ListView;

import com.example.food24h.AddFoodActivity;
import com.example.food24h.CreatedAccActivity;
import com.example.food24h.Food;
import com.example.food24h.LoginActivity;
import com.example.food24h.R;
import com.example.food24h.User;
import com.example.food24h.adapter.ListFoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodFrag extends Fragment {
    String key_user="";
    Button btn_add_food,btn_find_food;
    EditText editText_find_food;
    ListView listView_food;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_food_frag,container,false);
        Bundle bundle = getActivity().getIntent().getExtras();
        key_user = bundle.getString("key_user");

        btn_add_food = (Button) view.findViewById(R.id.add_food);
        btn_find_food = (Button) view.findViewById(R.id.btn_find_food);
        editText_find_food = (EditText) view.findViewById(R.id.edt_find_food);
        listView_food = (ListView) view.findViewById(R.id.list_food);

        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.position.equals("admin"))
                {

                    btn_add_food.setVisibility(View.VISIBLE);
                }
                else {
                    btn_add_food.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        load_list_food();

        btn_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFoodActivity.class);
                intent.putExtra("key_user",key_user);
                startActivity(intent);
            }
        });
        return view;
    }

    private void load_list_food() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Food");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Food> arrayList=new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (Integer.parseInt(food.sl)>=1)
                    {
                        food.sl=key_user;
                        food.text = dataSnapshot.getKey();
                        arrayList.add(food);
                        ListFoodAdapter listFoodAdapter = new ListFoodAdapter(getActivity(),R.layout.item_kos,arrayList);
                        listView_food.setAdapter(listFoodAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}