package com.jobhunthth.HTH0205.jobseekers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.Adapter.JobAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchJobActivity extends AppCompatActivity {
    RecyclerView listJob;
    private FirebaseFirestore firestore;
    private List<JobsAdModel> jobList;
    private JobAdapter jobAdapter;
    private TextInputEditText edtSearchJob;
    private TextView tvSelectedNameJob, tvSelectedLocationJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job);
        initView( );
        listener( );
        getAllJob( );
    }

    private void initView() {
        listJob = findViewById(R.id.ds_job_search);
        edtSearchJob = findViewById(R.id.edt_search_job);
        tvSelectedNameJob = findViewById(R.id.spin_search_name_job);
        tvSelectedLocationJob = findViewById(R.id.spn_search_location_job);
    }

    private void listener() {

        listJob.setLayoutManager(new LinearLayoutManager(SearchJobActivity.this, LinearLayoutManager.VERTICAL, false));
        jobList = new ArrayList<>( );
        jobAdapter = new JobAdapter(SearchJobActivity.this, jobList);
        listJob.setAdapter(jobAdapter);
        edtSearchJob.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString( ).toLowerCase( );
                getJobSearch(searchText);
            }
        });
        tvSelectedNameJob.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dialogSelectedNameJob( );
            }
        });
        tvSelectedLocationJob.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dialogSelectedLocationJob( );
            }
        });
    }

    private void getAllJob() {
        firestore = FirebaseFirestore.getInstance( );
        firestore.collection("JobsAd").get( )
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        JobsAdModel job = documentSnapshot.toObject(JobsAdModel.class);
                        String role = job.getRole( );
                        String idPutJob = job.getIdPutJob( );
                        String collectionName = role.equals("Cá nhân") ? "UserInfo" : "CompanyInfo";
                        String avatarField = role.equals("Cá nhân") ? "avatar" : "companyAvatar";

                        firestore.collection(collectionName).document(idPutJob)
                                .get( )
                                .addOnSuccessListener(snapshot -> {
                                    if (snapshot.exists( )) {
                                        String avatar = snapshot.getString(avatarField);
                                        job.setAvatar(avatar);
                                        jobList.add(job);
                                        jobAdapter.notifyDataSetChanged( );
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi có lỗi xảy ra
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi có lỗi xảy ra
                });
    }

    private void dialogSelectedLocationJob() {
        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchJobActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_loction);
        ImageButton btnClose = bottomSheetDialog.findViewById(R.id.btn_closeDialog);
        ListView lvLocationJob = bottomSheetDialog.findViewById(R.id.lv_ds_job);
        EditText edtSearch = bottomSheetDialog.findViewById(R.id.edt_search_job);
        String[] items = getResources( ).getStringArray(R.array.spn_vietnam_provinces);
        bottomSheetDialog.setCancelable(false); // Không cho phép hủy dialog khi nhấn nút back
        bottomSheetDialog.setCanceledOnTouchOutside(false); // Không cho phép hủy dialog khi click bên ngoài
        //
        ViewDialogBottom(bottomSheetDialog);
        edtSearch.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString( ).toLowerCase( );
                List<String> location = new ArrayList<>( );

                for (String loca : items) {
                    String name = loca.toLowerCase( );
                    if (name.startsWith(searchText)) {
                        location.add(loca);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchJobActivity.this, android.R.layout.simple_list_item_1, location);
                        lvLocationJob.setAdapter(adapter);
                    }
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvLocationJob.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> itemList = new ArrayList<>(Arrays.asList(items));
                getJobSearch(itemList.get(position));
                bottomSheetDialog.dismiss( );
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss( );
            }
        });
        lvLocationJob.setAdapter(adapter);
        bottomSheetDialog.show( );

    }

    private void getJobSearch(String searchText) {
        List<JobsAdModel> jobList2 = new ArrayList<>( );

        for (JobsAdModel jobs : jobList) {
            String name = jobs.getTitle( ).toLowerCase( );
            if (name.startsWith(searchText)) {
                jobList2.add(jobs);
                JobAdapter adapter = new JobAdapter(SearchJobActivity.this, jobList2);
                listJob.setAdapter(adapter);
            }
        }
    }

    private void dialogSelectedNameJob() {
        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchJobActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_loction);
        ImageButton btnClose = bottomSheetDialog.findViewById(R.id.btn_closeDialog);
        ListView lvNameJob = bottomSheetDialog.findViewById(R.id.lv_ds_job);
        EditText edtSearch = bottomSheetDialog.findViewById(R.id.edt_search_job);
        String[] items = getResources( ).getStringArray(R.array.spn_JobProfession);
        bottomSheetDialog.setCancelable(false); // Không cho phép hủy dialog khi nhấn nút back
        bottomSheetDialog.setCanceledOnTouchOutside(false); // Không cho phép hủy dialog khi click bên ngoài
        //
        ViewDialogBottom(bottomSheetDialog);

        edtSearch.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString( ).toLowerCase( );
                List<String> location = new ArrayList<>( );

                for (String loca : items) {
                    String name = loca.toLowerCase( );
                    if (name.startsWith(searchText)) {
                        location.add(loca);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchJobActivity.this, android.R.layout.simple_list_item_1, location);
                        lvNameJob.setAdapter(adapter);
                    }
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvNameJob.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> itemList = new ArrayList<>(Arrays.asList(items));
                getJobSearch(itemList.get(position));
                bottomSheetDialog.dismiss( );
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss( );
            }
        });
        lvNameJob.setAdapter(adapter);
        bottomSheetDialog.show( );

    }

    private void ViewDialogBottom(BottomSheetDialog bottomSheetDialog) {
        View bottomSheetView = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetView != null) {
            // Lấy chiều cao màn hình
            int screenHeight = getResources( ).getDisplayMetrics( ).heightPixels;

            // Cài đặt chiều cao và chiều rộng của BottomSheetDialog là MATCH_PARENT
            ViewGroup.LayoutParams layoutParams = bottomSheetView.getLayoutParams( );
            layoutParams.height = 3 * screenHeight / 4;
            bottomSheetView.setLayoutParams(layoutParams);

            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheetView);
            behavior.setPeekHeight(layoutParams.height);
        }
    }

}