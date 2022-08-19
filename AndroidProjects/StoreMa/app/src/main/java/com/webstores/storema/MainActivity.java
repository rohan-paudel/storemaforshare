package com.webstores.storema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.splashscreen.SplashScreen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.webstores.storema.activities.AddItemsActivity;
import com.webstores.storema.activities.AppActivity;
import com.webstores.storema.activities.SignInActivity;
import com.webstores.storema.activities.SignUpWIthPhoneActivity;
import com.webstores.storema.models.PhoneNumber;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTermsAndCondition;
    private MaterialCardView btnSignUpWithPhoneNumber;
    private ConstraintLayout btnSignIn;
    private String phoneNumber="", companyName = "";
    private String ifSignIn ="";
    private long backPressedTime = 0L;
    private FirebaseFirestore db;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTermsAndCondition = findViewById(R.id.text_view_terms_of_conditions);
        btnSignUpWithPhoneNumber = findViewById(R.id.btn_sign_up_with_phone_number);

        btnSignIn = findViewById(R.id.btn_sign_in);
        db = FirebaseFirestore.getInstance();


        textViewTermsAndCondition.setText(Html.fromHtml(getString(R.string.string_terms_and_others)));
        textViewTermsAndCondition.setMovementMethod(LinkMovementMethod.getInstance());

        btnSignUpWithPhoneNumber.setOnClickListener(v -> {
            Intent intentSignUpWithPhoneNumber = new Intent(MainActivity.this, SignUpWIthPhoneActivity.class);
            startActivity(intentSignUpWithPhoneNumber);
        });

        btnSignIn.setOnClickListener(v -> {
            Intent signIn = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signIn);
        });



        SharedPreferences sh = getSharedPreferences("SignInStored", MODE_PRIVATE);
        ifSignIn = sh.getString("is_sign_in", "");









        if(!ifSignIn.isEmpty()){
            if(ifSignIn.equals("1")){
                phoneNumber = sh.getString("phone_number", "");
                companyName = sh.getString("company_name", "");


                Intent intent = new Intent(MainActivity.this, AppActivity.class);
                intent.putExtra("mobile", phoneNumber);
                intent.putExtra("company_name", companyName);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000> System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show();
        }

        backPressedTime =  System.currentTimeMillis();
    }
}