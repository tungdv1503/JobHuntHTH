package com.jobhunthth.HTH0205.Employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jobhunthth.HTH0205.Employers.Interface.ReauthenticationCallback;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.Login;

public class ChangePassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView txt_goBack;
    private Button btnChangePassword;
    private TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmNewPassword;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private String TAG = ChangePassword.class.getName();
    private boolean isCheckValidate;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initUI();
        initListener();
    }

    private void initListener() {
        txt_goBack.setOnClickListener(view -> {
            onBackPressed();
        });

        btnChangePassword.setOnClickListener(view -> {
            validate();
        });
    }

    private void validate() {
        isCheckValidate = true;
        String currentPass = etCurrentPassword.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();
        String reNewPass = etConfirmNewPassword.getText().toString().trim();

        if(currentPass.isEmpty()){
            tilCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            isCheckValidate = false;
        }else {
            reauth(currentPass, new ReauthenticationCallback( ) {
                @Override
                public void onReauthenticationSuccess() {
                    tilNewPassword.setErrorEnabled(false);
                    tilCurrentPassword.setErrorEnabled(false);
                    tilConfirmNewPassword.setErrorEnabled(false);
                    if(newPass.isEmpty()){
                        tilNewPassword.setError("Vui lòng nhập mật khẩu mới");
                        isCheckValidate = false;
                    }else if(newPass.equals(currentPass)){
                        tilNewPassword.setError("Mật khẩu mới khác mật khẩu cũ");
                        isCheckValidate = false;
                    } else if (!newPass.equals(reNewPass)) {
                        tilNewPassword.setError("Mật khẩu không giống nhau");
                        isCheckValidate = false;
                    }

                    if(reNewPass.isEmpty()){
                        tilConfirmNewPassword.setError("Vui lòng nhập lại mật khẩu mới");
                        isCheckValidate = false;
                    }else if(reNewPass.equals(currentPass)){
                        tilConfirmNewPassword.setError("Mật khẩu mới khác mật khẩu cũ");
                        isCheckValidate = false;
                    } else if (!reNewPass.equals(newPass)) {
                        tilConfirmNewPassword.setError("Mật khẩu không giống nhau");
                        isCheckValidate = false;
                    }

                    if(isCheckValidate){
                        changePass();
                    }
                }

                @Override
                public void onReauthenticationFailure(Exception exception) {
                    tilCurrentPassword.setError("Mật khẩu không giống");
                    isCheckValidate = false;
                }
            });
        };

    }

    private void changePass() {
        String newPass = etNewPassword.getText().toString();
        mAuth.getCurrentUser().updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent( ChangePassword.this, Login.class );
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangePassword.this, "Lỗi", Toast.LENGTH_SHORT).show( );
                    }
                });
    }

    private void reauth(String currentPass, ReauthenticationCallback callback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            callback.onReauthenticationSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onReauthenticationFailure(e);
                    }
                });
    }

    private void initUI() {
        dialog = new ProgressDialog(ChangePassword.this);
        mAuth = FirebaseAuth.getInstance();
        txt_goBack = findViewById(R.id.txt_goBack);
        tilCurrentPassword = findViewById(R.id.tilCurrentPassword);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        tilConfirmNewPassword = findViewById(R.id.tilConfirmNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }
}