package com.webstores.storema.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.webstores.storema.R;
import com.webstores.storema.adapters.OrdersListAdapter;
import com.webstores.storema.models.ItemsModel;
import com.webstores.storema.models.OrdersModel;
import com.webstores.storema.models.PhoneNumber;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrdersFragment extends Fragment implements TextWatcher {

    private RecyclerView ordersRecyclerList;
    private OrdersListAdapter ordersListAdapter;
    private FirebaseFirestore db;
    private OrdersModel orderModel;
    private EditText search_term;

    private ProgressBar progressBar;
    private MaterialCardView cardProgress;

    private TextView btn_filter_all, btn_filter_accepted, btn_filter_shipped, btn_filter_picked, btn_filter_delivered, btn_filter_rejected;

    private TextView no_information_text, no_search_result;

    private HorizontalScrollView horizontal_scroll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRecyclerList = view.findViewById(R.id.orders_list_recycler_view);
        search_term = view.findViewById(R.id.search_term);
        db = FirebaseFirestore.getInstance();
        btn_filter_all = view.findViewById(R.id.btn_filter_all);
        btn_filter_accepted = view.findViewById(R.id.btn_filter_accepted);
        btn_filter_shipped = view.findViewById(R.id.btn_filter_shipped);
        btn_filter_picked = view.findViewById(R.id.btn_filter_picked);
        btn_filter_delivered = view.findViewById(R.id.btn_filter_delivered);
        btn_filter_rejected = view.findViewById(R.id.btn_filter_rejected);
        no_information_text = view.findViewById(R.id.no_information_text);
        no_search_result = view.findViewById(R.id.no_search_result);
        horizontal_scroll = view.findViewById(R.id.horizontal_scroll);

        progressBar = view.findViewById(R.id.progress);
        cardProgress = view.findViewById(R.id.card_progress);

        horizontal_scroll.scrollTo(0,0);

        search_term.addTextChangedListener(OrdersFragment.this);


        btn_filter_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search_term.setText("");

                no_search_result.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                btn_filter_all.setTextColor(Color.parseColor("#000000"));
                btn_filter_accepted.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_shipped.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_picked.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_delivered.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_rejected.setTextColor(Color.parseColor("#FF5856D6"));
                showData();

            }
        });

        btn_filter_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                no_search_result.setVisibility(View.GONE);
                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                search_term.removeTextChangedListener(OrdersFragment.this);
                search_term.setText("");
                search_term.addTextChangedListener(OrdersFragment.this);

                progressBar.setVisibility(View.VISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                btn_filter_all.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_accepted.setTextColor(Color.parseColor("#000000"));
                btn_filter_shipped.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_picked.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_delivered.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_rejected.setTextColor(Color.parseColor("#FF5856D6"));


                filterButtonClicked("accepted");
            }
        });

        btn_filter_shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                no_search_result.setVisibility(View.GONE);

                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                search_term.removeTextChangedListener(OrdersFragment.this);
                search_term.setText("");
                search_term.addTextChangedListener(OrdersFragment.this);

                progressBar.setVisibility(View.VISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                btn_filter_all.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_accepted.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_shipped.setTextColor(Color.parseColor("#000000"));
                btn_filter_picked.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_delivered.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_rejected.setTextColor(Color.parseColor("#FF5856D6"));

                filterButtonClicked("shipped");

            }
        });

        btn_filter_picked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                no_search_result.setVisibility(View.GONE);

                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                search_term.removeTextChangedListener(OrdersFragment.this);
                search_term.setText("");
                search_term.addTextChangedListener(OrdersFragment.this);


                progressBar.setVisibility(View.VISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                btn_filter_all.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_accepted.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_shipped.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_picked.setTextColor(Color.parseColor("#000000"));
                btn_filter_delivered.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_rejected.setTextColor(Color.parseColor("#FF5856D6"));

                filterButtonClicked("picked");

            }
        });

        btn_filter_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                no_search_result.setVisibility(View.GONE);

                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                search_term.removeTextChangedListener(OrdersFragment.this);
                search_term.setText("");
                search_term.addTextChangedListener(OrdersFragment.this);


                progressBar.setVisibility(View.VISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                btn_filter_all.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_accepted.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_shipped.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_picked.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_delivered.setTextColor(Color.parseColor("#000000"));
                btn_filter_rejected.setTextColor(Color.parseColor("#FF5856D6"));

                filterButtonClicked("delivered");

            }
        });

        btn_filter_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                no_search_result.setVisibility(View.GONE);

                View view1 = getActivity().getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                search_term.removeTextChangedListener(OrdersFragment.this);
                search_term.setText("");
                search_term.addTextChangedListener(OrdersFragment.this);


                progressBar.setVisibility(View.VISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                btn_filter_all.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_accepted.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_shipped.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_picked.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_delivered.setTextColor(Color.parseColor("#FF5856D6"));
                btn_filter_rejected.setTextColor(Color.parseColor("#000000"));

                filterButtonClicked("rejected");

            }
        });

        //black  = -16777216
        //button = -10987818









        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                ordersListAdapter = new OrdersListAdapter();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ordersRecyclerList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        ordersRecyclerList.setAdapter(ordersListAdapter);
                    }
                });



            }
        });










        return view;
    }

    private void filterButtonClicked(String statusss) {

        DocumentReference documentReference = db.collection("_"+PhoneNumber.getPhoneNumber()+"_orders").document();
        db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").whereEqualTo("order_status", statusss).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        receiveData(task);


                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void receiveData(Task<QuerySnapshot> task) {


        if(task.isSuccessful()){
            ordersListAdapter.clearData();

            if(task.getResult().size()>0){


                for(DocumentSnapshot snapshot: task.getResult()){

                    String totalOrders = snapshot.getString("total_orders");
                    int intTotalOrders = Integer.parseInt(totalOrders);
                    ArrayList<String> ordersID = new ArrayList<>();
                    ArrayList<String> ordersPrice = new ArrayList<>();
                    ArrayList<String> ordersQuantity = new ArrayList<>();
                    ArrayList<String> ordersItemName = new ArrayList<>();
                    ArrayList<String> ordersImage = new ArrayList<>();
                    ArrayList<String> ordersGst = new ArrayList<>();


                    int number = 0;

                    for (int i = 0; i< intTotalOrders; i++){
                        number = number +1;

                        String orderNames = "order";
                        String orderPrices = "price";
                        String orderQuantities = "value";
                        String orderItemName = "order_name";
                        String orderImage = "order_image";
                        String orderGst = "gst";

                        orderNames = orderNames +number+"";
                        orderPrices = orderPrices+number+"";
                        orderQuantities = orderQuantities +number+"";
                        orderItemName = orderItemName +number+"";
                        orderImage = orderImage + number+"";
                        orderGst = orderGst+number+"";

                        ordersID.add(snapshot.getString(orderNames));
                        ordersPrice.add(snapshot.getString(orderPrices));
                        ordersQuantity.add(snapshot.getString(orderQuantities));
                        ordersItemName.add(snapshot.getString(orderItemName));
                        ordersImage.add(snapshot.getString(orderImage));
                        ordersGst.add(snapshot.getString(orderGst));
                    }


                    orderModel = new OrdersModel(snapshot.getString("order_number"),
                            snapshot.getString("order_date"), snapshot.getString("customer_name"),
                            snapshot.getString("customer_phone"), snapshot.getString("customer_email"),
                            snapshot.getString("customer_address"), ordersID, ordersPrice, ordersQuantity, snapshot.getString("delivery_charges"),
                            snapshot.getString("order_status"), ordersItemName, ordersImage, totalOrders, ordersGst);


                    ordersListAdapter.updateItemsList(orderModel);

                    progressBar.setVisibility(View.GONE);
                    cardProgress.setVisibility(View.GONE);
                    no_information_text.setVisibility(View.GONE);



                }

            } else {
                orderModel = null;
                ordersListAdapter.updateItemsList(orderModel);
                progressBar.setVisibility(View.GONE);
                cardProgress.setVisibility(View.GONE);
                no_information_text.setVisibility(View.VISIBLE);


            }




//                            progressBar.setVisibility(View.GONE);
//                            cardProgress.setVisibility(View.GONE);
        }



    }


    private void showDataAccordingToNumber(String number){

        DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document();
        db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").orderBy("order_number").startAt(number).endAt(number+"\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if(task.isSuccessful()){
                            ordersListAdapter.clearData();

                            if(task.getResult().size()>0){

                                for(DocumentSnapshot snapshot: task.getResult()){

                                    String totalOrders = snapshot.getString("total_orders");
                                    int intTotalOrders = Integer.parseInt(totalOrders);
                                    ArrayList<String> ordersID = new ArrayList<>();
                                    ArrayList<String> ordersPrice = new ArrayList<>();
                                    ArrayList<String> ordersQuantity = new ArrayList<>();
                                    ArrayList<String> ordersItemName = new ArrayList<>();
                                    ArrayList<String> ordersImage = new ArrayList<>();
                                    ArrayList<String> ordersGst = new ArrayList<>();


                                    int number = 0;

                                    for (int i = 0; i< intTotalOrders; i++){
                                        number = number +1;

                                        String orderNames = "order";
                                        String orderPrices = "price";
                                        String orderQuantities = "value";
                                        String orderItemName = "order_name";
                                        String orderImage = "order_image";
                                        String orderGst = "gst";

                                        orderNames = orderNames +number+"";
                                        orderPrices = orderPrices+number+"";
                                        orderQuantities = orderQuantities +number+"";
                                        orderItemName = orderItemName +number+"";
                                        orderImage = orderImage + number+"";
                                        orderGst = orderGst+number+"";

                                        ordersID.add(snapshot.getString(orderNames));
                                        ordersPrice.add(snapshot.getString(orderPrices));
                                        ordersQuantity.add(snapshot.getString(orderQuantities));
                                        ordersItemName.add(snapshot.getString(orderItemName));
                                        ordersImage.add(snapshot.getString(orderImage));
                                        ordersGst.add(snapshot.getString(orderGst));
                                    }


                                    orderModel = new OrdersModel(snapshot.getString("order_number"),
                                            snapshot.getString("order_date"), snapshot.getString("customer_name"),
                                            snapshot.getString("customer_phone"), snapshot.getString("customer_email"),
                                            snapshot.getString("customer_address"), ordersID, ordersPrice, ordersQuantity, snapshot.getString("delivery_charges"),
                                            snapshot.getString("order_status"), ordersItemName, ordersImage, totalOrders, ordersGst);


                                    ordersListAdapter.updateItemsList(orderModel);

                                    no_search_result.setVisibility(View.GONE);


                                }

                            } else {
                                orderModel = null;
                                ordersListAdapter.updateItemsList(orderModel);
                                no_search_result.setVisibility(View.VISIBLE);

                            }




//                            progressBar.setVisibility(View.GONE);
//                            cardProgress.setVisibility(View.GONE);
                        }








                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
                    }
                });






    }


    private void showDataAccordingToName(String name) {


        DocumentReference document = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document();
        db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").orderBy("customer_name").startAt(name).endAt(name+"\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            ordersListAdapter.clearData();

                            if(task.getResult().size()>0){

                                for(DocumentSnapshot snapshot: task.getResult()){

                                    String totalOrders = snapshot.getString("total_orders");
                                    int intTotalOrders = Integer.parseInt(totalOrders);
                                    ArrayList<String> ordersID = new ArrayList<>();
                                    ArrayList<String> ordersPrice = new ArrayList<>();
                                    ArrayList<String> ordersQuantity = new ArrayList<>();
                                    ArrayList<String> ordersItemName = new ArrayList<>();
                                    ArrayList<String> ordersImage = new ArrayList<>();
                                    ArrayList<String> ordersGst = new ArrayList<>();


                                    int number = 0;

                                    for (int i = 0; i< intTotalOrders; i++){
                                        number = number +1;

                                        String orderNames = "order";
                                        String orderPrices = "price";
                                        String orderQuantities = "value";
                                        String orderItemName = "order_name";
                                        String orderImage = "order_image";
                                        String orderGst = "gst";

                                        orderNames = orderNames +number+"";
                                        orderPrices = orderPrices+number+"";
                                        orderQuantities = orderQuantities +number+"";
                                        orderItemName = orderItemName +number+"";
                                        orderImage = orderImage + number+"";
                                        orderGst = orderGst+number+"";

                                        ordersID.add(snapshot.getString(orderNames));
                                        ordersPrice.add(snapshot.getString(orderPrices));
                                        ordersQuantity.add(snapshot.getString(orderQuantities));
                                        ordersItemName.add(snapshot.getString(orderItemName));
                                        ordersImage.add(snapshot.getString(orderImage));
                                        ordersGst.add(snapshot.getString(orderGst));
                                    }


                                    orderModel = new OrdersModel(snapshot.getString("order_number"),
                                            snapshot.getString("order_date"), snapshot.getString("customer_name"),
                                            snapshot.getString("customer_phone"), snapshot.getString("customer_email"),
                                            snapshot.getString("customer_address"), ordersID, ordersPrice, ordersQuantity, snapshot.getString("delivery_charges"),
                                            snapshot.getString("order_status"), ordersItemName, ordersImage, totalOrders, ordersGst);


                                    ordersListAdapter.updateItemsList(orderModel);
                                    no_search_result.setVisibility(View.GONE);



                                }

                            } else {
                                orderModel = null;
                                ordersListAdapter.updateItemsList(orderModel);
                                no_search_result.setVisibility(View.VISIBLE);

                            }








                    }
                }

    });}



    private void showData() {

        db.collection("_"+ PhoneNumber.getPhoneNumber()+"_orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){


                            ordersListAdapter.clearData();

                            if(task.getResult().size()>0) {
                                for (DocumentSnapshot snapshot : task.getResult()) {

                                    String totalOrders = snapshot.getString("total_orders");
                                    int intTotalOrders = Integer.parseInt(totalOrders);
                                    ArrayList<String> ordersID = new ArrayList<>();
                                    ArrayList<String> ordersPrice = new ArrayList<>();
                                    ArrayList<String> ordersQuantity = new ArrayList<>();
                                    ArrayList<String> ordersItemName = new ArrayList<>();
                                    ArrayList<String> ordersImage = new ArrayList<>();
                                    ArrayList<String> ordersGst = new ArrayList<>();


                                    int number = 0;

                                    for (int i = 0; i < intTotalOrders; i++) {
                                        number = number + 1;

                                        String orderNames = "order";
                                        String orderPrices = "price";
                                        String orderQuantities = "value";
                                        String orderItemName = "order_name";
                                        String orderImage = "order_image";
                                        String orderGst = "gst";

                                        orderNames = orderNames + number + "";
                                        orderPrices = orderPrices + number + "";
                                        orderQuantities = orderQuantities + number + "";
                                        orderItemName = orderItemName + number + "";
                                        orderImage = orderImage + number + "";
                                        orderGst = orderGst+number+"";

                                        ordersID.add(snapshot.getString(orderNames));
                                        ordersPrice.add(snapshot.getString(orderPrices));
                                        ordersQuantity.add(snapshot.getString(orderQuantities));
                                        ordersItemName.add(snapshot.getString(orderItemName));
                                        ordersImage.add(snapshot.getString(orderImage));
                                        ordersGst.add(snapshot.getString(orderGst));
                                    }


                                    orderModel = new OrdersModel(snapshot.getString("order_number"),
                                            snapshot.getString("order_date"), snapshot.getString("customer_name"),
                                            snapshot.getString("customer_phone"), snapshot.getString("customer_email"),
                                            snapshot.getString("customer_address"), ordersID, ordersPrice, ordersQuantity, snapshot.getString("delivery_charges"),
                                            snapshot.getString("order_status"), ordersItemName, ordersImage, totalOrders, ordersGst);


                                    ordersListAdapter.updateItemsList(orderModel);
                                    progressBar.setVisibility(View.GONE);
                                    cardProgress.setVisibility(View.GONE);

                                        no_information_text.setVisibility(View.GONE);


                                }


                            }else {
                                progressBar.setVisibility(View.GONE);
                                cardProgress.setVisibility(View.GONE);
                                no_information_text.setVisibility(View.VISIBLE);
                            }

//                            progressBar.setVisibility(View.GONE);
//                            cardProgress.setVisibility(View.GONE);
                        }
                    }
                }). addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to get data.", Toast.LENGTH_SHORT).show();
                    }
                });










    }


    @Override
    public void onResume() {
        super.onResume();
        search_term.clearFocus();
        search_term.removeTextChangedListener(OrdersFragment.this);
        search_term.setText("");

        search_term.addTextChangedListener(OrdersFragment.this);
        if(btn_filter_all.getTextColors().getDefaultColor() == -16777216){

            showData();
        }

        if(btn_filter_accepted.getTextColors().getDefaultColor() == -16777216){

            filterButtonClicked("accepted");
        }

        if(btn_filter_shipped.getTextColors().getDefaultColor() == -16777216){

            filterButtonClicked("shipped");
        }

        if(btn_filter_picked.getTextColors().getDefaultColor() == -16777216){

            filterButtonClicked("picked");
        }

        if(btn_filter_delivered.getTextColors().getDefaultColor() == -16777216){

            filterButtonClicked("delivered");
        }

        if(btn_filter_rejected.getTextColors().getDefaultColor() == -16777216){

            filterButtonClicked("rejected");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {


        horizontal_scroll.smoothScrollTo(0,0);
        btn_filter_all.setTextColor(Color.parseColor("#000000"));
        btn_filter_accepted.setTextColor(Color.parseColor("#FF5856D6"));
        btn_filter_shipped.setTextColor(Color.parseColor("#FF5856D6"));
        btn_filter_picked.setTextColor(Color.parseColor("#FF5856D6"));
        btn_filter_delivered.setTextColor(Color.parseColor("#FF5856D6"));
        btn_filter_rejected.setTextColor(Color.parseColor("#FF5856D6"));

        no_information_text.setVisibility(View.GONE);

        if(editable.toString().length()==0){

            no_search_result.setVisibility(View.GONE);
            showData();
        } else if(Character.isDigit(editable.charAt(editable.length()-1))){
            showDataAccordingToNumber(editable.toString());

        } else if(editable.toString().matches("[a-zA-Z]+")){
            showDataAccordingToName(editable.toString());

        } else {
            Toast.makeText(getContext(), "Please enter valid term", Toast.LENGTH_SHORT).show();
            StringBuffer stringBuffer= new StringBuffer(search_term.getText().toString());
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            search_term.setText(stringBuffer);
            search_term.setSelection(search_term.getText().length());
        }







    }
}