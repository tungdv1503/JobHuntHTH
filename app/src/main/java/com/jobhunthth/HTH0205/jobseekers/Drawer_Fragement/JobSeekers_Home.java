package com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter_doctor;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter_insurance;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter_marketing;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter_sale;
import com.jobhunthth.HTH0205.jobseekers.activity.SearchJobActivity;

import java.util.ArrayList;
import java.util.List;


public class JobSeekers_Home extends Fragment {


    private RecyclerView recyclerView,recyclerViewsale,recyclerViewmarketing,recyclerViewinsurance,recyclerViewdoctor;

    private JobAdapter jobAdapter;
    private JobAdapter_marketing jobAdapter_marketing;
    private JobAdapter_sale jobAdapter_sale;
    private JobAdapter_insurance jobAdapter_insurance;
    private JobAdapter_doctor jobAdapter_doctor;

    private List<JobsAdModel> jobList;
    private List<JobsAdModel> jobList1;
    private List<JobsAdModel> jobList2;
    private List<JobsAdModel> jobList3;
    private List<JobsAdModel> jobList4;
    private FirebaseFirestore firestore;
    private TextView tvSearch;

    public JobSeekers_Home() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_seekers__home, container, false);
        initView(v);
        recyclerView = v.findViewById(R.id.programing_jobs);
        recyclerViewsale = v.findViewById(R.id.sale_jobs);
        recyclerViewmarketing = v.findViewById(R.id.marketing_id);
        recyclerViewinsurance = v.findViewById(R.id.insurance_id);
        recyclerViewdoctor = v.findViewById(R.id.doctor_id);

        jobList = new ArrayList<>();
        jobList1 = new ArrayList<>();
        jobList2 = new ArrayList<>();
        jobList3 = new ArrayList<>();
        jobList4 = new ArrayList<>();

        jobAdapter = new JobAdapter(getActivity(), jobList);
        jobAdapter_marketing = new JobAdapter_marketing(getActivity(), jobList1);
        jobAdapter_sale = new JobAdapter_sale(getActivity(), jobList2);
        jobAdapter_insurance = new JobAdapter_insurance(getActivity(), jobList3);
        jobAdapter_doctor = new JobAdapter_doctor(getActivity(), jobList4);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("JobsAd").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        JobsAdModel job = documentSnapshot.toObject(JobsAdModel.class);
                        String idPutJob = job.getIdPutJob();
                        String profession = documentSnapshot.getString("profession");
                        String collectionName = job.getRole().equals("Cá nhân") ? "UserInfo" : (job.getRole().equals("Công ty") ? "CompanyInfo" : "");
                        String avatarField = job.getRole().equals("Cá nhân") ? "avatar" : (job.getRole().equals("Công ty") ? "companyAvatar" : "");


                        assert profession != null;
                        if (profession.equals("Công nghệ thông tin")) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(jobAdapter);
                            firestore.collection(collectionName).document(idPutJob)
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            String avatar = snapshot.getString(avatarField);
                                            job.setAvatar(avatar);
                                            jobList.add(job);
                                            jobAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý khi có lỗi xảy ra
                                    });
                        } else if (profession.equals("Marketing")) {
                            recyclerViewmarketing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerViewmarketing.setAdapter(jobAdapter_marketing);
                            firestore.collection(collectionName).document(idPutJob)
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            String avatar = snapshot.getString(avatarField);
                                            job.setAvatar(avatar);
                                            jobList1.add(job);
                                            jobAdapter_marketing.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý khi có lỗi xảy ra
                                    });
                        } else if (profession.equals("Kế toán")) {
                            recyclerViewsale.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerViewsale.setAdapter(jobAdapter_sale);
                            firestore.collection(collectionName).document(idPutJob)
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            String avatar = snapshot.getString(avatarField);
                                            job.setAvatar(avatar);
                                            jobList2.add(job);
                                            jobAdapter_sale.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý khi có lỗi xảy ra
                                    });
                        } else if (profession.equals("Bảo hiểm")) {
                            recyclerViewinsurance.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerViewinsurance.setAdapter(jobAdapter_insurance);
                            firestore.collection(collectionName).document(idPutJob)
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            String avatar = snapshot.getString(avatarField);
                                            job.setAvatar(avatar);
                                            jobList3.add(job);
                                            jobAdapter_insurance.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý khi có lỗi xảy ra
                                    });
                        } else if (profession.equals("Bác sĩ")) {
                            recyclerViewdoctor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerViewdoctor.setAdapter(jobAdapter_doctor);
                            firestore.collection(collectionName).document(idPutJob)
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            String avatar = snapshot.getString(avatarField);
                                            job.setAvatar(avatar);
                                            jobList4.add(job);
                                            jobAdapter_doctor.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý khi có lỗi xảy ra
                                    });
                        }


                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra
                });
        listener();
        return v;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void initView(View v) {
        tvSearch = v.findViewById(R.id.edit_search_job);
    }

    private void listener() {

        tvSearch.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity( ), SearchJobActivity.class);
                startActivity(intent);
            }
        });
    }


}

