package com.jobhunthth.HTH0205.Employers.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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
    private TextView companyNameTextView, companyScaleTextView, companyIndustryTextView, companyPhoneTextView, companyWebsiteTextView, companyAddressTextView, companyDescriptionTextView;
    private ImageButton btnEditCompanyName, btnEditCompanySize, btnEditCompanyIndustry, btnEditCompanyPhone,
            btnEditCompanyWeb, btnEditCompanyAddress, btnEditCompanyDesc;
    private final String TAG = Company_Info.class.getName( );
    private String txt = "";
    private ProgressDialog dialog;

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
            String editText = getEditText( );
            if (!editText.isEmpty( )) {
                dialog.show();
                mStore.collection("CompanyInfo").document( mAuth.getCurrentUser().getUid() )
                        .update("companyName",editText)
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                companyNameTextView.setText(editText);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show( );
                            }
                        });
            }
        });
    }

    private String getEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext( ));

        View view1 = View.inflate(getContext( ), R.layout.dialog_edit_text, null);
        TextInputEditText editText = view1.findViewById(R.id.dialog_edt);
        builder.setView(view1);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String edtChange = editText.getText( ).toString( ).trim( );
                        if (!edtChange.isEmpty( )) {
                            txt = edtChange;
                            dialogInterface.dismiss( );
                        }
                    }
                })
                .setPositiveButton("Cancle", new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss( );
                    }
                });
        builder.create().show();
        return txt;
    }

    private void getData() {
        mStore.collection("CompanyInfo").document(mAuth.getCurrentUser( ).getUid( )).get( )
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
        dialog = new ProgressDialog(getContext());
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
    }

    @Override
    public void onResume() {
        super.onResume( );
        getData( );
    }
}