package com.webstores.storema.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.webstores.storema.MainActivity;
import com.webstores.storema.R;

public class ContactUsActivity extends AppCompatActivity {
    TextView whatsappButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);



        whatsappButton = findViewById(R.id.bthhhhh);
        whatsappButton.setOnClickListener(v -> {
            String contact = "+919451509357"; // use country code with your phone number
            String url = "https://api.whatsapp.com/send?phone=" + contact;
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {

                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("WhatsApp is not installed!");
                ad.setMessage("WhatsApp is not installed in your phone. In order to message, you have to get whatsapp or number is +91 9451509357. Copy it below");
                ad.setPositiveButton(Html.fromHtml("<font color='#356859'>Get WhatsApp</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + "com.whatsapp")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.whatsapp")));
                        }
                    }
                });
                ad.setNegativeButton(Html.fromHtml("<font color='#50554F'>Copy number to clipboard</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Number Copied", "+94741711537");
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ContactUsActivity.this, "Number Copied", Toast.LENGTH_SHORT).show();
                    }
                });
                ad.show();


                e.printStackTrace();
            }
        });








    }
}