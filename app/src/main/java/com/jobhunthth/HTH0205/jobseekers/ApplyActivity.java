package com.jobhunthth.HTH0205.jobseekers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Employers.AdapterItemView.DetailJobsAd;
import com.jobhunthth.HTH0205.Employers.Interface.AccountInfoCallBack;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Models.ApplicantsModel;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.Models.UserProfileModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.RegisterUserProfile;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.Date;

public class ApplyActivity extends AppCompatActivity {


    private FirebaseFirestore mStore;
    private FirebaseUser mUser;
    private String TAG = DetailJobsAd.class.getName( );
    private Toolbar toolbar;
    private RoundedImageView jobDetail_avatar;
    private Button jobDetail_btn_moreJob, btn_applyJob, btn_unApplyJob, btn_contact;
    private TextView jobDetail_salary, jobDetail_typeOfWork, jobDetail_numberRecruiter,
            jobDetail_address, jobDetail_Title, jobDetail_companyName, jobDetail_exDate, jobDetail_age,
            jobDetail_gender, jobDetail_jobDesc, jobDetail_education;
    private ProgressDialog dialog;
    private String idJobCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        Intent intent = getIntent( );
        JobsAdModel job = (JobsAdModel) intent.getSerializableExtra("job");

        initUI( );
        dialog.show( );
        initListener(job);
        showData(job);
        setSupportActionBar(toolbar);
        getSupportActionBar( ).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar( ).setTitle("Detail job");

    }

    private void initListener(JobsAdModel job) {

        btn_applyJob.setOnClickListener(view -> {
            dialog.show( );
            mStore.collection("UserProfile").document(mUser.getUid( )).get( )
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful( )) {
                                DocumentSnapshot doc = task.getResult( );
                                if (doc.exists( )) {
                                    dialog.dismiss();
                                    UserProfileModel model = doc.toObject(UserProfileModel.class);
                                    if (isProfileValid(model)) {
                                        Log.d(TAG, "onCompleteApply: ");
                                        applyCV(job);
                                    } else {
                                        showAlertDialog();
                                    }
                                }else {
                                    showAlertDialog();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Log.e(TAG, "initListener: " + e);
                    });
        });

        btn_unApplyJob.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ApplyActivity.this);
            builder.setTitle("Bạn có muốn huỷ ứng tuyển không ? ");
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                        mStore.collection("ApplyJobs").document(idJobCurrent).delete( )
                                .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        btn_applyJob.setVisibility(View.VISIBLE);
                                        btn_unApplyJob.setVisibility(View.GONE);
                                        idJobCurrent = null;
                                        dialogInterface.dismiss( );
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener( ) {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    })
                    .setNegativeButton("Cancle", (dialogInterface, i) -> {
                        dialogInterface.dismiss( );
                    });
            builder.create( ).show( );
        });

    }

    private void showAlertDialog() {
        dialog.dismiss();
        Log.d(TAG, "onCompleteFall: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyActivity.this);
        builder.setMessage("Vui lòng thêm các thông tin cho hồ sơ để ứng tuyển");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    Intent intent = new Intent(ApplyActivity.this, RegisterUserProfile.class);
                    startActivity(intent);
                    dialogInterface.dismiss( );
                })
                .setNegativeButton("Cancle", (dialogInterface, i) -> {
                    dialogInterface.dismiss( );
                });
        builder.create( ).show( );
    }

    private boolean isProfileValid(UserProfileModel model) {

        if (model != null && model.getAvatar( ) != null && model.getCv( ) != null && model.getProfession( ) != null && model.getEducation( ) != null
                && model.getMaxSalary( ) != null && model.getMinSalary( ) != null && model.getTypeSalary( ) != null && model.getSkill( ).size( ) > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void applyCV(JobsAdModel job) {
        String idJob = job.getJobId( );
        String idSeeker = mUser.getUid( );
        String idApplicants = mStore.collection("ApplyJobs").document( ).getId( );
        Date date = Calendar.getInstance( ).getTime( );

        ApplicantsModel model = new ApplicantsModel(idJob, idSeeker, idApplicants, 0, date);

        mStore.collection("ApplyJobs").document(idApplicants)
                .set(model)
                .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful( )) {
                            dialog.dismiss( );
                            btn_applyJob.setVisibility(View.GONE);
                            btn_unApplyJob.setVisibility(View.VISIBLE);
                            idJobCurrent = model.getIdApplicants( );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss( );
                    }
                });

    }

    private void showData(JobsAdModel job) {
        if (job.getRole( ).equals("Cá nhân")) {
            getAccountInfo(job.getIdPutJob( ), new AccountInfoCallBack( ) {
                @Override
                public void onSuccess(UserInfoModel info) {
                    dialog.dismiss( );
                    jobDetail_companyName.setText(info.getName( ));
                    Glide.with(ApplyActivity.this).load(info.getAvatar( )).error(R.drawable.avatar).into(jobDetail_avatar);
                    jobDetail_Title.setText(job.getTitle( ));
                    jobDetail_address.setText(job.getAddress( ));
                    jobDetail_age.setText(job.getMinAge( ) + "-" + job.getMaxAge( ) + " tuổi");
                    jobDetail_gender.setText(job.getGender( ));
                    jobDetail_exDate.setText(job.getExDate( ));
                    jobDetail_jobDesc.setText(job.getDesc( ));
                    jobDetail_numberRecruiter.setText(job.getNumber( ));
                    jobDetail_education.setText(job.getEducation( ));
                    jobDetail_salary.setText(job.getMinSalary( ) + "-" + job.getMaxSalary( ) + " " + job.getTypeOfSalary( ) + "/tháng");
                    jobDetail_typeOfWork.setText(job.getTypeOfWork( ));
                }

                @Override
                public void onFailure(Exception e) {
                    dialog.dismiss( );
                }
            });
        } else {
            getCompanyInfo(job.getIdPutJob( ), new CompanyInfoCallBack( ) {
                @Override
                public void onSuccess(CompanyInfoModel company) {
                    dialog.dismiss( );
                    jobDetail_companyName.setText(company.getCompanyName( ));
                    Glide.with(ApplyActivity.this).load(company.getCompanyAvatar( )).error(R.drawable.avatar).into(jobDetail_avatar);
                    jobDetail_Title.setText(job.getTitle( ));
                    jobDetail_address.setText(job.getAddress( ));
                    jobDetail_age.setText(job.getMinAge( ) + "-" + job.getMaxAge( ) + " tuổi");
                    jobDetail_gender.setText(job.getGender( ));
                    jobDetail_exDate.setText(job.getExDate( ));
                    jobDetail_jobDesc.setText(job.getDesc( ));
                    jobDetail_numberRecruiter.setText(job.getNumber( ));
                    jobDetail_education.setText(job.getEducation( ));
                    jobDetail_salary.setText(job.getMinSalary( ) + "-" + job.getMaxSalary( ) + " " + job.getTypeOfSalary( ) + "/tháng");
                    jobDetail_typeOfWork.setText(job.getTypeOfWork( ));
                }

                @Override
                public void onFailure(Exception e) {
                    dialog.dismiss( );
                }
            });
        }

        mStore.collection("ApplyJobs")
                .whereEqualTo("idSeeker", mUser.getUid( ))
                .whereEqualTo("idJob", job.getJobId( ))
                .get( )
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot doc = task.getResult( );
                        if (!doc.isEmpty( ) && doc != null) {
                            for (QueryDocumentSnapshot document : doc) {
                                ApplicantsModel model = document.toObject(ApplicantsModel.class);
                                idJobCurrent = model.getIdApplicants( );
                                dialog.dismiss( );
                                btn_unApplyJob.setVisibility(View.VISIBLE);
                                btn_applyJob.setVisibility(View.GONE);
                            }
                        } else {
                            dialog.dismiss( );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss( );
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
    }

    private void getAccountInfo(String id, AccountInfoCallBack callBack) {
        mStore.collection("UserInfo").document(id).get( )
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            DocumentSnapshot document = task.getResult( );
                            if (document.exists( )) {
                                UserInfoModel info = document.toObject(UserInfoModel.class);
                                callBack.onSuccess(info);
                            }
                        } else {
                            Exception e = task.getException( );
                            if (e != null) {
                                // Log the error or show an error message
                                Log.e("GetCompanyInfo", "Error getting company information: " + e.getMessage( ));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GetCompanyInfo", "Failed to get company information: " + e.getMessage( ));
                    }
                });
    }

    private void getCompanyInfo(String id, CompanyInfoCallBack callBack) {
        mStore.collection("CompanyInfo").document(id).get( )
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            DocumentSnapshot document = task.getResult( );
                            if (document.exists( )) {
                                CompanyInfoModel company = document.toObject(CompanyInfoModel.class);
                                callBack.onSuccess(company);
                            }
                        } else {
                            Exception e = task.getException( );
                            if (e != null) {
                                // Log the error or show an error message
                                Log.e("GetCompanyInfo", "Error getting company information: " + e.getMessage( ));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GetCompanyInfo", "Failed to get company information: " + e.getMessage( ));
                    }
                });
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance( );
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        toolbar = findViewById(R.id.toolbar);
        dialog = new ProgressDialog(ApplyActivity.this);
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
        jobDetail_education = findViewById(R.id.jobDetail_education);
        btn_applyJob = findViewById(R.id.btn_applyJob);
        btn_unApplyJob = findViewById(R.id.btn_unApplyJob);
        btn_contact = findViewById(R.id.btn_contact);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId( )) {
            case android.R.id.home: {
                onBackPressed( );
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}