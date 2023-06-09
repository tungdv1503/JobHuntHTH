package com.jobhunthth.HTH0205.Employers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jobhunthth.HTH0205.Employers.Fragment.Company_Info;
import com.jobhunthth.HTH0205.Employers.Fragment.HomeEmployer;
import com.jobhunthth.HTH0205.Employers.Fragment.UserInfo;
import com.jobhunthth.HTH0205.R;
import com.jobhunthth.HTH0205.Register_Login.Login;
import com.jobhunthth.HTH0205.Register_Login.RegisterEmployerInfo;
import com.jobhunthth.HTH0205.Register_Login.RegisterInfo;
import com.jobhunthth.HTH0205.jobseekers.MainScreen;

public class Employers_Activity extends AppCompatActivity {

    private String TAG = Employers_Activity.class.getName();
    private Toolbar toolbar_employer;
    private NavigationView nav_employer;
    private DrawerLayout drawerLayout_employer;
    private FirebaseFirestore mStore;
    private FirebaseUser mUser;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employers);
        sharedPreferences = getSharedPreferences("CheckVT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        initUI( );
        initListener( );

        setSupportActionBar(toolbar_employer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_employer.setNavigationIcon(R.drawable.menu);
        ChangeFragment(new HomeEmployer());

    }

    private void initListener() {
        nav_employer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener( ) {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_Home_employer:{
                        ChangeFragment(new HomeEmployer());
                        return true;
                    }

                    case R.id.menu_Account_employer:{
                        mStore.collection("UserInfo").document(mUser.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult( );
                                            if(doc.exists()){
                                                ChangeFragment(new UserInfo());
                                            }else {
                                                showAlertDialog(1);
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener( ) {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: "+e );
                                    }
                                });
                        return true;
                    }

                    case R.id.menu_ChangePassword:{
                        Intent intent = new Intent( Employers_Activity.this, ChangePassword.class );
                        startActivity(intent);
                        drawerLayout_employer.close();
                        return true;
                    }

                    case R.id.menu_change_mode:{
                        sharedPreferences = getSharedPreferences("CheckVT", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("VaiTro",1);
                        editor.apply( );
                        Intent intent = new Intent(Employers_Activity.this, MainScreen.class);
                        startActivity(intent);
                        finishAffinity();
                        return true;
                    }

                    case R.id.menu_Info_employer:{
                        mStore.collection("CompanyInfo").document(mUser.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult( );
                                            if(doc.exists()){
                                                ChangeFragment(new Company_Info());
                                            }else {
                                                showAlertDialog(2);
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener( ) {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "GetCompanyInfo: "+e );
                                    }
                                });
                        return true;
                    }

                    case R.id.menu_Logout_employer:{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Employers_Activity.this);
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
                }
                return false;
            }
        });
    }

    private void showAlertDialog(int typeAlert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa đăng ký thông tin, bạn có muốn đăng ký luôn không ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(typeAlert==1){
                    Intent intent = new Intent( Employers_Activity.this, RegisterInfo.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }else if(typeAlert==2){
                    Intent intent = new Intent( Employers_Activity.this, RegisterEmployerInfo.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                drawerLayout_employer.openDrawer(GravityCompat.START);
                return true;
            }

            default:return super.onOptionsItemSelected(item);
        }
    }

    private void initUI() {
        mStore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance( ).getCurrentUser( );
        toolbar_employer = findViewById(R.id.toolbar_employer);
        nav_employer = findViewById(R.id.nav_employer);
        drawerLayout_employer = findViewById(R.id.drawerLayout_employer);
    }

    private void ChangeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_employer,fragment)
                .disallowAddToBackStack()
                .commit();
        drawerLayout_employer.close();
    };

}