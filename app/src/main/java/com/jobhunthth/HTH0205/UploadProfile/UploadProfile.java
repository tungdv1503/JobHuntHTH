package com.jobhunthth.HTH0205.UploadProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.Login;
import com.jobhunthth.HTH0205.UploadProfile.Fragment.Input_Information;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;

import java.io.Serializable;

public class UploadProfile extends AppCompatActivity {
Button upload;
LinearLayout linearLayout;
ImageView back,logout,imageavt,imagebackgr;
TextView nameaccount,age1,educate1,uni1,majors1,yearsofexp1,sdt1,email1,address1,githublink1,viewcv,exp;
    FirebaseAuth mAuth ;
    String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db ;
    DocumentReference profileRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);
        db = FirebaseFirestore.getInstance();
        upload = findViewById(R.id.update_infor);
        mAuth = FirebaseAuth.getInstance();
        exp = findViewById(R.id.exp);
        profileRef = db.collection("profile").document(currentUid);
        age1 = findViewById(R.id.age);
        nameaccount = findViewById(R.id.nameaccount);
        educate1 = findViewById(R.id.educate);
        uni1 = findViewById(R.id.uni);
        majors1 = findViewById(R.id.major);
        yearsofexp1 = findViewById(R.id.yearsofexp);
        email1 = findViewById(R.id.email);
        sdt1 = findViewById(R.id.sdt);
        address1 = findViewById(R.id.address);
        githublink1 = findViewById(R.id.githublink);
        viewcv = findViewById(R.id.viewcv);
        logout = findViewById(R.id.logout);
        imageavt = findViewById(R.id.avtUrl);
        imagebackgr = findViewById(R.id.backgrUrl);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Logout();
            }
        });

        back = findViewById(R.id.back);
//        visible = findViewById(R.id.visibleinfor);
        linearLayout = findViewById(R.id.information);
        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String avtUrl = documentSnapshot.getString("avtUrl");
                    String phone = documentSnapshot.getString("phone");
                    String age = documentSnapshot.getString("age");//
                    String educate = documentSnapshot.getString("educate");//
                    String uni = documentSnapshot.getString("university");
                    String majors = documentSnapshot.getString("majors");
                    String yearsofexp = documentSnapshot.getString("years_of_exp");
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String address = documentSnapshot.getString("address");
                    String viewcv = documentSnapshot.getString("viewcv");
                    String githublink = documentSnapshot.getString("github");
                    String backgrUrl = documentSnapshot.getString("backgrUrl");
                    if (Integer.parseInt(yearsofexp) <= 1) {
                        exp.setText("Fresher");
                        exp.setTextColor(Color.BLUE);
                    } else if (Integer.parseInt(yearsofexp) <= 4) {
                        exp.setText("Junior");
                        exp.setTextColor(Color.YELLOW);
                    } else if (Integer.parseInt(yearsofexp) <= 8) {
                        exp.setText("Senior");
                        exp.setTextColor(Color.RED);
                    } else {
                        exp.setText("Master");
                        exp.setTextColor(0xFF800080);
                    }
                    nameaccount.setText(name);
                    sdt1.setText(phone);
                    age1.setText(age);
                    educate1.setText(educate);
                    uni1.setText(uni);
                    majors1.setText(majors);
                    yearsofexp1.setText(yearsofexp);
                    email1.setText(email);
                    address1.setText(address);
                    githublink1.setText(githublink);
                    Glide.with(UploadProfile.this)
                            .load(avtUrl)
                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                            .into(imageavt);
                    Glide.with(UploadProfile.this)
                            .load(backgrUrl)
                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                            .into(imagebackgr);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                Input_Information fragment = new Input_Information();
                linearLayout.setVisibility(View.GONE);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.updateinfor, fragment)
                        .commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void Logout(){
        mAuth = FirebaseAuth.getInstance();
        // Tạo đối tượng AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có muốn đăng xuất không?");

// Thêm nút Đồng ý
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Khởi tạo Intent và chuyển sang màn hình đăng nhập
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
            }
        });

// Thêm nút Hủy bỏ
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Đóng hộp thoại và không thực hiện gì thêm
                dialog.dismiss();
            }
        });

// Hiển thị hộp thoại
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}