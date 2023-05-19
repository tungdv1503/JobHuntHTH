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
import com.jobhunthth.HTH0205.Employers.Employer_Edit_Account;
import com.jobhunthth.HTH0205.Models.EmployerModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class Account_Employer extends Fragment {

    private View view;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private RoundedImageView Account_employer_img;
    private TextView Account_employer_name,Account_employer_email,Account_employer_address,Account_employer_phone;
    private Button Account_employer_btn_edit;
    private final String TAG = Account_Employer.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account__employer, container, false);

        initUI();

        getData();

        initListener();

        return view;
    }

    private void initListener() {
        Account_employer_btn_edit.setOnClickListener(view1 -> {
            Intent intent = new Intent( getContext(), Employer_Edit_Account.class );
            startActivity(intent);
        });
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
        Glide.with(this).load(company.getAvatar()).error(R.drawable.avatar).into(Account_employer_img);
        Account_employer_name.setText(company.getName());
        Account_employer_email.setText(company.getEmail());
        Account_employer_phone.setText(company.getPhone_number());
        Account_employer_address.setText(company.getAddress());
    }

    private void initUI() {
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        Account_employer_img = view.findViewById(R.id.Account_employer_img);
        Account_employer_name = view.findViewById(R.id.Account_employer_name);
        Account_employer_email = view.findViewById(R.id.Account_employer_email);
        Account_employer_address = view.findViewById(R.id.Account_employer_address);
        Account_employer_phone = view.findViewById(R.id.Account_employer_phone);
        Account_employer_btn_edit = view.findViewById(R.id.Account_employer_btn_edit);
    }

    @Override
    public void onResume() {
        super.onResume( );
        getData();
    }
}