package com.jobhunthth.HTH0205.Employers.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jobhunthth.HTH0205.Employers.Adapter.ApplicationAdapter;
import com.jobhunthth.HTH0205.Models.ApplicantsModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.ArrayList;

public class UnapplicationListFragment extends Fragment {

    private View view;
    private Spinner application_spn;
    private RecyclerView applicantsList;
    private FirebaseFirestore mStore;
    private ProgressDialog dialog;
    private ApplicationAdapter adapter;
    ArrayList<ApplicantsModel> list;
    private String TAG = UnapplicationListFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unapplication_list, container, false);

        Intent intent = getActivity().getIntent();
        JobsAdModel model = (JobsAdModel) intent.getSerializableExtra("job");

        initUI();
        showData(model);
        initListener();


        return view;
    }

    private void showData(JobsAdModel model) {
        list = new ArrayList<>();
        dialog.show();
        mStore.collection("ApplyJobs")
                .whereEqualTo("idJob",model.getJobId())
                .whereEqualTo("state",2)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        ApplicantsModel model1 = doc.toObject(ApplicantsModel.class);
                        list.add(model1);
                    }
                    if(list.size()==queryDocumentSnapshots.size()){
                        dialog.dismiss();
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        ApplicationAdapter adapter = new ApplicationAdapter(list,getContext());
                        applicantsList.setLayoutManager(layoutManager);
                        applicantsList.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Log.e(TAG, "showData: "+e );
                });
    }

    private void initListener() {

    }

    private void initUI() {
        dialog = new ProgressDialog(getContext());
        list = new ArrayList<>(  );
        adapter = new ApplicationAdapter(list,getContext());
        mStore = FirebaseFirestore.getInstance();
        application_spn = view.findViewById(R.id.application_spn);
        applicantsList = view.findViewById(R.id.applicantsList);
    }

    @Override
    public void onResume() {
        super.onResume( );
        Intent intent = getActivity().getIntent();
        JobsAdModel model = (JobsAdModel) intent.getSerializableExtra("job");
        showData(model);
    }
}