package com.jobhunthth.HTH0205.jobseekers.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.R;

public class FollowCVActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txt_title;
    private Spinner spn_sort;
    private RecyclerView applicantsList;
    private FirebaseFirestore mStore;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_cv_activity);

        Intent intent = getIntent();
        int key = intent.getIntExtra("key",-1);

        initUI();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        setTxtTitle(key);
        showData(key);

    }

    private void showData(int key) {

    }

    private void setTxtTitle(int key) {
        switch (key){
            case 0:{
                txt_title.setText("Hồ sơ đang ứng tuyển");
                break;
            }
            case 1:{
                txt_title.setText("Hồ sơ được chấp nhận");
                break;
            }
            case 2:{
                txt_title.setText("Hồ sơ không chấp nhận");
                break;
            }
        }
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        toolbar = findViewById(R.id.toolbar);
        txt_title = findViewById(R.id.txt_title);
        spn_sort = findViewById(R.id.spn_sort);
        applicantsList = findViewById(R.id.applicantsList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
            }

            default:return super.onOptionsItemSelected(item);
        }
    }
}