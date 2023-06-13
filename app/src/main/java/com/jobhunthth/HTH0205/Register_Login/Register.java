package com.jobhunthth.HTH0205.Register_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Check_Login.check_Login;
import com.jobhunthth.HTH0205.Employers.Employers_Activity;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;

public class Register extends AppCompatActivity {
    TextInputEditText edtEmail, edtPassword;
    TextInputLayout layoutEmail, layoutPassword;
    Button btn_reg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView loginnow,txtRegister;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtEmail = findViewById(R.id.email_edt);
        edtPassword = findViewById(R.id.password_edt);
        btn_reg = findViewById(R.id.button_register);
        loginnow = findViewById(R.id.LoginNow);
//        txtRegister = findViewById(R.id.txtRegister);
        progressBar = findViewById(R.id.progressbar);
        loginnow.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext( ), Login.class);
            startActivity(intent);
            finish( );
        });
        btn_reg.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(edtEmail.getText( ));
                password = String.valueOf(edtPassword.getText( ));
                mAuth = FirebaseAuth.getInstance( );
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show( );
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show( );
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful( )) {
                                    Toast.makeText(Register.this, "Succes", Toast.LENGTH_SHORT).show( );
//                                    Intent intent = new Intent(getApplicationContext( ), Login.class);
//                                    startActivity(intent);
                                    checkRecruiter(email,password);
                                    finish( );
                                } else {

                                    Toast.makeText(Register.this, "Fail", Toast.LENGTH_SHORT).show( );

                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed( );
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn nút back 2 lần liên tiếp để thoát", Toast.LENGTH_SHORT).show( );

        new Handler(Looper.getMainLooper( )).postDelayed(new Runnable( ) {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    private void checkRecruiter(String email,String password) {
        Intent intent;
        intent = new Intent(Register.this, check_Login.class);
        Bundle bundleTK = new Bundle( );
        bundleTK.putString("Email",email);
        bundleTK.putString("Password",password);
        intent.putExtra("bundleTK",bundleTK);
        startActivity(intent);
    }
}