package com.jobhunthth.HTH0205.Employers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Models.JobsAdModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.RegisterEmployerInfo;
import com.jobhunthth.HTH0205.Register_Login.RegisterInfo;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Employers_AddJobs extends AppCompatActivity {

    FirebaseFirestore mStore;
    private TextInputEditText edt_Title, edt_MinSalary, edt_MaxSalary,edt_countRecruit,edt_addressWork,edt_MinAge,edt_MaxAge,edt_exDate;
    private TextInputLayout til_Date;
    private RadioButton rdo_USD, rdo_VND;
    private RadioGroup rdog_Gender,rdog_Role;
    private TextView edt_JobDesc;
    private Spinner spn_Jobtype, spn_JobProfession,spn_area,spn_EducationLevel;
    private Button btn_AddJob;
    private Toolbar jobAd_Toolbar;
    private String TAG = Employers_AddJobs.class.getName( );
    private FirebaseUser mUser;
    private ProgressDialog dialog;
    String[] jobType = new String[2];
    String[] area = new String[1];
    String[] education = new String[1];
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers_add_jobs);

        initUI( );
        initListener( );

        setSupportActionBar(jobAd_Toolbar);
        getSupportActionBar( ).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar( ).setTitle("Add job");

    }

    private void initListener() {
        spnListener();

        btn_AddJob.setOnClickListener(view -> {
            checkRdo();
        });


        Calendar selectedDate = Calendar.getInstance();
        edt_exDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Employers_AddJobs.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // Lưu ngày đã chọn vào biến toàn cục
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        // Gán giá trị vào EditText
                        String selectedDateStr = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        edt_exDate.setText(selectedDateStr);

                        til_Date.setError(null);
                    }
                }, year, month, day);

                // Hiển thị dialog chọn ngày
                datePickerDialog.show();
            }
        });
    }

    private void checkRdo() {
        int id = rdog_Role.getCheckedRadioButtonId();
        RadioButton rdo = findViewById(id);
        String text = rdo.getText().toString().trim();
        Log.d(TAG, "checkRdo: "+text );
        switch (text){
            case "Cá nhân":{
                mStore.collection("UserInfo").document(mUser.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult( );
                                    if(doc.exists()){
                                        validateData();
                                    }else {
                                        showAlertDialog(1);
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: "+e );
                            }
                        });
                break;
            }
            case "Công ty":{
                mStore.collection("CompanyInfo").document(mUser.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult( );
                                    if(doc.exists()){
                                        validateData();
                                    }else {
                                        showAlertDialog(2);
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: "+e );
                            }
                        });
                break;
            }
        }
    }

    private void showAlertDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa đăng ký thông tin, bạn có muốn đăng ký luôn không ?");

        switch (i){
            case 1:{
                mIntent = new Intent( Employers_AddJobs.this, RegisterInfo.class );
                mIntent.putExtra("type",1);
                break;
            }
            case 2:{
                mIntent = new Intent( Employers_AddJobs.this, RegisterEmployerInfo.class );
                mIntent.putExtra("type",1);
                break;
            }
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(mIntent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addJob(){
        String Title = edt_Title.getText( ).toString( ).trim( );
        String number = edt_countRecruit.getText().toString().trim();
        String address = edt_addressWork.getText().toString().trim();
        String minSalary = edt_MinSalary.getText( ).toString( ).trim( );
        String maxSalary = edt_MaxSalary.getText( ).toString( ).trim( );
        String Desc = edt_JobDesc.getText().toString().trim();
        String minAge = edt_MinAge.getText().toString().trim();
        String maxAge = edt_MaxAge.getText().toString().trim();
        String exDate = edt_exDate.getText().toString().trim();
        String gender = getGender();
        String typeOfSalary = getTypeOfSalary( );
        String role = getRole();
        Date currentTime = Calendar.getInstance( ).getTime( );
        dialog.show();
        String jobId = mStore.collection("JobsAd").document().getId();

        JobsAdModel jobAd = new JobsAdModel(Title, number, address, gender, minAge, maxAge,
                typeOfSalary, minSalary, maxSalary, Desc, currentTime,role,mUser.getUid()
                ,area[0],jobType[1],jobType[0],jobId,exDate,education[0],0);
        mStore.collection("JobsAd").document( jobId ).set(jobAd)
                .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful( )) {
                            dialog.dismiss();
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

    private String getRole() {
        int selectedId = rdog_Role.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        String selectedRole = "";
        if (radioButton != null) {
            selectedRole = radioButton.getText().toString();
        }
        Log.e(TAG, "getRole: "+selectedRole );
        return selectedRole;
    }

    private void validateData() {
        String title = edt_Title.getText().toString().trim();
        String number = edt_countRecruit.getText().toString().trim();
        String address = edt_addressWork.getText().toString().trim();
        String minSalary = edt_MinSalary.getText().toString().trim();
        String maxSalary = edt_MaxSalary.getText().toString().trim();
        String desc = edt_JobDesc.getText().toString().trim();
        String minAge = edt_MinAge.getText().toString().trim();
        String maxAge = edt_MaxAge.getText().toString().trim();
        String exDate = edt_exDate.getText().toString().trim();
        String typeSalary = getTypeOfSalary();

        boolean isValidate = true;

        if (title.isEmpty()) {
            edt_Title.setError("Vui lòng nhập tiêu đề");
            isValidate = false;
        }

        if (number.isEmpty()) {
            edt_countRecruit.setError("Vui lòng nhập số lượng");
            isValidate = false;
        }else if(Long.parseLong(number)<=0){
            edt_countRecruit.setError("Vui lòng nhập số lượng phải lớn hơn 0");
            isValidate = false;
        }

        if (address.isEmpty()) {
            edt_addressWork.setError("Vui lòng nhập địa chỉ");
            isValidate = false;
        }

        if (minSalary.isEmpty()) {
            edt_MinSalary.setError("Vui lòng nhập lương thấp nhất");
            isValidate = false;
        }else if(Long.parseLong(minSalary)<=1000 && typeSalary.equals("VND")){
            edt_MinSalary.setError("Vui lòng nhập lương thấp nhất phải từ 1.000VND");
            isValidate = false;
        }else if(Long.parseLong(minSalary)<=1 && typeSalary.equals("USD")){
            edt_MinSalary.setError("Vui lòng nhập lương thấp nhất phải từ 1USD");
            isValidate = false;
        }else if(Long.parseLong(minSalary)>=Long.parseLong(maxSalary)){
            edt_MinSalary.setError("Lương thấp nhất phải bé hơn lương cao nhất");
            isValidate = false;
        }

        if (maxSalary.isEmpty()) {
            edt_MaxSalary.setError("Vui lòng nhập cao nhất");
            isValidate = false;
        }else if(Long.parseLong(maxSalary)<=1000 && typeSalary.equals("VND")){
            edt_MaxSalary.setError("Vui lòng nhập cao nhất phải dưới 1.000VND");
            isValidate = false;
        }else if(Long.parseLong(maxSalary)<=1 && typeSalary.equals("USD")){
            edt_MaxSalary.setError("Vui lòng nhập cao nhất phải dưới 1USD");
            isValidate = false;
        }else if(Long.parseLong(maxSalary)<=Long.parseLong(minSalary)){
            edt_MaxSalary.setError("Lương cao nhất phải lớn hơn lương thấp nhất");
            isValidate = false;
        }

        if (desc.isEmpty()) {
            edt_JobDesc.setError("Vui lòng nhập miêu tả công việc");
            isValidate = false;
        }

        if (minAge.isEmpty()) {
            edt_MinAge.setError("Vui lòng nhập tuổi thấp nhất");
            isValidate = false;
        }else if(Integer.parseInt(minAge)<=16){
            edt_MinAge.setError("Tuổi thấp nhất phải từ 16 tuổi trở lên");
            isValidate = false;
        }

        if (maxAge.isEmpty()) {
            edt_MaxAge.setError("Vui lòng nhập tuổi cao nhất");
            isValidate = false;
        }else if(Integer.parseInt(maxAge)>65){
            edt_MaxAge.setError("Vui lòng nhập tuổi cao nhất phải bé hơn 65 tuổi");
            isValidate = false;
        }

        if (exDate.isEmpty()) {
            til_Date.setError("Vui lòng chọn ngày hết hạn");
            isValidate = false;
        } else {
            // Kiểm tra ngày hết hạn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date currentDate = Calendar.getInstance().getTime();
            Date expirationDate;

            try {
                expirationDate = dateFormat.parse(exDate);
            } catch (ParseException e) {
                e.printStackTrace();
                expirationDate = null;
            }

            assert expirationDate != null;
            if (expirationDate.before(currentDate)) {
                til_Date.setError("Vui lòng chọn ngày hết hạn hợp lệ");
                isValidate = false;
            }
        }

        if(isValidate){
            addJob();
        };
    }

    private String getGender() {
        int selectedId = rdog_Gender.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        String selectedGender = "";
        if (radioButton != null) {
            selectedGender = radioButton.getText().toString();
        }
        return selectedGender;
    }

    private String getTypeOfSalary() {
        if (rdo_VND.isChecked( )) {
            return "VND";
        } else {
            return "USD";
        }
    }

    private void spnListener() {
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

        spn_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area[0] = adapterView.getItemAtPosition(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_EducationLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                education[0] = adapterView.getItemAtPosition(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance( );
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        dialog = new ProgressDialog(this);
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
        edt_countRecruit = findViewById(R.id.edt_countRecruit);
        edt_addressWork = findViewById(R.id.edt_addressWork);
        edt_MinAge = findViewById(R.id.edt_MinAge);
        edt_MaxAge = findViewById(R.id.edt_MaxAge);
        rdog_Role = findViewById(R.id.rdog_Role);
        rdog_Gender = findViewById(R.id.rdog_Gender);
        spn_area = findViewById(R.id.spn_area);
        spn_EducationLevel = findViewById(R.id.spn_EducationLevel);
        edt_exDate = findViewById(R.id.edt_exDate);
        til_Date = findViewById(R.id.til_Date);
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