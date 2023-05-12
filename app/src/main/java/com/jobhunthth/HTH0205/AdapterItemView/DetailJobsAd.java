package com.jobhunthth.HTH0205.AdapterItemView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

public class DetailJobsAd extends AppCompatActivity {

    private String TAG = DetailJobsAd.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jobs_ad);

        initUI( );

        showData( );

    }

    private void showData() {
        Intent intent = getIntent( );
        Bundle bundle = intent.getBundleExtra("bundle");
        JobsAdModel job = (JobsAdModel) bundle.getSerializable("job");
    }

    private void initUI() {
    }
}