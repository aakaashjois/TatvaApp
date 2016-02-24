package com.biryanistudio.tatvaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
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
        final CardView eventCard = (CardView) findViewById(R.id.eventCard);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_event);
        ImageView eventPoster = (ImageView) findViewById(R.id.eventPoster);
        final TextView eventName = (TextView) findViewById(R.id.eventName);
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
        ImageButton eventPhone1 = (ImageButton) findViewById(R.id.eventPhone1);
        ImageButton eventPhone2 = (ImageButton) findViewById(R.id.eventPhone2);
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.organizer1Layout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.organizer2Layout);
        LinearLayout eventRate = (LinearLayout) findViewById(R.id.eventRate);
        LinearLayout eventTeam = (LinearLayout) findViewById(R.id.eventTeam);
        LinearLayout layoutFirstPrize = (LinearLayout) findViewById(R.id.layoutFirstPrize);
        LinearLayout layoutSecondPrize = (LinearLayout) findViewById(R.id.layoutSecondPrize);
        final CoordinatorLayout eventCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.eventCoordinatorLayout);
        final LinearLayout layoutCardViewHolder = (LinearLayout) findViewById(R.id.cardViewHolder);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            eventCard.setTransitionName("eventCardHolder");
            eventPoster.setTransitionName("eventPosterHolder");
        }
        eventPoster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        eventPoster.setImageResource(posterid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Palette.from(BitmapFactory.decodeResource(getResources(), posterid)).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    Palette.Swatch dark = palette.getDarkVibrantSwatch();
                    Palette.Swatch muted = palette.getLightMutedSwatch();
                    if (vibrant != null) {
                        collapsingToolbarLayout.setBackgroundColor(vibrant.getRgb());
                        eventName.setBackgroundColor(vibrant.getRgb());
                        eventName.setAlpha((float) 0.9);
                        eventName.setTextColor(vibrant.getTitleTextColor());
                    }
                    if (dark != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(dark.getRgb());
                            getWindow().setNavigationBarColor(dark.getRgb());
                            layoutCardViewHolder.setBackgroundColor(dark.getRgb());
                            eventCoordinatorLayout.setBackgroundColor(dark.getRgb());
                        }
                    }
                    if (muted != null) {
                        eventCard.setCardBackgroundColor(muted.getRgb());
                    }

                }
            });
        }

        eventName.setText(name);
        eventDay.setText(day);
        eventTime.setText(time);
        eventLocation.setText(location);
        eventDescription.setText(description);

        if (!(teammembers != null && teammembers.equalsIgnoreCase("null")))
            eventTeamMembers.setText(teammembers);
        else
            eventTeam.setVisibility(View.GONE);

        if (!(rate != null && rate.equalsIgnoreCase("null")))
            eventTicketRate.setText(rate);
        else
            eventRate.setVisibility(View.GONE);

        if (!(organizer1 != null && organizer1.equalsIgnoreCase("null")))
            eventOrganizer1.setText(organizer1);
        else
            layout1.setVisibility(View.GONE);

        if (!(organizer2 != null && organizer2.equalsIgnoreCase("null")))
            eventOrganizer2.setText(organizer2);
        else
            layout2.setVisibility(View.GONE);

        if (!(firstPrize != null && firstPrize.equalsIgnoreCase("null")))
            eventFirstPrize.setText(firstPrize);
        else
            layoutFirstPrize.setVisibility(View.GONE);

        if (!(secondPrize != null && secondPrize.equalsIgnoreCase("null")))
            eventSecondPrize.setText(secondPrize);
        else
            layoutSecondPrize.setVisibility(View.GONE);

        if (!(phone1 != null && phone1.equalsIgnoreCase("null")))
            eventPhone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone1)), null));
                }
            });
        else
            eventPhone1.setVisibility(View.INVISIBLE);

        if (!(phone2 != null && phone2.equalsIgnoreCase("null")))
            eventPhone2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone2)), null));
                }
            });
        else
            eventPhone2.setVisibility(View.INVISIBLE);

    }
}
