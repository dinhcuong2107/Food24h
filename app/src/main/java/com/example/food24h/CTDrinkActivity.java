package com.example.food24h;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class CTDrinkActivity extends AppCompatActivity {
    ImageView imageView;
    Button btn_fix, btn_buy, btn_inc, btn_red, btn_time;
    String key_user="",key_sp="",img="",time_now="";
    int slsp,spmax;
    LinearLayout layout1;
    EditText edt_namesp,edt_pricesp,edt_slsp,edt_motasp,edtmap;
    TextView t_namesp,t_pricesp,t_motasp,textViewTT;
    DatabaseReference databaseReference;

    Calendar calendar = Calendar.getInstance();
    int hour =calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctdrink);
        imageView = (ImageView) findViewById(R.id.img_ctdrink);

        layout1 = (LinearLayout) findViewById(R.id.linear_motactdrink);
        btn_time = (Button) findViewById(R.id.time_ordctdrink);
        edtmap = (EditText) findViewById(R.id.edt_mapctdrink);

        btn_buy =(Button) findViewById(R.id.btn_buy_ctdrink);
        btn_fix = (Button) findViewById(R.id.btn_fix_ctdrink);
        btn_inc = (Button) findViewById(R.id.inc_ctdrink);
        btn_red = (Button) findViewById(R.id.red_ctdrink);

        edt_namesp = (EditText) findViewById(R.id.edt_namectdrink);
        edt_pricesp= (EditText) findViewById(R.id.edt_price_ctdrink);
        edt_slsp= (EditText) findViewById(R.id.sl_ctdrink);
        edt_motasp= (EditText) findViewById(R.id.mota_ctdrink);

        t_namesp = (TextView) findViewById(R.id.text_namectdrink);
        t_pricesp = (TextView) findViewById(R.id.text_pricectdrink);
        t_motasp = (TextView) findViewById(R.id.text_motactdrink);
        textViewTT = (TextView) findViewById(R.id.textTTD);

        // nhận data từ intent
        key_sp = getIntent().getStringExtra("key_sp");
        key_user =  getIntent().getStringExtra("key_user");

        databaseReference= FirebaseDatabase.getInstance().getReference("Drink").child(key_sp);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Drink drink = snapshot.getValue(Drink.class);
                img = drink.image;
                new Load_img_drink().execute(drink.image);
                edt_namesp.setText(""+drink.name_drink);
                edt_pricesp.setText(""+drink.price);
                edt_slsp.setText(""+drink.sl);
                spmax = Integer.parseInt(""+drink.sl);
                edt_motasp.setText(""+drink.text);

                t_namesp.setText(""+drink.name_drink);
                t_pricesp.setText("Giá:     "+drink.price);
                t_motasp.setText(""+drink.text);

                load_TT();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.position.equals("admin"))
                {
                    edt_namesp.setVisibility(View.VISIBLE);
                    edt_pricesp.setVisibility(View.VISIBLE);
                    edt_motasp.setVisibility(View.VISIBLE);

                    btn_fix.setVisibility(View.VISIBLE);
                    btn_buy.setVisibility(View.GONE);

                    layout1.setVisibility(View.GONE);
                    btn_time.setVisibility(View.GONE);
                    edtmap.setVisibility(View.GONE);
                    textViewTT.setVisibility(View.GONE);
                    t_namesp.setVisibility(View.GONE);
                    t_pricesp.setVisibility(View.GONE);
                    t_motasp.setVisibility(View.GONE);
                }
                else {
                    edt_namesp.setVisibility(View.GONE);
                    edt_pricesp.setVisibility(View.GONE);
                    edt_motasp.setVisibility(View.GONE);

                    btn_fix.setVisibility(View.GONE);
                    btn_buy.setVisibility(View.VISIBLE);

                    layout1.setVisibility(View.VISIBLE);
                    btn_time.setVisibility(View.VISIBLE);
                    edtmap.setVisibility(View.VISIBLE);

                    t_namesp.setVisibility(View.VISIBLE);
                    t_pricesp.setVisibility(View.VISIBLE);
                    t_motasp.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_slsp.getText().toString().length()==0){
                    edt_slsp.setText("0");
                }
                else {
                    if (edt_namesp.getText().toString().length()==0 || edt_motasp.getText().toString().length()==0||edt_pricesp.getText().toString().length()==0)
                    {
                        Toast.makeText(CTDrinkActivity.this, "Chọn đủ thông tin",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Drink drink = new Drink();
                        drink.image = img;
                        drink.name_drink = edt_namesp.getText().toString();
                        drink.sl = edt_slsp.getText().toString();
                        drink.price = edt_pricesp.getText().toString();
                        drink.text = edt_motasp.getText().toString();

                        databaseReference= FirebaseDatabase.getInstance().getReference();

    /*                          databaseReference.child("Users").push().setValue(user);
                                Toast.makeText(CreatedActivity.this, "hoàn thành", Toast.LENGTH_SHORT).show();
                                new_intent();
    */
                        databaseReference.child("Drink").child(key_sp).setValue(drink, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error==null){
                                    Toast.makeText(CTDrinkActivity.this, "hoàn thành", Toast.LENGTH_SHORT).show();
                                    new_intent();
                                }else {
                                    Toast.makeText(CTDrinkActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            void new_intent(){
                                Intent intent = new Intent(CTDrinkActivity.this,MainActivity.class);
                                intent.putExtra("key_user",key_user);
                                startActivity(intent);
                            }

                        });
                    }
                }}
        });
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slsp = Integer.parseInt(edt_slsp.getText().toString());
                if (slsp==0){
                    Toast.makeText(CTDrinkActivity.this,"Số lượng phải lớn hơn 0", Toast.LENGTH_LONG).show();
                }
                if (slsp > spmax)
                {
                    Toast.makeText(CTDrinkActivity.this,"Số lượng bị giới hạn < " +spmax, Toast.LENGTH_LONG).show();
                }else {
                    if (edtmap.getText().toString().length()>0){
                        if (btn_time.getText().toString().length()>0){
                            SPOrd spOrd = new SPOrd();
                            spOrd.key_user_ord = key_user;
                            spOrd.key_sp_ord = key_sp;
                            req_time_now();
                            spOrd.time_ord=btn_time.getText().toString();
                            spOrd.day_ord=time_now;
                            spOrd.type="2";
                            spOrd.sl_sp_ord = edt_slsp.getText().toString();
                            spOrd.status ="0";
                            spOrd.add = ""+edtmap.getText().toString();
                            databaseReference= FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Order").push().setValue(spOrd, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error==null){
                                        int sl_new = spmax - Integer.parseInt(edt_slsp.getText().toString());
                                        String string_new = ""+sl_new;
                                        databaseReference= FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Drink").child(key_sp).child("sl").setValue(string_new);
                                        Toast.makeText(CTDrinkActivity.this, "hoàn thành", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CTDrinkActivity.this, MainActivity.class);
                                        intent.putExtra("key_user",key_user);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(CTDrinkActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(CTDrinkActivity.this,"Chọn thời gian muốn nhận", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(CTDrinkActivity.this,"Thêm địa chỉ nhận hàng", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
//                TimePickerDialog tpd = TimePickerDialog.newInstance(
//                        CreateBookingActivity.this,
//                        now.get(Calendar.HOUR_OF_DAY),
//                        now.get(Calendar.MINUTE),
//                        true
//                );111111111
                // Create DatePickerDialog (Spinner Mode):
                TimePickerDialog timePickerDialog = new TimePickerDialog(CTDrinkActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeSetListener, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),true);

                // Show
                timePickerDialog.show();
            }
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    btn_time.setText(""+hourOfDay + ":" + minute);
                }
            };
        });
        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_slsp.getText().toString().length()==0){
                    edt_slsp.setText("0");
                }
                slsp = Integer.parseInt(edt_slsp.getText().toString());
                if (slsp==0){
                    slsp=0;
                }else {
                    slsp--;
                }
                edt_slsp.setText(""+ slsp);
                load_TT();
            }
        });

        btn_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_slsp.getText().toString().length()==0){
                    edt_slsp.setText("0");
                }
                slsp = Integer.parseInt(edt_slsp.getText().toString());
                slsp++;
                edt_slsp.setText(""+slsp);
                load_TT();
            }
        });
    }

    private void load_TT() {
        int tempSL = Integer.parseInt(edt_slsp.getText().toString());
        int price = Integer.parseInt(edt_pricesp.getText().toString());
        int sum = tempSL*price;
        String priceSum = ""+sum;

        StringBuilder builder = new StringBuilder(""+priceSum);
        if (priceSum.length()>6) {
            int halfway1 = priceSum.length()-3;
            int halfway2 = priceSum.length()-6;
            builder.insert(halfway1, ".");
            builder.insert(halfway2, ".");
        }else if (priceSum.length()>9)
        {
            int halfway1 = priceSum.length()-3;
            int halfway2 = priceSum.length()-6;
            int halfway3 = priceSum.length()-9;
            builder.insert(halfway1, ".");
            builder.insert(halfway2, ".");
            builder.insert(halfway3, ".");
        }else {
            int halfway = priceSum.length()-3;
            builder.insert(halfway, ".");
        }
        textViewTT.setText(builder.toString()+" VNĐ" );
    }

    private void req_time_now(){
        android.icu.util.Calendar mcurrentTime = android.icu.util.Calendar.getInstance();
        int hour_now = mcurrentTime.get(android.icu.util.Calendar.HOUR_OF_DAY);
        int minute_now = mcurrentTime.get(android.icu.util.Calendar.MINUTE);
        time_now = ""+day+"/"+month+"/"+year;
    }

    private class Load_img_drink extends AsyncTask<String,Void, Bitmap> {
        Bitmap bit_map= null;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url= new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bit_map= BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bit_map;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
