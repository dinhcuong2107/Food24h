package com.example.food24h;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddDrinkActivity extends AppCompatActivity {
    ImageView imageView;
    String url_temp="",key_user="";
    int sl_food;
    DatabaseReference databaseReference;
    EditText edt_name_d,edt_price_d,edt_sl_d,edt_mota;
    Button btn_inc,btn_red,btn_add;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        // nhận data từ intent
        key_user =  getIntent().getStringExtra("key_user");

        imageView = (ImageView) findViewById(R.id.img_add_drink);
        edt_name_d = (EditText) findViewById(R.id.edt_add_namedrink);
        edt_price_d = (EditText) findViewById(R.id.edt_price_drink);
        edt_sl_d = (EditText) findViewById(R.id.sl_drink);
        edt_mota = (EditText) findViewById(R.id.mota_drink);
        btn_add = (Button) findViewById(R.id.btn_add_drink);
        btn_inc = (Button) findViewById(R.id.inc_drink);
        btn_red = (Button) findViewById(R.id.red_drink);

        if (edt_sl_d.getText().toString().length()==0){
            edt_sl_d.setText("0");
        }

        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_sl_d.getText().toString().length()==0){
                    edt_sl_d.setText("0");
                }
                if (url_temp.equals(""))
                {
                    Toast.makeText(AddDrinkActivity.this, "Chọn ảnh minh họa",Toast.LENGTH_LONG).show();
                }
                else {
                    if (edt_name_d.getText().toString().length()==0 || edt_mota.getText().toString().length()==0||edt_price_d.getText().toString().length()==0)
                    {
                        Toast.makeText(AddDrinkActivity.this, "Chọn đủ thông tin",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Drink drink = new Drink();
                        drink.name_drink = edt_name_d.getText().toString();
                        drink.sl = edt_sl_d.getText().toString();
                        drink.price = edt_price_d.getText().toString();
                        drink.image = url_temp;
                        drink.text = edt_mota.getText().toString();

                        databaseReference= FirebaseDatabase.getInstance().getReference();

    /*                          databaseReference.child("Users").push().setValue(user);
                                Toast.makeText(CreatedActivity.this, "hoàn thành", Toast.LENGTH_SHORT).show();
                                new_intent();
    */
                        databaseReference.child("Drink").push().setValue(drink, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error==null){
                                    Toast.makeText(AddDrinkActivity.this, "hoàn thành", Toast.LENGTH_SHORT).show();
                                    new_intent();
                                }else {
                                    Toast.makeText(AddDrinkActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            void new_intent(){
                                Intent intent = new Intent(AddDrinkActivity.this,MainActivity.class);
                                intent.putExtra("key_user",key_user);
                                startActivity(intent);
                            }

                        });
                    }
                }}
        });

        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_sl_d.getText().toString().length()==0){
                    edt_sl_d.setText("0");
                }
                sl_food = Integer.parseInt(edt_sl_d.getText().toString());
                if (sl_food==0){
                    sl_food=0;
                }else {
                    sl_food--;
                }
                edt_sl_d.setText(""+ sl_food);
            }
        });

        btn_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_sl_d.getText().toString().length()==0){
                    edt_sl_d.setText("0");
                }
                sl_food = Integer.parseInt(edt_sl_d.getText().toString());
                sl_food++;
                edt_sl_d.setText(""+sl_food);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

                FirebaseStorage storage;
                StorageReference storageReference;
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                if(filePath != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(AddDrinkActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            url_temp=downloadUrl.toString();
                                        }
                                    });
                                    Toast.makeText(AddDrinkActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddDrinkActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}