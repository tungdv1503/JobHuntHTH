package com.jobhunthth.HTH0205.Employers.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Interface.AccountInfoCallBack;
import com.jobhunthth.HTH0205.Models.UserInfoModel;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;
import com.makeramen.roundedimageview.RoundedImageView;

public class UserInfo extends Fragment {
    private ImageButton imgEditName, imgEditBirthDay, imgEditGender, imgEditPhoneNumber, imgEditEmail, imgEditAddress, imgEditName1;
    private TextView tvNameUserInfor, tvBrithDay, tvGender, tvPhoneNumber, tvEmail, tvAddress, tvTypeUser;
    private RoundedImageView imgAvtUser;
    private EditText edtNameUser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance( );
    FirebaseUser currentUser = firebaseAuth.getCurrentUser( );
    FirebaseFirestore db = FirebaseFirestore.getInstance( );


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    UserInfoModel infoModel = null;

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
        imgEditName.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                tvNameUserInfor.setVisibility(View.VISIBLE);
                edtNameUser.setVisibility(View.GONE);
            }
        });
        imgEditName1.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                edtNameUser.setText(infoModel.getName( ));
                db.collection("UserInfo").document(currentUser.getUid( )).update("name", edtNameUser.getText( ).toString( ).trim( ))
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful( )) {
                                    Toast.makeText(getContext( ), "Thành công", Toast.LENGTH_SHORT).show( );
                                }
                            }
                        });
                tvNameUserInfor.setVisibility(View.GONE);
                edtNameUser.setVisibility(View.VISIBLE);
                edtNameUser.setText(edtNameUser.getText( ).toString( ).trim( ));
            }
        });
        return v;
    }

    private void initView(View v) {
        // ánh xạ ImageViewButton
        imgEditName = v.findViewById(R.id.imgbtn_edit_name);
        imgEditName1 = v.findViewById(R.id.imgbtn_edit_name1);
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
        // ánh xạ RoundedImageView
        imgAvtUser = v.findViewById(R.id.img_avataruser);
        //
        edtNameUser = v.findViewById(R.id.edt_nameuser);

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

}