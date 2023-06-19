package com.jobhunthth.HTH0205.Register_Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Employers.Employers_Activity;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class RegisterEmployerInfo extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 1001;
    private RoundedImageView companyAvatarImageView;
    private TextInputEditText companyNameEditText, companyScaleEditText, companyIndustryEditText, companyPhoneEditText,
            companyWebsiteEditText, companyAddressEditText, companyDescriptionEditText;
    private TextInputLayout companyAddressTextInputLayout, companyWebsiteTextInputLayout, companyPhoneTextInputLayout,
            companyIndustryTextInputLayout, companyScaleTextInputLayout, companyNameTextInputLayout, companyDescriptionTextInputLayout;
    private Button addCompanyButton;
    private TextView txt_skip;
    private String imgUri;
    private FirebaseUser mUser;
    private FirebaseFirestore mStore;
    private ProgressDialog dialog;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employer_info);

        initUI( );

        initListener( );
    }

    private void initListener() {
        companyAvatarImageView.setOnClickListener(view -> {
            Intent intent = new Intent( );
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });

        addCompanyButton.setOnClickListener(view -> {
            if (validate( )) {
                dialog.show( );
                String companyName = companyNameEditText.getText( ).toString( ).trim( );
                String companyScale = companyScaleEditText.getText( ).toString( ).trim( );
                String companyIndustry = companyIndustryEditText.getText( ).toString( ).trim( );
                String companyPhone = companyPhoneEditText.getText( ).toString( ).trim( );
                String companyWebsite = companyWebsiteEditText.getText( ).toString( ).trim( );
                String companyAddress = companyAddressEditText.getText( ).toString( ).trim( );
                String companyDescription = companyDescriptionEditText.getText( ).toString( ).trim( );

                CompanyInfoModel companyInfoModel = new CompanyInfoModel(imgUri, companyName, companyScale, companyIndustry, companyPhone, companyWebsite,
                        companyAddress, companyDescription, mUser.getUid( ));

                mStore.collection("CompanyInfo").document(mUser.getUid( )).set(companyInfoModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>( ) {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss( );
                                int type = mIntent.getIntExtra("type", -1);
                                if (type == -1) {
                                    Intent intent = new Intent(RegisterEmployerInfo.this, Employers_Activity.class);
                                    startActivity(intent);
                                    finishAffinity( );
                                } else {
                                    onBackPressed( );
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss( );
                                Toast.makeText(RegisterEmployerInfo.this, "Lỗi", Toast.LENGTH_SHORT).show( );
                            }
                        });
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                int type = mIntent.getIntExtra("type", -1);
                if (type == -1) {
                    Intent intent = new Intent(RegisterEmployerInfo.this, Employers_Activity.class);
                    startActivity(intent);
                    finishAffinity( );
                } else {
                    onBackPressed( );
                }
            }
        });
    }

    private boolean validate() {
        String companyName = companyNameEditText.getText( ).toString( ).trim( );
        String companyScale = companyScaleEditText.getText( ).toString( ).trim( );
        String companyIndustry = companyIndustryEditText.getText( ).toString( ).trim( );
        String companyPhone = companyPhoneEditText.getText( ).toString( ).trim( );
        String companyWebsite = companyWebsiteEditText.getText( ).toString( ).trim( );
        String companyAddress = companyAddressEditText.getText( ).toString( ).trim( );
        String companyDescription = companyDescriptionEditText.getText( ).toString( ).trim( );
        companyAddressTextInputLayout.setError(null);
        companyDescriptionTextInputLayout.setError(null);
        companyNameTextInputLayout.setError(null);
        companyPhoneTextInputLayout.setError(null);
        companyScaleTextInputLayout.setError(null);
        companyWebsiteTextInputLayout.setError(null);
        companyIndustryTextInputLayout.setError(null);

        boolean isValid = true;

        if (companyName.isEmpty( )) {
            companyNameEditText.setError("Tên công ty không được để trống");
            isValid = false;
        }

        if (companyScale.isEmpty( )) {
            companyScaleEditText.setError("Quy mô công ty không được để trống");
            isValid = false;
        }

        if (companyIndustry.isEmpty( )) {
            companyIndustryEditText.setError("Ngành nghề công ty không được để trống");
            isValid = false;
        }

        if (companyPhone.isEmpty( )) {
            companyPhoneEditText.setError("Số điện thoại công ty không được để trống");
            isValid = false;
        }

        if (companyWebsite.isEmpty( )) {
            companyWebsiteEditText.setError("Website công ty không được để trống");
            isValid = false;
        } else if (!isValidWebsite(companyWebsite)) {
            companyWebsiteEditText.setError("Địa chỉ website không hợp lệ");
            isValid = false;
        }

        if (companyAddress.isEmpty( )) {
            companyAddressEditText.setError("Địa chỉ làm việc không được để trống");
            isValid = false;
        }

        if (companyDescription.isEmpty( )) {
            companyDescriptionEditText.setError("Mô tả công ty không được để trống");
            isValid = false;
        }

        return isValid;

    }

    private boolean isValidWebsite(String website) {
        String regex = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$";
        return website.matches(regex);
    }

    private void initUI() {
        mIntent = getIntent( );
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        mStore = FirebaseFirestore.getInstance( );
        dialog = new ProgressDialog(this);
        companyAvatarImageView = findViewById(R.id.companyAvatarImageView);
        companyNameTextInputLayout = findViewById(R.id.companyNameTextInputLayout);
        companyNameEditText = findViewById(R.id.companyNameEditText);
        companyScaleTextInputLayout = findViewById(R.id.companyScaleTextInputLayout);
        companyScaleEditText = findViewById(R.id.companyScaleEditText);
        companyIndustryTextInputLayout = findViewById(R.id.companyIndustryTextInputLayout);
        companyIndustryEditText = findViewById(R.id.companyIndustryEditText);
        companyPhoneTextInputLayout = findViewById(R.id.companyPhoneTextInputLayout);
        companyPhoneEditText = findViewById(R.id.companyPhoneEditText);
        companyWebsiteTextInputLayout = findViewById(R.id.companyWebsiteTextInputLayout);
        companyWebsiteEditText = findViewById(R.id.companyWebsiteEditText);
        companyAddressTextInputLayout = findViewById(R.id.companyAddressTextInputLayout);
        companyAddressEditText = findViewById(R.id.companyAddressEditText);
        companyDescriptionTextInputLayout = findViewById(R.id.companyDescriptionTextInputLayout);
        companyDescriptionEditText = findViewById(R.id.companyDescriptionEditText);
        addCompanyButton = findViewById(R.id.addCompanyButton);
        txt_skip = findViewById(R.id.txt_skip);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData( ) != null) {
            Uri uri = data.getData( );
            dialog.show( );
            uploadImageToStorage(uri, FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ));
        }
    }

    private void uploadImageToStorage(Uri imageUri, String userID) {
        StorageReference storageRef = FirebaseStorage.getInstance( ).getReference( );
        StorageReference imageRef = storageRef.child("company_images/" + userID + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(RegisterEmployerInfo.this).load(imageUri).error(R.drawable.avatar).into(companyAvatarImageView);
                        imageRef.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                imgUri = downloadUri.toString( );
                                dialog.dismiss( );
                            }
                        }).addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss( );
                        Toast.makeText(RegisterEmployerInfo.this, "Lỗi", Toast.LENGTH_SHORT).show( );
                    }
                });
    }
}