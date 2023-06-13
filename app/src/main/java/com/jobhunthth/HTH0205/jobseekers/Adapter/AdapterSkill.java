package com.jobhunthth.HTH0205.jobseekers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jobhunthth.HTH0205.R;

import java.util.List;

public class AdapterSkill extends RecyclerView.Adapter<AdapterSkill.myAdapterSkill>{

    private List<String> skills;
    private Context context;

    public AdapterSkill(Context context, List<String> skills) {
        this.context = context;
        this.skills = skills;
    }

    @NonNull
    @Override
    public myAdapterSkill onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_skill, parent, false);
        return new myAdapterSkill(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapterSkill holder, int position) {
        String item = skills.get(position);

        holder.tvSkill.setText(item);
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    static class myAdapterSkill extends RecyclerView.ViewHolder {
        TextView tvSkill;
        public myAdapterSkill(@NonNull View itemView) {
            super(itemView);
            tvSkill = itemView.findViewById(R.id.tv_skill);
        }
    }
}

