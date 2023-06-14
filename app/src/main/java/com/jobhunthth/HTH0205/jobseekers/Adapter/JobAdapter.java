package com.jobhunthth.HTH0205.jobseekers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.AdapterItemView.DetailJobsAd;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;
import com.jobhunthth.HTH0205.jobseekers.ApplyActivity;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private List<JobsAdModel> jobList4;
    private FirebaseFirestore mStore;
    public JobAdapter(Context context, List<JobsAdModel> jobList) {
        this.context = context;
        this.jobList4 = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item, parent, false);
        mStore = FirebaseFirestore.getInstance();
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobsAdModel job = jobList4.get(position);

        holder.tvLocation.setText(job.getAddress());
        Glide.with(context)
                .load(job.getAvatar())
                .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                .into(holder.img);
        holder.tvName.setText(job.getTitle());
        holder.tvViewCount.setText(String.valueOf(job.getView()));
//        holder.tvApplyCount.setText(String.valueOf(job.get()));
        holder.tvMinSalary.setText(job.getMinSalary());
        holder.tvMaxSalary.setText(job.getMaxSalary());

        holder.itemView.setOnClickListener(view -> {
            mStore.collection("JobsAd").document( job.getJobId() ).update("view", FieldValue.increment(1));
            Intent intent = new Intent( holder.itemView.getContext(), ApplyActivity.class );
            intent.putExtra("job",job);
            intent.putExtra("pos",position);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList4.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation, tvName, tvViewCount, tvApplyCount, tvMinSalary, tvMaxSalary;
        CardView cardView;
        ImageView img;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tv_location_job);
            tvName = itemView.findViewById(R.id.tv_name_job);
            tvViewCount = itemView.findViewById(R.id.tv_view_job);
            tvApplyCount = itemView.findViewById(R.id.tv_apply_job);
            img = itemView.findViewById(R.id.company_img);
            tvMinSalary = itemView.findViewById(R.id.tv_minsalary_job);
            tvMaxSalary = itemView.findViewById(R.id.tv_maxsalary_job);

        }
    }
}
