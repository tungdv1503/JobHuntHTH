package com.jobhunthth.HTH0205.Employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Adapter.Jobs_Adapter;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employers_Activity extends AppCompatActivity {

    FirebaseFirestore mStore;
    private ScrollView scrollView;
    private FloatingActionButton flt_Add;
    private RecyclerView list_Jobs;
    private boolean isButtonVisible;
    private String TAG = Employers_Activity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers);

        initUI( );

        showData( );
        initListener( );

    }

    private void initListener() {
        scrollView.getViewTreeObserver( ).addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener( ) {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY( );

                if (scrollY > 0 && !isButtonVisible) {
                    // Hiển thị nút floating button
                    flt_Add.setVisibility(View.VISIBLE);
                    isButtonVisible = true;
                } else if (scrollY == 0 && isButtonVisible) {
                    // Ẩn nút floating button
                    flt_Add.setVisibility(View.GONE);
                    isButtonVisible = false;
                }
            }
        });

        flt_Add.setOnClickListener(view -> {
            Intent intent = new Intent(this, Employers_AddJobs.class);
            startActivity(intent);
        });
    }

    private void showData() {
        ArrayList<JobsAdModel> list = new ArrayList<>(  );

        mStore.collection("JobsAd").whereEqualTo("id_Company","1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Log.e(TAG, "onComplete: "+doc.toObject(JobsAdModel.class).getTitle() );
                                JobsAdModel job = doc.toObject(JobsAdModel.class);
                                list.add(job);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                                    , RecyclerView.VERTICAL, false);
                            list_Jobs.setLayoutManager(layoutManager);
                            Jobs_Adapter adapter = new Jobs_Adapter(list);
                            list_Jobs.setAdapter(adapter);
                        }else {
                            Log.e(TAG, "onComplete: "+task.getException() );
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: "+e );
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume( );
        showData();
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        scrollView = findViewById(R.id.myScrollView);
        flt_Add = findViewById(R.id.flt_Add);
        list_Jobs = findViewById(R.id.list_Jobs);
    }
}