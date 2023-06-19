package com.jobhunthth.HTH0205.Employers.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class Company_Info extends Fragment {

    private View view;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private RoundedImageView Account_employer_img;
    private TextView companyNameTextView, companyScaleTextView, companyIndustryTextView, companyPhoneTextView, companyWebsiteTextView, companyAddressTextView, companyDescriptionTextView;
    private ImageButton btnEditCompanyName, btnEditCompanySize, btnEditCompanyIndustry, btnEditCompanyPhone,
            btnEditCompanyWeb, btnEditCompanyAddress, btnEditCompanyDesc;
    private LinearLayout lo_companyName, lo_companyScale, lo_companyIndustry, lo_companyPhone, lo_companyWeb,
            lo_companyAddress, lo_companyDesc;
    private EditText edt_companyName, edt_companyScale, edt_companyIndustry, edt_companyPhone, edt_companyWeb,
            edt_companyAddress, edt_companyDesc;
    private final String TAG = Company_Info.class.getName( );
    private ProgressDialog dialog;
    private CompanyInfoModel mCompanyInfo;
    private boolean isCompanyNameChecked, isCompanySizeChecked, isCompanyAddressChecked,
            isCompanyIndustryChecked, isCompanyPhoneChecked, isCompanyWebChecked, isCompanyDescChecked;

    private static final int REQUEST_IMAGE_PICKER = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_company_info, container, false);

        initUI( );

        getData( );

        initListener( );

        return view;
    }

    private void initListener() {
        btnEditCompanyName.setOnClickListener(view1 -> {
            if (!isCompanyNameChecked) {
                companyNameTextView.setVisibility(View.GONE);
                lo_companyName.setVisibility(View.VISIBLE);
                edt_companyName.setText(mCompanyInfo.getCompanyName( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanyName);
                isCompanyNameChecked = true;
            } else {
                String text = edt_companyName.getText( ).toString( ).trim( );
                companyNameTextView.setVisibility(View.VISIBLE);
                lo_companyName.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanyName);
                if (!text.isEmpty( )) {
                    if (!text.equals(mCompanyInfo.getCompanyName( ))) {
                        mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                .update("companyName", text);
                        getData( );
                    }
                }
                isCompanyNameChecked = false;
            }
        });

        btnEditCompanySize.setOnClickListener(view1 -> {
            if (!isCompanySizeChecked) {
                companyScaleTextView.setVisibility(View.GONE);
                lo_companyScale.setVisibility(View.VISIBLE);
                edt_companyScale.setText(mCompanyInfo.getCompanyScale( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanySize);
                isCompanySizeChecked = true;
            } else {
                String text = edt_companyScale.getText( ).toString( ).trim( );
                companyScaleTextView.setVisibility(View.VISIBLE);
                lo_companyScale.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanySize);
                if (!text.isEmpty( )) {
                    if (!text.equals(mCompanyInfo.getCompanyScale( ))) {
                        mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                .update("companyScale", text);
                        getData( );
                    }
                }
                isCompanySizeChecked = false;
            }
        });

        btnEditCompanyAddress.setOnClickListener(view1 -> {
            if (!isCompanyAddressChecked) {
                companyAddressTextView.setVisibility(View.GONE);
                lo_companyAddress.setVisibility(View.VISIBLE);
                edt_companyAddress.setText(mCompanyInfo.getCompanyAddress( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanyAddress);
                isCompanyAddressChecked = true;
            } else {
                String text = edt_companyAddress.getText( ).toString( ).trim( );
                companyAddressTextView.setVisibility(View.VISIBLE);
                lo_companyAddress.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanyAddress);
                if (!text.isEmpty( )) {
                    if (!text.equals(mCompanyInfo.getCompanyAddress( ))) {
                        mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                .update("companyAddress", text);
                        getData( );
                    }
                }
                isCompanyAddressChecked = false;
            }
        });

        btnEditCompanyIndustry.setOnClickListener(view1 -> {
            if (!isCompanyIndustryChecked) {
                companyIndustryTextView.setVisibility(View.GONE);
                lo_companyIndustry.setVisibility(View.VISIBLE);
                edt_companyIndustry.setText(mCompanyInfo.getCompanyIndustry( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanyIndustry);
                isCompanyIndustryChecked = true;
            } else {
                String text = edt_companyIndustry.getText( ).toString( ).trim( );
                companyIndustryTextView.setVisibility(View.VISIBLE);
                lo_companyIndustry.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanyIndustry);
                if (!text.isEmpty( )) {
                    if (!text.equals(mCompanyInfo.getCompanyIndustry( ))) {
                        mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                .update("companyIndustry", text);
                        getData( );
                    }
                }
                isCompanyIndustryChecked = false;
            }
        });

        btnEditCompanyPhone.setOnClickListener(view1 -> {
            if (!isCompanyPhoneChecked) {
                companyPhoneTextView.setVisibility(View.GONE);
                lo_companyPhone.setVisibility(View.VISIBLE);
                edt_companyPhone.setText(mCompanyInfo.getCompanyPhone( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanyPhone);
                isCompanyPhoneChecked = true;
            } else {
                String text = edt_companyPhone.getText( ).toString( ).trim( );
                companyPhoneTextView.setVisibility(View.VISIBLE);
                lo_companyPhone.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanyPhone);
                if (!text.isEmpty( )) {
                    if (!text.equals(mCompanyInfo.getCompanyPhone( ))) {
                        mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                .update("companyPhone", text);
                        getData( );
                    }
                }
                isCompanyPhoneChecked = false;
            }
        });

        btnEditCompanyWeb.setOnClickListener(view1 -> {
            if (!isCompanyWebChecked) {
                companyWebsiteTextView.setVisibility(View.GONE);
                lo_companyWeb.setVisibility(View.VISIBLE);
                edt_companyWeb.setText(mCompanyInfo.getCompanyWebsite( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanyWeb);
                isCompanyWebChecked = true;
            } else {
                String text = edt_companyWeb.getText( ).toString( ).trim( );
                companyWebsiteTextView.setVisibility(View.VISIBLE);
                lo_companyWeb.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanyWeb);
                if (!text.isEmpty( )) {
                    if(!isValidWebsite(text)){
                        Toast.makeText(getContext(), "Website không hợp lệ", Toast.LENGTH_SHORT).show( );
                    }else {
                        if (!text.equals(mCompanyInfo.getCompanyWebsite( ))) {
                            mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                    .update("companyWebsite", text);
                            getData( );
                        }
                    }
                }
                isCompanyWebChecked = false;
            }
        });

        btnEditCompanyDesc.setOnClickListener(view1 -> {
            if (!isCompanyDescChecked) {
                companyDescriptionTextView.setVisibility(View.GONE);
                lo_companyDesc.setVisibility(View.VISIBLE);
                edt_companyDesc.setText(mCompanyInfo.getCompanyDescription( ));
                Glide.with(this).load(R.drawable.check_24).into(btnEditCompanyDesc);
                isCompanyDescChecked = true;
            } else {
                String text = edt_companyDesc.getText( ).toString( ).trim( );
                companyDescriptionTextView.setVisibility(View.VISIBLE);
                lo_companyDesc.setVisibility(View.GONE);
                Glide.with(this).load(R.drawable.edit_24).into(btnEditCompanyDesc);
                if (!text.isEmpty( )) {
                    if (!text.equals(mCompanyInfo.getCompanyDescription( ))) {
                        mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                .update("companyDescription", text);
                        getData( );
                    }
                }
                isCompanyDescChecked = false;
            }
        });

        Account_employer_img.setOnClickListener(view1 -> {
            Intent intent = new Intent( );
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });
    }

    private boolean isValidWebsite(String website) {
        String regex = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$";
        return website.matches(regex);
    }

    private void getData() {
        mStore.collection("CompanyInfo").document(mAuth.getCurrentUser( ).getUid( )).get( )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>( ) {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CompanyInfoModel company = documentSnapshot.toObject(CompanyInfoModel.class);
                        assert company != null;
                        mCompanyInfo = company;
                        showData(company);
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage( ));
                    }
                });
    }

    private void showData(CompanyInfoModel company) {
        Glide.with(this).load(company.getCompanyAvatar( )).error(R.drawable.avatar).into(Account_employer_img);
        companyNameTextView.setText(company.getCompanyName( ));
        companyScaleTextView.setText(company.getCompanyScale( ));
        companyIndustryTextView.setText(company.getCompanyIndustry( ));
        companyPhoneTextView.setText(company.getCompanyPhone( ));
        companyWebsiteTextView.setText(company.getCompanyWebsite( ));
        companyAddressTextView.setText(company.getCompanyAddress( ));
        companyDescriptionTextView.setText(company.getCompanyDescription( ));
    }

    private void initUI() {
        mAuth = FirebaseAuth.getInstance( );
        mStore = FirebaseFirestore.getInstance( );
        dialog = new ProgressDialog(getContext( ));
        Account_employer_img = view.findViewById(R.id.Account_employer_img);
        companyNameTextView = view.findViewById(R.id.companyNameTextView);
        companyScaleTextView = view.findViewById(R.id.companyScaleTextView);
        companyIndustryTextView = view.findViewById(R.id.companyIndustryTextView);
        companyPhoneTextView = view.findViewById(R.id.companyPhoneTextView);
        companyWebsiteTextView = view.findViewById(R.id.companyWebsiteTextView);
        companyAddressTextView = view.findViewById(R.id.companyAddressTextView);
        companyDescriptionTextView = view.findViewById(R.id.companyDescriptionTextView);
        btnEditCompanyName = view.findViewById(R.id.btn_editCompanyName);
        btnEditCompanySize = view.findViewById(R.id.btn_editCompanySize);
        btnEditCompanyIndustry = view.findViewById(R.id.btn_editCompanyIndustry);
        btnEditCompanyPhone = view.findViewById(R.id.btn_editCompanyPhone);
        btnEditCompanyWeb = view.findViewById(R.id.btn_editCompanyWeb);
        btnEditCompanyAddress = view.findViewById(R.id.btn_editCompanyAddress);
        btnEditCompanyDesc = view.findViewById(R.id.btn_editCompanyDesc);
        edt_companyName = view.findViewById(R.id.edt_companyName);
        lo_companyName = view.findViewById(R.id.lo_companyName);
        lo_companyScale = view.findViewById(R.id.lo_companyScale);
        edt_companyScale = view.findViewById(R.id.edt_companyScale);
        lo_companyIndustry = view.findViewById(R.id.lo_companyIndustry);
        edt_companyIndustry = view.findViewById(R.id.edt_companyIndustry);
        lo_companyPhone = view.findViewById(R.id.lo_companyPhone);
        edt_companyPhone = view.findViewById(R.id.edt_companyPhone);
        edt_companyWeb = view.findViewById(R.id.edt_companyWeb);
        lo_companyWeb = view.findViewById(R.id.lo_companyWeb);
        lo_companyAddress = view.findViewById(R.id.lo_companyAddress);
        edt_companyAddress = view.findViewById(R.id.edt_companyAddress);
        lo_companyDesc = view.findViewById(R.id.lo_companyDesc);
        edt_companyDesc = view.findViewById(R.id.edt_companyDesc);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data.getData( ) != null && data != null) {
            Uri uri = data.getData( );
            dialog.show( );
            uploadImageToStorage(uri, mAuth.getCurrentUser( ).getUid( ));
        }
    }

    private void uploadImageToStorage(Uri imageUri, String id) {
        StorageReference storageRef = FirebaseStorage.getInstance( ).getReference( );
        StorageReference imageRef = storageRef.child("company_images/" + id + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(getContext( )).load(imageUri).error(R.drawable.avatar).into(Account_employer_img);
                        imageRef.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                mStore.collection("CompanyInfo").document(mCompanyInfo.getAccountId( ))
                                        .update("companyAvatar", downloadUri.toString( ))
                                        .addOnSuccessListener(new OnSuccessListener<Void>( ) {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss( );
                                            }
                                        });
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
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume( );
        getData( );
    }
}