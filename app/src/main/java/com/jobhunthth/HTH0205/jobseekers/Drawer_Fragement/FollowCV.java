package com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.activity.FollowCVActivity;

public class FollowCV extends Fragment {

    private View view;
    private Button btn_WaitCV,btn_OkCV,btn_RefuseCV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_follow_cv, container, false);

        initUI();
        initListener();

        return view;
    }

    private void initListener() {
        btn_WaitCV.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), FollowCVActivity.class);
            intent.putExtra("key",0);
            startActivity(intent);
        });

        btn_OkCV.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), FollowCVActivity.class);
            intent.putExtra("key",1);
            startActivity(intent);
        });

        btn_RefuseCV.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), FollowCVActivity.class);
            intent.putExtra("key",2);
            startActivity(intent);
        });
    }

    private void initUI() {
        btn_WaitCV = view.findViewById(R.id.btn_WaitCV);
        btn_OkCV = view.findViewById(R.id.btn_OkCV);
        btn_RefuseCV = view.findViewById(R.id.btn_RefuseCV);
    }
}