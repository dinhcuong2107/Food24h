package com.example.food24h.fragment;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food24h.AddFoodActivity;
import com.example.food24h.CTOrdActivity;
import com.example.food24h.LoginActivity;
import com.example.food24h.MainActivity;
import com.example.food24h.R;
import com.example.food24h.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class SettingFrag extends Fragment {
ImageView imageView,temp;
TextView t_name, t_phone, t_pos;
Button button_avt,button_phone,button_pass,button_out;
String key_user="",url_temp="";
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting_frag,container,false);

        imageView = (ImageView) view.findViewById(R.id.st_img_avt);

        t_name = (TextView) view.findViewById(R.id.st_name);
        t_phone = (TextView) view.findViewById(R.id.st_phone);
        t_pos = (TextView) view.findViewById(R.id.st_pos);

        button_avt = (Button) view.findViewById(R.id.st_change_avt);
        button_phone = (Button) view.findViewById(R.id.st_change_phone);
        button_pass = (Button) view.findViewById(R.id.st_change_pass);
        button_out = (Button) view.findViewById(R.id.st_out);

        Bundle bundle = getActivity().getIntent().getExtras();
        key_user = bundle.getString("key_user");

        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.avt != null)
                {
                    Picasso.with(getActivity()).load(user.avt).into(imageView);
                }
                t_name.setText(""+user.fullname);
                t_phone.setText(""+user.phonenumber);
                t_pos.setText(""+user.position);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog_Avt(Gravity.CENTER);
            }
        });

        button_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog_Pass(Gravity.CENTER);
            }
        });

        button_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog_Phone(Gravity.CENTER);
            }
        });

        button_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void openDialog_Pass(int gra) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_pass);

        Window window = dialog.getWindow();
        if (window == null){return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win = window.getAttributes();
        win.gravity = gra;
        window.setAttributes(win);

        if (Gravity.CENTER== gra){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        EditText editTextPass = dialog.findViewById(R.id.dialog_pass);
        EditText editTextNewPass = dialog.findViewById(R.id.dialog_newpass);
        EditText editTextAgainPass = dialog.findViewById(R.id.dialog_againpass);
        Button button = dialog.findViewById(R.id.dialog_up_pass);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference= FirebaseDatabase.getInstance().getReference("User").child(key_user);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if (user.password.equals(editTextPass.getText().toString().trim()))
                        {
                            if (editTextNewPass.getText().toString().length()>7)
                            {
                                if (editTextNewPass.getText().toString().equals(editTextAgainPass.getText().toString()) & editTextNewPass.getText().toString().length()>0)
                                {
                                    databaseReference= FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("User").child(key_user).child("password").setValue(editTextNewPass.getText().toString());
                                    Toast.makeText(getActivity(),"đổi mật khẩu thành công",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getActivity(),"nhập lại mật khẩu mới",Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getActivity(),"mật khẩu không đủ bảo mật",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getActivity(),"mật khẩu không chính xác",Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        dialog.show();
    }

    private void openDialog_Phone(int gra) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_phone);

        Window window = dialog.getWindow();
        if (window == null){return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win = window.getAttributes();
        win.gravity = gra;
        window.setAttributes(win);

        if (Gravity.CENTER== gra){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        EditText editText = dialog.findViewById(R.id.dialog_phone);
        Button button = dialog.findViewById(R.id.dialog_up_phone);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length()==10)
                {
                    databaseReference= FirebaseDatabase.getInstance().getReference();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                User user = dataSnapshot.getValue(User.class);
                                if (url_temp.length()>=0)
                                {
                                    databaseReference= FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("User").child(key_user).child("phonenumber").setValue(editText.getText().toString());
                                    Toast.makeText(getActivity(),"cập nhật số điện thoại thành công",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getActivity(),"K thành công",Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(),"Số điện thoại không hợp lệ",Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    private void openDialog_Avt(int gra) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_image);

        Window window = dialog.getWindow();
        if (window == null){return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams win = window.getAttributes();
        win.gravity = gra;
        window.setAttributes(win);

        if (Gravity.CENTER== gra){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        ImageView img = dialog.findViewById(R.id.dialog_img);
        Button button = dialog.findViewById(R.id.dialog_up_avt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            User user = dataSnapshot.getValue(User.class);
                            if (url_temp.length()>=0)
                            {
                                databaseReference= FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("User").child(key_user).child("avt").setValue(url_temp);
                                Toast.makeText(getActivity(),"cập nhật ảnh thành công",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(),"K tìm thấy ảnh",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage;
                StorageReference storageReference;
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp = img;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }

                });
            }
        });
        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            temp.setImageURI(filePath);

            FirebaseStorage storage;
            StorageReference storageReference;
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            if(filePath != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}