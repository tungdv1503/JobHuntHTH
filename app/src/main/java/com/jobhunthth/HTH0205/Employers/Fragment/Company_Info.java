package com.jobhunthth.HTH0205.Employers.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Edit_Company_Info;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class Company_Info extends Fragment {

    private View view;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private RoundedImageView Account_employer_img;
    private TextView companyNameTextView,companyScaleTextView  ,companyIndustryTextView ,companyPhoneTextView ,companyWebsiteTextView
            ,companyAddressTextView ,companyDescriptionTextView ;
    private Button Account_employer_btn_edit;
    private final String TAG = Company_Info.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_company_info, container, false);

        initUI();

        getData();

        initListener();

        return view;
    }

    private void initListener() {
        Account_employer_btn_edit.setOnClickListener(view1 -> {
            Intent intent = new Intent( getContext(), Edit_Company_Info.class );
            startActivity(intent);
        });
    }

    private void getData() {
        mStore.collection("CompanyInfo").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>( ) {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CompanyInfoModel company = documentSnapshot.toObject(CompanyInfoModel.class);
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

    private void showData(CompanyInfoModel company) {
        Glide.with(this).load(company.getCompanyAvatar()).error(R.drawable.avatar).into(Account_employer_img);
        companyNameTextView.setText("Tên công ty : "+company.getCompanyName());
        companyScaleTextView.setText("Quy mô công ty : "+company.getCompanyScale());
        companyIndustryTextView.setText("Ngành nghề công ty : "+company.getCompanyIndustry());
        companyPhoneTextView.setText("Số điện thoại công ty : "+company.getCompanyPhone());
        companyWebsiteTextView.setText("Website công ty : "+company.getCompanyWebsite());
        companyAddressTextView.setText("Địa chỉ : "+company.getCompanyAddress());
        companyDescriptionTextView.setText("Mô tả : "+company.getCompanyDescription());
    }

    private void initUI() {
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        Account_employer_img = view.findViewById(R.id.Account_employer_img);
        companyNameTextView = view.findViewById(R.id.companyNameTextView);
        companyScaleTextView = view.findViewById(R.id.companyScaleTextView);
        companyIndustryTextView = view.findViewById(R.id.companyIndustryTextView);
        companyPhoneTextView = view.findViewById(R.id.companyPhoneTextView);
        companyWebsiteTextView = view.findViewById(R.id.companyWebsiteTextView);
        companyAddressTextView = view.findViewById(R.id.companyAddressTextView);
        companyDescriptionTextView = view.findViewById(R.id.companyDescriptionTextView);
        Account_employer_btn_edit = view.findViewById(R.id.Account_employer_btn_edit);
    }

    @Override
    public void onResume() {
        super.onResume( );
        getData();
    }
}