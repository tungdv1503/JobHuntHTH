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
import java.util.List;

public class OkapplicationListFragment extends Fragment {

    private View view;
    private Spinner application_spn;
    private RecyclerView applicantsList;
    private FirebaseFirestore mStore;
    private ProgressDialog dialog;
    private ApplicationAdapter adapter;
    List<ApplicantsModel> list;
    private String TAG = OkapplicationListFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_okapplication_list, container, false);

        Intent intent = getActivity().getIntent();
        JobsAdModel model = (JobsAdModel) intent.getSerializableExtra("job");

        initUI();
        showData(model);
        initListener();

        return view;
    }

    private void showData(JobsAdModel model) {
        dialog.show();
        mStore.collection("ApplyJobs")
                .whereEqualTo("idJob",model.getJobId())
                .whereEqualTo("state",1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ApplicantsModel> newList = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        ApplicantsModel model1 = doc.toObject(ApplicantsModel.class);
                        newList.add(model1);
                    }
                    dialog.dismiss();
                    if(!newList.isEmpty()&&newList.size()==queryDocumentSnapshots.size()){
                        list = newList;
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