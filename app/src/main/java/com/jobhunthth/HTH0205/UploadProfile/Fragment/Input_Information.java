package com.jobhunthth.HTH0205.UploadProfile.Fragment;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Input_Information extends Fragment {
    int REQUESTS_CODE_FOLDER = 123;
    int RESULT_OK = 123;
    int REQUESTS_CODE_FOLDER1 = 1234;
    int RESULT_OK1 = 1234;
    int REQUESTS_CODE_FOLDER2 = 12344;
    int RESULT_OK2 = 12344;
    ImageView visible;
    ScrollView invisible;
    TextInputEditText phone_edt, name_edt, age_edt, university_edt, majors_edt, years_edt, address_edt, github_edt;
    Button cv, avatar, background, save;
    Bitmap bitmapcv,bitmapavt,bitmapbackgr;
   // Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapcv,bitmapavt,bitmapbackgr,50,50, true);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.information_update, container, false);
        visible = v.findViewById(R.id.visibleinfor);
        invisible = v.findViewById(R.id.invisible);
        phone_edt = v.findViewById(R.id.phone_edt);
        name_edt = v.findViewById(R.id.name_edt);
        age_edt = v.findViewById(R.id.age_edt);
        university_edt = v.findViewById(R.id.university_edt);
        majors_edt = v.findViewById(R.id.majors_edt);
        years_edt = v.findViewById(R.id.yearsofexp_edt);
        address_edt = v.findViewById(R.id.address_edt);
        github_edt = v.findViewById(R.id.github_edt);
        cv = v.findViewById(R.id.cv);
        avatar = v.findViewById(R.id.avt);
        background = v.findViewById(R.id.backgr);
        save = v.findViewById(R.id.save);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadProfile.class);
                startActivity(intent);

            }
        });
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTS_CODE_FOLDER);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTS_CODE_FOLDER1);
            }
        });
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTS_CODE_FOLDER2);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một đối tượng HashMap chứa thông tin muốn lưu
                Map<String, Object> profileData = new HashMap<>();
                profileData.put("name", name_edt.getText().toString());
                profileData.put("address", address_edt.getText().toString());
                profileData.put("phone", phone_edt.getText().toString());
                profileData.put("university", university_edt.getText().toString());
                profileData.put("majors", majors_edt.getText().toString());
                profileData.put("years_of_exp", years_edt.getText().toString());
                profileData.put("github", github_edt.getText().toString());

                // Chuyển đổi bitmapcv thành một mảng byte và lưu vào Firestore dưới dạng base64 string
                ByteArrayOutputStream cvStream = new ByteArrayOutputStream();
                bitmapcv.compress(Bitmap.CompressFormat.JPEG, 100, cvStream);
                byte[] cvBytes = cvStream.toByteArray();
                String cvBase64 = Base64.encodeToString(cvBytes, Base64.DEFAULT);
                profileData.put("bitmapcv", cvBase64);

                // Chuyển đổi bitmapavt thành một mảng byte và lưu vào Firestore dưới dạng base64 string
                ByteArrayOutputStream avtStream = new ByteArrayOutputStream();
                bitmapavt.compress(Bitmap.CompressFormat.JPEG, 100, avtStream);
                byte[] avtBytes = avtStream.toByteArray();
                String avtBase64 = Base64.encodeToString(avtBytes, Base64.DEFAULT);
                profileData.put("bitmapavt", avtBase64);

                // Chuyển đổi bitmapbackgr thành một mảng byte và lưu vào Firestore dưới dạng base64 string
                ByteArrayOutputStream backgrStream = new ByteArrayOutputStream();
                bitmapbackgr.compress(Bitmap.CompressFormat.JPEG, 100, backgrStream);
                byte[] backgrBytes = backgrStream.toByteArray();
                String backgrBase64 = Base64.encodeToString(backgrBytes, Base64.DEFAULT);
                profileData.put("bitmapbackgr", backgrBase64);

                // Ghi dữ liệu vào Firestore với tên tài liệu là uid của tài khoản đang đăng nhập
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                db.collection("profile").document(currentUid).set(profileData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUESTS_CODE_FOLDER && requestCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                 bitmapcv = BitmapFactory.decodeStream(inputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            super.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUESTS_CODE_FOLDER1 && requestCode == RESULT_OK1 && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
               bitmapavt = BitmapFactory.decodeStream(inputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            super.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUESTS_CODE_FOLDER2 && requestCode == RESULT_OK2 && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
               bitmapbackgr = BitmapFactory.decodeStream(inputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }}