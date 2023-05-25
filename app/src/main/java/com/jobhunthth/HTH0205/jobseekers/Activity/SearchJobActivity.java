package com.jobhunthth.HTH0205.jobseekers.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Adapter.Jobs_Adapter;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.ArrayList;

public class SearchJobActivity extends AppCompatActivity {
    RecyclerView rcvSearch;
    FirebaseFirestore mStore;
    boolean timThay;
    String chuoiCanTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);
        timThay = false;
        initUI();
        Intent i = getIntent();
        chuoiCanTim = i.getBundleExtra("bundle").getString("chuoicantim");
        showData();

    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        rcvSearch = findViewById(R.id.rv_search);
    }

    private void showData() {

        ArrayList<JobsAdModel> list = new ArrayList<>();

        mStore.collection("JobsAd").whereEqualTo("id_Company", "1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.e(TAG, "onComplete: " + doc.toObject(JobsAdModel.class).getTitle());
                                JobsAdModel job = doc.toObject(JobsAdModel.class);
                                list.add(job);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                                    , RecyclerView.VERTICAL, false);
                            ArrayList<JobsAdModel> listJob = new ArrayList<>();
                            for (JobsAdModel job : list) {
                                // Kiểm tra xem phần tử có chứa chuỗi cần tìm kiếm hay không
                                if (job.getTitle().contains(chuoiCanTim)) {
                                    listJob.add(job);
                                    rcvSearch.setLayoutManager(layoutManager);
                                    Jobs_Adapter adapter = new Jobs_Adapter(listJob);
                                    rcvSearch.setAdapter(adapter);
                                    timThay = true;
                                    break; // Thoát khỏi vòng lặp nếu tìm thấy
                                }
                            }

                        } else {
                            Log.e(TAG, "onComplete: " + task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
    }

}