package com.jobhunthth.HTH0205.Employers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Models.EmployerModel;
import com.jobhunthth.HTH0205.R;

import java.io.IOException;

public class Register_Employer extends AppCompatActivity {

    EditText companyEmailEditText,companyNameEditText,companyPhoneEditText,companyAddressEditText;
    Button selectImageButton;
    ImageView companyImageView;
    Button registerButton;
    private final int REQUEST_IMAGE_PICKER = 1001;
    private String imgUri;
    private FirebaseFirestore mStore;
    private FirebaseUser mUser;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employer);

        initUI();

        initListener();
    }

    private void initListener() {
        selectImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(  );
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_IMAGE_PICKER);
        });

        companyImageView.setOnClickListener(view -> {
            Intent intent = new Intent(  );
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_IMAGE_PICKER);
        });

        registerButton.setOnClickListener(view -> {
            dialog.show();
            String email = companyEmailEditText.getText().toString().trim();
            String name = companyNameEditText.getText().toString().trim();
            String address = companyAddressEditText.getText().toString().trim();
            String phone = companyPhoneEditText.getText().toString().trim();
            if(email.length()!=0 && name.length()!=0 && address.length()!=0 && phone.length()!=0 && imgUri != null){
                EmployerModel model = new EmployerModel(email,imgUri,name,phone,address);
                mStore.collection("employer").document(mUser.getUid())
                        .set(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register_Employer.this, "Đăng ký thông tin thành công", Toast.LENGTH_SHORT).show( );
                                    dialog.dismiss();
                                    Intent intent = new Intent( Register_Employer.this,Employers_Activity.class );
                                    startActivity(intent);
                                    finishAffinity();
                                }else if(task.isCanceled()){
                                    Toast.makeText(Register_Employer.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show( );
                                    dialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register_Employer.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show( );
                                dialog.dismiss();
                            }
                        });
            }else {
                dialog.dismiss();
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show( );
            }
        });
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        companyEmailEditText = findViewById(R.id.company_email);
        companyNameEditText = findViewById(R.id.company_name);
        companyPhoneEditText = findViewById(R.id.company_phone);
        companyAddressEditText = findViewById(R.id.company_address);
        selectImageButton = findViewById(R.id.select_image_button);
        companyImageView = findViewById(R.id.company_image);
        registerButton = findViewById(R.id.register_button);
        dialog = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null||data!=null||resultCode==REQUEST_IMAGE_PICKER||resultCode==RESULT_OK){
            Uri uri = data.getData();
            dialog.show();
            uploadImageToStorage(uri,FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    private void uploadImageToStorage(Uri imageUri, String companyId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("company_images/" + companyId + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(Register_Employer.this).load(imageUri).error(R.drawable.avatar).into(companyImageView);
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                imgUri = downloadUri.toString();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

}