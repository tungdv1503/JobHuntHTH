package com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobSeekers_Home extends Fragment {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<JobsAdModel> jobList;
    private FirebaseFirestore firestore;
    private TextInputLayout tipSearch;
    public JobSeekers_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_seekers__home, container, false);
        initView(v);
        recyclerView = v.findViewById(R.id.programing_jobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(), jobList);
        recyclerView.setAdapter(jobAdapter);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("JobsAd").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        JobsAdModel job = documentSnapshot.toObject(JobsAdModel.class);
                        String role = job.getRole();
                        String idPutJob = job.getIdPutJob();
                        String collectionName = role.equals("Cá nhân") ? "UserInfo" : "CompanyInfo";
                        String avatarField = role.equals("Cá nhân") ? "avatar" : "companyAvatar";

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
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra
                });
        listener();
        return v;
    }
    private void initView(View v){
        tipSearch = v.findViewById(R.id.tiplayout_search_home);

    }
    private void listener(){
        tipSearch.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

            }
        });
    }



}
