package com.webstores.storema.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.webstores.storema.R;
import com.webstores.storema.activities.OrderDetailsActivity;
import com.webstores.storema.models.PhoneNumber;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;


public class DashboardFragment extends Fragment {


    private FirebaseFirestore dbRoot;
    private String userPhoneNumber;
    private String companyName;
    private TextView viewOrdersReceived, viewCompletedOrders, viewPendingOrders, viewTotalRevenues, viewShippedOrders, viewAcceptedOrders, viewPickedUpOrders, viewTotalDelivered;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewOrdersReceived = view.findViewById(R.id.textview_orders_received);
        viewCompletedOrders = view.findViewById(R.id.text_view_completed_orders);
        viewPendingOrders = view.findViewById(R.id.text_view_pending_orders);
        viewTotalRevenues = view.findViewById(R.id.text_view_total_revenues);
        viewShippedOrders = view.findViewById(R.id.text_view_shipped_orders);
        viewAcceptedOrders = view.findViewById(R.id.text_view_accepted_orders);
        viewPickedUpOrders = view.findViewById(R.id.text_view_picked_up_orders);
        viewTotalDelivered = view.findViewById(R.id.text_view_total_delivered);



        dbRoot = FirebaseFirestore.getInstance();
        userPhoneNumber = getActivity().getIntent().getStringExtra("mobile");
        companyName = getActivity().getIntent().getStringExtra("company_name");

        PhoneNumber phoneNumber = new PhoneNumber(userPhoneNumber, companyName);







        DocumentReference document = dbRoot.collection("_" + userPhoneNumber + "_business").document("data_for_orders");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    viewAcceptedOrders.setText(documentSnapshot.getString("number_of_accepted_orders"));
                    viewOrdersReceived.setText(documentSnapshot.getString("number_of_all_orders"));
                    viewCompletedOrders.setText(documentSnapshot.getString("number_of_delivered_orders"));
                    viewPendingOrders.setText(Integer.parseInt(documentSnapshot.getString("number_of_all_orders"))-Integer.parseInt(documentSnapshot.getString("number_of_accepted_orders"))
                    - Integer.parseInt(documentSnapshot.getString("number_of_rejected_orders"))+"");
                    viewTotalRevenues.setText( new DecimalFormat("##,##,###.##").format( new BigDecimal(documentSnapshot.getString("total_revenue")))+"");
                    viewShippedOrders.setText(documentSnapshot.getString("number_of_shipped_orders"));
                    viewPickedUpOrders.setText(documentSnapshot.getString("number_of_picked_up_orders"));
                    viewTotalDelivered.setText(documentSnapshot.getString("number_of_delivered_orders"));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
