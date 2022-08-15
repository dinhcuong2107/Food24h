package com.example.food24h;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatedAccActivity extends AppCompatActivity {
    Button btn_dob, btn_created;
    EditText edt_name, edt_phone, edt_pw, edt_pw_again;
    RadioButton radio_boy, radio_girl;
    String error = "";
    Boolean temp_ck_phone;
    ProgressDialog progressDialog;
    DatabaseReference databaseReferenceCheck, databaseReference;
    DatePickerDialog.OnDateSetListener dateSetListener;
    DatabaseReference getDatabaseReferenceCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_acc);
        btn_dob = (Button) findViewById(R.id.user_dob);
        btn_created = (Button) findViewById(R.id.user_created);
        edt_name = (EditText) findViewById(R.id.user_name);
        edt_phone = (EditText) findViewById(R.id.user_phone);
        edt_pw = (EditText) findViewById(R.id.user_pw);
        edt_pw_again = (EditText) findViewById(R.id.user_pw_again);
        radio_boy = (RadioButton) findViewById(R.id.user_boy);
        radio_girl = (RadioButton) findViewById(R.id.user_girl);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String temp_date = day + "/" + month + "/" + year;
                btn_dob.setText(temp_date);
            }
        };

        btn_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreatedAccActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        btn_created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_name.getText().toString().length() == 0) {
                    error = "Bạn chưa điền tên";
                    Toast.makeText(CreatedAccActivity.this, "" + error, Toast.LENGTH_LONG).show();
                } else {

                    if (radio_boy.isChecked() == false && radio_girl.isChecked() == false) {
                        error = "Bạn chưa chọn giới tính";
                        Toast.makeText(CreatedAccActivity.this, "" + error, Toast.LENGTH_LONG).show();
                    } else {
                        if (btn_dob.getText().toString().length() == 0) {
                            error = "Bạn chưa thêm ngày sinh";
                            Toast.makeText(CreatedAccActivity.this, "" + error, Toast.LENGTH_LONG).show();
                        } else {
                            if (edt_phone.getText().toString().length() != 10) {
                                error = "Bạn chưa điền chính xác số điện thoại";
                                Toast.makeText(CreatedAccActivity.this, "" + error, Toast.LENGTH_LONG).show();
                            } else {
                                if (edt_pw.getText().toString().length() < 8) {
                                    error = "Mật khẩu chưa đủ an toàn";
                                    Toast.makeText(CreatedAccActivity.this, "" + error, Toast.LENGTH_LONG).show();
                                } else {
                                    if (edt_pw.getText().toString().equals(edt_pw_again.getText().toString())) {
                                        checkPhone();
                                    } else {
                                        error = "Mật khẩu không trùng khớp";
                                        Toast.makeText(CreatedAccActivity.this, "" + error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void checkPhone() {
        progressDialog = new ProgressDialog(CreatedAccActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        databaseReferenceCheck = FirebaseDatabase.getInstance().getReference("User");
        databaseReferenceCheck.orderByChild("phonenumber").equalTo(edt_phone.getText().toString());
        databaseReferenceCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User users = dataSnapshot.getValue(User.class);

                    if (users.phonenumber.equals(edt_phone.getText().toString().trim())) {
                        progressDialog.dismiss();
                        Toast.makeText(CreatedAccActivity.this, "Số điên thoại đã được sử dụng", Toast.LENGTH_LONG).show();
                        temp_ck_phone = false;
                        break;
                    } else {
                        temp_ck_phone = true;
                    }
                }
                if (temp_ck_phone == true) {
                    creating();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void creating() {
        User user_add = new User();
        if (radio_boy.isChecked()) {
            user_add.sex = "Nam";
        }
        if (radio_girl.isChecked()) {
            user_add.sex = "Nữ";
        }
        user_add.fullname = edt_name.getText().toString();
        user_add.avt = "https://firebasestorage.googleapis.com/v0/b/food24h-374e7.appspot.com/o/avatar-default-icon%20(1).png?alt=media&token=1b8a9c5b-0ced-4e06-9b82-ffbe5854de9b";
        user_add.dateofbirth = btn_dob.getText().toString();
        user_add.phonenumber = edt_phone.getText().toString();
        user_add.password = edt_pw.getText().toString();
        user_add.position = "user";
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").push().setValue(user_add, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    progressDialog.dismiss();
                    Toast.makeText(CreatedAccActivity.this, "hoàn thành", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreatedAccActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatedAccActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
