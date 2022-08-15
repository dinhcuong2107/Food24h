package com.example.food24h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CTOrdActivity extends AppCompatActivity {
String key_user,key_ord;
DatabaseReference databaseReference,databaseReference_Ord,databaseReference_SP,databaseReference_User;
ImageView imageView;
int i =0;
Button button,btn_user;
TextView textViewNameSP,textViewNameUser,textViewPhone,textViewAdd,textViewSL,textViewPrice,textViewTime,textViewSTT,textViewTT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctord);

        button =(Button) findViewById(R.id.ct_btn_back);
        btn_user = (Button) findViewById(R.id.ct_btn_huy);
        imageView =(ImageView) findViewById(R.id.img_ctord);
        textViewNameSP = (TextView) findViewById(R.id.ct_name_sp);
        textViewSL = (TextView) findViewById(R.id.ct_sl_sp);
        textViewPrice = (TextView) findViewById(R.id.ct_price_sp);
        textViewTime = (TextView) findViewById(R.id.ct_time_sp);
        textViewSTT = (TextView) findViewById(R.id.ct_stt_sp);
        textViewNameUser = (TextView) findViewById(R.id.ct_name_use);
        textViewAdd = (TextView) findViewById(R.id.ct_add_sp);
        textViewPhone = (TextView) findViewById(R.id.ct_phone_use);
        textViewTT = (TextView) findViewById(R.id.textTT);

        key_ord = getIntent().getStringExtra("key_ord");
        key_user =  getIntent().getStringExtra("key_user");

        textViewSTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user.position.equals("admin"))
                        {
                            if (i%4==0){
                                textViewSTT.setText("Chờ xác nhận");
                            }
                            if (i%4==1){
                                textViewSTT.setText("Đã xác nhận");
                            }
                            if (i%4==2){
                                textViewSTT.setText("Đang giao hàng");
                            }
                            if (i%4==3){
                                textViewSTT.setText("Đã nhận hàng thành công");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        databaseReference_Ord= FirebaseDatabase.getInstance().getReference("Order").child(key_ord);
        databaseReference_Ord.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SPOrd spOrd = snapshot.getValue(SPOrd.class);

                load_user(spOrd.key_user_ord);

                if (spOrd.time_ord !=null)
                {
                    textViewTime.setText(""+spOrd.time_ord + "  "+spOrd.day_ord);
                }
                textViewAdd.setText(""+spOrd.add);
                textViewSL.setText(""+spOrd.sl_sp_ord);
                if (spOrd.status.equals("0")){
                    textViewSTT.setText("Chờ xác nhận");
                }
                if (spOrd.status.equals("1")){
                    textViewSTT.setText("Đã xác nhận");
                }
                if (spOrd.status.equals("2")){
                    textViewSTT.setText("Đang giao hàng");
                }
                if (spOrd.status.equals("3")){
                    textViewSTT.setText("Đã nhận hàng thành công");
                }
                if (spOrd.type.equals("1")){
                    databaseReference_SP = FirebaseDatabase.getInstance().getReference("Food").child(spOrd.key_sp_ord);
                    databaseReference_SP.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Food food = snapshot.getValue(Food.class);
                            Picasso.with(CTOrdActivity.this).load(food.image).into(imageView);
                            textViewNameSP.setText(""+food.name_food);
                            textViewPrice.setText(""+food.price);
                            int i1 = Integer.parseInt(food.price)*Integer.parseInt(textViewSL.getText().toString());
                            String s = ""+i1;
                            StringBuilder builder = new StringBuilder(s);
                            if (s.length() >6) {
                                int halfway1 = s.length()-3;
                                int halfway2 = s.length()-6;
                                builder.insert(halfway1, ".");
                                builder.insert(halfway2, ".");
                            }else if (s.length()>9)
                            {
                                int halfway1 = s.length()-3;
                                int halfway2 = s.length()-6;
                                int halfway3 = s.length()-9;
                                builder.insert(halfway1, ".");
                                builder.insert(halfway2, ".");
                                builder.insert(halfway3, ".");
                            }else {
                                int halfway = s.length()-3;
                                builder.insert(halfway, ".");
                            }

                            textViewTT.setText(builder.toString()+" VNĐ" );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    databaseReference_SP = FirebaseDatabase.getInstance().getReference("Drink").child(spOrd.key_sp_ord);
                    databaseReference_SP.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Drink drink = snapshot.getValue(Drink.class);
                            Picasso.with(CTOrdActivity.this).load(drink.image).into(imageView);
                            textViewNameSP.setText(""+drink.name_drink);
                            textViewPrice.setText(""+drink.price);
                            int i1 = Integer.parseInt(drink.price)*Integer.parseInt(textViewSL.getText().toString());
                            String s = ""+i1;
                            StringBuilder builder = new StringBuilder(s);
                            if (s.length() >6) {
                                int halfway1 = s.length()-3;
                                int halfway2 = s.length()-6;
                                builder.insert(halfway1, ".");
                                builder.insert(halfway2, ".");
                            }else if (s.length()>9)
                            {
                                int halfway1 = s.length()-3;
                                int halfway2 = s.length()-6;
                                int halfway3 = s.length()-9;
                                builder.insert(halfway1, ".");
                                builder.insert(halfway2, ".");
                                builder.insert(halfway3, ".");
                            }else {
                                int halfway = s.length()-3;
                                builder.insert(halfway, ".");
                            }

                            textViewTT.setText(builder.toString()+" VNĐ" );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user.position.equals("admin"))
                        {
                            databaseReference= FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Order").child(key_ord).child("status").setValue(""+i);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(CTOrdActivity.this,MainActivity.class);
                intent.putExtra("key_user",key_user);
                startActivity(intent);
            }
        });
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewSTT.getText().length()==12)
                {
                    databaseReference= FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Order").child(key_ord).child("status").setValue("hủy");

                    databaseReference_Ord= FirebaseDatabase.getInstance().getReference("Order").child(key_ord);
                    databaseReference_Ord.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SPOrd spOrd = snapshot.getValue(SPOrd.class);

                            load_user(spOrd.key_user_ord);
                            if (spOrd.type.equals("1")){
                                databaseReference_SP = FirebaseDatabase.getInstance().getReference("Food").child(spOrd.key_sp_ord);
                                databaseReference_SP.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Food food = snapshot.getValue(Food.class);
                                        int i1 = Integer.parseInt(textViewSL.getText().toString())+Integer.parseInt(food.sl);


                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Food").child(spOrd.key_sp_ord);
                                                databaseReference.child("sl").setValue(""+i1);
                                                // Do something after 1s = 1000ms
                                                Intent intent = new Intent(CTOrdActivity.this,MainActivity.class);
                                                intent.putExtra("key_user",key_user);
                                                startActivity(intent);
                                            }
                                        }, 1000);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else {
                                databaseReference_SP = FirebaseDatabase.getInstance().getReference("Drink").child(spOrd.key_sp_ord);
                                databaseReference_SP.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Drink drink = snapshot.getValue(Drink.class);
                                        int i1 = Integer.parseInt(textViewSL.getText().toString())+Integer.parseInt(drink.sl);

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Drink").child(spOrd.key_sp_ord);
                                                databaseReference.child("sl").setValue(""+i1);
                                                // Do something after 1s = 1000ms
                                                Intent intent = new Intent(CTOrdActivity.this,MainActivity.class);
                                                intent.putExtra("key_user",key_user);
                                                startActivity(intent);
                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Toast.makeText(CTOrdActivity.this,"Trạng thái đơn hàng không được hủy",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void load_user(String key_user_ord) {
        databaseReference_User = FirebaseDatabase.getInstance().getReference("User").child(key_user_ord);
        databaseReference_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                textViewNameUser.setText(""+user.fullname);
                textViewPhone.setText("SĐT: "+user.phonenumber);
                if (user.position.equals("admin"))
                {
                    btn_user.setVisibility(View.GONE);
                }else {
                    btn_user.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}