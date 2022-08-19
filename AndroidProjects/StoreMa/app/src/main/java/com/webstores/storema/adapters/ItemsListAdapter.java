package com.webstores.storema.adapters;

import android.content.ClipData;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.webstores.storema.R;
import com.webstores.storema.activities.EditItemsActivity;
import com.webstores.storema.models.ItemsModel;
import com.webstores.storema.models.PhoneNumber;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder> {


    private ArrayList<ItemsModel> itemsModelArrayList;

    private ViewGroup parent;

    private FirebaseFirestore dbRoot = FirebaseFirestore.getInstance();


    public ItemsListAdapter() {
        itemsModelArrayList = new ArrayList<>();
    }

    public void clearData(){
        itemsModelArrayList.clear();
    }


    public void updateItemsList(ItemsModel itemsModel) {

        if(itemsModel == null){
            this.notifyDataSetChanged();
        }else{
            this.itemsModelArrayList.add(itemsModel);
            this.notifyDataSetChanged();

        }


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ItemsModel model = itemsModelArrayList.get(position);

        holder.itemPrice.setText(model.getMrp());


        holder.itemTitle.setText(model.getItemName());
        Glide.with(parent.getContext())
                .load(model.getItemImage1())
                .into(holder.itemImage);

        holder.activeItemsSwitch.setChecked(model.getIsItemActive() != null && model.getIsItemActive().equals("1"));

        holder.activeItemsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                                if (b) {
                                                                    HashMap<String, String> items = new HashMap<>();

                                                                    items.put("is_item_active", "1");

                                                                    dbRoot.collection("_" + PhoneNumber.getPhoneNumber() + "_items").document(model.getItemId())
                                                                            .set(items, SetOptions.merge()).
                                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    model.setIsItemActive("1");

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(parent.getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                }
                                                                else if(!b){


                                                                    HashMap<String, String> items = new HashMap<>();

                                                                    items.put("is_item_active", "0");

                                                                    dbRoot.collection("_" + PhoneNumber.getPhoneNumber() + "_items").document(model.getItemId())
                                                                            .set(items, SetOptions.merge()).
                                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    model.setIsItemActive("0");

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(parent.getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        }
        );




        holder.itemSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(parent.getContext(), EditItemsActivity.class);
                intent.putExtra("item_name", model.getItemName());
                intent.putExtra("category_position", model.getItemCategoryPositionNumber());
                intent.putExtra("item_mrp", model.getMrp());
                if(model.getIsDiscountInPercentage().equals("1")){
                    intent.putExtra("discount", model.getDiscount()+"%");
                }else {
                    intent.putExtra("discount", model.getDiscount());
                }
                intent.putExtra("unit_position", model.getPiecesUnitPositionNumber());

                intent.putExtra("item_id", model.getItemId());

                intent.putExtra("image1", model.getItemImage1());
                intent.putExtra("image2", model.getItemImage2());
                intent.putExtra("image3", model.getItemImage3());
                intent.putExtra("image4", model.getItemImage4());
                intent.putExtra("image5", model.getItemImage5());

                intent.putExtra("is_item_active", model.getIsItemActive());

                intent.putExtra("gst", model.getGst());
                intent.putExtra("description", model.getDescription());
                intent.putExtra("extra_1_title", model.getExtraField1Name());
                intent.putExtra("extra_1_value", model.getExtraField1Value());
                intent.putExtra("extra_2_title", model.getExtraField2Name());
                intent.putExtra("extra_2_value", model.getExtraField2Value());
                intent.putExtra("extra_3_title", model.getExtraField3Name());
                intent.putExtra("extra_3_value", model.getExtraField3Value());
                intent.putExtra("number_of_extra_fields", model.getNumberOfExtraFields());
                parent.getContext().startActivity(intent);






            }
        });



    }

    @Override
    public int getItemCount() {
        return itemsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView itemTitle;
        private TextView itemPrice;
        private ImageView itemImage;
        private MaterialCardView itemSettings;
        private SwitchMaterial activeItemsSwitch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.item_title);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemImage = itemView.findViewById(R.id.img_item_logo);
            itemSettings = itemView.findViewById(R.id.item_settings);
            activeItemsSwitch = itemView.findViewById(R.id.switch_item_status);

        }
    }


}
