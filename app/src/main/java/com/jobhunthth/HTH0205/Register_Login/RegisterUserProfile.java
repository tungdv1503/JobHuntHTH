package com.jobhunthth.HTH0205.Register_Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Models.UserProfileModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.Adapter.AdapterSkill;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.UserProfile;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserProfile extends AppCompatActivity {

    private static final String TAG = RegisterUserProfile.class.getName( );
    private RoundedImageView profileAvatar;
    private Button btnUploadCV, btn_Save;
    private ImageButton btnSalary;
    private TextView profileSalary, profileName, profileCV;
    private Spinner spnEducation, spnProfession;
    private ImageButton btnSkill;
    private RecyclerView listSkill;
    private ProgressDialog dialog;
    private FirebaseUser mUser;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mStore;
    private UserProfileModel model;
    private String img, cv;
    private String selectedProfession = "";
    private Toolbar toolbar;
    private static final int REQUEST_IMAGE_PICKER = 1001;
    private static final int REQUEST_CV_PICKER = 1002;
    private ArrayAdapter<CharSequence> professionAdapter, educationAdapter;
    private AdapterSkill adapterSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_profile);

        initUI( );

        setSupportActionBar(toolbar);
        toolbar.setTitle("Register user profile");
        getSupportActionBar( ).setDisplayHomeAsUpEnabled(true);

        getData( );

    }

    private void getData() {
        dialog.show( );
        profileName.setText(mUser.getDisplayName( ));
        mStore.collection("UserProfile").document(mUser.getUid( ))
                .get( )
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            DocumentSnapshot doc = task.getResult( );
                            if (doc.exists( )) {
                                dialog.dismiss( );
                                model = doc.toObject(UserProfileModel.class);
                                showData(model);
                            } else {
                                dialog.dismiss( );
                                List<String> skill = new ArrayList<>( );
                                model = new UserProfileModel(null, null, null, null, null, null, null, skill);
                                mStore.collection("UserProfile").document(mUser.getUid( )).set(model)
                                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful( )) {
                                                    initListener( );
                                                    dialog.dismiss( );
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener( ) {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: " + e);
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e);
                    }
                });
    }

    private void showData(UserProfileModel model) {
        initListener( );
        setAdapter( );
        professionAdapter = ArrayAdapter.createFromResource(RegisterUserProfile.this,
                R.array.spn_JobProfession, android.R.layout.simple_spinner_item);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        educationAdapter = ArrayAdapter.createFromResource(RegisterUserProfile.this
                , R.array.spn_education_levels, android.R.layout.simple_spinner_item);
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog.dismiss( );
        Glide.with(RegisterUserProfile.this).load(model.getAvatar( )).error(R.drawable.avatar).into(profileAvatar);
        String profession = model.getProfession( );
        String education = model.getEducation( );
        String cv = model.getCv( );
        String minSalary = model.getMinSalary( );
        String maxSalary = model.getMaxSalary( );
        String typeOfSalary = model.getTypeSalary( );
        if (cv != null) {
            if (cv.isEmpty( )) {
                profileCV.setText("Bạn chưa có cv");
            } else {
                profileCV.setText("Bạn đã đăng tải cv");
            }
        }

        Log.d(TAG, "showData: " + profession + "/" + education);

        if (profession != null) {
            if (!profession.isEmpty( )) {
                int position = professionAdapter.getPosition(profession);
                if (position != -1) {
                    spnProfession.setSelection(position);
                }
            }
        }

        if (education != null) {
            if (!education.isEmpty( )) {
                int position = educationAdapter.getPosition(education);
                if (position != -1) {
                    spnEducation.setSelection(position);
                }
            }
        }

        if (minSalary != null && maxSalary != null && typeOfSalary != null) {
            if (!minSalary.isEmpty( ) && !maxSalary.isEmpty( ) && !typeOfSalary.isEmpty( )) {
                profileSalary.setText(minSalary + " - " + maxSalary + " " + typeOfSalary + "/Tháng");
            }
        }

    }

    private void setAdapter() {
        adapterSkill = new AdapterSkill(RegisterUserProfile.this, model.getSkill( ));
        LinearLayoutManager layoutManager = new LinearLayoutManager(RegisterUserProfile.this, LinearLayoutManager.VERTICAL, false);
        listSkill.setLayoutManager(layoutManager);
        listSkill.setAdapter(adapterSkill);
    }

    private void initListener() {
        profileAvatar.setOnClickListener(view1 -> {
            Intent intent = new Intent( );
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });

        btnUploadCV.setOnClickListener(view1 -> {
            Intent intent = new Intent( );
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CV_PICKER);
        });

        spnProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.show( );
                String text = adapterView.getItemAtPosition(i).toString( ).trim( );
                mStore.collection("UserProfile").document(mUser.getUid( ))
                        .update("profession", text)
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss( );
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.show( );
                String text = adapterView.getItemAtPosition(i).toString( ).trim( );
                mStore.collection("UserProfile").document(mUser.getUid( ))
                        .update("education", text)
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss( );
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSalary.setOnClickListener(view1 -> {
            showSalaryDialog( );
        });

        btnSkill.setOnClickListener(view1 -> {
            showSkillDialog( );
        });

        btn_Save.setOnClickListener(view -> {
            checkValue( );
        });
    }

    private void checkValue() {
        dialog.show( );
        mStore.collection("UserProfile").document(mUser.getUid( ))
                .get( )
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful( )) {
                        DocumentSnapshot doc = task.getResult( );
                        if (doc != null && doc.exists( )) {
                            UserProfileModel model1 = doc.toObject(UserProfileModel.class);
                            if (model1.getAvatar( ) != null && model1.getCv( ) != null && model1.getProfession( ) != null && model1.getMinSalary( ) != null
                                    && model1.getMaxSalary( ) != null && model1.getTypeSalary( ) != null && model1.getEducation( ) != null && model1.getSkill( ) != null) {
                                onBackPressed();
                            }else {
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserProfile.this);
                                builder.setTitle("Vui lòng nhập đầy đủ các thông tin");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener( ) {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener( ) {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.create().show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss( );
                    Log.e(TAG, "checkValue: " + e);
                });
    }

    private void showSkillDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserProfile.this);
        builder.setTitle("Chọn kỹ năng");

        View dialogView = LayoutInflater.from(RegisterUserProfile.this).inflate(R.layout.dialog_skill, null);
        builder.setView(dialogView);

        Spinner spinnerSkill = dialogView.findViewById(R.id.spinner_skill);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedSkill = spinnerSkill.getSelectedItem( ).toString( );
                List<String> listSkill = model.getSkill( );
                if (!listSkill.contains(selectedSkill)) {
                    listSkill.add(selectedSkill);
                    mStore.collection("UserProfile").document(mUser.getUid( ))
                            .update("skill", listSkill)
                            .addOnSuccessListener(new OnSuccessListener<Void>( ) {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setAdapter( );
                                    Toast.makeText(RegisterUserProfile.this, "Kỹ năng đã được lưu", Toast.LENGTH_SHORT).show( );
                                }
                            })
                            .addOnFailureListener(new OnFailureListener( ) {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterUserProfile.this, "Lỗi: " + e.getMessage( ), Toast.LENGTH_SHORT).show( );
                                }
                            });
                } else {
                    Toast.makeText(RegisterUserProfile.this, "Kỹ năng đã có", Toast.LENGTH_SHORT).show( );
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss( );
            }
        });

        AlertDialog dialog = builder.create( );
        dialog.show( );
    }

    private void showSalaryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserProfile.this);
        builder.setTitle("Thông tin lương");

        View dialogView = LayoutInflater.from(RegisterUserProfile.this).inflate(R.layout.dialog_desired_salary, null);
        builder.setView(dialogView);

        TextInputLayout inputLayoutMinSalary = dialogView.findViewById(R.id.input_layout_min_salary);
        TextInputEditText etMinSalary = dialogView.findViewById(R.id.et_min_salary);
        TextInputLayout inputLayoutMaxSalary = dialogView.findViewById(R.id.input_layout_max_salary);
        TextInputEditText etMaxSalary = dialogView.findViewById(R.id.et_max_salary);
        RadioGroup radioGroupSalaryType = dialogView.findViewById(R.id.radio_group_salary_type);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String minSalary = etMinSalary.getText( ).toString( ).trim( );
                String maxSalary = etMaxSalary.getText( ).toString( ).trim( );
                String typeSalary = getTypeOfSalary(radioGroupSalaryType, dialogView);

                if (checkSalarySave(minSalary, maxSalary, typeSalary, etMinSalary, etMaxSalary)) {
                    mStore.collection("UserProfile").document(mUser.getUid( ))
                            .update("minSalary", minSalary,
                                    "maxSalary", maxSalary,
                                    "typeSalary", typeSalary)
                            .addOnSuccessListener(new OnSuccessListener<Void>( ) {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    profileSalary.setText(minSalary + " - " + maxSalary + " " + typeSalary + "/Tháng");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener( ) {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterUserProfile.this, "Lỗi: " + e.getMessage( ), Toast.LENGTH_SHORT).show( );
                                }
                            });
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss( );
            }
        });

        AlertDialog dialog = builder.create( );
        dialog.show( );
    }

    private boolean checkSalarySave(String minSalary, String maxSalary, String typeSalary,
                                    TextInputEditText etMinSalary, TextInputEditText etMaxSalary) {
        boolean isValidate = true;

        if (minSalary.isEmpty( )) {
            etMinSalary.setError("Vui lòng nhập lương thấp nhất");
            isValidate = false;
        } else if (Long.parseLong(minSalary) <= 1000 && typeSalary.equals("VND")) {
            etMinSalary.setError("Vui lòng nhập lương thấp nhất phải từ 1.000VND");
            isValidate = false;
        } else if (Long.parseLong(minSalary) <= 1 && typeSalary.equals("USD")) {
            etMinSalary.setError("Vui lòng nhập lương thấp nhất phải từ 1USD");
            isValidate = false;
        } else if (Long.parseLong(minSalary) >= Long.parseLong(maxSalary)) {
            etMinSalary.setError("Lương thấp nhất phải bé hơn lương cao nhất");
            isValidate = false;
        }

        if (maxSalary.isEmpty( )) {
            etMaxSalary.setError("Vui lòng nhập cao nhất");
            isValidate = false;
        } else if (Long.parseLong(maxSalary) <= 1000 && typeSalary.equals("VND")) {
            etMaxSalary.setError("Vui lòng nhập cao nhất phải dưới 1.000VND");
            isValidate = false;
        } else if (Long.parseLong(maxSalary) <= 1 && typeSalary.equals("USD")) {
            etMaxSalary.setError("Vui lòng nhập cao nhất phải dưới 1USD");
            isValidate = false;
        } else if (Long.parseLong(maxSalary) <= Long.parseLong(minSalary)) {
            etMaxSalary.setError("Lương cao nhất phải lớn hơn lương thấp nhất");
            isValidate = false;
        }

        return isValidate;
    }

    private String getTypeOfSalary(RadioGroup radioGroupSalaryType, View dialogView) {
        int id = radioGroupSalaryType.getCheckedRadioButtonId( );
        RadioButton selectedButton = dialogView.findViewById(id);
        String selectText = "";
        if (selectedButton != null) {
            selectText = selectedButton.getText( ).toString( ).trim( );
        }
        return selectText;
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        mStorage = FirebaseStorage.getInstance( );
        mStore = FirebaseFirestore.getInstance( );
        dialog = new ProgressDialog(RegisterUserProfile.this);
        profileAvatar = findViewById(R.id.profile_avatar);
        profileName = findViewById(R.id.profile_name);
        btnUploadCV = findViewById(R.id.btn_uploadCV);
        spnProfession = findViewById(R.id.spn_profession);
        btnSalary = findViewById(R.id.btn_salary);
        profileSalary = findViewById(R.id.profile_salary);
        spnEducation = findViewById(R.id.spn_education);
        btnSkill = findViewById(R.id.btn_skill);
        listSkill = findViewById(R.id.list_skill);
        profileCV = findViewById(R.id.profile_cv);
        btn_Save = findViewById(R.id.btn_Save);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData( ) != null) {
            Uri imageUri = data.getData( );
            dialog.show( );
            uploadImageToStorage(imageUri, FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ));
        }

        if (requestCode == REQUEST_CV_PICKER && resultCode == RESULT_OK && data != null && data.getData( ) != null) {
            Uri cvUri = data.getData( );
            dialog.show( );
            uploadCVToStorage(cvUri, FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( ));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId( )) {
            case android.R.id.home: {
                showDialogBack();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserProfile.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn sẽ không thể xin việc nếu chưa hoàn thành hồ sơ, bạn vẫn muốn thoát");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void uploadCVToStorage(Uri uri, String uid) {
        StorageReference storageRef = FirebaseStorage.getInstance( ).getReference( );
        StorageReference imageRef = storageRef.child("cv/" + uid + ".pdf");

        imageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profileCV.setText("Cv đã được tải");
                        imageRef.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                mStore.collection("UserProfile").document(mUser.getUid( ))
                                        .update("cv", String.valueOf(downloadUri));
                                cv = downloadUri.toString( );
                                dialog.dismiss( );
                            }
                        }).addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss( );
                        Toast.makeText(RegisterUserProfile.this, "Lỗi", Toast.LENGTH_SHORT).show( );
                    }
                });
    }

    private void uploadImageToStorage(Uri imageUri, String userID) {
        StorageReference storageRef = FirebaseStorage.getInstance( ).getReference( );
        StorageReference imageRef = storageRef.child("company_images/" + userID + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(RegisterUserProfile.this).load(imageUri).error(R.drawable.avatar).into(profileAvatar);
                        imageRef.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                Log.d(TAG, "onSuccessImage: ");
                                img = downloadUri.toString( );
                                mStore.collection("UserProfile").document(mUser.getUid( ))
                                        .update("avatar", img);
                                dialog.dismiss( );
                            }
                        }).addOnFailureListener(new OnFailureListener( ) {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss( );
                        Toast.makeText(RegisterUserProfile.this, "Lỗi", Toast.LENGTH_SHORT).show( );
                    }
                });
    }

}