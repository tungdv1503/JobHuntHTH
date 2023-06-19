package com.jobhunthth.HTH0205.Employers.AdapterItemView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Adapter.DetailJobPagerAdapter;
import com.jobhunthth.HTH0205.Employers.Interface.AccountInfoCallBack;
import com.jobhunthth.HTH0205.Employers.Interface.CompanyInfoCallBack;
import com.jobhunthth.HTH0205.Employers.Employers_EditJob;
import com.jobhunthth.HTH0205.Models.CompanyInfoModel;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class DetailJobsAd extends AppCompatActivity {

   private Toolbar toolbar;
    private JobsAdModel mjob;
    private ProgressDialog dialog;
    private FirebaseFirestore mStore;
    private TabLayout detailJob_tab;
    private ViewPager2 detailJob_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jobs_ad);

        Intent intent = getIntent();
        JobsAdModel job = (JobsAdModel) intent.getSerializableExtra("job");

        mjob = job;

        initUi();

        DetailJobPagerAdapter adapter = new DetailJobPagerAdapter(DetailJobsAd.this,2);
        detailJob_viewPager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(detailJob_tab,detailJob_viewPager
        ,(tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thông tin");
                    break;
                case 1:
                    tab.setText("Hồ sơ");
                    break;
            }
        });
        tabLayoutMediator.attach();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail job");

    }

    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        dialog = new ProgressDialog(DetailJobsAd.this);
        mStore = FirebaseFirestore.getInstance();
        detailJob_tab = findViewById(R.id.detailJob_tab);
        detailJob_viewPager = findViewById(R.id.detailJob_viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
            case R.id.cmpDetail_edit:{
                Intent intent = new Intent( DetailJobsAd.this, Employers_EditJob.class );
                intent.putExtra("job",mjob);
                startActivity(intent);
                return true;
            }

            case R.id.cmpDetail_delete:{
                dialog.show();
                mStore.collection("JobsAd").document(mjob.getJobId()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailJobsAd.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.company_detail_menu,menu);
        return true;
    }
}