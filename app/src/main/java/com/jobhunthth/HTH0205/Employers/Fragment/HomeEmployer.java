package com.jobhunthth.HTH0205.Employers.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jobhunthth.HTH0205.Employers.Adapter.Jobs_Adapter;
import com.jobhunthth.HTH0205.Employers.Employers_AddJobs;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.ArrayList;

public class HomeEmployer extends Fragment {

    private ScrollView scrollView;
    private FloatingActionButton flt_Add;
    private RecyclerView list_Jobs;
    private boolean isButtonVisible;
    private FirebaseUser mUser;
    private FirebaseFirestore mStore;
    private View view;
    private String TAG = HomeEmployer.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_employer, container, false);

        initUI();
        showData();
        initListener();
        
        return view;
    }

    private void showData() {
        ArrayList<JobsAdModel> list = new ArrayList<>();

        mStore.collection("JobsAd")
                .whereEqualTo("idPutJob", mUser.getUid())
                .orderBy("currentTime", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            JobsAdModel job = doc.toObject(JobsAdModel.class);
                            list.add(job);
                        }

                        // Kiểm tra xem đã lấy xong tất cả dữ liệu chưa
                        if (list.size() == querySnapshot.size()) {
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            Jobs_Adapter adapter = new Jobs_Adapter(list);
                            list_Jobs.setLayoutManager(layoutManager);
                            list_Jobs.setAdapter(adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: "+e );
                    }
                });

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
            Intent intent = new Intent(getContext(), Employers_AddJobs.class);
            startActivity(intent);
        });

    }

    private void initUI() {
        scrollView = view.findViewById(R.id.myScrollView);
        flt_Add = view.findViewById(R.id.flt_Add);
        list_Jobs = view.findViewById(R.id.list_Jobs);
        mStore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
    }

    @Override
    public void onResume() {
        super.onResume( );
        showData();
    }
}