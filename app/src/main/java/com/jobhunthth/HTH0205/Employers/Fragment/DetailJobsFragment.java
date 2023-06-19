package com.jobhunthth.HTH0205.Employers.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.AdapterItemView.DetailJobsAd;
import com.jobhunthth.HTH0205.Employers.Interface.AccountInfoCallBack;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class DetailJobsFragment extends Fragment {

    private FirebaseFirestore mStore;
    private ProgressDialog dialog;
    private String TAG = DetailJobsAd.class.getName();
    private RoundedImageView jobDetail_avatar;
    private Button jobDetail_btn_moreJob;
    private TextView jobDetail_salary, jobDetail_typeOfWork, jobDetail_numberRecruiter,
            jobDetail_address,jobDetail_Title, jobDetail_companyName, jobDetail_exDate,jobDetail_age,
            jobDetail_gender, jobDetail_jobDesc,jobDetail_education;


    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_jobs, container, false);
        Intent intent = getActivity( ).getIntent();
        JobsAdModel job = (JobsAdModel) intent.getSerializableExtra("job");
        initUI( );
        initListener();
        dialog.show();
        showData(job);
        return view;
    }

    private void initListener() {

    }

    private void showData(JobsAdModel job) {
        if(job.getRole().equals("Cá nhân")){
            getAccountInfo(job.getIdPutJob( ), new AccountInfoCallBack( ) {
                @Override
                public void onSuccess(UserInfoModel info) {
                    dialog.dismiss();
                    jobDetail_companyName.setText(info.getName());
                    Glide.with(DetailJobsFragment.this).load(info.getAvatar()).error(R.drawable.avatar).into(jobDetail_avatar);
                    jobDetail_Title.setText(job.getTitle());
                    jobDetail_address.setText(job.getAddress());
                    jobDetail_age.setText(job.getMinAge()+"-"+job.getMaxAge()+" tuổi");
                    jobDetail_gender.setText(job.getGender());
                    jobDetail_exDate.setText(job.getExDate());
                    jobDetail_jobDesc.setText(job.getDesc());
                    jobDetail_numberRecruiter.setText(job.getNumber());
                    jobDetail_education.setText(job.getEducation());
                    jobDetail_salary.setText(job.getMinSalary()+"-"+job.getMaxSalary()+" "+job.getTypeOfSalary()+"/tháng");
                    jobDetail_typeOfWork.setText(job.getTypeOfWork());
                }

                @Override
                public void onFailure(Exception e) {
                    dialog.dismiss();
                }
            });
        }
        else {
            getCompanyInfo(job.getIdPutJob( ), new CompanyInfoCallBack( ) {
                @Override
                public void onSuccess(CompanyInfoModel company) {
                    dialog.dismiss();
                    jobDetail_companyName.setText(company.getCompanyName());
                    Glide.with(DetailJobsFragment.this).load(company.getCompanyAvatar()).error(R.drawable.avatar).into(jobDetail_avatar);
                    jobDetail_Title.setText(job.getTitle());
                    jobDetail_address.setText(job.getAddress());
                    jobDetail_age.setText(job.getMinAge()+"-"+job.getMaxAge()+" tuổi");
                    jobDetail_gender.setText(job.getGender());
                    jobDetail_exDate.setText(job.getExDate());
                    jobDetail_jobDesc.setText(job.getDesc());
                    jobDetail_numberRecruiter.setText(job.getNumber());
                    jobDetail_education.setText(job.getEducation());
                    jobDetail_salary.setText(job.getMinSalary()+"-"+job.getMaxSalary()+" "+job.getTypeOfSalary()+"/tháng");
                    jobDetail_typeOfWork.setText(job.getTypeOfWork());
                }

                @Override
                public void onFailure(Exception e) {
                    dialog.dismiss();
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
        dialog = new ProgressDialog(getContext());
        jobDetail_avatar = view.findViewById(R.id.jobDetail_avatar);
        jobDetail_Title = view.findViewById(R.id.jobDetail_Title);
        jobDetail_companyName = view.findViewById(R.id.jobDetail_companyName);
        jobDetail_exDate = view.findViewById(R.id.jobDetail_exDate);
        jobDetail_btn_moreJob = view.findViewById(R.id.jobDetail_btn_moreJob);
        jobDetail_salary = view.findViewById(R.id.jobDetail_salary);
        jobDetail_typeOfWork = view.findViewById(R.id.jobDetail_typeOfWork);
        jobDetail_numberRecruiter = view.findViewById(R.id.jobDetail_numberRecruiter);
        jobDetail_address = view.findViewById(R.id.jobDetail_address);
        jobDetail_age = view.findViewById(R.id.jobDetail_age);
        jobDetail_gender = view.findViewById(R.id.jobDetail_gender);
        jobDetail_jobDesc = view.findViewById(R.id.jobDetail_jobDesc);
        jobDetail_education = view.findViewById(R.id.jobDetail_education);
    }
}