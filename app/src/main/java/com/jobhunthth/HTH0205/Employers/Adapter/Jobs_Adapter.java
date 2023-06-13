package com.jobhunthth.HTH0205.Employers.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.AdapterItemView.DetailJobsAd;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Employers.Employers_EditJob;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Jobs_Adapter extends RecyclerView.Adapter<Jobs_Adapter.myViewHolder> {

    private List<JobsAdModel> Jobs_list;

    private ProgressDialog dialog;
    private FirebaseFirestore mStore;

    public Jobs_Adapter(List<JobsAdModel> jobs_list) {
        Jobs_list = jobs_list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext( ))
                .inflate(R.layout.adapter_job_item_view, parent, false);
        dialog = new ProgressDialog(view.getContext());
        mStore = FirebaseFirestore.getInstance();
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        JobsAdModel job = Jobs_list.get(position);

        getCompany(job.getIdPutJob( ), new CompanyInfoCallBack( ) {
            @Override
            public void onSuccess(CompanyInfoModel company) {
                holder.textJobTitle.setText(job.getTitle());
                holder.textArea.setText(job.getArea());
                holder.textJobType.setText(job.getTypeOfWork());
                holder.text_exDate.setText(job.getExDate());
                holder.textMinSalary.setText(job.getMinSalary());
                holder.textMaxSalary.setText(job.getMaxSalary());
                holder.text_typeOfSalary.setText(job.getTypeOfSalary());
                holder.textView.setText(job.getView()+" lượt xem");
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), view);
                popupMenu.inflate(R.menu.item_click_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_popup_edit:
                                Intent intent = new Intent( holder.itemView.getContext(), Employers_EditJob.class );
                                intent.putExtra("job",job);
                                holder.itemView.getContext().startActivity(intent);
                                return true;
                            case R.id.menu_popup_delete:
                                dialog.show();
                                mStore.collection("JobsAd").document(job.getJobId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                int position = holder.getAdapterPosition();
                                                Jobs_list.remove(position);
                                                notifyItemRemoved(position);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(holder.itemView.getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            }
        });

        holder.itemView.setOnClickListener(view -> {
            mStore.collection("JobsAd").document( job.getJobId() ).update("view", FieldValue.increment(1));
            Intent intent = new Intent( holder.itemView.getContext(), DetailJobsAd.class );
            intent.putExtra("job",job);
            intent.putExtra("pos",position);
            holder.itemView.getContext().startActivity(intent);
        });

    }

    private void getCompany(String id_company, CompanyInfoCallBack callBack) {
        FirebaseFirestore.getInstance().collection("CompanyInfo").document(id_company).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                CompanyInfoModel company = document.toObject(CompanyInfoModel.class);
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

    private String formatTimeAgo(Date createdAt) {
        long timeInMillis = createdAt.getTime();
        long currentTimeMillis = System.currentTimeMillis();
        long timeDiff = currentTimeMillis - timeInMillis;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (days >= 30) {
            long months = days / 30;
            return months + " tháng trước";
        } else if (days >= 1) {
            return days + " ngày trước";
        } else if (hours >= 1) {
            return hours + " giờ trước";
        } else if (minutes >= 1) {
            return minutes + " phút trước";
        } else {
            return "Vài giây trước";
        }
    }

    @Override
    public int getItemCount() {
        return Jobs_list.size( );
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        private TextView textJobTitle;
        private TextView textArea;
        private TextView text_exDate;
        private TextView textJobType;
        private TextView textMinSalary;
        private TextView textMaxSalary;
        private TextView textView;
        private TextView textCV;
        private TextView textApplied;
        private TextView text_typeOfSalary;

        public myViewHolder(View itemView) {
            super(itemView);
            textJobTitle = itemView.findViewById(R.id.text_job_title);
            textArea = itemView.findViewById(R.id.text_area);
            text_exDate = itemView.findViewById(R.id.text_exDate);
            textJobType = itemView.findViewById(R.id.text_job_type);
            textMinSalary = itemView.findViewById(R.id.text_minSalary);
            textMaxSalary = itemView.findViewById(R.id.text_maxSalary);
            textView = itemView.findViewById(R.id.text_view);
            textCV = itemView.findViewById(R.id.text_cv);
            textApplied = itemView.findViewById(R.id.text_applied);
            text_typeOfSalary = itemView.findViewById(R.id.text_typeOfSalary);
        }
    }

}
