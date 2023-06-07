package com.jobhunthth.HTH0205.Check_Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Employers_Activity;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.RegisterInfo;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;

import java.util.HashMap;
import java.util.Map;

public class check_Login extends AppCompatActivity {
    LinearLayout jobfind, recruter;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);
        db = FirebaseFirestore.getInstance( );
        currentUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        if (currentUser != null) {
            String currentUid = currentUser.getUid( );
        }
        jobfind = findViewById(R.id.jobsearch);
        recruter = findViewById(R.id.recruters);
        sharedPreferences = getSharedPreferences("CheckVT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit( );
        Intent iCheck = getIntent( );
        Bundle bundleTKMK = iCheck.getBundleExtra("bundleTK");
        String email = bundleTKMK.getString("Email");
        String password = bundleTKMK.getString("Password");
        jobfind.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    //SharedPreferences lưu trạng thái màn hình ==> thêm thông tin tài khoản
                    // ==> hồ sơ xin việc ==> vào màn hình đã chọn
                    editor.putInt("VaiTro", 1);
                    editor.apply( );
                    String currentUid = currentUser.getUid( );
                    Map<String, Object> data = new HashMap<>( );
                    data.put("status", "yes");
                    db.collection("jobsearch").document(currentUid).set(data);
//                    Intent intent = new Intent(getApplicationContext( ), MainScreen.class);
//                    startActivity(intent);
                    Signin(1, email, password);

                }
            }
        });

        recruter.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    //SharedPreferences lưu trạng thái màn hình ==> thêm thông tin tài khoản
                    // ==> thông tin công ty ==> vào màn hình đã chọn
                    editor.putInt("VaiTro", 2);
                    editor.apply( );
                    String currentUid = currentUser.getUid( );
                    Map<String, Object> data = new HashMap<>( );
                    data.put("Admin_Permission_access", "no");
                    db.collection("recruiter").document(currentUid).set(data);
                    Intent intent = new Intent(getApplicationContext( ), RegisterInfo.class);
                    startActivity(intent);
                    finish( );
//                    Intent intent = new Intent(getApplicationContext( ), Register_Employer.class);
//                    startActivity(intent);
                    Signin(2, email, password);
                }
            }
        });

    }

    private void Signin(int check, String email, String password) {
        mAuth = FirebaseAuth.getInstance( );
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful( )) {
                            if (check == 1) {
                                Intent intent = new Intent(check_Login.this, MainScreen.class);
                                startActivity(intent);
                                finish( );
                            } else if (check == 2) {
                                Intent intent = new Intent(check_Login.this, Employers_Activity.class);
                                startActivity(intent);
                                finish( );
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit( );
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply( );
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(check_Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show( );
                        }

                    }

                });
    }
}