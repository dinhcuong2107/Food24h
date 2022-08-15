package com.example.food24h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView textViewlogup;
    DatabaseReference databaseReference;
    EditText phone_login,pass_login;
    TextView pass_miss;
    Button btn_login;
    String login_phone="",login_pass="",key_user="";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewlogup = (TextView) findViewById(R.id.sigup);
        phone_login=(EditText) findViewById(R.id.editTextLoginTK);
        pass_login=(EditText)findViewById(R.id.editTextLoginMK);
        btn_login = (Button) findViewById(R.id.buttonsigin);
        pass_miss = (TextView) findViewById(R.id.textpassmiss);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        textViewlogup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CreatedAccActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("User");
                databaseReference.orderByChild("phonenumber").equalTo(phone_login.getText().toString());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            User user = dataSnapshot.getValue(User.class);
                            if (user.password.equals(pass_login.getText().toString().trim()))
                            {
                                key_user= dataSnapshot.getKey();
                                login_phone = user.phonenumber;
                                login_pass = user.password;
                                intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("key_user",key_user);
                            }
                        }
                        if (phone_login.getText().toString().equals(login_phone) & pass_login.getText().toString().equals(login_pass))
                        {
                            Toast.makeText(LoginActivity.this,"đăng nhập thành công",Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            delete_edittext();
                        }
                        else {
                            dialog_login();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            void delete_edittext(){
                phone_login.getText().clear();
                pass_login.getText().clear();
            }

        });

    }
    private void dialog_login(){
        Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.dialog_error_login);
        dialog.show();
    }
}