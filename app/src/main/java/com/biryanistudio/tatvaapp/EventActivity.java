package com.biryanistudio.tatvaapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        int posterid = intent.getExtras().getInt("posterid");
        String name = intent.getExtras().getString("name");
        String day = intent.getExtras().getString("day");
        String time = intent.getExtras().getString("time");
        String location = intent.getExtras().getString("location");
        String description = intent.getExtras().getString("description");
        String teammembers = intent.getExtras().getString("teammembers");
        String rate = intent.getExtras().getString("rate");
        String firstPrize = intent.getExtras().getString("firstPrize");
        String secondPrize = intent.getExtras().getString("secondPrize");
        String organizer1 = intent.getExtras().getString("organizer1");
        String organizer2 = intent.getExtras().getString("organizer2");
        final String phone1 = intent.getExtras().getString("phone1");
        final String phone2 = intent.getExtras().getString("phone2");


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_event);
        ImageView eventPoster = (ImageView) findViewById(R.id.eventPoster);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        TextView eventDay = (TextView) findViewById(R.id.eventDay);
        TextView eventTime = (TextView) findViewById(R.id.eventTime);
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        TextView eventTicketRate = (TextView) findViewById(R.id.eventTicketRate);
        TextView eventTeamMembers = (TextView) findViewById(R.id.eventTeamMembers);
        TextView eventFirstPrize = (TextView) findViewById(R.id.eventFirstPrize);
        TextView eventSecondPrize = (TextView) findViewById(R.id.eventSecondPrize);
        TextView eventOrganizer1 = (TextView) findViewById(R.id.eventOrganizer1);
        TextView eventOrganizer2 = (TextView) findViewById(R.id.eventOrganizer2);
        Button eventPhone1 = (Button) findViewById(R.id.eventPhone1);
        Button eventPhone2 = (Button) findViewById(R.id.eventPhone2);
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.organizer1Layout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.organizer2Layout);

        Palette palette = Palette.from(BitmapFactory.decodeResource(getResources(),posterid)).generate();
        Palette.Swatch swatch = palette.getVibrantSwatch();
        Palette.Swatch dark = palette.getDarkVibrantSwatch();
        if(swatch!=null) {
            collapsingToolbarLayout.setBackgroundColor(swatch.getRgb());
            eventName.setTextColor(swatch.getTitleTextColor());
        }
        if(dark!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(dark.getRgb());
                getWindow().setNavigationBarColor(dark.getRgb());
            }
        }

        eventName.setText(name);
        eventPoster.setImageResource(posterid);
        eventDay.setText(day);
        eventTime.setText(time);
        eventLocation.setText(location);
        eventDescription.setText(description);
        eventTeamMembers.setText(teammembers);
        eventTicketRate.setText(rate);

        if(!organizer1.equalsIgnoreCase("null"))
            eventOrganizer1.setText(organizer1);
        else
            layout1.setVisibility(View.GONE);

        if(!organizer2.equalsIgnoreCase("null"))
            eventOrganizer2.setText(organizer2);
        else
            layout2.setVisibility(View.GONE);

        eventFirstPrize.setText(firstPrize);
        eventSecondPrize.setText(secondPrize);
        eventPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:"+phone1));
                startActivity(intent1);
            }
        });
        eventPhone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:"+phone2));
                startActivity(intent2);
            }
        });
    }
}
