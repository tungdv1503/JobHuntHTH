package com.jobhunthth.HTH0205.Register_Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;

public class RegisterInfo extends AppCompatActivity {

    private RoundedImageView avatarImageView;
    private TextInputEditText nameEditText,phoneEditText,addressEditText,birthEditText,emailEditText;
    private TextInputLayout birthTextInputLayout,nameTextInputLayout,phoneTextInputLayout,emailTextInputLayout,addressTextInputLayout;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton,femaleRadioButton;
    private Button saveButton;
    private FirebaseUser mUser;
    private FirebaseFirestore mStore;
    private final int REQUEST_IMAGE_PICKER = 1001;
    private ProgressDialog dialog;
    private String imgUri;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        initUI();

        showData();

        initListener();
    }

    private void showData() {
        if(mUser!=null){
            emailEditText.setText(mUser.getEmail());
        }
    }

    private void initListener() {
        avatarImageView.setOnClickListener(view -> {
            Intent intent = new Intent(  );
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,REQUEST_IMAGE_PICKER);
        });

        birthEditText.setOnClickListener(view -> selectBirth());

        saveButton.setOnClickListener(view -> {
            saveInfo();
        });
    }

    private void saveInfo() {
        if(checkValidate()){
            dialog.show();
            String name = nameEditText.getText().toString().trim();
            String birth = birthEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String gender = getGender();
            UserInfoModel info = new UserInfoModel(name,birth,phone,email,address,gender,imgUri);

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(imgUri))
                    .build();

            mUser.updateProfile(profileUpdates);

            mStore.collection("UserInfo").document( mUser.getUid() ).set(info)
                    .addOnSuccessListener(new OnSuccessListener<Void>( ) {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            int type = mIntent.getIntExtra("type",-1);
                            //type được sử dụng để kiểm tra xem phần mở activity này lên là gì để
                            //tiếp tục điều hướng ứng dụng
                            if(type==-1){
                                Intent intent = new Intent( RegisterInfo.this,RegisterEmployerInfo.class );
                                startActivity(intent);
                            }else if(type==2){
                                Intent intent = new Intent( RegisterInfo.this, MainScreen.class );
                                startActivity(intent);
                            }
                            else {
                                onBackPressed();
                            }
                            finishAffinity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener( ) {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterInfo.this, "Lỗi", Toast.LENGTH_SHORT).show( );

                        }
                    });
        }
    }

    private String getGender() {
        if(maleRadioButton.isChecked()){
            return "Nam";
        }else {
            return "Nữ";
        }
    }

    private boolean checkValidate() {
        String name = nameEditText.getText().toString().trim();
        String birth = birthEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        nameTextInputLayout.setError(null);
        birthTextInputLayout.setError(null);
        phoneTextInputLayout.setError(null);
        emailTextInputLayout.setError(null);
        addressTextInputLayout.setError(null);
        if(name.isEmpty()){
            nameTextInputLayout.setError("Vui lòng nhập tên");
            return false;
        }
        if(birth.isEmpty()){
            birthTextInputLayout.setError("Vui lòng chọn ngày tháng năm sinh của bạn");
            return false;
        }
        if(phone.isEmpty()){
            phoneTextInputLayout.setError("Vui lòng nhập số điện thoại");
            return false;
        }
        if(email.isEmpty()){
            emailTextInputLayout.setError("Vui lòng nhập email");
            return false;
        }else if(!isValidEmail(email)){
            emailTextInputLayout.setError("Email không hợp lệ");
            return false;
        }
        if(address.isEmpty()){
            addressTextInputLayout.setError("Vui lòng nhập địa chỉ");
            return false;
        }
        return true;
    }


    Calendar calendar = Calendar.getInstance();

    private void selectBirth(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
            // Gán giá trị vào EditText
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            birthEditText.setText(selectedDate);
        }, year, month, day);

        // Hiển thị dialog chọn ngày tháng năm sinh
        datePickerDialog.show();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void initUI() {
        mIntent = getIntent();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dialog = new ProgressDialog(this);
        mStore = FirebaseFirestore.getInstance();
        avatarImageView = findViewById(R.id.ResInfo_avatar);
        nameTextInputLayout = findViewById(R.id.ResInfo_TIL_name);
        nameEditText = findViewById(R.id.ResInfo_EDT_name);
        birthTextInputLayout = findViewById(R.id.ResInfo_TIL_birth);
        birthEditText = findViewById(R.id.ResInfo_EDT_birth);
        genderRadioGroup = findViewById(R.id.ResInfo_gender_radio_group);
        maleRadioButton = findViewById(R.id.ResInfo_rdo_nam);
        femaleRadioButton = findViewById(R.id.ResInfo_rdo_nu);
        phoneTextInputLayout = findViewById(R.id.ResInfo_TIL_phone);
        phoneEditText = findViewById(R.id.ResInfo_EDT_phone);
        emailTextInputLayout = findViewById(R.id.ResInfo_TIL_email);
        emailEditText = findViewById(R.id.ResInfo_EDT_email);
        addressTextInputLayout = findViewById(R.id.ResInfo_TIL_address);
        addressEditText = findViewById(R.id.ResInfo_EDT_address);
        saveButton = findViewById(R.id.ResInfo_btn_save);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_PICKER||resultCode==RESULT_OK||data!=null||data.getData()!=null){
            Uri uri = data.getData();
            dialog.show();
            uploadImageToStorage(uri,FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    private void uploadImageToStorage(Uri imageUri, String userID) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("company_images/" + userID + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(RegisterInfo.this).load(imageUri).error(R.drawable.avatar).into(avatarImageView);
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                imgUri = downloadUri.toString();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(RegisterInfo.this, "Lỗi", Toast.LENGTH_SHORT).show( );
                    }
                });
    }
}