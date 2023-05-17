package com.jobhunthth.HTH0205.Employers.AdapterItemView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Models.EmployerModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

public class DetailJobsAd extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private String TAG = DetailJobsAd.class.getName();
    private Toolbar toolbar;
    private ImageButton backButton;
    private TextView jobTitleTextView,companyNameTextView,
            jobDescriptionTextView,jobRequirementsTextView,jobPerksTextView,jobTypeTextView,job_profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jobs_ad);

        initUI( );

        getDataIntent();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail job");

    }

    private void getDataIntent() {
        Intent intent = getIntent( );
        Bundle bundle = intent.getBundleExtra("bundle");
        JobsAdModel job = (JobsAdModel) bundle.getSerializable("job");
        getCompanyInfo(job.getId_Company( ), new CompanyInfoCallBack( ) {
            @Override
            public void onSuccess(EmployerModel company) {
                companyNameTextView.setText(company.getName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        jobTitleTextView.setText(job.getTitle());
        jobDescriptionTextView.setText(job.getJob_Description());
        jobRequirementsTextView.setText(job.getJob_Requirements());
        jobPerksTextView.setText(job.getJob_Perks());
        jobTypeTextView.setText(job.getType_Job());
        job_profession.setText(job.getJob_Profession());
    }

    private void getCompanyInfo(String id_company, CompanyInfoCallBack callBack) {
        mStore.collection("employer").document(id_company).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                EmployerModel company = document.toObject(EmployerModel.class);
                                callBack.onSuccess(company);
                            }
                        } else {
                            Exception e = task.getException();
                            if (e != null) {
                                // Log the error or show an error message
                                Log.e("GetCompanyInfo", "Error getting company information: " + e.getMessage());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GetCompanyInfo", "Failed to get company information: " + e.getMessage());
                    }
                });
    }
    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        jobTitleTextView = findViewById(R.id.job_title);
        companyNameTextView = findViewById(R.id.company_name);
        jobDescriptionTextView = findViewById(R.id.job_description);
        jobRequirementsTextView = findViewById(R.id.job_requirements);
        jobPerksTextView = findViewById(R.id.job_perk);
        jobTypeTextView = findViewById(R.id.job_type);
        job_profession = findViewById(R.id.job_profession);
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