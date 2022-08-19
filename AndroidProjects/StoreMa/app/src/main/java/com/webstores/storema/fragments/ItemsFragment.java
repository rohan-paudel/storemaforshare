package com.webstores.storema.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.icu.lang.UCharacter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.webstores.storema.R;
import com.webstores.storema.activities.AddItemsActivity;
import com.webstores.storema.adapters.ItemsListAdapter;
import com.webstores.storema.models.ItemsModel;
import com.webstores.storema.models.PhoneNumber;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ItemsFragment extends Fragment {

    private MaterialCardView btnAddMoreItems;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MaterialCardView cardProgress;

    private FirebaseFirestore db;
    private ItemsModel itemsModel;

    private EditText search_items;







    private ItemsListAdapter itemsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_items, container, false);

        recyclerView = view.findViewById(R.id.items_list_recycler_view);
        progressBar = view.findViewById(R.id.progress);
        cardProgress = view.findViewById(R.id.card_progress);
        search_items = view.findViewById(R.id.search_items);



        db = FirebaseFirestore.getInstance();



        ExecutorService service = Executors.newSingleThreadExecutor();


        service.execute(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });


                itemsListAdapter = new ItemsListAdapter();


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                        recyclerView.setAdapter(itemsListAdapter);

                        btnAddMoreItems = view.findViewById(R.id.btn_add_more_items);


                        search_items.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                                if(editable.toString().isEmpty())showData();


                                if(editable.toString().length()==0) {
                                    showData();

                                }
//                                }else if(editable.toString().matches("[a-zA-Z]+")){
//                                   }
//
                                  else {
                                    showDataAccordingToName(editable.toString());
//                                    Toast.makeText(getContext(), "Please enter valid term", Toast.LENGTH_SHORT).show();
//                                    StringBuffer stringBuffer= new StringBuffer(search_items.getText().toString());
//                                    stringBuffer.deleteCharAt(stringBuffer.length()-1);
//                                    search_items.setText(stringBuffer);
//                                    search_items.setSelection(search_items.getText().length());
                                }


                            }
                        });




                        btnAddMoreItems.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), AddItemsActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

        });










        return view;
    }

    private void showDataAccordingToName(String toString) {

        DocumentReference documentReference = db.collection("_"+ PhoneNumber.getPhoneNumber()+"_items").document();
        db.collection("_"+ PhoneNumber.getPhoneNumber()+"_items").orderBy("item_name").startAt(toString).endAt(toString+"\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            itemsListAdapter.clearData();

                            if (task.getResult().size() > 0) {

                                for(DocumentSnapshot snapshot: task.getResult()){

                                    itemsModel = new ItemsModel(snapshot.getString("item_id"), snapshot.getString("item_name"),
                                            snapshot.getString("pieces_unit_text"), snapshot.getString("item_image_1"),
                                            snapshot.getString("item_image_2"), snapshot.getString("item_image_3"),
                                            snapshot.getString("item_image_4"), snapshot.getString("item_image_5"),
                                            snapshot.getString("is_image_1"), snapshot.getString("is_image_2"),
                                            snapshot.getString("is_image_3"), snapshot.getString("is_image_4"),
                                            snapshot.getString("is_image_5"), snapshot.getString("is_discount_in_percentage"),
                                            snapshot.getString("discount"), snapshot.getString("item_gst"),
                                            snapshot.getString("mrp"), snapshot.getString("item_category_text"),
                                            snapshot.getString("item_category_position"), snapshot.getString("pieces_unit_position"),
                                            snapshot.getString("extra_field_title_1"), snapshot.getString("extra_field_value_1"),
                                            snapshot.getString("extra_field_title_2"), snapshot.getString("extra_field_value_2"),
                                            snapshot.getString("extra_field_title_3"), snapshot.getString("extra_field_value_3"),
                                            snapshot.getString("item_description"), snapshot.getString("is_item_active"), snapshot.getString("number_of_extra_fields"));

                                    itemsListAdapter.updateItemsList(itemsModel);



                                }





                            } else {


                                itemsModel = null;
                                itemsListAdapter.updateItemsList(itemsModel);



                                // ToDO for not result found

                            }

                        }


                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });






    }

    @Override
    public void onResume() {
        super.onResume();
        itemsListAdapter.clearData();
        showData();

    }

    private void showData() {

        db.collection("_"+ PhoneNumber.getPhoneNumber()+"_items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            itemsListAdapter.clearData();
                            for(DocumentSnapshot snapshot: task.getResult()){

                                itemsModel = new ItemsModel(snapshot.getString("item_id"), snapshot.getString("item_name"),
                                        snapshot.getString("pieces_unit_text"), snapshot.getString("item_image_1"),
                                        snapshot.getString("item_image_2"), snapshot.getString("item_image_3"),
                                        snapshot.getString("item_image_4"), snapshot.getString("item_image_5"),
                                        snapshot.getString("is_image_1"), snapshot.getString("is_image_2"),
                                        snapshot.getString("is_image_3"), snapshot.getString("is_image_4"),
                                        snapshot.getString("is_image_5"), snapshot.getString("is_discount_in_percentage"),
                                        snapshot.getString("discount"), snapshot.getString("item_gst"),
                                        snapshot.getString("mrp"), snapshot.getString("item_category_text"),
                                        snapshot.getString("item_category_position"), snapshot.getString("pieces_unit_position"),
                                        snapshot.getString("extra_field_title_1"), snapshot.getString("extra_field_value_1"),
                                        snapshot.getString("extra_field_title_2"), snapshot.getString("extra_field_value_2"),
                                        snapshot.getString("extra_field_title_3"), snapshot.getString("extra_field_value_3"),
                                        snapshot.getString("item_description"), snapshot.getString("is_item_active"), snapshot.getString("number_of_extra_fields"));

                                itemsListAdapter.updateItemsList(itemsModel);



                            }

                            progressBar.setVisibility(View.GONE);
                            cardProgress.setVisibility(View.GONE);
                        }
                    }
                }). addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to get data.", Toast.LENGTH_SHORT).show();
                    }
                });








    }
}