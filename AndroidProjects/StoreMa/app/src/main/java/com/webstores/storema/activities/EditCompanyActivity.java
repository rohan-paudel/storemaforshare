package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webstores.storema.R;
import com.webstores.storema.models.PhoneNumber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ServiceConfigurationError;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditCompanyActivity extends AppCompatActivity {


    MaterialCardView btn_back_activity_setup_charges;
    private final int ADDBUSINESSICONREQUESTCODE = 1000;
    private final int ADDSIGNATUREICONREQUESTCODE = 2000;
    private MaterialCardView btnSignUpDone;
    private Spinner businessTypeSpinner;
    private MaterialCardView btnAddBusinessIcon, btnAddSignatureIcon;
    private View viewBusinessIcon, viewSignatureIcon;
    private boolean isBusinessIconClicked = false;
    private boolean isSignatureIconClicked = false;
    private ProgressBar addBusinessProgress;
    private Uri businessIconURI, signatureIconURI;
    private Uri businessIconDownloadURI, businessSignatureDownloadURI;
    private byte[] businessIconByte, signatureIconByte;
    private TextInputEditText extraField1Title, extraField1Value;
    private TextInputEditText extraField2Title, extraField2Value;
    private TextInputEditText extraField3Title, extraField3Value;

    private boolean isBusinessPresent = false;
    private boolean isSignaturePresent = false;

    private TextInputLayout main_extra_field_1_title, main_extra_field_2_title, main_extra_field_3_title;
    private TextInputLayout main_extra_field_1_value, main_extra_field_2_value, main_extra_field_3_value;

    private EditText delivery_charges;
    private String stringExtraField1Title = "", stringExtraField1Value = "";
    private String stringExtraField2Title = "", stringExtraField2Value = "";
    private String stringExtraField3Title = "", stringExtraField3Value = "";
    private ConstraintLayout btnExtraField;
    private StorageReference reference;
    private FirebaseFirestore db;
    private ImageView imgBusinessIcon, imgSignatureIcon;



    private TextInputEditText businessName,gst, email, address, description;
    private int numberOfExtraFields = 0;

    String businessType;

    private String userPhoneNumber, stringBusinessType, stringBusinessName, stringBusinessGST, stringBusinessEmail, stringBusinessAddress, stringBusinessDescription, deliveryCharges, storeLink, numberOfItems, isBusinessActive;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);



        btn_back_activity_setup_charges = findViewById(R.id.btn_back_activity_setup_charges);


        businessTypeSpinner = findViewById(R.id.business_type_spinner);
        btnSignUpDone = findViewById(R.id.btn_sign_up_done);

        btnExtraField = findViewById(R.id.button_extra_fields);

        btnAddBusinessIcon = findViewById(R.id.btn_add_business_icon);
        imgBusinessIcon = findViewById(R.id.img_business_icon);
        viewBusinessIcon = findViewById(R.id.view_business_icon);


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



        addBusinessProgress = findViewById(R.id.add_business_progress);


        btnAddSignatureIcon = findViewById(R.id.btn_signature_icon);
        imgSignatureIcon = findViewById(R.id.img_signature_icon);
        viewSignatureIcon = findViewById(R.id.view_signature_icon);

        businessName = findViewById(R.id.business_name);
        gst = findViewById(R.id.gst);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);


        btn_back_activity_setup_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                reference = FirebaseStorage.getInstance().getReference();
                db = FirebaseFirestore.getInstance();


        DocumentReference documentReference = db.collection("_" + PhoneNumber.getPhoneNumber() + "_business").document("business_details");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    businessName.setText(documentSnapshot.getString("business_name"));
                    isBusinessActive = documentSnapshot.getString("is_business_active");
                    userPhoneNumber = documentSnapshot.getString("mobile");


                    storeLink = documentSnapshot.getString("store_link");
                    deliveryCharges = documentSnapshot.getString("delivery_charges");

                    if(documentSnapshot.getString("business_logo").isEmpty()){

                    }else {
                        businessIconDownloadURI = Uri.parse(documentSnapshot.getString("business_logo"));
                        viewBusinessIcon.setVisibility(View.GONE);
                        imgBusinessIcon.setVisibility(View.VISIBLE);
                        Glide.with(EditCompanyActivity.this)
                                .load(documentSnapshot.getString("business_logo"))
                                .into(imgBusinessIcon);
                    }

                    if(documentSnapshot.getString("business_signature").isEmpty()){

                    }else {
                        businessSignatureDownloadURI = Uri.parse(documentSnapshot.getString("business_signature"));
                        viewSignatureIcon.setVisibility(View.GONE);
                        imgSignatureIcon.setVisibility(View.VISIBLE);
                        Glide.with(EditCompanyActivity.this)
                                .load(documentSnapshot.getString("business_signature"))
                                .into(imgSignatureIcon);
                    }

                    gst.setText(documentSnapshot.getString("business_gst"));
                    email.setText(documentSnapshot.getString("business_email"));
                    address.setText(documentSnapshot.getString("business_address"));
                    description.setText(documentSnapshot.getString("business_description"));
                    numberOfExtraFields = Integer.parseInt(documentSnapshot.getString("number_of_extra_fields"));
                    stringBusinessType = documentSnapshot.getString("business_type");
                    extraField1Title.setText(documentSnapshot.getString("extra_field_title_1"));
                    extraField1Value.setText(documentSnapshot.getString("extra_field_value_1"));
                    extraField2Title.setText(documentSnapshot.getString("extra_field_title_2"));
                    extraField2Value.setText(documentSnapshot.getString("extra_field_value_2"));
                    extraField3Title.setText(documentSnapshot.getString("extra_field_title_3"));
                    extraField3Value.setText(documentSnapshot.getString("extra_field_value_3"));





                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                    if(documentSnapshot.getString("number_of_extra_fields").equals("0")){
                        numberOfExtraFields=0;
                    } else if(documentSnapshot.getString("number_of_extra_fields").equals("1")){
                        numberOfExtraFields =1;
                        extraField1Title.setVisibility(View.VISIBLE);
                        extraField1Value.setVisibility(View.VISIBLE);

                        main_extra_field_1_title.setVisibility(View.VISIBLE);
                        main_extra_field_1_value.setVisibility(View.VISIBLE);


                    } else if(documentSnapshot.getString("number_of_extra_fields").equals("2")){
                        numberOfExtraFields =2;
                        extraField1Title.setVisibility(View.VISIBLE);
                        extraField1Value.setVisibility(View.VISIBLE);

                        extraField2Title.setVisibility(View.VISIBLE);
                        extraField2Value.setVisibility(View.VISIBLE);

                        main_extra_field_1_title.setVisibility(View.VISIBLE);
                        main_extra_field_1_value.setVisibility(View.VISIBLE);

                        main_extra_field_2_title.setVisibility(View.VISIBLE);
                        main_extra_field_2_value.setVisibility(View.VISIBLE);

                    } else if(documentSnapshot.getString("number_of_extra_fields").equals("3")){

                        numberOfExtraFields =3;
                        extraField1Title.setVisibility(View.VISIBLE);
                        extraField1Value.setVisibility(View.VISIBLE);

                        extraField2Title.setVisibility(View.VISIBLE);
                        extraField2Value.setVisibility(View.VISIBLE);

                        extraField3Title.setVisibility(View.VISIBLE);
                        extraField3Value.setVisibility(View.VISIBLE);

                        main_extra_field_1_title.setVisibility(View.VISIBLE);
                        main_extra_field_1_value.setVisibility(View.VISIBLE);

                        main_extra_field_2_title.setVisibility(View.VISIBLE);
                        main_extra_field_2_value.setVisibility(View.VISIBLE);

                        main_extra_field_3_title.setVisibility(View.VISIBLE);
                        main_extra_field_3_value.setVisibility(View.VISIBLE);

                    }


                    if(stringBusinessType.equals("Clothing Store")){
                        businessTypeSpinner.setSelection(0);
                    }

                    if(stringBusinessType.equals("Cosmetics Store")){
                        businessTypeSpinner.setSelection(1);
                    }

                    if(stringBusinessType.equals("Electrical Supply Store")){
                        businessTypeSpinner.setSelection(2);
                    }

                    if(stringBusinessType.equals("Electronics Store")){
                        businessTypeSpinner.setSelection(3);
                    }

                    if(stringBusinessType.equals("Fruits Store")){
                        businessTypeSpinner.setSelection(4);
                    }

                    if(stringBusinessType.equals("Grocery Store")){
                        businessTypeSpinner.setSelection(5);
                    }

                    if(stringBusinessType.equals("Second Hand Store")){
                        businessTypeSpinner.setSelection(6);
                    }

                    if(stringBusinessType.equals("Tile Store")){
                        businessTypeSpinner.setSelection(7);
                    }

                        }
                    });










                }
            }
        });



            }
        });




















        btnExtraField.setOnClickListener(new View.OnClickListener() {
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
                } else if (numberOfExtraFields == 2) {
                    extraField3Title.setVisibility(View.VISIBLE);
                    extraField3Value.setVisibility(View.VISIBLE);
                    main_extra_field_3_title.setVisibility(View.VISIBLE);
                    main_extra_field_3_value.setVisibility(View.VISIBLE);
                    numberOfExtraFields++;
                } else{
                    Toast.makeText(EditCompanyActivity.this, "Cannot add more that 3 fields.", Toast.LENGTH_SHORT).show();

                }

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.business_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                stringBusinessType = adapter.getItem(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                stringBusinessType = "Grocery Store";

            }
        });

        businessTypeSpinner.setAdapter(adapter);






        btnSignUpDone.setOnClickListener(v -> {

            if(!businessName.getText().toString().isEmpty()) {


                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                addBusinessProgress.setVisibility(View.VISIBLE);
                btnSignUpDone.setVisibility(View.GONE);

                stringExtraField1Title = extraField1Title.getText().toString();
                stringExtraField2Title = extraField2Title.getText().toString();
                stringExtraField3Title = extraField2Title.getText().toString();

                stringExtraField1Value = extraField1Value.getText().toString();
                stringExtraField2Value = extraField2Value.getText().toString();
                stringExtraField3Value = extraField3Value.getText().toString();


                stringBusinessName = businessName.getText().toString();
                stringBusinessGST = gst.getText().toString();
                stringBusinessEmail = email.getText().toString();
                stringBusinessAddress = address.getText().toString();
                stringBusinessDescription = description.getText().toString();


                if (businessIconURI != null) {

                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), businessIconURI);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        businessIconByte = stream.toByteArray();




                        StorageReference businessIconReference = reference.child(System.currentTimeMillis() + "." + getFileExtension(businessIconURI));

                        businessIconReference.putBytes(businessIconByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                businessIconReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        businessIconDownloadURI = uri;


                                        if(signatureIconURI != null){
                                            if(businessSignatureDownloadURI==null){

                                            }else {
                                                saveToFirestore();
                                            }
                                        } else{

                                            saveToFirestore();

                                        }




                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                addBusinessProgress.setVisibility(View.GONE);
                                btnSignUpDone.setVisibility(View.VISIBLE);
                                Toast.makeText(EditCompanyActivity.this, "Failed to upload images due to some issue.", Toast.LENGTH_SHORT).show();
                            }
                        });















                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                if (signatureIconURI != null) {

                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), signatureIconURI);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        signatureIconByte = stream.toByteArray();






                        StorageReference signatureIconReference = reference.child(System.currentTimeMillis() + "." + getFileExtension(signatureIconURI));

                        signatureIconReference.putBytes(signatureIconByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                signatureIconReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        businessSignatureDownloadURI = uri;


                                        if(businessIconURI != null){
                                            if(businessIconDownloadURI  == null){

                                            }else {
                                                saveToFirestore();
                                            }
                                        } else{

                                            saveToFirestore();

                                        }




                                    }
                                });
                            }});


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                if(businessIconURI == null && signatureIconURI == null){
                    saveToFirestore();
                }










            }
            else {
                Toast.makeText(this, "Please add business name", Toast.LENGTH_SHORT).show();
            }








        });

        btnAddBusinessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, ADDBUSINESSICONREQUESTCODE);
            }
        });

        btnAddSignatureIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, ADDSIGNATUREICONREQUESTCODE);

            }
        });


    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }


    public void saveToFirestore() {




        HashMap<String, String> items = new HashMap<>();



        items.put("business_name", stringBusinessName);

        items.put("business_description", stringBusinessDescription);
        items.put("business_logo", businessIconDownloadURI == null ? "" : businessIconDownloadURI.toString());
        items.put("business_signature", businessSignatureDownloadURI == null ? "" : businessSignatureDownloadURI.toString());
        items.put("business_gst", stringBusinessGST);
        items.put("business_email", stringBusinessEmail);
        items.put("business_address", stringBusinessAddress);
        items.put("delivery_charges", deliveryCharges);
        items.put("store_link", storeLink);
        items.put("is_business_active", isBusinessActive);
        items.put("business_type", stringBusinessType);
        items.put("number_of_extra_fields", numberOfExtraFields + "");
        items.put("extra_field_title_1", stringExtraField1Title);
        items.put("extra_field_title_2", stringExtraField2Title);
        items.put("extra_field_title_3", stringExtraField3Title);
        items.put("extra_field_value_1", stringExtraField1Value);
        items.put("extra_field_value_2", stringExtraField2Value);
        items.put("extra_field_value_3", stringExtraField3Value);
        items.put("token", "");


        db.collection("_" + userPhoneNumber + "_business").document("business_details")
                .set(items, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(EditCompanyActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                        
                        onBackPressed();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addBusinessProgress.setVisibility(View.GONE);
                        btnSignUpDone.setVisibility(View.VISIBLE);

                        Toast.makeText(EditCompanyActivity.this, "Failed due to some issues occured.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


//
//        stringBusinessName = businessName.getText().toString();
//        stringBusinessGST = gst.getText().toString();
//        stringBusinessEmail = email.getText().toString();
//        stringBusinessAddress = address.getText().toString();
//        stringBusinessDescription = address.getText().toString();
//        deliveryCharges = "0.00";
//        storeLink = stringBusinessName.replaceAll(" ", "").toLowerCase()+userPhoneNumber.substring(6);
//        numberOfItems = "0";




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDBUSINESSICONREQUESTCODE) {
                imgBusinessIcon.setVisibility(View.VISIBLE);
                viewBusinessIcon.setVisibility(View.GONE);
                isBusinessIconClicked = true;
                imgBusinessIcon.setImageURI(data.getData());
                businessIconURI = data.getData();



            }

            if (requestCode == ADDSIGNATUREICONREQUESTCODE) {
                imgSignatureIcon.setVisibility(View.VISIBLE);
                viewSignatureIcon.setVisibility(View.GONE);
                isSignatureIconClicked = true;
                imgSignatureIcon.setImageURI(data.getData());
                signatureIconURI = data.getData();



            }
        }
    }
}