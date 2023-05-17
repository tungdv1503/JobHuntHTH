package com.jobhunthth.HTH0205.Employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;

import java.util.Calendar;
import java.util.Date;

public class Employers_AddJobs extends AppCompatActivity {

    FirebaseFirestore mStore;
    private EditText edt_Title, edt_JobDesc, edt_Jobreq, edt_JobPerk;
    private Spinner spn_Jobtype,spn_JobProfession;
    private Button btn_AddJob;
    private Toolbar jobAd_Toolbar;
    private String TAG = Employers_AddJobs.class.getName();
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers_add_jobs);

        initUI( );
        showSpnJobType( );
        initListener();

        setSupportActionBar(jobAd_Toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add job");

    }

    private void initListener() {
        final String[] jobType = new String[2];
        spnListener(jobType);
        btn_AddJob.setOnClickListener(view -> {
            String Title = edt_Title.getText().toString().trim();
            String Desc = edt_JobDesc.getText().toString().trim();
            String Req = edt_Jobreq.getText().toString().trim();
            String Perk = edt_JobPerk.getText().toString().trim();
            Date currentTime = Calendar.getInstance().getTime();
            if(Title==null || Desc == null || Req==null || Perk == null || jobType[0] == null){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show( );
            }else {
                JobsAdModel job = new JobsAdModel(mUser.getUid(),Title,Desc,Req,jobType[0],jobType[1],currentTime,Perk);
                mStore.collection("JobsAd").add(job)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                   onBackPressed();
                                }else {
                                    Log.e(TAG, "onComplete: "+task.getException() );
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Employers_AddJobs.this, "Fail", Toast.LENGTH_SHORT).show( );
                            }
                        });
            }
        });
    }

    private void spnListener(String[] jobType) {
        spn_Jobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobType[0] = adapterView.getItemAtPosition(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_JobProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobType[1] = adapterView.getItemAtPosition(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showSpnJobType() {
        ArrayAdapter<CharSequence> adapter_jobType = ArrayAdapter.createFromResource(this,
                R.array.spn_JobType, android.R.layout.simple_spinner_item);
        adapter_jobType.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn_Jobtype.setAdapter(adapter_jobType);

        ArrayAdapter<CharSequence> adapter_jobProfession = ArrayAdapter.createFromResource(this,
                R.array.spn_JobProfession, android.R.layout.simple_spinner_item);
        adapter_jobProfession.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn_JobProfession.setAdapter(adapter_jobProfession);
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        edt_Title = findViewById(R.id.edt_Title);
        edt_JobDesc = findViewById(R.id.edt_JobDesc);
        edt_Jobreq = findViewById(R.id.edt_Jobreq);
        edt_JobPerk = findViewById(R.id.edt_JobPerk);
        spn_Jobtype = findViewById(R.id.spn_Jobtype);
        spn_JobProfession = findViewById(R.id.spn_JobProfession);
        btn_AddJob = findViewById(R.id.btn_AddJob);
        jobAd_Toolbar = findViewById(R.id.jobAd_Toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
            }
            default:return super.onOptionsItemSelected(item);
        }
    }
}