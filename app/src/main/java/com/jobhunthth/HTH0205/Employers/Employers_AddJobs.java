package com.jobhunthth.HTH0205.Employers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
    private EditText edt_Title, edt_MinSalary, edt_MaxSalary;
    private RadioButton rdo_USD, rdo_VND;
    private TextView edt_JobDesc;
    private Spinner spn_Jobtype, spn_JobProfession;
    private Button btn_AddJob;
    private Toolbar jobAd_Toolbar;
    private String TAG = Employers_AddJobs.class.getName( );
    private FirebaseUser mUser;
    private String Desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers_add_jobs);

        initUI( );
        showSpnJobType( );
        initListener( );

        setSupportActionBar(jobAd_Toolbar);
        getSupportActionBar( ).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar( ).setTitle("Add job");

    }

    private void initListener() {
        final String[] jobType = new String[2];
        spnListener(jobType);

        edt_JobDesc.setOnClickListener(view -> {
            showDescDialog( );
        });

        btn_AddJob.setOnClickListener(view -> {
            String Title = edt_Title.getText( ).toString( ).trim( );
            String typeOfSalary = getTypeOfSalary( );
            String minSalary = edt_MinSalary.getText( ).toString( ).trim( );
            String maxSalary = edt_MaxSalary.getText( ).toString( ).trim( );
            Date currentTime = Calendar.getInstance( ).getTime( );
            if (Title.length( ) == 0 || Desc.length( ) == 0 || jobType[0] == null || typeOfSalary.length( ) == 0 || minSalary.length( ) == 0 || maxSalary.length( ) == 0) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show( );
            } else {
                JobsAdModel job = new JobsAdModel(mUser.getUid( ), Title, Desc, jobType[0], jobType[1], currentTime);
                mStore.collection("JobsAd").add(job)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful( )) {
                                    onBackPressed( );
                                } else {
                                    Log.e(TAG, "onComplete: " + task.getException( ));
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

    private String getTypeOfSalary() {
        if (rdo_VND.isChecked( )) {
            return "VND";
        } else {
            return "USD";
        }
    }

    private void showDescDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm thông tin công việc");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_employer_add_job_description, null);
        builder.setView(dialogView);
        EditText edt_DialogJob = dialogView.findViewById(R.id.edt_DialogJob);
        if (Desc != null) {
            edt_DialogJob.setText(Desc);
        }
        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Desc = edt_DialogJob.getText( ).toString( ).trim( );
                edt_JobDesc.setText(Desc);

                dialogInterface.dismiss( );
            }
        });

        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss( );
            }
        });

        builder.create( ).show( );
    }

    private void spnListener(String[] jobType) {
        spn_Jobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobType[0] = adapterView.getItemAtPosition(i).toString( ).trim( );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_JobProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobType[1] = adapterView.getItemAtPosition(i).toString( ).trim( );
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
        mStore = FirebaseFirestore.getInstance( );
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        edt_Title = findViewById(R.id.edt_Title);
        edt_MinSalary = findViewById(R.id.edt_MinSalary);
        edt_MaxSalary = findViewById(R.id.edt_MaxSalary);
        rdo_USD = findViewById(R.id.rdo_USD);
        rdo_VND = findViewById(R.id.rdo_VND);
        edt_JobDesc = findViewById(R.id.edt_JobDesc);
        spn_Jobtype = findViewById(R.id.spn_Jobtype);
        spn_JobProfession = findViewById(R.id.spn_JobProfession);
        btn_AddJob = findViewById(R.id.btn_AddJob);
        jobAd_Toolbar = findViewById(R.id.jobAd_Toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId( )) {
            case android.R.id.home: {
                onBackPressed( );
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}