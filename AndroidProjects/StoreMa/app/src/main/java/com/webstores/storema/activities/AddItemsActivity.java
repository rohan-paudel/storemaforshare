package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webstores.storema.R;
import com.webstores.storema.models.PhoneNumber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddItemsActivity extends AppCompatActivity {

    private ImageView imgGallery1;
    private ImageView imgGallery2;
    private ImageView imgGallery3;
    private ImageView imgGallery4;
    private ImageView imgGallery5;


    private MaterialCardView btnItemsAddDone;

    private ProgressBar addItemsProgressBar;

    private StorageReference reference;
    private FirebaseFirestore db;

    private TextInputEditText editTextItemName;
    private String stringItemName;

    private boolean isClickable = true;

    private Spinner itemCategorySpinner;
    private String stringItemCategory;
    private String itemCategoryPositionNumber;


    private Uri imgGallery1URI, imgGallery2URI, imgGallery3URI, imgGallery4URI, imgGallery5URI;
    private Uri imgGallery1DownloadURI, imgGallery2DownloadURI, imgGallery3DownloadURI, imgGallery4DownloadURI, imgGallery5DownloadURI;
    private byte[] imgGallery1Byte, imgGallery2Byte, imgGallery3Byte, imgGallery4Byte, imgGallery5Byte;

    private String stringImage1Download = "", stringImage2Download = "", stringImage3Download = "", stringImage4Download = "", stringImage5Download = "";

    private TextInputEditText editTextMrp;
    private String stringMrp;

    private Spinner piecesUnitSpinner;

    private TextView idididid;
    private String stringPiecesUnit;
    private String piecesUnitPositionNumber;


    private String isImage1 = "0";
    private String isImage2 = "0";
    private String isImage3 = "0";
    private String isImage4 = "0";
    private String isImage5 = "0";

    private boolean isImg1Success = false;
    private boolean isImg2Success = false;
    private boolean isImg3Success = false;
    private boolean isImg4Success = false;
    private boolean isImg5Success = false;


    private int numberOfExtraFields = 0;

    private int IMG1_REQUEST_CODE = 1000;
    private int IMG2_REQUEST_CODE = 2000;
    private int IMG3_REQUEST_CODE = 3000;
    private int IMG4_REQUEST_CODE = 4000;
    private int IMG5_REQUEST_CODE = 5000;


    private String itemId;
    private String isItemActive = "1";

    private TextInputEditText editTextDiscount;
    private String stringDiscount;

    private TextInputEditText editTextGst;
    private String stringGst;

    private TextInputEditText editTextItemsDescription;
    private String stringItemsDescription;

    private String isDiscountInPercentage;


    private TextInputEditText extraField1Title, extraField1Value;
    private TextInputEditText extraField2Title, extraField2Value;
    private TextInputEditText extraField3Title, extraField3Value;

    private TextInputLayout main_extra_field_1_title, main_extra_field_2_title, main_extra_field_3_title;
    private TextInputLayout main_extra_field_1_value, main_extra_field_2_value, main_extra_field_3_value;
    private String stringExtraField1Title = "", stringExtraField1Value = "";
    private String stringExtraField2Title = "", stringExtraField2Value = "";
    private String stringExtraField3Title = "", stringExtraField3Value = "";

    private ConstraintLayout buttonExtraFields;


    private MaterialCardView btnBackActivityAddItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        btnBackActivityAddItems = findViewById(R.id.btn_back_activity_add_items);


        btnItemsAddDone = findViewById(R.id.btn_items_add_done);

        reference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        idididid = findViewById(R.id.idididid);

        imgGallery1 = findViewById(R.id.img_gallery1);
        imgGallery2 = findViewById(R.id.img_gallery2);
        imgGallery3 = findViewById(R.id.img_gallery3);
        imgGallery4 = findViewById(R.id.img_gallery4);


        addItemsProgressBar = findViewById(R.id.add_item_progress);

        imgGallery5 = findViewById(R.id.img_gallery5);

        imgGallery2.setVisibility(View.GONE);
        imgGallery3.setVisibility(View.GONE);
        imgGallery4.setVisibility(View.GONE);
        imgGallery5.setVisibility(View.GONE);

        itemCategorySpinner = findViewById(R.id.item_category_spinner);

        piecesUnitSpinner = findViewById(R.id.pieces_unit);

        buttonExtraFields = findViewById(R.id.button_extra_fields_add_items);

        editTextItemsDescription = findViewById(R.id.item_description);
        editTextGst = findViewById(R.id.gst);
        editTextDiscount = findViewById(R.id.discount);
        editTextMrp = findViewById(R.id.mrp);
        editTextItemName = findViewById(R.id.item_name);


        extraField1Title = findViewById(R.id.extra_field_1_title);
        extraField2Title = findViewById(R.id.extra_field_2_title);
        extraField3Title = findViewById(R.id.extra_field_3_title);

        extraField1Value = findViewById(R.id.extra_field_1_value);
        extraField2Value = findViewById(R.id.extra_field_2_value);
        extraField3Value = findViewById(R.id.extra_field_3_value);


        main_extra_field_1_title = findViewById(R.id.main_extra_field_1_title);
        main_extra_field_2_title = findViewById(R.id.main_extra_field_2_title);
        main_extra_field_3_title = findViewById(R.id.main_extra_field_3_title);


        main_extra_field_1_value = findViewById(R.id.main_extra_field_1_value);
        main_extra_field_2_value = findViewById(R.id.main_extra_field_2_value);
        main_extra_field_3_value = findViewById(R.id.main_extra_field_3_value);




        editTextItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.item_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        itemCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stringItemCategory = adapter.getItem(i).toString();
                itemCategoryPositionNumber = i + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                stringItemCategory = "";
                itemCategoryPositionNumber = "";

            }
        });

        itemCategorySpinner.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.pieces_unit_values, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        piecesUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stringPiecesUnit = adapter2.getItem(i).toString();
                piecesUnitPositionNumber = i + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                stringPiecesUnit = "/Pics";
                piecesUnitPositionNumber = "";

            }
        });

        piecesUnitSpinner.setAdapter(adapter2);


        buttonExtraFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfExtraFields == 0) {
                    extraField1Title.setVisibility(View.VISIBLE);
                    extraField1Value.setVisibility(View.VISIBLE);
                    main_extra_field_1_title.setVisibility(View.VISIBLE);
                    main_extra_field_1_value.setVisibility(View.VISIBLE);
                    numberOfExtraFields++;

                } else if (numberOfExtraFields == 1) {
                    extraField2Title.setVisibility(View.VISIBLE);
                    extraField2Value.setVisibility(View.VISIBLE);
                    main_extra_field_2_title.setVisibility(View.VISIBLE);
                    main_extra_field_2_value.setVisibility(View.VISIBLE);
                    numberOfExtraFields++;
                    return;
                } else if (numberOfExtraFields == 2) {
                    extraField3Title.setVisibility(View.VISIBLE);
                    extraField3Value.setVisibility(View.VISIBLE);
                    main_extra_field_3_title.setVisibility(View.VISIBLE);
                    main_extra_field_3_value.setVisibility(View.VISIBLE);
                    numberOfExtraFields++;
                } else {

                }
            }
        });


        btnItemsAddDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!(editTextItemName.getText().toString().isEmpty())) {
                    if (!(editTextMrp.getText().toString().isEmpty())) {
                            if ((editTextDiscount.getText().toString().matches("^[0-9%]+$")) || editTextDiscount.getText().toString().isEmpty()) {
                                if((editTextGst.getText().toString().matches("^[0-9%]+$")) || editTextGst.getText().toString().isEmpty()){

                                    if(!(editTextItemsDescription.getText().toString().isEmpty())){

                                        if(isImage1.equals("1")){



                                            btnItemsAddDone.setVisibility(View.GONE);
                                            addItemsProgressBar.setVisibility(View.VISIBLE);
                                            idididid.setText("Uploading Please wait");


                                            stringExtraField1Title = extraField1Title.getText().toString();
                                            stringExtraField2Title = extraField2Title.getText().toString();
                                            stringExtraField3Title = extraField2Title.getText().toString();

                                            stringExtraField1Value = extraField1Value.getText().toString();
                                            stringExtraField2Value = extraField2Value.getText().toString();
                                            stringExtraField3Value = extraField3Value.getText().toString();


                                            stringItemName = editTextItemName.getText().toString();
                                            if (editTextDiscount.getText().toString().contains("%")) {
                                                isDiscountInPercentage = "1";
                                                stringDiscount = editTextDiscount.getText().toString().replace("%", "");

                                            } else {
                                                isDiscountInPercentage = "0";
                                                stringDiscount = editTextDiscount.getText().toString().isEmpty()?"0":editTextDiscount.getText().toString();
                                            }


                                            stringMrp = editTextMrp.getText().toString();

                                            if (editTextGst.getText().toString().contains("%")) {
                                                stringGst = editTextGst.getText().toString().replace("%", "");
                                            } else if (!editTextGst.getText().toString().isEmpty()) {
                                                stringGst = editTextGst.getText().toString();
                                            } else {
                                                stringGst = "0";
                                            }

                                            stringItemsDescription = editTextItemsDescription.getText().toString();

                                            if (isClickable) {
                                                upload1Image();
                                                upload2Image();
                                                upload3Image();
                                                upload4Image();
                                                upload5Image();

                                                uupload1();

                                            }
                                            isClickable = false;


                                            if (isImg1Success && isImg2Success && isImg3Success && isImg4Success && isImg5Success) {


                                                itemId = stringItemName.replaceAll(" ", "") + stringMrp + 1 + (new Random().nextInt(100));
                                                saveToFireStore();


                                            } else {


                                                Toast.makeText(AddItemsActivity.this, "Please Wait Image is uploading", Toast.LENGTH_SHORT).show();


                                            }



                                            


                                        }
                                        else {
                                            Toast.makeText(AddItemsActivity.this, "Please add at least one pic.", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {

                                        Toast.makeText(AddItemsActivity.this, "Description cannot be empty.", Toast.LENGTH_SHORT).show();
                                    }


                                } else{
                                    Toast.makeText(AddItemsActivity.this, "GST should be in number or leave empty if not applicable", Toast.LENGTH_SHORT).show();
                                    
                                }
                                



                            } else {

                                Toast.makeText(AddItemsActivity.this, "Discount should be in number or in percentage only. also can Leave empty", Toast.LENGTH_SHORT).show();
                                
                            }





                    } else {
                        Toast.makeText(AddItemsActivity.this, "MRP field cannot be empty", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(AddItemsActivity.this, "Item name cannot be empty.", Toast.LENGTH_SHORT).show();
                }




            }
        });


        imgGallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, IMG1_REQUEST_CODE);
            }
        });

        imgGallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, IMG2_REQUEST_CODE);
            }
        });

        imgGallery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, IMG3_REQUEST_CODE);
            }
        });

        imgGallery4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, IMG4_REQUEST_CODE);
            }
        });

        imgGallery5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, IMG5_REQUEST_CODE);
            }
        });


        btnBackActivityAddItems.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void saveToFireStore() {

        HashMap<String, String> items = new HashMap<>();

        items.put("item_name", stringItemName);

        if (isImage1.equals("1")) {
            items.put("item_image_1", imgGallery1DownloadURI.toString());
        } else {
            items.put("item_image_1", "");
        }
        if (isImage2.equals("1")) {
            items.put("item_image_2", imgGallery2DownloadURI.toString());
        } else {
            items.put("item_image_2", "");
        }
        if (isImage3.equals("1")) {
            items.put("item_image_3", imgGallery3DownloadURI.toString());
        } else {
            items.put("item_image_3", "");
        }
        if (isImage4.equals("1")) {
            items.put("item_image_4", imgGallery4DownloadURI.toString());
        } else {
            items.put("item_image_4", "");
        }
        if (isImage5.equals("1")) {
            items.put("item_image_5", imgGallery5DownloadURI.toString());
        } else {
            items.put("item_image_5", "");
        }

        items.put("is_image_1", isImage1);
        items.put("is_image_2", isImage2);
        items.put("is_image_3", isImage3);
        items.put("is_image_4", isImage4);
        items.put("is_image_5", isImage5);


        items.put("unit", stringPiecesUnit);
        items.put("is_discount_in_percentage", isDiscountInPercentage);
        items.put("discount", stringDiscount);
        items.put("item_gst", stringGst);
        items.put("number_of_extra_fields", numberOfExtraFields + "");
        items.put("extra_field_title_1", stringExtraField1Title);
        items.put("extra_field_title_2", stringExtraField2Title);
        items.put("extra_field_title_3", stringExtraField3Title);
        items.put("extra_field_value_1", stringExtraField1Value);
        items.put("extra_field_value_2", stringExtraField2Value);
        items.put("extra_field_value_3", stringExtraField3Value);

        items.put("mrp", stringMrp);
        items.put("item_category_position", itemCategoryPositionNumber);
        items.put("item_category_text", stringItemCategory);
        items.put("pieces_unit_position", piecesUnitPositionNumber);
        items.put("pieces_unit_text", stringPiecesUnit);
        items.put("item_description", stringItemsDescription);
        items.put("item_id", itemId);
        items.put("is_item_active", isItemActive);



        db.collection("_" + PhoneNumber.getPhoneNumber() + "_items").document(itemId).set(items, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddItemsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnItemsAddDone.setVisibility(View.VISIBLE);
                        addItemsProgressBar.setVisibility(View.GONE);
                        Toast.makeText(AddItemsActivity.this, "Sorry, there is something wrong.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG1_REQUEST_CODE) {
                imgGallery1.setImageURI(data.getData());
                imgGallery1URI = data.getData();
                isImage1 = "1";
                imgGallery2.setVisibility(View.VISIBLE);

            }

            if (requestCode == IMG2_REQUEST_CODE) {
                imgGallery2.setImageURI(data.getData());
                imgGallery2URI = data.getData();
                isImage2 = "1";
                imgGallery3.setVisibility(View.VISIBLE);

            }


            if (requestCode == IMG3_REQUEST_CODE) {
                imgGallery3.setImageURI(data.getData());
                imgGallery3URI = data.getData();
                isImage3 = "1";
                imgGallery4.setVisibility(View.VISIBLE);


            }


            if (requestCode == IMG4_REQUEST_CODE) {
                imgGallery4.setImageURI(data.getData());
                imgGallery4URI = data.getData();
                isImage4 = "1";
                imgGallery5.setVisibility(View.VISIBLE);


            }

            if (requestCode == IMG5_REQUEST_CODE) {
                imgGallery5.setImageURI(data.getData());
                imgGallery5URI = data.getData();
                isImage5 = "1";


            }


        }
    }

    private void upload4Image() {


        if (isImage4.equals("1")) {
            try {
                Bitmap imageBitmap ,imageBitmap2, imageBitmapMain;

                imageBitmap  = MediaStore.Images.Media.getBitmap(AddItemsActivity.this.getContentResolver(), imgGallery4URI);

                int width = imageBitmap.getWidth();
                int height = imageBitmap.getHeight();
                int maxSize = 512;
                float bitmapRatio = ((float) width) / (float) height;
                if (bitmapRatio > 1) {
                    width = maxSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = maxSize;
                    width = (int) (height * bitmapRatio);
                }


                ByteArrayOutputStream stream = new ByteArrayOutputStream(512);
                imageBitmapMain = Bitmap.createScaledBitmap(imageBitmap, width, height, true);
                imageBitmapMain.compress(Bitmap.CompressFormat.JPEG,50, stream);

                imgGallery4Byte = stream.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void upload1Image() {


        if (isImage1.equals("1")) {

            try {
                Bitmap imageBitmap ,imageBitmap2, imageBitmapMain;

                imageBitmap  = MediaStore.Images.Media.getBitmap(AddItemsActivity.this.getContentResolver(), imgGallery1URI);

                int width = imageBitmap.getWidth();
                int height = imageBitmap.getHeight();
                int maxSize = 512;
                float bitmapRatio = ((float) width) / (float) height;
                if (bitmapRatio > 1) {
                    width = maxSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = maxSize;
                    width = (int) (height * bitmapRatio);
                }


                ByteArrayOutputStream stream = new ByteArrayOutputStream(512);
                imageBitmapMain = Bitmap.createScaledBitmap(imageBitmap, width, height, true);
                imageBitmapMain.compress(Bitmap.CompressFormat.JPEG,50, stream);

                imgGallery1Byte = stream.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }





    }


    private void upload3Image() {

        if (isImage3.equals("1")) {
            try {
                Bitmap imageBitmap ,imageBitmap2, imageBitmapMain;

                imageBitmap  = MediaStore.Images.Media.getBitmap(AddItemsActivity.this.getContentResolver(), imgGallery3URI);

                int width = imageBitmap.getWidth();
                int height = imageBitmap.getHeight();
                int maxSize = 512;
                float bitmapRatio = ((float) width) / (float) height;
                if (bitmapRatio > 1) {
                    width = maxSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = maxSize;
                    width = (int) (height * bitmapRatio);
                }


                ByteArrayOutputStream stream = new ByteArrayOutputStream(512);
                imageBitmapMain = Bitmap.createScaledBitmap(imageBitmap, width, height, true);
                imageBitmapMain.compress(Bitmap.CompressFormat.JPEG,50, stream);

                imgGallery3Byte = stream.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    private void upload5Image() {
        if (isImage5.equals("1")) {
            try {
                Bitmap imageBitmap ,imageBitmap2, imageBitmapMain;

                imageBitmap  = MediaStore.Images.Media.getBitmap(AddItemsActivity.this.getContentResolver(), imgGallery5URI);

                int width = imageBitmap.getWidth();
                int height = imageBitmap.getHeight();
                int maxSize = 512;
                float bitmapRatio = ((float) width) / (float) height;
                if (bitmapRatio > 1) {
                    width = maxSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = maxSize;
                    width = (int) (height * bitmapRatio);
                }


                ByteArrayOutputStream stream = new ByteArrayOutputStream(512);
                imageBitmapMain = Bitmap.createScaledBitmap(imageBitmap, width, height, true);
                imageBitmapMain.compress(Bitmap.CompressFormat.JPEG,50, stream);

                imgGallery5Byte = stream.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    private void upload2Image() {

        if (isImage2.equals("1")) {
            try {
                Bitmap imageBitmap ,imageBitmap2, imageBitmapMain;

                imageBitmap  = MediaStore.Images.Media.getBitmap(AddItemsActivity.this.getContentResolver(), imgGallery2URI);

                int width = imageBitmap.getWidth();
                int height = imageBitmap.getHeight();
                int maxSize = 512;
                float bitmapRatio = ((float) width) / (float) height;
                if (bitmapRatio > 1) {
                    width = maxSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = maxSize;
                    width = (int) (height * bitmapRatio);
                }


                ByteArrayOutputStream stream = new ByteArrayOutputStream(512);
                imageBitmapMain = Bitmap.createScaledBitmap(imageBitmap, width, height, true);
                imageBitmapMain.compress(Bitmap.CompressFormat.JPEG,50, stream);

                imgGallery2Byte = stream.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public void uupload1() {


        if (isImage1.equals("1")) {
            StorageReference image1Reference = reference.child(System.currentTimeMillis() + "." + getFileExtension(imgGallery1URI));
            image1Reference.putBytes(imgGallery1Byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image1Reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgGallery1DownloadURI = uri;
                            isImg1Success = true;
                            uupload2();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemsActivity.this, "Image 1 not uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            isImg1Success = true;
            uupload2();

        }


    }

    public void uupload2() {

        if (isImage2.equals("1")) {
            StorageReference image2Reference = reference.child(System.currentTimeMillis() + "." + getFileExtension(imgGallery2URI));
            image2Reference.putBytes(imgGallery2Byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image2Reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgGallery2DownloadURI = uri;
                            isImg2Success = true;
                            uupload3();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemsActivity.this, "Image 2 not uploaded", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            isImg2Success = true;
            uupload3();
        }


    }


    public void uupload3() {


        if (isImage3.equals("1")) {
            StorageReference image3Reference = reference.child(System.currentTimeMillis() + "." + getFileExtension(imgGallery3URI));
            image3Reference.putBytes(imgGallery3Byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image3Reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgGallery3DownloadURI = uri;
                            isImg3Success = true;
                            uupload4();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemsActivity.this, "Image 3 not uploaded", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            isImg3Success = true;
            uupload4();
        }


    }


    public void uupload4() {

        if (isImage4.equals("1")) {
            StorageReference image4Reference = reference.child(System.currentTimeMillis() + "." + getFileExtension(imgGallery4URI));
            image4Reference.putBytes(imgGallery4Byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image4Reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgGallery4DownloadURI = uri;
                            isImg4Success = true;
                            uupload5();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemsActivity.this, "Image 4 not uploaded", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            isImg4Success = true;
            uupload5();
        }


    }


    public void uupload5() {


        if (isImage5.equals("1")) {
            StorageReference image5Reference = reference.child(System.currentTimeMillis() + "." + getFileExtension(imgGallery5URI));
            image5Reference.putBytes(imgGallery5Byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image5Reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgGallery5DownloadURI = uri;
                            isImg5Success = true;
                            idididid.setText("Click here to complete");
                            btnItemsAddDone.setVisibility(View.VISIBLE);
                            addItemsProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemsActivity.this, "Image 5 not uploaded", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            isImg5Success = true;
            idididid.setText("Click here to complete");
            btnItemsAddDone.setVisibility(View.VISIBLE);
            addItemsProgressBar.setVisibility(View.GONE);
        }


    }


    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }


    private void checkTHings() {


    }


}