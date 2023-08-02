package com.example.book_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class flashActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // delay 2s rồi vào trang chủ

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },2000);  //2s second
    }

    private void checkUser() {
        //get current user. if logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            //user not logged in
            // vào main screen
            startActivity(new Intent(flashActivity.this, MainActivity.class));
            finish(); //vào activity
        }
        else {
            //user logged in check user type, same as done in login screen
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //get user type
                            String userType =""+snapshot.child("userType").getValue();
                            //check user type
                            if (userType.equals("user")){
                                //phần của simple user, mở user dashboard
                                startActivity(new Intent(flashActivity.this, DashboardUserActivity.class));
                                finish();
                            }
                            else if(userType.equals("admin")){
                                //phần của admin, mở admin dashboard
                                startActivity(new Intent(flashActivity.this, DashboardAdminActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
        }
    }
}