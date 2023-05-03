package com.jobhunthth.HTH0205.UploadProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.Fragment.Input_Information;

public class UploadProfile extends AppCompatActivity {
Button upload;
LinearLayout linearLayout;
ImageView visible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);
        upload = findViewById(R.id.update_infor);
//        visible = findViewById(R.id.visibleinfor);
        linearLayout = findViewById(R.id.information);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Input_Information fragment = new Input_Information();
                linearLayout.setVisibility(View.GONE);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.updateinfor,fragment)
                        .commit();
            }
        });
//        visible.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                linearLayout.setVisibility(View.VISIBLE);
//            }
//        });
    }
}