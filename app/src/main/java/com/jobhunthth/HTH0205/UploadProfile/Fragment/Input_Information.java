package com.jobhunthth.HTH0205.UploadProfile.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
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
    String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextInputEditText phone_edt, name_edt, age_edt, university_edt, majors_edt, years_edt, address_edt, github_edt,educate1;
    Button cv, avatar, background, save;
    Bitmap bitmapcv,bitmapavt,bitmapbackgr;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;

  DocumentReference profileRef;


   // Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapcv,bitmapavt,bitmapbackgr,50,50, true);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.information_update, container, false);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        visible = v.findViewById(R.id.visibleinfor);
        profileRef = db.collection("profile").document(currentUid);
        invisible = v.findViewById(R.id.invisible);
        phone_edt = v.findViewById(R.id.phone_edt);
        name_edt = v.findViewById(R.id.name_edt);
        educate1 = v.findViewById(R.id.educate);
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
        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String phone = documentSnapshot.getString("phone");
                    String age = documentSnapshot.getString("age");//
                    String educate = documentSnapshot.getString("educate");//
                    String uni = documentSnapshot.getString("university");
                    String majors = documentSnapshot.getString("majors");
                    String yearsofexp = documentSnapshot.getString("years_of_exp");
                    String address = documentSnapshot.getString("address");
                    String githublink = documentSnapshot.getString("github");

                    name_edt.setText(name);
                    phone_edt.setText(phone);
                  age_edt.setText(age);
                    educate1.setText(educate);
                    university_edt.setText(uni);
                    majors_edt.setText(majors);
                   years_edt.setText(yearsofexp);
                    address_edt.setText(address);
                    github_edt.setText(githublink);

                }
            }
        });
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


        Map<String, Object> profileData = new HashMap<>();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một đối tượng HashMap chứa thông tin muốn lưu

                profileData.put("name", name_edt.getText().toString());
                profileData.put("address", address_edt.getText().toString());
                profileData.put("phone", phone_edt.getText().toString());
                profileData.put("age", age_edt.getText().toString());
                profileData.put("educate", educate1.getText().toString());
                profileData.put("university", university_edt.getText().toString());
                profileData.put("majors", majors_edt.getText().toString());
                profileData.put("years_of_exp", years_edt.getText().toString());
                profileData.put("github", github_edt.getText().toString());

                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Lấy dữ liệu từ Firestore với ID là currentUid
                db.collection("profile").document(currentUid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    // Nếu có tài liệu có ID trùng với currentUid, tiến hành update dữ liệu
                                    db.collection("profile").document(currentUid).update(profileData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Update successful", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Nếu không có tài liệu nào có ID trùng với currentUid, tiến hành thêm mới
                                    db.collection("profile").document(currentUid).set(profileData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Save successful", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Save failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                // Chuyển đổi bitmapcv thành một mảng byte và lưu vào Firestore dưới dạng base64 string

                StorageReference cvRef = storageRef.child("images/" + currentUid + "/cv.jpg");
                ByteArrayOutputStream cvStream = new ByteArrayOutputStream();
                bitmapcv.compress(Bitmap.CompressFormat.JPEG, 100, cvStream);
                byte[] cvBytes = cvStream.toByteArray();
                UploadTask uploadTask = cvRef.putBytes(cvBytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        cvRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileData.put("cvUrl", uri.toString());
                                db.collection("profile").document(currentUid).update("cvUrl", uri.toString());
                            }
                        });
                    }
                });

// Lưu ảnh đại diện
                StorageReference Sfavt = storageRef.child("images/" + currentUid + "/avt.jpg");
                ByteArrayOutputStream avtStream = new ByteArrayOutputStream();
                bitmapavt.compress(Bitmap.CompressFormat.JPEG, 100, avtStream);
                byte[] avtBytes = avtStream.toByteArray();
                UploadTask uploadTask1 = Sfavt.putBytes(avtBytes);
                uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Sfavt.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileData.put("avtUrl", uri.toString());
                                db.collection("profile").document(currentUid).update("avtUrl", uri.toString());
                            }
                        });
                    }
                });

// Lưu ảnh nền
                StorageReference backgrRef = storageRef.child("images/" + currentUid + "/backgr.jpg");
                ByteArrayOutputStream backgrStream = new ByteArrayOutputStream();
                bitmapbackgr.compress(Bitmap.CompressFormat.JPEG, 100, backgrStream);
                byte[] backgrBytes = backgrStream.toByteArray();
                UploadTask uploadTask2 = backgrRef.putBytes(backgrBytes);
                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        backgrRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileData.put("backgrUrl", uri.toString());
                                db.collection("profile").document(currentUid).update("backgrUrl", uri.toString());
                            }
                        });
                    }
                });

//                // Chuyển đổi bitmapavt thành một mảng byte và lưu vào Firestore dưới dạng base64 string
//                ByteArrayOutputStream avtStream = new ByteArrayOutputStream();
//                bitmapavt.compress(Bitmap.CompressFormat.JPEG, 100, avtStream);
//                byte[] avtBytes = avtStream.toByteArray();
//                String avtBase64 = Base64.encodeToString(avtBytes, Base64.DEFAULT);
//                profileData.put("bitmapavt", avtBase64);
//
//                // Chuyển đổi bitmapbackgr thành một mảng byte và lưu vào Firestore dưới dạng base64 string
//                ByteArrayOutputStream backgrStream = new ByteArrayOutputStream();
//                bitmapbackgr.compress(Bitmap.CompressFormat.JPEG, 100, backgrStream);
//                byte[] backgrBytes = backgrStream.toByteArray();
//                String backgrBase64 = Base64.encodeToString(backgrBytes, Base64.DEFAULT);
//                profileData.put("bitmapbackgr", backgrBase64);
//
//                // Ghi dữ liệu vào Firestore với tên tài liệu là uid của tài khoản đang đăng nhập
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                db.collection("profile").document(currentUid).set(profileData)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getContext(), "oke", Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Tạo một đối tượng HashMap chứa thông tin muốn lưu
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DocumentReference profileRef = db.collection("profile_info").document(currentUid);
//                Map<String, Object> profileData = new HashMap<>();
//                profileData.put("name", name_edt.getText().toString());
//                profileData.put("address", address_edt.getText().toString());
//                profileData.put("phone", phone_edt.getText().toString());
//                profileData.put("university", university_edt.getText().toString());
//                profileData.put("majors", majors_edt.getText().toString());
//                profileData.put("years_of_exp", years_edt.getText().toString());
//                profileData.put("github", github_edt.getText().toString());
//                profileRef.set(profileData);
//
//                // Lưu avatar vào bộ sưu tập avatar
//                DocumentReference avatarRef = db.collection("avatar").document(currentUid);
//                ByteArrayOutputStream avtStream = new ByteArrayOutputStream();
//                bitmapavt.compress(Bitmap.CompressFormat.JPEG, 100, avtStream);
//                byte[] avtBytes = avtStream.toByteArray();
//                String avtBase64 = Base64.encodeToString(avtBytes, Base64.DEFAULT);
//                Map<String, Object> avatarData = new HashMap<>();
//                avatarData.put("bitmapavt", avtBase64);
//                avatarRef.set(avatarData);
//
//                // Lưu background vào bộ sưu tập background
//                DocumentReference backgroundRef = db.collection("background").document(currentUid);
//                ByteArrayOutputStream backgrStream = new ByteArrayOutputStream();
//                bitmapbackgr.compress(Bitmap.CompressFormat.JPEG, 100, backgrStream);
//                byte[] backgrBytes = backgrStream.toByteArray();
//                String backgrBase64 = Base64.encodeToString(backgrBytes, Base64.DEFAULT);
//                Map<String, Object> backgroundData = new HashMap<>();
//                backgroundData.put("bitmapbackgr", backgrBase64);
//                backgroundRef.set(backgroundData);
//
//                // Lưu cv vào bộ sưu tập cv
//                DocumentReference cvRef = db.collection("cv").document(currentUid);
//                ByteArrayOutputStream cvStream = new ByteArrayOutputStream();
//                bitmapcv.compress(Bitmap.CompressFormat.JPEG, 100, cvStream);
//                byte[] cvBytes = cvStream.toByteArray();
//                String cvBase64 = Base64.encodeToString(cvBytes, Base64.DEFAULT);
//                Map<String, Object> cvData = new HashMap<>();
//                cvData.put("bitmapcv", cvBase64);
//                cvRef.set(cvData);
//
//            }});


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