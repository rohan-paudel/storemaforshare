package com.webstores.storema.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.zxing.WriterException;
import com.webstores.storema.QRGContents;
import com.webstores.storema.QRGEncoder;
import com.webstores.storema.R;

public class ShareStoreLinkActivity extends AppCompatActivity {

    private TextView store_link, text_company_name;
    private MaterialCardView btn_copy_store_link, btn_back_activity_share_store_link;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private ImageView qrCodeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_store_link);


        store_link = findViewById(R.id.store_link);
        btn_copy_store_link = findViewById(R.id.btn_copy_store_link);
        qrCodeIV = findViewById(R.id.idIVQrcode);
        text_company_name = findViewById(R.id.text_company_name);
        btn_back_activity_share_store_link = findViewById(R.id.btn_back_activity_share_store_link);

        text_company_name.setText(getIntent().getStringExtra("company_name"));

        store_link.setText("https://storema.in/?store="+getIntent().getStringExtra("store_link"));

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        qrgEncoder = new QRGEncoder(store_link.getText().toString(), null, QRGContents.Type.TEXT, dimen);

        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.getBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (Exception e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }



        btn_copy_store_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied store link", store_link.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ShareStoreLinkActivity.this, "Store link copied", Toast.LENGTH_SHORT).show();
            }
        });

        btn_back_activity_share_store_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });







    }
}