package com.webstores.storema.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.webstores.storema.MainActivity;
import com.webstores.storema.R;
import com.webstores.storema.activities.AddBusinessActivity;
import com.webstores.storema.activities.AppActivity;
import com.webstores.storema.activities.ContactUsActivity;
import com.webstores.storema.activities.EditCompanyActivity;
import com.webstores.storema.activities.SetupChargesActivity;
import com.webstores.storema.activities.ShareStoreLinkActivity;
import com.webstores.storema.activities.SignInActivity;
import com.webstores.storema.models.PhoneNumber;

import java.util.HashMap;


public class SettingsFragment extends Fragment {

    private MaterialButton btnEditBusiness;
    private FirebaseFirestore dbRoot;
    private TextView viewCompanyName;
    private SwitchMaterial activeBusinessSwitch;
    private ImageView companyIconImage;
    private MaterialCardView btnShareStoreLink;
    private String stringStoreLink = "", deliveryCharges = "";
    private MaterialCardView btn_setup_charges, btn_privacy_policy, btn_terms_of_use;
    private String privacyPolicy = "https://www.storema.in/documents/privacypolicy.html";
    private String termsOfUse = "https://www.storema.in/documents/termsOfUse.html";
    private MaterialCardView btn_log_out;
    private MaterialCardView contact_us;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnEditBusiness = view.findViewById(R.id.btn_edit_business);
        viewCompanyName = view.findViewById(R.id.text_view_company_name);
        activeBusinessSwitch = view.findViewById(R.id.switch1);
        companyIconImage = view.findViewById(R.id.img_business_icon_setting_fragment);
        btnShareStoreLink = view.findViewById(R.id.btn_share_store_link);
        btn_setup_charges = view.findViewById(R.id.btn_setup_charges);
        btn_privacy_policy = view.findViewById(R.id.btn_privacy_policy);
        btn_terms_of_use = view.findViewById(R.id.btn_terms_of_use);
        btn_log_out = view.findViewById(R.id.btn_log_out);

        contact_us = view.findViewById(R.id.contact_us);




        dbRoot = FirebaseFirestore.getInstance();

        btnEditBusiness.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditCompanyActivity.class);
            startActivity(intent);
        });

        activeBusinessSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                                if (b) {
                                                                    HashMap<String, String> items = new HashMap<>();

                                                                    items.put("is_business_active", "1");

                                                                    dbRoot.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("business_details")
                                                                            .set(items, SetOptions.merge()).
                                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                }
                                                                else if(!b){


                                                                    HashMap<String, String> items = new HashMap<>();

                                                                    items.put("is_business_active", "0");

                                                                    dbRoot.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("business_details")
                                                                            .set(items, SetOptions.merge()).
                                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        }
        );





        //Refresh was here







        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        btnShareStoreLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShareStoreLinkActivity.class);
                intent.putExtra("store_link", stringStoreLink);
                intent.putExtra("company_name", viewCompanyName.getText().toString());
                startActivity(intent);

            }
        });


        btn_setup_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getContext(), SetupChargesActivity.class);


                DocumentReference documentReference = dbRoot.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("business_details");
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            deliveryCharges = documentSnapshot.getString("delivery_charges");
                            intent2.putExtra("delivery_charges", deliveryCharges);
                            startActivity(intent2);

                        }
                    }
                });


            }
        });

        btn_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(privacyPolicy);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });


        btn_terms_of_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(termsOfUse);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });


        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getActivity().getSharedPreferences("SignInStored", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.putString("is_sign_in", "0");
                myEdit.apply();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(stringStoreLink)
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
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Log out successful", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    void refresh(){
        DocumentReference documentReference = dbRoot.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("business_details");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    viewCompanyName.setText(documentSnapshot.getString("business_name"));
                    String switchStatus = documentSnapshot.getString("is_business_active");

                    activeBusinessSwitch.setChecked(switchStatus != null && switchStatus.equals("1"));
                    stringStoreLink = documentSnapshot.getString("store_link");
                    deliveryCharges = documentSnapshot.getString("delivery_charges");
                    Glide.with(getContext())
                            .load(documentSnapshot.getString("business_logo"))
                            .into(companyIconImage);

                }
            }
        });
    }
}