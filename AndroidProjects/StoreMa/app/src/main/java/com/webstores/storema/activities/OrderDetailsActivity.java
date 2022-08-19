package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.webstores.storema.R;
import com.webstores.storema.adapters.OrdersSubListAdapter;
import com.webstores.storema.models.OrdersSubListModel;
import com.webstores.storema.models.PhoneNumber;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {

    private OrdersSubListAdapter ordersSubListAdapter;
    private RecyclerView subOrderListRecyclerView;
    private OrdersSubListModel ordersSubListModel;

    private MaterialCardView btn_back_activity_order_details, btn_update_status, btn_reject_order, btn_inform_customer;

    private TextView update_status_text_view;


    String totalOrders, deliveryCharges, customerName, customerPhone, customerEmail, customerAddress, orderStatus, orderNumber, orderDate;
    ArrayList<String> ordersPrice, ordersName, ordersImage, ordersQuantity, ordersGst;
    private int intTotalOrders;

    private TextView total_items_price, items_delivery_charge, items_total_net, order_number, date, text_customer_name, text_customer_phone, text_customer_email, text_customer_address, items_gst;

    private float intTotalItemPrice = 0;
    private float intAllTotalPrice = 0;
    private float intAllTotalGST = 0;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        subOrderListRecyclerView = findViewById(R.id.sub_order_list_recycler_view);
        db = FirebaseFirestore.getInstance();

        total_items_price = findViewById(R.id.total_items_price);
        items_delivery_charge = findViewById(R.id.items_delivery_charge);
        items_total_net = findViewById(R.id.items_total_net);
        btn_update_status = findViewById(R.id.btn_update_status);

        btn_reject_order = findViewById(R.id.btn_reject_order);
        btn_inform_customer = findViewById(R.id.btn_inform_customer);
        items_gst = findViewById(R.id.items_gst);

        update_status_text_view = findViewById(R.id.update_status_text_view);

        btn_back_activity_order_details = findViewById(R.id.btn_back_activity_order_details);


        order_number = findViewById(R.id.order_number);
        date = findViewById(R.id.date);


        text_customer_name = findViewById(R.id.text_customer_name);
        text_customer_phone = findViewById(R.id.text_customer_phone);
        text_customer_email = findViewById(R.id.text_customer_email);
        text_customer_address = findViewById(R.id.text_customer_address);


        ordersSubListAdapter = new OrdersSubListAdapter();

        totalOrders = getIntent().getStringExtra("totalOrders");
        intTotalOrders = Integer.parseInt(totalOrders);



        deliveryCharges = getIntent().getStringExtra("deliveryCharges");

        items_delivery_charge.setText(deliveryCharges);

        orderNumber = getIntent().getStringExtra("orderNumber");
        customerName = getIntent().getStringExtra("customerName");
        customerPhone = getIntent().getStringExtra("customerPhone");
        customerEmail = getIntent().getStringExtra("customerEmail");
        customerAddress = getIntent().getStringExtra("customerAddress");
        orderStatus = getIntent().getStringExtra("orderStatus");
        ordersPrice = getIntent().getStringArrayListExtra("ordersPrice");
        ordersGst = getIntent().getStringArrayListExtra("ordersGst");
        ordersName = getIntent().getStringArrayListExtra("ordersName");
        ordersImage = getIntent().getStringArrayListExtra("ordersImage");
        ordersQuantity = getIntent().getStringArrayListExtra("ordersQuantity");
        orderDate = getIntent().getStringExtra("orderDate");


        for(int i = 0; i < intTotalOrders; i++) {

            ordersSubListModel = new OrdersSubListModel(ordersName.get(i), ordersPrice.get(i), ordersImage.get(i),
                    ordersQuantity.get(i));

            ordersSubListAdapter.updateItemsList(ordersSubListModel);

            intTotalItemPrice = intTotalItemPrice + (Float.parseFloat(ordersPrice.get(i))*Float.parseFloat(ordersQuantity.get(i)));
            intAllTotalGST = intAllTotalGST+(Float.parseFloat(ordersGst.get(i))*Float.parseFloat(ordersQuantity.get(i)));


        }

        items_gst.setText(String. format("%.02f",intAllTotalGST)+"");
        intAllTotalPrice = intTotalItemPrice+Float.parseFloat(deliveryCharges)+intAllTotalGST;

        total_items_price.setText(String. format("%.02f",intTotalItemPrice)+"");

        items_total_net.setText(String. format("%.02f",intAllTotalPrice)+"");

        order_number.setText(orderNumber);

        if(orderStatus.equals("rejected")){
            order_number.setTextColor(Color.parseColor("#50cc0000"));
        }
        else if(orderStatus.equals("new")){

            order_number.setTextColor(Color.parseColor("#00cc00"));
        } else if(orderStatus.equals("delivered")) {
            order_number.setTextColor(Color.parseColor("#50000000"));
        } else {
            order_number.setTextColor(Color.parseColor("#f00000cc"));
        }



        date.setText(orderDate);
        text_customer_name.setText(customerName);
        text_customer_phone.setText("+91-"+customerPhone);
        text_customer_email.setText(customerEmail);
        text_customer_address.setText(customerAddress);


        btn_back_activity_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        setUpAllThings();

        btn_inform_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendSMSIntent = new Intent(Intent.ACTION_VIEW);
                sendSMSIntent.setData(Uri.parse("smsto:"));
                sendSMSIntent.setType("vnd.android-dir/mms-sms");
                sendSMSIntent.putExtra("address",  "+91-"+customerPhone);
                sendSMSIntent.putExtra("sms_body", "Hello "+text_customer_name.getText().toString()+", \nThis is to notify you that your order "+orderNumber+" status is updated to "+orderStatus+".\nFor any information needed please contact our business.\n"+
                        "From: \n"+PhoneNumber.getCompanyName()+"\n"+PhoneNumber.getPhoneNumber());
                
                try {
                    startActivity(sendSMSIntent);
                } catch(android.content.ActivityNotFoundException e){
                    Toast.makeText(OrderDetailsActivity.this, "Sms sending failed", Toast.LENGTH_SHORT).show();
                    
                }
                
            }
        });









        subOrderListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        subOrderListRecyclerView.setAdapter(ordersSubListAdapter);
























    }




    private void setUpAllThings() {
        if(orderStatus.equals("rejected")){

            btn_update_status.setClickable(false);

            btn_update_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#50cc0000")));

            update_status_text_view.setTextColor(Color.WHITE);


            update_status_text_view.setText("Order is rejected");

            btn_reject_order.setVisibility(View.GONE);

        }


        if(orderStatus.equals("new")){

            btn_update_status.setClickable(true);
            btn_reject_order.setClickable(true);



            btn_reject_order.setVisibility(View.VISIBLE);

            btn_reject_order.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    btn_update_status.setClickable(false);
                    btn_reject_order.setClickable(false);

                    new AlertDialog.Builder(OrderDetailsActivity.this)
                            .setTitle("Reject order")
                            .setMessage("Are you sure, you want to reject this order?")
                            .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {



                                    HashMap<String, String> items = new HashMap<>();
                                    items.put("order_status", "rejected");

                                    db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").document(orderNumber+"")
                                            .set(items, SetOptions.merge()).
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {


                                                    DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("data_for_orders");
                                                    document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            if (documentSnapshot.exists()) {

                                                                int oldrejectedOrder = Integer.parseInt(documentSnapshot.getString("number_of_rejected_orders"));

                                                                HashMap<String, String> updateNumber = new HashMap<>();
                                                                updateNumber.put("number_of_rejected_orders", oldrejectedOrder + 1 + "");

                                                                db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("data_for_orders")
                                                                        .set(updateNumber, SetOptions.merge()).
                                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(OrderDetailsActivity.this, "Order status set to rejected", Toast.LENGTH_SHORT).show();
                                                                                orderStatus = "rejected";


                                                                                setUpAllThings();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }



                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            });


                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    btn_update_status.setClickable(true);
                                    btn_reject_order.setClickable(true);

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show();

                    }});

            update_status_text_view.setText("Accept order");

            btn_update_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    btn_update_status.setClickable(false);

                    btn_reject_order.setVisibility(View.GONE);

                    HashMap<String, String> items = new HashMap<>();
                    items.put("order_status", "accepted");

                    db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").document(orderNumber+"")
                            .set(items, SetOptions.merge()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("data_for_orders");
                                    document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {

                                                int oldAcceptedOrder = Integer.parseInt(documentSnapshot.getString("number_of_accepted_orders"));

                                                HashMap<String, String> updateNumber = new HashMap<>();
                                                updateNumber.put("number_of_accepted_orders", oldAcceptedOrder+1+"");

                                                db.collection("_"+ PhoneNumber.getPhoneNumber()+"_business").document("data_for_orders")
                                                        .set(updateNumber, SetOptions.merge()).
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                                                                orderStatus = "accepted";
                                                                setUpAllThings();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });



                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            });





        }


        if(orderStatus.equals("accepted")) {
            btn_update_status.setClickable(true);
            update_status_text_view.setText("Set order to shipped");
            btn_reject_order.setVisibility(View.GONE);

            btn_update_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    btn_update_status.setClickable(false);

                    HashMap<String, String> items = new HashMap<>();
                    items.put("order_status", "shipped");

                    db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").document(orderNumber+"")
                            .set(items, SetOptions.merge()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {



                                    DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("data_for_orders");
                                    document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {

                                                int oldShippedOrder = Integer.parseInt(documentSnapshot.getString("number_of_shipped_orders"));

                                                HashMap<String, String> updateNumber = new HashMap<>();
                                                updateNumber.put("number_of_shipped_orders", oldShippedOrder+1+"");

                                                db.collection("_"+ PhoneNumber.getPhoneNumber()+"_business").document("data_for_orders")
                                                        .set(updateNumber, SetOptions.merge()).
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Order status set to shipped", Toast.LENGTH_SHORT).show();
                                                                orderStatus = "shipped";
                                                                setUpAllThings();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });



                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

        }


        if(orderStatus.equals("shipped")) {

            btn_update_status.setClickable(true);

            update_status_text_view.setText("Set order to picked up");
            btn_reject_order.setVisibility(View.GONE);


            btn_update_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    btn_update_status.setClickable(false);

                    HashMap<String, String> items = new HashMap<>();
                    items.put("order_status", "picked");

                    db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").document(orderNumber+"")
                            .set(items, SetOptions.merge()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {



                                    DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("data_for_orders");
                                    document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {

                                                int oldPickedUpOrder = Integer.parseInt(documentSnapshot.getString("number_of_picked_up_orders"));

                                                HashMap<String, String> updateNumber = new HashMap<>();
                                                updateNumber.put("number_of_picked_up_orders", oldPickedUpOrder+1+"");

                                                db.collection("_"+ PhoneNumber.getPhoneNumber()+"_business").document("data_for_orders")
                                                        .set(updateNumber, SetOptions.merge()).
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Order status set to picked up", Toast.LENGTH_SHORT).show();
                                                                orderStatus = "picked";
                                                                setUpAllThings();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });



                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });


        }

        if(orderStatus.equals("picked")){

            btn_update_status.setClickable(true);


            update_status_text_view.setText("Set order to delivered");
            btn_reject_order.setVisibility(View.GONE);

            btn_update_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    btn_update_status.setClickable(false);


                    HashMap<String, String> items = new HashMap<>();
                    items.put("order_status", "delivered");

                    db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").document(orderNumber+"")
                            .set(items, SetOptions.merge()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {



                                    DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("data_for_orders");
                                    document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {

                                                int oldDeliveredOrder = Integer.parseInt(documentSnapshot.getString("number_of_delivered_orders"));
                                                float oldReviue = Float.parseFloat(documentSnapshot.getString("total_revenue"));

                                                HashMap<String, String> updateNumber = new HashMap<>();
                                                updateNumber.put("number_of_delivered_orders", oldDeliveredOrder+1+"");
                                                updateNumber.put("total_revenue", oldReviue+Float.parseFloat(items_total_net.getText().toString())+"");


                                                db.collection("_"+ PhoneNumber.getPhoneNumber()+"_business").document("data_for_orders")
                                                        .set(updateNumber, SetOptions.merge()).
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Order status set to delivered", Toast.LENGTH_SHORT).show();


                                                                orderStatus = "delivered";
                                                                setUpAllThings();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });



                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    });




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

        }

        if(orderStatus.equals("delivered")){

            btn_update_status.setClickable(false);
            btn_reject_order.setVisibility(View.GONE);

            btn_update_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));

            update_status_text_view.setTextColor(Color.BLACK);

            update_status_text_view.setText("The order is completed");

        }
    }



}