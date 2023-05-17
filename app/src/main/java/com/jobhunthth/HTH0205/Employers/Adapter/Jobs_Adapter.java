package com.jobhunthth.HTH0205.Employers.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.AdapterItemView.DetailJobsAd;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Models.EmployerModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Jobs_Adapter extends RecyclerView.Adapter<Jobs_Adapter.myViewHolder> {

    private List<JobsAdModel> Jobs_list;

    public Jobs_Adapter(List<JobsAdModel> jobs_list) {
        Jobs_list = jobs_list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext( ))
                .inflate(R.layout.adapter_job_item_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        JobsAdModel job = Jobs_list.get(position);
        getCompany(job.getId_Company( ), new CompanyInfoCallBack( ) {
            @Override
            public void onSuccess(EmployerModel company) {
                holder.textCompanyName.setText(company.getName());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        holder.textJobTitle.setText(job.getTitle());
        holder.textJobType.setText(job.getType_Job());
        holder.textPostedTime.setText(formatDate(job.getCreateAt()));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailJobsAd.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("job",job);
            intent.putExtra("bundle",bundle);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    private void getCompany(String id_company, CompanyInfoCallBack callBack) {
        FirebaseFirestore.getInstance().collection("employer").document(id_company).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                EmployerModel company = document.toObject(EmployerModel.class);
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

    private String formatDate(Date createAt) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(createAt);
    }

    @Override
    public int getItemCount() {
        return Jobs_list.size( );
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView textJobTitle;
        public TextView textCompanyName;
        public TextView textJobType;
        public TextView textPostedTime;

        public myViewHolder(View itemView) {
            super(itemView);
            textJobTitle = itemView.findViewById(R.id.text_job_title);
            textCompanyName = itemView.findViewById(R.id.text_company_name);
            textJobType = itemView.findViewById(R.id.text_job_type);
            textPostedTime = itemView.findViewById(R.id.text_posted_time);
        }
    }
}
