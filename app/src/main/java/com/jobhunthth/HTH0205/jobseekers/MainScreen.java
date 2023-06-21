package com.jobhunthth.HTH0205.jobseekers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Employers_Activity;
import com.jobhunthth.HTH0205.Employers.Fragment.UserInfo;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.Login;
import com.jobhunthth.HTH0205.Register_Login.RegisterEmployerInfo;
import com.jobhunthth.HTH0205.Register_Login.RegisterInfo;
import com.jobhunthth.HTH0205.UploadProfile.UploadProfile;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.Favourrecruiter;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.JobSeekers_Home;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.Setting;
import com.jobhunthth.HTH0205.jobseekers.Drawer_Fragement.UserProfile;

public class MainScreen extends AppCompatActivity {
    LinearLayout profile;
    TextView nameaccount;
    ImageView imageavt, menu;
    DrawerLayout drawerLayoutMain;
    String currentUid = FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( );
    private FirebaseFirestore db = FirebaseFirestore.getInstance( );
    private DocumentReference profileRef = db.collection("profile").document(currentUid);
    NavigationView navigationMenu;
    SharedPreferences sharedPreferences;
    private String TAG = MainScreen.class.getName( );
    private FirebaseUser mUser;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        profile = findViewById(R.id.profile);
        nameaccount = findViewById(R.id.nameaccount);
        imageavt = findViewById(R.id.imageavt);
        menu = findViewById(R.id.menu);
        drawerLayoutMain = findViewById(R.id.drawerlayout_menu);
        navigationMenu = findViewById(R.id.navigationMenu);
        showAccount();
        ChangeFragment(new JobSeekers_Home( ));
        //
        menu.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawerlayout_menu);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        profileRef.get( ).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>( ) {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists( )) {
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

        profile.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext( ), UploadProfile.class);
                startActivity(intent);
                finish( );
            }
        });
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener( ) {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId( )) {
                    case R.id.fragment_home: {
                        Fragment fragment = new JobSeekers_Home( );
                        ChangeFragment(fragment);
                        break;

                    }
                    case R.id.fragment_info: {
                        db.collection("UserInfo").document(FirebaseAuth.getInstance( ).getCurrentUser( ).getUid( )).get( )
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful( )) {
                                            DocumentSnapshot doc = task.getResult( );
                                            if (doc.exists( )) {
                                                ChangeFragment(new UserInfo( ));
                                            } else {
                                                showAlertDialog( );
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
                        return true;
                    }
                    case R.id.fragment_profile: {
                        ChangeFragment(new UserProfile( ));
                        break;
                    }
                    case R.id.fragment_employer:
                        Fragment fragment1 = new Setting( );
                        ChangeFragment(fragment1);
                        break;

                    case R.id.fragment_Setting:
//                        Fragment fragment2 = new FragmentThongKe();
//                        FragmentManager fragmentManager2 = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
//                        fragmentTransaction2.replace(R.id.fragment_view, fragment2).commit();
//                        drawerLayoutMain.close();
                        break;

                    case R.id.fragment_change_mode: {
                        sharedPreferences = getSharedPreferences("CheckVT", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit( );
                        editor.putInt("VaiTro", 2);
                        editor.apply( );
                        Intent intent = new Intent(MainScreen.this, Employers_Activity.class);
                        startActivity(intent);
                        finishAffinity( );
                        break;
                    }

                    case R.id.fragment_gioithieu:
//                        Fragment fragment3 = new FragmentGioiThieu();
//                        FragmentManager fragmentManager3 = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
//                        fragmentTransaction3.replace(R.id.fragment_view, fragment3).commit();
//                        drawerLayoutMain.close();
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
                        builder.setTitle("Xác nhận");
                        builder.setMessage("Bạn có muốn đăng xuất không?");

                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        });

                        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                }
                return false;
            }
        });
    }

    private void showAccount() {
        nameaccount.setText(mUser.getDisplayName());
        Glide.with(MainScreen.this).load(mUser.getPhotoUrl()).into(imageavt);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa đăng ký thông tin, bạn có muốn đăng ký luôn không ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainScreen.this, RegisterInfo.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                dialog.dismiss( );
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss( );
            }
        });
        AlertDialog dialog = builder.create( );
        dialog.show( );
    }

    private void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager( );
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.replace(R.id.drawer, fragment)
                .disallowAddToBackStack( )
                .commit( );
        drawerLayoutMain.close( );
    }

    ;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId( )) {
            case R.id.menu:
                drawerLayoutMain.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver( ) {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("onAvatarChange")){
                Glide.with(MainScreen.this).load(mUser.getPhotoUrl()).into(imageavt);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart( );
        IntentFilter intentFilter = new IntentFilter("onAvatarChange");
        LocalBroadcastManager.getInstance(MainScreen.this).registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop( );
        IntentFilter intentFilter = new IntentFilter("onAvatarChange");
        LocalBroadcastManager.getInstance(MainScreen.this).unregisterReceiver(broadcastReceiver);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed( );
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn nút back 2 lần liên tiếp để thoát", Toast.LENGTH_SHORT).show( );

        new Handler(Looper.getMainLooper( )).postDelayed(new Runnable( ) {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);

    }
}
