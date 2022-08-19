package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.webstores.storema.R;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {

    private TextView textPhoneNumber, textPassword;

    private String stringPhoneNumber, stringPassword;

    private MaterialCardView btnSignIn;

    private FirebaseFirestore dbRoot;

    private ProgressBar signInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        dbRoot = FirebaseFirestore.getInstance();

        textPassword = findViewById(R.id.user_password);
        textPhoneNumber = findViewById(R.id.user_phone_sign_in);
        btnSignIn = findViewById(R.id.btn_sign_in);
        signInProgress = findViewById(R.id.sign_in_progress);

        btnSignIn.setOnClickListener(v -> {

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            signInProgress.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);

            if(!(textPhoneNumber.getText().toString().trim().isEmpty()) && !(textPassword.getText().toString().trim().isEmpty())){

                if(textPhoneNumber.getText().length() == 10 && textPassword.getText().length() >= 8){

                    stringPhoneNumber = textPhoneNumber.getText().toString();
                    stringPassword = textPassword.getText().toString();

                    DocumentReference documentReference = dbRoot.collection("_"+stringPhoneNumber+"_business").document("business_details");

                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){

                                if(stringPassword.equals(documentSnapshot.getString("password"))){

                                    if(documentSnapshot.getString("is_business_added").equals("1")){

                                        SharedPreferences sh = getSharedPreferences("SignInStored", MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sh.edit();
                                        myEdit.putString("is_sign_in", "1");
                                        myEdit.putString("phone_number", stringPhoneNumber);
                                        myEdit.putString("company_name", documentSnapshot.getString("business_name"));
                                        myEdit.apply();
                                        String link = documentSnapshot.getString("store_link");
                                        FirebaseMessaging.getInstance().subscribeToTopic(link)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Subscribed";
                                                        if (!task.isSuccessful()) {
                                                            msg = "Subscribe failed";
                                                        }
                                                        Log.d("Hello", msg);
                                                    }
                                                });

                                        Intent intent = new Intent(SignInActivity.this, AppActivity.class);
                                        intent.putExtra("mobile", stringPhoneNumber);
                                        intent.putExtra("company_name", documentSnapshot.getString("business_name"));
                                        startActivity(intent);
                                        Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(SignInActivity.this, "Please first add your business.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignInActivity.this, AddBusinessActivity.class);

                                        intent.putExtra("mobile", stringPhoneNumber);

                                        startActivity(intent);
                                    }



                                }else {
                                    signInProgress.setVisibility(View.GONE);
                                    btnSignIn.setVisibility(View.VISIBLE);
                                    Toast.makeText(SignInActivity.this, "Password didn't matched", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(SignInActivity.this, "Please first make an account.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this, SignUpWIthPhoneActivity.class);
                                startActivity(intent);
                            }




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            signInProgress.setVisibility(View.GONE);
                            btnSignIn.setVisibility(View.VISIBLE);
                            Toast.makeText(SignInActivity.this, "Something Error occured.", Toast.LENGTH_SHORT).show();
                        }
                    });



                }
                else {
                    signInProgress.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Please check phone number once", Toast.LENGTH_SHORT).show();
                }


            } else {
                signInProgress.setVisibility(View.GONE);
                btnSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Please enter the complete details.", Toast.LENGTH_SHORT).show();
            }

        });


    }
}