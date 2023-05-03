package com.jobhunthth.HTH0205.Employers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jobhunthth.HTH0205.Adapter.Jobs_Adapter;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employers_Activity extends AppCompatActivity {

    private ScrollView scrollView;
    private FloatingActionButton flt_Add;
    private RecyclerView list_Jobs;
    private boolean isButtonVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers);

        initUI();

        showData();
        initListener();

    }

    private void initListener() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener( ) {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY( );

                if (scrollY > 0 && !isButtonVisible) {
                    // Hiển thị nút floating button
                    flt_Add.setVisibility(View.VISIBLE);
                    isButtonVisible = true;
                } else if (scrollY == 0 && isButtonVisible) {
                    // Ẩn nút floating button
                    flt_Add.setVisibility(View.GONE);
                    isButtonVisible = false;
                }
            }
        });

        flt_Add.setOnClickListener(view -> {
            Intent intent = new Intent( this, Employers_AddJobs.class );
            startActivity(intent);
        });
    }

    private void showData() {
        Date current = new Date(  );
        List<JobsAdModel> list = new ArrayList<>(  );

        JobsAdModel job1 = new JobsAdModel("1","1","Tuyển nhân viên",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job2 = new JobsAdModel("2","1","Tuyển sinh viên",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job3 = new JobsAdModel("3","1","Tuyển bảo vệ",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job4 = new JobsAdModel("4","1","Tuyển bảo vệ",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job5 = new JobsAdModel("5","1","Tuyển bảo vệ",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job6 = new JobsAdModel("6","1","Tuyển bảo vệ",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job7 = new JobsAdModel("7","1","Tuyển bảo vệ",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");
        JobsAdModel job8 = new JobsAdModel("8","1","Tuyển bảo vệ",
                "Mô tả","Yêu cầu 20 năm kinh nghiệm","Full time",current,"10tr/m");

        if(list.size()<=0){
            list.add(job1);
            list.add(job2);
            list.add(job3);
            list.add(job4);
            list.add(job5);
            list.add(job6);
            list.add(job7);
            list.add(job8);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        list_Jobs.setLayoutManager(layoutManager);
        Jobs_Adapter adapter = new Jobs_Adapter(list);
        list_Jobs.setAdapter(adapter);
    }

    private void initUI() {
        scrollView = findViewById(R.id.myScrollView);
        flt_Add = findViewById(R.id.flt_Add);
        list_Jobs = findViewById(R.id.list_Jobs);
    }
}