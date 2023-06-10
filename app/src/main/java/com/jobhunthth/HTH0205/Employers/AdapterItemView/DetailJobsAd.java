package com.jobhunthth.HTH0205.Employers.AdapterItemView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Interface.AccountInfoCallBack;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Employers_EditJob;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class DetailJobsAd extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private String TAG = DetailJobsAd.class.getName();
    private Toolbar toolbar;
    private RoundedImageView jobDetail_avatar;
    private Button jobDetail_btn_moreJob;
    private TextView jobDetail_salary, jobDetail_typeOfWork, jobDetail_numberRecruiter,
            jobDetail_address,jobDetail_Title, jobDetail_companyName, jobDetail_exDate,jobDetail_age,
            jobDetail_gender, jobDetail_jobDesc;
    private ProgressDialog dialog;

    private JobsAdModel mjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jobs_ad);

        Intent intent = getIntent( );
        JobsAdModel job = (JobsAdModel) intent.getSerializableExtra("job");
        int pos = intent.getIntExtra("pos",-1);

        mjob = job;

        initUI( );
        initListener();
        showData(job);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail job");

    }

    private void initListener() {

    }

    private void showData(JobsAdModel job) {
        if(job.getRole().equals("Cá nhân")){
            getAccountInfo(job.getIdPutJob( ), new AccountInfoCallBack( ) {
                @Override
                public void onSuccess(UserInfoModel info) {
                    jobDetail_companyName.setText(info.getName());
                    Glide.with(DetailJobsAd.this).load(info.getAvatar()).error(R.drawable.avatar).into(jobDetail_avatar);
                    jobDetail_Title.setText(job.getTitle());
                    jobDetail_address.setText(job.getAddress());
                    jobDetail_age.setText(job.getMinAge()+"-"+job.getMaxAge()+" tuổi");
                    jobDetail_gender.setText(job.getGender());
                    jobDetail_exDate.setText(job.getExDate());
                    jobDetail_jobDesc.setText(job.getDesc());
                    jobDetail_numberRecruiter.setText(job.getNumber());
                    jobDetail_salary.setText(job.getMinSalary()+"-"+job.getMaxSalary()+" "+job.getTypeOfSalary()+"/tháng");
                    jobDetail_typeOfWork.setText(job.getTypeOfWork());
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
        else {
            getCompanyInfo(job.getIdPutJob( ), new CompanyInfoCallBack( ) {
                @Override
                public void onSuccess(CompanyInfoModel company) {
                    jobDetail_companyName.setText(company.getCompanyName());
                    Glide.with(DetailJobsAd.this).load(company.getCompanyAvatar()).error(R.drawable.avatar).into(jobDetail_avatar);
                    jobDetail_Title.setText(job.getTitle());
                    jobDetail_address.setText(job.getAddress());
                    jobDetail_age.setText(job.getMinAge()+"-"+job.getMaxAge()+" tuổi");
                    jobDetail_gender.setText(job.getGender());
                    jobDetail_exDate.setText(job.getExDate());
                    jobDetail_jobDesc.setText(job.getDesc());
                    jobDetail_numberRecruiter.setText(job.getNumber());
                    jobDetail_salary.setText(job.getMinSalary()+"-"+job.getMaxSalary()+" "+job.getTypeOfSalary()+"/tháng");
                    jobDetail_typeOfWork.setText(job.getTypeOfWork());
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }

    private void getAccountInfo(String id, AccountInfoCallBack callBack) {
        mStore.collection("UserInfo").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserInfoModel info = document.toObject(UserInfoModel.class);
                                callBack.onSuccess(info);
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

    private void getCompanyInfo(String id, CompanyInfoCallBack callBack) {
        mStore.collection("CompanyInfo").document( id ).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                CompanyInfoModel company = document.toObject(CompanyInfoModel.class);
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
        dialog = new ProgressDialog(DetailJobsAd.this);
        jobDetail_avatar = findViewById(R.id.jobDetail_avatar);
        jobDetail_Title = findViewById(R.id.jobDetail_Title);
        jobDetail_companyName = findViewById(R.id.jobDetail_companyName);
        jobDetail_exDate = findViewById(R.id.jobDetail_exDate);
        jobDetail_btn_moreJob = findViewById(R.id.jobDetail_btn_moreJob);
        jobDetail_salary = findViewById(R.id.jobDetail_salary);
        jobDetail_typeOfWork = findViewById(R.id.jobDetail_typeOfWork);
        jobDetail_numberRecruiter = findViewById(R.id.jobDetail_numberRecruiter);
        jobDetail_address = findViewById(R.id.jobDetail_address);
        jobDetail_age = findViewById(R.id.jobDetail_age);
        jobDetail_gender = findViewById(R.id.jobDetail_gender);
        jobDetail_jobDesc = findViewById(R.id.jobDetail_jobDesc);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
            case R.id.cmpDetail_edit:{
                Intent intent = new Intent( DetailJobsAd.this, Employers_EditJob.class );
                intent.putExtra("job",mjob);
                startActivity(intent);
                return true;
            }

            case R.id.cmpDetail_delete:{
                dialog.show();
                mStore.collection("JobsAd").document(mjob.getJobId()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailJobsAd.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.company_detail_menu,menu);
        return true;
    }
}