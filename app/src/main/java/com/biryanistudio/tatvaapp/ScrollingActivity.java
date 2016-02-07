package com.biryanistudio.tatvaapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {

    RecyclerView eventList;
    FloatingActionButton fab;
    List<EventData> eventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbarnew = (Toolbar) findViewById(R.id.toolbarnew);
        setSupportActionBar(toolbarnew);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        collapsingToolbarLayout.setTitle("Tatva 2016");
        initData();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        eventList = (RecyclerView) findViewById(R.id.eventList);
        //eventList.setHasFixedSize(true);
        eventList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(eventDataList, getApplicationContext());
        recyclerViewAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setTransitionName("cardView");
                    ViewGroup relativeLayout = (ViewGroup) ((ViewGroup) view).getChildAt(0);
                    View poster = relativeLayout.getChildAt(0);
                    poster.setTransitionName("eventPoster");
                    ViewGroup linearLayout = (ViewGroup) relativeLayout.getChildAt(1);
                    View title = linearLayout.getChildAt(0);
                    title.setTransitionName("eventName");
                    View timings = linearLayout.getChildAt(1);
                    timings.setTransitionName("eventTimings");

                    Pair<View, String> cardPair = Pair.create(view, "cardView");
                    Pair<View, String> posterPair = Pair.create(poster, "eventPoster");
                    Pair<View, String> namePair = Pair.create(title, "eventName");
                    Pair<View, String> timingsPair = Pair.create(timings, "eventTimings");

                    Intent intent = new Intent(ScrollingActivity.this, EventActivity.class);
                    intent.putExtra("position", position);
                    //noinspection unchecked
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, cardPair, posterPair, namePair, timingsPair);
                    ActivityCompat.startActivity(ScrollingActivity.this, intent, optionsCompat.toBundle());
                } else {

                    Intent intent = new Intent(ScrollingActivity.this, EventActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }


            }
        });
        eventList.setAdapter(recyclerViewAdapter);

        fab.setVisibility(View.INVISIBLE);
        if ((ContextCompat.checkSelfPermission(ScrollingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ScrollingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else
            fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScrollingActivity.this, MapsActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, fab, getResources().getString(R.string.fab_transition));
                startActivity(intent, optionsCompat.toBundle());
            }
        });

    }

    private void initData() {
        eventDataList = new ArrayList<>();
        eventDataList.add(new EventData("Circuit Debugging", "Day 1 - 10:00 AM", R.drawable.circuitdebugging));
        eventDataList.add(new EventData("Corporate Conglomerate", "Day 1 - 9:30 AM", R.drawable.main));
        eventDataList.add(new EventData("Gaming - PC", "Day 1 - 9:30 AM", R.drawable.main));
        eventDataList.add(new EventData("JAM", "Day 1 - 10:00 AM", R.drawable.main));
        eventDataList.add(new EventData("Mock GRE", "Day 1 - 10:00 AM", R.drawable.mockgre));
        eventDataList.add(new EventData("Mock Stock", "Day 1 - 1:30 PM", R.drawable.main));
        eventDataList.add(new EventData("Pattern Printing", "Day 1 - 9:30 AM", R.drawable.main));
        eventDataList.add(new EventData("Product Launch", "Day 1 - 9:30 AM", R.drawable.main));
        eventDataList.add(new EventData("Tech DC", "Day 1 - 9:30 AM", R.drawable.techdc));
        eventDataList.add(new EventData("Technogen", "Day 1 - 10:00 AM", R.drawable.main));
        eventDataList.add(new EventData("Tech Talk", "Day 1 - 10:00 AM", R.drawable.techtalk));
        eventDataList.add(new EventData("Treasure Hunt", "Day 1 - 2:30 PM", R.drawable.treasurehunt));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_about || super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                fab.setVisibility(View.VISIBLE);
            else {
                fab.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.parentView), "Please allow location access to enable all features.", Snackbar.LENGTH_LONG)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ScrollingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                            }
                        })
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EventViewHolder> {

    OnItemClickListener mItemClickListener;
    List<EventData> eventDataList;
    Context mContext;

    RecyclerViewAdapter(List<EventData> eventDatas, Context context) {
        this.eventDataList = eventDatas;
        this.mContext = context;
    }


    @Override
    public RecyclerViewAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.EventViewHolder holder, int position) {

        holder.eventTitle.setText(this.eventDataList.get(position).eventName);
        holder.eventTiming.setText(this.eventDataList.get(position).eventTimings);
        Picasso.with(mContext).load(this.eventDataList.get(position).eventPosterId).into(holder.eventPoster);

    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

        TextView eventTitle;
        TextView eventTiming;
        ImageView eventPoster;
        CardView eventCard;


        public EventViewHolder(final View itemView) {
            super(itemView);
            this.eventTitle = (TextView) itemView.findViewById(R.id.title);
            this.eventTiming = (TextView) itemView.findViewById(R.id.timings);
            this.eventCard = (CardView) itemView.findViewById(R.id.card);
            this.eventPoster = (ImageView) itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

    }
}

class EventData {

    String eventName;
    String eventTimings;
    int eventPosterId;

    EventData(String title, String time, int id) {
        this.eventName = title;
        this.eventTimings = time;
        this.eventPosterId = id;
    }

}
