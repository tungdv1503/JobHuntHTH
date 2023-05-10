package com.jobhunthth.HTH0205.Register_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Check_Login.check_Login;
import com.jobhunthth.HTH0205.Employers.Employers_Activity;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    TextInputEditText edtEmail, edtPassword;
    Button btn_log;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView loginnow;
    SharedPreferences sharedPreferences;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    String uid;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = findViewById(R.id.email_edt);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
        }
        db = FirebaseFirestore.getInstance();
        edtPassword = findViewById(R.id.password_edt);
        btn_log = findViewById(R.id.login_button);
        loginnow = findViewById(R.id.LoginNow);
        progressBar = findViewById(R.id.progressbar);
        loginnow.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        });

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String Email = sharedPreferences.getString("email", "");
        String Password = sharedPreferences.getString("password", "");
        edtEmail.setText(Email);
        edtPassword.setText(Password);

        if (!edtEmail.getText().toString().trim().equals("") && !edtPassword.getText().toString().trim().equals("")) {
            showAlertDialog();

        }

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    uid = currentUser.getUid();
                }
                db = FirebaseFirestore.getInstance();
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(edtEmail.getText());
                password = String.valueOf(edtPassword.getText());
                mAuth = FirebaseAuth.getInstance();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                db.collection("jobsearch")
                                        .whereEqualTo(FieldPath.documentId(), uid)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    boolean hasJobSearchData = false;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String documentId = document.getId();
                                                        Log.d("IDDDDDDDDDDDDDDDDĐ",documentId);
                                                        if (documentId.equals(uid)) {
                                                            hasJobSearchData = true;
                                                            break;
                                                        }
                                                    }
                                                    if (hasJobSearchData) {
                                                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        checkRecruiter(uid);
                                                    }
                                                } else {
                                                    Toast.makeText(Login.this, "Lỗi khi kiểm tra dữ liệu jobsearch", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.apply();
                                finish();
                            }
                        });



            }
        });
    }

    public void showAlertDialog() {

        // Tạo một đối tượng AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

        // Thiết lập tiêu đề và thông điệp cho hộp thoại
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn đăng nhập bằng tài khoản này không?");

        // Định nghĩa một nút "Đóng" cho hộp thoại
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss(); // Đóng hộp thoại
            }
        });
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    db.collection("jobsearch")
                                            .whereEqualTo(FieldPath.documentId(), uid)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        boolean hasJobSearchData = false;
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String documentId = document.getId();
                                                            Log.d("IDDDDDDDDDDDDDDDDĐ",documentId);
                                                            if (documentId.equals(uid)) {
                                                                hasJobSearchData = true;
                                                                break;
                                                            }
                                                        }
                                                        if (hasJobSearchData) {
                                                            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            checkRecruiter(uid);
                                                        }
                                                    } else {
                                                        Toast.makeText(Login.this, "Lỗi khi kiểm tra dữ liệu jobsearch", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", edtEmail.getText().toString().trim());
                                    editor.putString("password", edtPassword.getText().toString().trim());
                                    editor.apply();
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });

            }

        });
        // Tạo hộp thoại từ đối tượng AlertDialog.Builder
        AlertDialog dialog = builder.create();

        // Hiển thị hộp thoại
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn nút back 2 lần liên tiếp để thoát", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);

    }
    private void checkRecruiter(String uid) {
        db.collection("recruiter")
                .whereEqualTo(FieldPath.documentId(), uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean hasRecruiterData = !task.getResult().isEmpty();
                            Intent intent;
                            if (hasRecruiterData) {
                                intent = new Intent(getApplicationContext(), Employers_Activity.class);
                            } else {
                                intent = new Intent(getApplicationContext(), check_Login.class);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Lỗi khi kiểm tra dữ liệu recruiter", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

