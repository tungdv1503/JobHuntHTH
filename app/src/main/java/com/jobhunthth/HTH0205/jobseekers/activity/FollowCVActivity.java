package com.jobhunthth.HTH0205.jobseekers.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Models.ApplicantsModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowCVActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txt_title;
    private Spinner spn_sort;
    private RecyclerView applicantsList;
    private FirebaseFirestore mStore;
    private FirebaseUser mUser;
    private ProgressDialog dialog;
    private Query query;
    private String TAG = FollowCVActivity.class.getName( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_cv_activity);

        Intent intent = getIntent( );
        int key = intent.getIntExtra("key", -1);

        initUI( );
        setSupportActionBar(toolbar);
        getSupportActionBar( ).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar( ).setTitle(null);
        setTxtTitle(key);
        getListJobId(key, -1);
        initListener( );

    }

    private void initListener() {
        spn_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        Intent intent = getIntent( );
                        int key = intent.getIntExtra("key", -1);
                        getListJobId(key, 0);
                        Log.e(TAG, "onItemSelected: " + key + "/" + i);
                        break;
                    }
                    case 1: {
                        Intent intent = getIntent( );
                        int key = intent.getIntExtra("key", -1);
                        getListJobId(key, 1);
                        Log.e(TAG, "onItemSelected: " + key + "/" + i);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getListJobId(int key, int sort) {
        dialog.show( );

        mStore.collection("ApplyJobs")
                .whereEqualTo("idSeeker", mUser.getUid( ))
                .whereEqualTo("state", key)
                .get( )
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>( ) {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> listId = new ArrayList<>( );
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            ApplicantsModel model = doc.toObject(ApplicantsModel.class);
                            listId.add(model.getIdJob( ));
                        }
                        if (listId.size( ) == queryDocumentSnapshots.size( )) {
                            showData(listId,sort);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
    }

    private void showData(List<String> listId, int sort) {

        if (listId.isEmpty( )) {
            dialog.dismiss( );
            return;
        }

        switch (sort){
            case 0:{
                query = mStore.collection("JobsAd")
                        .whereIn("jobId", listId)
                        .orderBy("currentTime", Query.Direction.ASCENDING);
                break;
            }
            case 1:{
                query = mStore.collection("JobsAd")
                        .whereIn("jobId", listId)
                        .orderBy("currentTime", Query.Direction.DESCENDING);
                break;
            }
            default:query = mStore.collection("JobsAd")
                    .whereIn("jobId", listId);
        }


                query.get( )
                .addOnSuccessListener(querySnapshot -> {
                    List<JobsAdModel> jobList = new ArrayList<>( );
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        JobsAdModel jobModel = documentSnapshot.toObject(JobsAdModel.class);
                        jobList.add(jobModel);
                    }
                    dialog.dismiss( );
                    if (jobList.size( ) == querySnapshot.size( )) {
                        LinearLayoutManager manager = new LinearLayoutManager(FollowCVActivity.this,
                                LinearLayoutManager.VERTICAL, false);
                        JobAdapter adapter = new JobAdapter(FollowCVActivity.this, jobList);
                        applicantsList.setLayoutManager(manager);
                        applicantsList.setAdapter(adapter);
                    }

                    dialog.dismiss( );
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi lấy thông tin công việc
                    dialog.dismiss( );
                    Log.e(TAG, "showData: " + e);
                });
    }


    private void setTxtTitle(int key) {
        switch (key) {
            case 0: {
                txt_title.setText("Hồ sơ đang ứng tuyển");
                break;
            }
            case 1: {
                txt_title.setText("Hồ sơ được chấp nhận");
                break;
            }
            case 2: {
                txt_title.setText("Hồ sơ không chấp nhận");
                break;
            }
        }
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance( );
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        dialog = new ProgressDialog(FollowCVActivity.this);
        toolbar = findViewById(R.id.toolbar);
        txt_title = findViewById(R.id.txt_title);
        spn_sort = findViewById(R.id.spn_sort);
        applicantsList = findViewById(R.id.applicantsList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId( )) {
            case android.R.id.home: {
                onBackPressed( );
                finish( );
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume( );
    }
}