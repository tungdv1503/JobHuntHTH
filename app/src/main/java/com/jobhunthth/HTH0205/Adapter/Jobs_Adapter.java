package com.jobhunthth.HTH0205.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.textCompanyName.setText("Hungdz");
        holder.textJobTitle.setText(job.getTitle());
        holder.textJobType.setText(job.getType_Job());
        holder.textPostedTime.setText(formatDate(job.getCreateAt()));
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
