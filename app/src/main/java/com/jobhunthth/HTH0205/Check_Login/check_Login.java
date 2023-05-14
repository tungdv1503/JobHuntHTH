package com.jobhunthth.HTH0205.Check_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Employers_Activity;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;

import java.util.HashMap;
import java.util.Map;

public class check_Login extends AppCompatActivity {
LinearLayout jobfind,recruter;
    FirebaseFirestore db;
    FirebaseUser currentUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);
        db = FirebaseFirestore.getInstance();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            String currentUid = currentUser.getUid();
        }
        jobfind  = findViewById(R.id.jobsearch);
        recruter = findViewById(R.id.recruters);
        jobfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    String currentUid = currentUser.getUid();
                    Map<String, Object> data = new HashMap<>();
                    data.put("status", "yes");
                    db.collection("jobsearch").document(currentUid).set(data);
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    startActivity(intent);
                    finish();


                }
            }
        });

        recruter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    String currentUid = currentUser.getUid();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Admin_Permission_access", "no");
                    db.collection("recruiter").document(currentUid).set(data);
                    Intent intent = new Intent(getApplicationContext(), Employers_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}