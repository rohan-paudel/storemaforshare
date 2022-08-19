package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.webstores.storema.R;

import java.util.concurrent.TimeUnit;

public class SignUpWIthPhoneActivity extends AppCompatActivity {

    private MaterialCardView btnSignUp;
    private EditText userPhoneNumber, userPassword1, userPassword2;

    private String userPhone, userFirstPassword, userSecondPassword;

    private FirebaseFirestore dbroot;

    private ProgressBar verificationProgressBar;

    private ConstraintLayout signIn;
    boolean isPresent = true;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_phone);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        btnSignUp = findViewById(R.id.btn_sign_up);
        mAuth = FirebaseAuth.getInstance();
        userPhoneNumber = findViewById(R.id.user_phone);
        userPassword1 = findViewById(R.id.user_password1);
        userPassword2 = findViewById(R.id.user_password2);
        verificationProgressBar = findViewById(R.id.verification_progress);
        signIn = findViewById(R.id.sing_in_inside_sign_up);

        signIn.setOnClickListener(v -> {

            Intent signff = new Intent(SignUpWIthPhoneActivity.this, SignInActivity.class);
            startActivity(signff);

        });

        dbroot = FirebaseFirestore.getInstance();


        btnSignUp.setOnClickListener(v -> {
            dbroot.collection('_' + userPhoneNumber.getText().toString() + "_business").document("business_details")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                Toast.makeText(SignUpWIthPhoneActivity.this, "You already have an account. Please sign In", Toast.LENGTH_SHORT).show();
                                isPresent = true;
                            } else {
                                isPresent = false;
                            }
                        }
                    });


            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if (!userPhoneNumber.getText().toString().trim().isEmpty() && userPhoneNumber.getText().length() == 10) {

                if (!userPassword1.getText().toString().trim().isEmpty() && !userPassword2.getText().toString().trim().isEmpty()) {

                    if (userPassword1.getText().length() >= 8 && userPassword2.getText().length() >= 8) {
                        if (userPassword1.getText().toString().trim().equals(userPassword2.getText().toString().trim())) {

                            if (!isPresent) {


                                verificationProgressBar.setVisibility(View.VISIBLE);
                                btnSignUp.setVisibility(View.GONE);

                                userPhone = userPhoneNumber.getText().toString();
                                userFirstPassword = userPassword1.getText().toString();
                                userSecondPassword = userPassword2.getText().toString();

                                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential credential) {


                                        verificationProgressBar.setVisibility(View.GONE);
                                        btnSignUp.setVisibility(View.VISIBLE);

                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        // This callback is invoked in an invalid request for verification is made,
                                        // for instance if the the phone number format is not valid.


                                        verificationProgressBar.setVisibility(View.GONE);
                                        btnSignUp.setVisibility(View.VISIBLE);
                                        Toast.makeText(SignUpWIthPhoneActivity.this, "Sorry : +" + e.getMessage(), Toast.LENGTH_SHORT).show();


                                        // Show a message and update the UI
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        // The SMS verification code has been sent to the provided phone number, we
                                        // now need to ask the user to enter the code and then construct a credential
                                        // by combining the code with a verification ID.
                                        verificationProgressBar.setVisibility(View.GONE);
                                        btnSignUp.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(SignUpWIthPhoneActivity.this, MobileVarificationActivity.class);
                                        intent.putExtra("mobile", userPhone);
                                        intent.putExtra("password", userFirstPassword);
                                        intent.putExtra("verificationId", verificationId);
                                        startActivity(intent);
                                    }
                                };

                                PhoneAuthOptions options =
                                        PhoneAuthOptions.newBuilder(mAuth)
                                                .setPhoneNumber("+91" + userPhone)       // Phone number to verify
                                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                .setActivity(this)                 // Activity (for callback binding)
                                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                .build();
                                PhoneAuthProvider.verifyPhoneNumber(options);

                            }



                        } else {
                            Toast.makeText(this, "Your both password doesnot match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Password lenght is too small", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "Enter password in both fields", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(this, "Enter your phone number correctly.", Toast.LENGTH_SHORT).show();
            }


        });


    }

    private void alreadyPresentAccount() {


    }
}