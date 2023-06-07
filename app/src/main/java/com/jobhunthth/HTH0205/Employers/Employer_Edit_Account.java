package com.jobhunthth.HTH0205.Employers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Models.EmployerModel;
import com.jobhunthth.HTH0205.R;

public class Employer_Edit_Account extends AppCompatActivity {

    private Toolbar toolbar_employer_Edit;
    private EditText Edt_company_email,Edt_company_name,Edt_company_phone,Edt_company_address;
    private Button Btn_select_image_button,Btn_register_button;
    private ImageView ImgEdt_company_image;
    private final int REQUEST_IMAGE_PICKER = 1001;
    private String imgUri;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private String TAG = Employer_Edit_Account.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_edit_account);

        initUI();
        
        getData();

        initListener();

        setSupportActionBar(toolbar_employer_Edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_employer_Edit.setTitle("Edit account");
    }

    private void getData() {
        mStore.collection("employer").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>( ) {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        EmployerModel company = documentSnapshot.toObject(EmployerModel.class);
                        assert company != null;
                        showData(company);
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: "+e.getMessage() );
                    }
                });
    }

    private void showData(EmployerModel company) {
        Edt_company_email.setText(company.getEmail());
        Edt_company_address.setText(company.getAddress());
        Edt_company_name.setText(company.getName());
        Edt_company_phone.setText(company.getPhone_number());
        Glide.with(this).load(company.getAvatar()).error(R.drawable.avatar).into(ImgEdt_company_image);
        imgUri = company.getAvatar();
    }

    private void initListener() {
        Btn_select_image_button.setOnClickListener(view -> {
            Intent intent = new Intent(  );
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_IMAGE_PICKER);
        });

        ImgEdt_company_image.setOnClickListener(view -> {
            Intent intent = new Intent(  );
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_IMAGE_PICKER);
        });

        Btn_register_button.setOnClickListener(view -> {
            dialog.show();
            String email = Edt_company_email.getText().toString().trim();
            String name = Edt_company_name.getText().toString().trim();
            String address = Edt_company_address.getText().toString().trim();
            String phone = Edt_company_phone.getText().toString().trim();
            if(email.length()!=0 && name.length()!=0 && address.length()!=0 && phone.length()!=0 && imgUri != null){
                EmployerModel model = new EmployerModel(email,imgUri,name,phone,address);
                mStore.collection("employer").document(mAuth.getCurrentUser().getUid())
                        .set(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Employer_Edit_Account.this, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show( );
                                    dialog.dismiss();
                                    onBackPressed();
                                }else if(task.isCanceled()){
                                    Toast.makeText(Employer_Edit_Account.this, "Sửa thất bại", Toast.LENGTH_SHORT).show( );
                                    dialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Employer_Edit_Account.this, "Sửa thất bại", Toast.LENGTH_SHORT).show( );
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
        mAuth = FirebaseAuth.getInstance();
        toolbar_employer_Edit = findViewById(R.id.toolbar_employer_Edit);
        Edt_company_email = findViewById(R.id.Edt_company_email);
        Edt_company_name = findViewById(R.id.Edt_company_name);
        Edt_company_phone = findViewById(R.id.Edt_company_phone);
        Edt_company_address = findViewById(R.id.Edt_company_address);
        Btn_select_image_button = findViewById(R.id.Btn_select_image_button);
        Btn_register_button = findViewById(R.id.Btn_register_button);
        ImgEdt_company_image = findViewById(R.id.ImgEdt_company_image);
        dialog = new ProgressDialog(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null||data!=null||requestCode==REQUEST_IMAGE_PICKER||resultCode==RESULT_OK){
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
                        Glide.with(Employer_Edit_Account.this).load(imageUri).error(R.drawable.avatar).into(ImgEdt_company_image);
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