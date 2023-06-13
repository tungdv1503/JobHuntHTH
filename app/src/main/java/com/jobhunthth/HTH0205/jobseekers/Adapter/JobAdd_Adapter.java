package com.jobhunthth.HTH0205.jobseekers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.ArrayList;

public class JobAdd_Adapter extends RecyclerView.Adapter<JobAdd_Adapter.ViewHolder> {
    Context context;
    ArrayList<JobsAdModel> list;

    public JobAdd_Adapter(Context context, ArrayList<JobsAdModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext( ))
                .inflate(R.layout.jobad_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobsAdModel jobsAdModel = list.get(position);
        holder.tvMinSalaryJob.setText(jobsAdModel.getMinSalary());
        holder.tvMaxSalaryJob.setText(jobsAdModel.getMaxSalary());
        holder.tvNameJob.setText(jobsAdModel.getTitle());
        holder.tvViewJob.setText(jobsAdModel.getView());
        holder.tvLocationJob.setText(jobsAdModel.getArea());
        holder.tvLocationDetailJob.setText(jobsAdModel.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMinSalaryJob,tvMaxSalaryJob,tvLocationJob,tvLocationDetailJob,tvNameJob,tvViewJob,tvApplyJob;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApplyJob=itemView.findViewById(R.id.tv_apply_job);
            tvMinSalaryJob=itemView.findViewById(R.id.tv_minsalary_job);
            tvMaxSalaryJob=itemView.findViewById(R.id.tv_maxsalary_job);
            tvLocationDetailJob=itemView.findViewById(R.id.tv_location_detail_job);
            tvLocationJob=itemView.findViewById(R.id.tv_location_job);
            tvNameJob=itemView.findViewById(R.id.tv_name_job);
            tvViewJob=itemView.findViewById(R.id.tv_view_job);
        }
    }
}
