package com.jobhunthth.HTH0205.jobseekers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.Favourrecruiter;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.Setting;

public class MainScreen extends AppCompatActivity {
    LinearLayout profile;
    TextView nameaccount;
    ImageView imageavt, menu;
    DrawerLayout drawerLayoutMain;
    String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference profileRef = db.collection("profile").document(currentUid);
    NavigationView navigationMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        profile = findViewById(R.id.profile);
        nameaccount = findViewById(R.id.nameaccount);
        imageavt = findViewById(R.id.imageavt);
        menu = findViewById(R.id.menu);
        drawerLayoutMain = findViewById(R.id.drawerlayout_menu);
        navigationMenu = findViewById(R.id.navigationMenu);

        //
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawerlayout_menu);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        profileRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String avtUrl = documentSnapshot.getString("avtUrl");
                    nameaccount.setText(name);
                    Glide.with(MainScreen.this)
                            .load(avtUrl)
                            .placeholder(R.drawable.people) // Ảnh placeholder hiển thị khi chờ tải ảnh
                            .error(R.drawable.people) // Ảnh hiển thị khi lỗi tải ảnh
                            .into(imageavt);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadProfile.class);
                startActivity(intent);
                finish();
            }
        });
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fragment_favourite:
                        Toast.makeText(MainScreen.this, "hehe", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new Favourrecruiter();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.drawer, fragment).commit();
                        drawerLayoutMain.close();
                        break;
                    case R.id.fragment_employer:
                        Fragment fragment1 = new Setting();
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        fragmentTransaction1.replace(R.id.drawer, fragment1).commit();
                        drawerLayoutMain.close();
                        break;

                    case R.id.fragment_Setting:
//                        Fragment fragment2 = new FragmentThongKe();
//                        FragmentManager fragmentManager2 = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
//                        fragmentTransaction2.replace(R.id.fragment_view, fragment2).commit();
//                        drawerLayoutMain.close();
                        break;
                    case R.id.fragment_gioithieu:
//                        Fragment fragment3 = new FragmentGioiThieu();
//                        FragmentManager fragmentManager3 = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
//                        fragmentTransaction3.replace(R.id.fragment_view, fragment3).commit();
//                        drawerLayoutMain.close();
                        break;
                    case R.id.back:
//                        Intent intent = new Intent(MainActivity.this, SensorService.class);
//                        startService(intent);
//                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                drawerLayoutMain.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);

    }
}
