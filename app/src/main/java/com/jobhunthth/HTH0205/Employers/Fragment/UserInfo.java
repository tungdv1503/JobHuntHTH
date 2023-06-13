package com.jobhunthth.HTH0205.Employers.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.Employers.Interface.AccountInfoCallBack;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;

public class UserInfo extends Fragment {
    private ImageButton imgEditName, imgEditBirthDay, imgEditGender, imgEditPhoneNumber, imgEditEmail, imgEditAddress;
    private TextView tvNameUserInfor, tvBrithDay, tvGender, tvPhoneNumber, tvEmail, tvAddress, tvTypeUser, txtLabelPhoneNumber, txtLabelEmail, txtLabelAddress;
    private RoundedImageView imgAvtUser;
    private EditText edtNameUser, edtPhoneNumber, edtEmailUser, edtAddressUser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance( );
    FirebaseUser currentUser = firebaseAuth.getCurrentUser( );
    FirebaseFirestore db = FirebaseFirestore.getInstance( );

    private boolean isUserNameChecked, isUserPhoneNumberChecked, isUserEmailChecked, isUserAddressChecked;
    private static final int REQUEST_IMAGE_PICKER = 1001;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    UserInfoModel infoModel = null;
    private ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);
        initView(v);
        if (currentUser != null) {
            // Người dùng đã đăng nhập, bạn có thể truy cập thông tin của người dùng tại đây
            String uid = currentUser.getUid( ); // Lấy ID người dùng
            getAccountInfo(uid, new AccountInfoCallBack( ) {
                @Override
                public void onSuccess(UserInfoModel info) {
                    tvNameUserInfor.setText(info.getName( ));
                    tvAddress.setText(info.getAddress( ));
                    tvEmail.setText(info.getEmail( ));
                    tvGender.setText(info.getGender( ));
                    tvBrithDay.setText(info.getBirth( ));
                    tvPhoneNumber.setText(info.getPhone( ));
                    Glide.with(getContext( ))
                            .load(info.getAvatar( ))
                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                            .into(imgAvtUser);
                    infoModel = info;
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
        initListener( );
        return v;
    }

    private void initView(View v) {
        dialog = new ProgressDialog(getContext( ));
        // ánh xạ ImageViewButton
        imgEditName = v.findViewById(R.id.imgbtn_edit_name);
        imgEditBirthDay = v.findViewById(R.id.imgbtn_edit_birthday);
        imgEditGender = v.findViewById(R.id.imgbtn_edit_gender);
        imgEditPhoneNumber = v.findViewById(R.id.imgbtn_edit_phonenumber);
        imgEditEmail = v.findViewById(R.id.imgbtn_edit_email);
        imgEditAddress = v.findViewById(R.id.imgbtn_edit_address);
        // ánh xạ TextView
        tvBrithDay = v.findViewById(R.id.tv_birthday);
        tvGender = v.findViewById(R.id.tv_gender);
        tvPhoneNumber = v.findViewById(R.id.tv_phonenumber);
        tvEmail = v.findViewById(R.id.tv_email);
        tvAddress = v.findViewById(R.id.tv_address);
        tvNameUserInfor = v.findViewById(R.id.userInfo_name);
        tvTypeUser = v.findViewById(R.id.type_user);
        txtLabelEmail = v.findViewById(R.id.txt_label_email);
        txtLabelPhoneNumber = v.findViewById(R.id.txt_label_phonenumber);
        txtLabelAddress = v.findViewById(R.id.txt_label_address);
        // ánh xạ RoundedImageView
        imgAvtUser = v.findViewById(R.id.img_avataruser);
        // ánh xạ EditText
        edtNameUser = v.findViewById(R.id.edt_nameuser);
        edtPhoneNumber = v.findViewById(R.id.edt_userinfor_phonenumber);
        edtEmailUser = v.findViewById(R.id.edt_userinfor_email);
        edtAddressUser = v.findViewById(R.id.edt_userinfor_address);
    }

    private void initListener() {
        imgEditName.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                if (!isUserNameChecked) {
                    edtNameUser.setVisibility(View.VISIBLE);
                    tvNameUserInfor.setVisibility(View.GONE);
                    edtNameUser.setText(infoModel.getName( ));
                    Glide.with(getContext( )).load(R.drawable.check_24).into(imgEditName);
                    isUserNameChecked = true;
                } else {
                    String text = edtNameUser.getText( ).toString( ).trim( );
                    tvNameUserInfor.setVisibility(View.VISIBLE);
                    edtNameUser.setVisibility(View.GONE);
                    Glide.with(getContext( )).load(R.drawable.edit_24).into(imgEditName);
                    if (!text.isEmpty( )) {
                        if (!text.equals(infoModel.getName( ))) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(text)
                                    .build();

                            currentUser.updateProfile(profileUpdates);
                            db.collection("UserInfo").document(currentUser.getUid( ))
                                    .update("name", text);
                            getAccountInfo(currentUser.getUid( ), new AccountInfoCallBack( ) {
                                @Override
                                public void onSuccess(UserInfoModel info) {
                                    tvNameUserInfor.setText(info.getName( ));
                                    tvAddress.setText(info.getAddress( ));
                                    tvEmail.setText(info.getEmail( ));
                                    tvGender.setText(info.getGender( ));
                                    tvBrithDay.setText(info.getBirth( ));
                                    tvPhoneNumber.setText(info.getPhone( ));
                                    Glide.with(getContext( ))
                                            .load(info.getAvatar( ))
                                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                                            .into(imgAvtUser);
                                    infoModel = info;
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                    }
                    isUserNameChecked = false;
                }
            }
        });
        imgEditBirthDay.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                showDatePickerDialog( );
            }
        });
        imgEditGender.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                showGenderDialog( );
            }
        });
        imgEditPhoneNumber.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (!isUserPhoneNumberChecked) {
                    edtPhoneNumber.setVisibility(View.VISIBLE);
                    txtLabelPhoneNumber.setVisibility(View.GONE);
                    edtPhoneNumber.setText(infoModel.getPhone( ));
                    Glide.with(getContext( )).load(R.drawable.check_24).into(imgEditPhoneNumber);
                    isUserPhoneNumberChecked = true;
                } else {
                    String text = edtPhoneNumber.getText( ).toString( ).trim( );
                    txtLabelPhoneNumber.setVisibility(View.VISIBLE);
                    edtPhoneNumber.setVisibility(View.GONE);
                    Glide.with(getContext( )).load(R.drawable.edit_24).into(imgEditPhoneNumber);
                    if (!text.isEmpty( )) {
                        if (!text.equals(infoModel.getName( ))) {
                            db.collection("UserInfo").document(currentUser.getUid( ))
                                    .update("phone", text);
                            getAccountInfo(currentUser.getUid( ), new AccountInfoCallBack( ) {
                                @Override
                                public void onSuccess(UserInfoModel info) {
                                    tvNameUserInfor.setText(info.getName( ));
                                    tvAddress.setText(info.getAddress( ));
                                    tvEmail.setText(info.getEmail( ));
                                    tvGender.setText(info.getGender( ));
                                    tvBrithDay.setText(info.getBirth( ));
                                    tvPhoneNumber.setText(info.getPhone( ));
                                    Glide.with(getContext( ))
                                            .load(info.getAvatar( ))
                                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                                            .into(imgAvtUser);
                                    infoModel = info;
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                    }
                    isUserPhoneNumberChecked = false;
                }
            }
        });
        imgEditEmail.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (!isUserEmailChecked) {
                    edtEmailUser.setVisibility(View.VISIBLE);
                    tvEmail.setVisibility(View.GONE);
                    edtEmailUser.setText(infoModel.getEmail( ));
                    Glide.with(getContext( )).load(R.drawable.check_24).into(imgEditEmail);
                    isUserEmailChecked = true;
                } else {
                    String text = edtEmailUser.getText( ).toString( ).trim( );
                    tvEmail.setVisibility(View.VISIBLE);
                    edtEmailUser.setVisibility(View.GONE);
                    Glide.with(getContext( )).load(R.drawable.edit_24).into(imgEditEmail);
                    if (!text.isEmpty( )) {
                        if (!text.equals(infoModel.getName( ))) {
                            db.collection("UserInfo").document(currentUser.getUid( ))
                                    .update("email", text);
                            getAccountInfo(currentUser.getUid( ), new AccountInfoCallBack( ) {
                                @Override
                                public void onSuccess(UserInfoModel info) {
                                    tvNameUserInfor.setText(info.getName( ));
                                    tvAddress.setText(info.getAddress( ));
                                    tvEmail.setText(info.getEmail( ));
                                    tvGender.setText(info.getGender( ));
                                    tvBrithDay.setText(info.getBirth( ));
                                    tvPhoneNumber.setText(info.getPhone( ));
                                    Glide.with(getContext( ))
                                            .load(info.getAvatar( ))
                                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                                            .into(imgAvtUser);
                                    infoModel = info;
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                    }
                    isUserEmailChecked = false;
                }
            }
        });
        imgEditAddress.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (!isUserAddressChecked) {
                    edtAddressUser.setVisibility(View.VISIBLE);
                    tvAddress.setVisibility(View.GONE);
                    edtAddressUser.setText(infoModel.getAddress( ));
                    Glide.with(getContext( )).load(R.drawable.check_24).into(imgEditAddress);
                    isUserAddressChecked = true;
                } else {
                    String text = edtAddressUser.getText( ).toString( ).trim( );
                    tvAddress.setVisibility(View.VISIBLE);
                    edtAddressUser.setVisibility(View.GONE);
                    Glide.with(getContext( )).load(R.drawable.edit_24).into(imgEditAddress);
                    if (!text.isEmpty( )) {
                        if (!text.equals(infoModel.getName( ))) {
                            db.collection("UserInfo").document(currentUser.getUid( ))
                                    .update("address", text);
                            getAccountInfo(currentUser.getUid( ), new AccountInfoCallBack( ) {
                                @Override
                                public void onSuccess(UserInfoModel info) {
                                    tvNameUserInfor.setText(info.getName( ));
                                    tvAddress.setText(info.getAddress( ));
                                    tvEmail.setText(info.getEmail( ));
                                    tvGender.setText(info.getGender( ));
                                    tvBrithDay.setText(info.getBirth( ));
                                    tvPhoneNumber.setText(info.getPhone( ));
                                    Glide.with(getContext( ))
                                            .load(info.getAvatar( ))
                                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                                            .into(imgAvtUser);
                                    infoModel = info;
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                    }
                    isUserAddressChecked = false;
                }
            }
        });
        imgAvtUser.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( );
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE_PICKER);
            }
        });
    }

    private void getAccountInfo(String id, AccountInfoCallBack callBack) {
        db.collection("UserInfo").document(id).get( )
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful( )) {
                            DocumentSnapshot document = task.getResult( );
                            if (document.exists( )) {
                                UserInfoModel info = document.toObject(UserInfoModel.class);
                                callBack.onSuccess(info);
                            }
                        } else {
                            Exception e = task.getException( );
                            if (e != null) {
                                // Log the error or show an error message
                                Log.e("GetCompanyInfo", "Error getting company information: " + e.getMessage( ));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GetCompanyInfo", "Failed to get company information: " + e.getMessage( ));
                    }
                });
    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance( );
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext( ),
                new DatePickerDialog.OnDateSetListener( ) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý ngày đã chọn
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        // Gọi hàm để cập nhật ngày sinh
                        updateBirthday(selectedDate);
                    }
                },
                year, month, day
        );

        // Hiển thị DatePickerDialog
        datePickerDialog.show( );
    }

    private void updateBirthday(String selectedDate) {
        // Ví dụ:
//        tvBrithDay.setText(selectedDate);
        String[] dateParts = selectedDate.split("/");
        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];

        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
        }

        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }

        // Cập nhật ngày sinh đã được định dạng
        String formattedDate = day + "/" + month + "/" + year;
        db.collection("UserInfo").document(currentUser.getUid( ))
                .update("birth", formattedDate);
        getAccountInfo(currentUser.getUid( ), new AccountInfoCallBack( ) {
            @Override
            public void onSuccess(UserInfoModel info) {
                tvNameUserInfor.setText(info.getName( ));
                tvAddress.setText(info.getAddress( ));
                tvEmail.setText(info.getEmail( ));
                tvGender.setText(info.getGender( ));
                tvBrithDay.setText(info.getBirth( ));
                tvPhoneNumber.setText(info.getPhone( ));
                Glide.with(getContext( ))
                        .load(info.getAvatar( ))
                        .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                        .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                        .into(imgAvtUser);
                infoModel = info;
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void showGenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext( ));
        builder.setTitle("Select Gender");

// Danh sách tùy chọn giới tính
        final String[] genderOptions = {"Male", "Female", "Other"};

        // Lựa chọn mặc định (nếu có)
        int defaultSelectedIndex = 0; // Giới tính Nam được chọn mặc định
        String currentGender = tvGender.getText( ).toString( );
        for (int i = 0; i < genderOptions.length; i++) {
            if (genderOptions[i].equalsIgnoreCase(currentGender)) {
                defaultSelectedIndex = i;
                break;
            }
        }

        // Đặt danh sách tùy chọn giới tính vào dialog
        builder.setSingleChoiceItems(genderOptions, defaultSelectedIndex, new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lựa chọn được chọn
                String selectedGender = genderOptions[which];
//                updateGender(selectedGender);
                db.collection("UserInfo").document(currentUser.getUid( ))
                        .update("gender", selectedGender);
                getAccountInfo(currentUser.getUid( ), new AccountInfoCallBack( ) {
                    @Override
                    public void onSuccess(UserInfoModel info) {
                        tvNameUserInfor.setText(info.getName( ));
                        tvAddress.setText(info.getAddress( ));
                        tvEmail.setText(info.getEmail( ));
                        tvGender.setText(info.getGender( ));
                        tvBrithDay.setText(info.getBirth( ));
                        tvPhoneNumber.setText(info.getPhone( ));
                        Glide.with(getContext( ))
                                .load(info.getAvatar( ))
                                .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                                .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                                .into(imgAvtUser);
                        infoModel = info;
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                dialog.dismiss( );
            }
        });
        builder.setPositiveButton("cancel", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Hiển thị dialog
        AlertDialog dialog = builder.create( );
        dialog.show( );
    }
    private void uploadImageToStorage(Uri imageUri, String id) {
        StorageReference storageRef = FirebaseStorage.getInstance( ).getReference( );
        StorageReference imageRef = storageRef.child("userinfor_images/" + id + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>( ) {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(getContext( )).load(imageUri).error(R.drawable.avatar).into(imgAvtUser);
                        imageRef.getDownloadUrl( ).addOnSuccessListener(new OnSuccessListener<Uri>( ) {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(downloadUri)
                                        .build();

                                currentUser.updateProfile(profileUpdates);
                                db.collection("UserInfo").document(currentUser.getUid( ))
                                        .update("avatar", downloadUri.toString( ))
                                        .addOnSuccessListener(new OnSuccessListener<Void>( ) {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss( );
                                            }
                                        });
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
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData( ) != null || data != null || requestCode == REQUEST_IMAGE_PICKER || resultCode == RESULT_OK) {
            Uri uri = data.getData( );
            dialog.show( );
            uploadImageToStorage(uri, currentUser.getUid( ));
        }
    }

}