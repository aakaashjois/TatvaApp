package com.biryanistudio.tatvaapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
        }

        CircleImageView tatvaLogoImageView = (CircleImageView) findViewById(R.id.tatvaLogoImageView);

        ImageButton facebookButton = (ImageButton) findViewById(R.id.facebookButton);
        ImageButton instagramButton = (ImageButton) findViewById(R.id.instagramButton);
        ImageButton webButton = (ImageButton) findViewById(R.id.webButton);

        CircleImageView appDevImageView = (CircleImageView) findViewById(R.id.appDevImage);

        Glide.with(getApplicationContext()).load(R.drawable.tatva_logo).fitCenter().into(tatvaLogoImageView);
        Glide.with(getApplicationContext()).load(R.drawable.devpic).fitCenter().into(appDevImageView);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + "")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Tatva15")));
                }

            }
        });

        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/tatva2016");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/tatva2016")));
                }
            }
        });

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tatvabnmit.com")));
            }
        });

    }
}
