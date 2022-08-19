package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.webstores.storema.R;
import com.webstores.storema.models.PhoneNumber;

import java.util.HashMap;

public class SetupChargesActivity extends AppCompatActivity {

    private String stringDeliveryCharges;
    private TextView details_on_delivery_charges;
    private MaterialCardView btn_delivery_charges, btn_back_activity_setup_charges;
    private EditText delivery_charges_setup;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup_charges);

        details_on_delivery_charges = findViewById(R.id.details_on_delivery_charges);
        btn_delivery_charges = findViewById(R.id.btn_delivery_charges);
        delivery_charges_setup = findViewById(R.id.delivery_charges_setup);
        btn_back_activity_setup_charges= findViewById(R.id.btn_back_activity_setup_charges);


        db = FirebaseFirestore.getInstance();

        stringDeliveryCharges = getIntent().getStringExtra("delivery_charges");

        details_on_delivery_charges.setText("Your current delivery charge is setup at Rs. "+stringDeliveryCharges+". If you want to change it, please fill the field below and complete it. After this change, only new orders will be affected.");
        delivery_charges_setup.setText(stringDeliveryCharges);
        delivery_charges_setup.requestFocus();


        btn_delivery_charges.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {




                if (!delivery_charges_setup.getText().toString().isEmpty()){

                    View v = SetupChargesActivity.this.getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    HashMap<String, String> items = new HashMap<>();
                    items.put("delivery_charges", delivery_charges_setup.getText().toString());

                    db.collection("_"+ PhoneNumber.getPhoneNumber()+"_business").document("business_details")
                            .set(items, SetOptions.merge()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SetupChargesActivity.this, "Successfully changed your delivery charges.", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SetupChargesActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(SetupChargesActivity.this, "Delivery Charges cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_back_activity_setup_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });






    }
}