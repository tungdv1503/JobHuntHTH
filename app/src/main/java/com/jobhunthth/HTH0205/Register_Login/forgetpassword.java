package com.jobhunthth.HTH0205.Register_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.jobhunthth.HTH0205.R;

public class forgetpassword extends AppCompatActivity {
TextInputEditText edt;
Button btn_cancel,btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        edt = findViewById(R.id.email_edt);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_send = findViewById(R.id.btn_send);
        btn_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),Login.class );
            startActivity(intent);
        });
        btn_send.setOnClickListener(view -> {
            String email = edt.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(forgetpassword.this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Gửi email reset thành công
                            showDialog("Gửi thành công", "Vui lòng kiểm tra email để đặt lại mật khẩu.");
                        } else {
                            // Gửi email reset thất bại
                            showDialog("Gửi thất bại", "Địa chỉ email chưa đúng hoặc có lỗi xảy ra.");
                        }
                    });
        });

    }
    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

}