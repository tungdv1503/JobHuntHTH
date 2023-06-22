package com.jobhunthth.HTH0205.Employers.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Interface.GetDataCallBack;
import com.jobhunthth.HTH0205.Models.ApplicantsModel;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.Models.UserProfileModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationHolder> {

    private List<ApplicantsModel> list;
    private Context context;
    private FirebaseFirestore mStore;
    private String TAG = ApplicationAdapter.class.getName( );

    public ApplicationAdapter(List<ApplicantsModel> list, Context context) {
        this.list = list;
        this.context = context;
        mStore = FirebaseFirestore.getInstance( );
    }

    @NonNull
    @Override
    public ApplicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.application_adapter_item, parent, false);
        return new ApplicationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationHolder holder, int position) {
        ApplicantsModel model = list.get(position);
        holder.cardView.setVisibility(View.GONE);
        if (model.getState() == 1) {
            holder.btnAccept.setVisibility(View.GONE);
        } else if (model.getState() == 2) {
            holder.btnRefuse.setVisibility(View.GONE);
        }
        getData(model, new GetDataCallBack() {
            @Override
            public void onGetInfoProfileSuccess(UserProfileModel profile, UserInfoModel info) {
                if (profile != null && info != null) {
                    holder.cardView.setVisibility(View.VISIBLE);
                    Glide.with(context).load(info.getAvatar()).error(R.drawable.avatar).into(holder.imageAvatar);
                    holder.textName.setText(info.getName());
                    holder.textGender.setText(info.getGender());
                    holder.textAddress.setText(info.getAddress());
                    holder.textSalary.setText(profile.getSalary());
                    holder.textAge.setText(String.valueOf(info.getAge()));
                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });

        holder.btnAccept.setOnClickListener(view -> {
            mStore.collection("ApplyJobs").document(model.getIdApplicants( )).update("state", 1)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "onBindViewHolder: "+e );
                    });
        });

        holder.btnRefuse.setOnClickListener(view -> {
            mStore.collection("ApplyJobs").document(model.getIdApplicants( )).update("state", 2)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "onBindViewHolder: "+e );
                    });
        });
    }

    @Override
    public int getItemCount() {
        return list.size( );
    }

    private void getData(ApplicantsModel model, GetDataCallBack callBack) {

        mStore.collection("UserInfo").document(model.getIdSeeker()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            UserInfoModel infoModel = doc.toObject(UserInfoModel.class);
                            Log.d(TAG, "getData: " + infoModel.getName());
                            mStore.collection("UserProfile").document(model.getIdSeeker()).get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            DocumentSnapshot doc2 = task2.getResult();
                                            if (doc2.exists()) {
                                                UserProfileModel profileModel = doc2.toObject(UserProfileModel.class);
                                                callBack.onGetInfoProfileSuccess(profileModel, infoModel);
                                            }
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(e -> {

                });
    }

    class ApplicationHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageAvatar;
        private TextView textAddress, textSalary, textGender, textAge, textName;
        private Button btnAccept, btnRefuse, btnCV;
        private CardView cardView;

        public ApplicationHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.image_avatar);
            textName = itemView.findViewById(R.id.text_name);
            textAge = itemView.findViewById(R.id.text_age);
            textGender = itemView.findViewById(R.id.text_gender);
            textAddress = itemView.findViewById(R.id.text_address);
            textSalary = itemView.findViewById(R.id.text_salary);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnRefuse = itemView.findViewById(R.id.btn_refuse);
            btnCV = itemView.findViewById(R.id.btn_cv);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
