package com.biryanistudio.tatvaapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {

	RecyclerView eventList;
	FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrolling);
		Toolbar toolbarnew = ( Toolbar ) findViewById(R.id.toolbarnew);
		setSupportActionBar(toolbarnew);
		fab = ( FloatingActionButton ) findViewById(R.id.fab);
		eventList = ( RecyclerView ) findViewById(R.id.eventList);
		eventList.setHasFixedSize(true);
		eventList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
		recyclerViewAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {

				if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
					ViewGroup linLayout = ( ViewGroup ) (( ViewGroup ) view).getChildAt(0);
					View titleView = linLayout.getChildAt(0);
					View timingsView = linLayout.getChildAt(1);
					titleView.setTransitionName("title");
					timingsView.setTransitionName("timings");
					Intent intent = new Intent(ScrollingActivity.this, EventActivity.class);
					ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, titleView, "title");
					ActivityCompat.startActivity(ScrollingActivity.this, intent, optionsCompat.toBundle());
				}

			}
		});
		eventList.setAdapter(recyclerViewAdapter);

		fab.setVisibility(View.INVISIBLE);
		if ( (ContextCompat.checkSelfPermission(ScrollingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scrolling, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if ( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		if ( requestCode == 123 ) {
			Map<String, Integer> perms = new HashMap<>();
			perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
			for ( int i = 0; i < permissions.length; i++ )
				perms.put(permissions[i], grantResults[i]);
			if ( perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
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

	Context mContext;
	String[] eventTitles;
	String[] eventTimings;
	String[] eventDescs;
	String[] eventContact1s;
	String[] eventContact2s;
	String[] eventPhone1s;
	String[] eventPhone2s;
	OnItemClickListener mItemClickListener;

	public RecyclerViewAdapter(Context context) {
		this.mContext = context;
		eventTitles = this.mContext.getResources().getStringArray(R.array.eventTitle);
		eventTimings = this.mContext.getResources().getStringArray(R.array.eventTimings);
		eventDescs = this.mContext.getResources().getStringArray(R.array.eventDesc);
		eventContact1s = this.mContext.getResources().getStringArray(R.array.eventContact1);
		eventContact2s = this.mContext.getResources().getStringArray(R.array.eventContact2);
		eventPhone1s = this.mContext.getResources().getStringArray(R.array.eventPhone1);
		eventPhone2s = this.mContext.getResources().getStringArray(R.array.eventPhone2);
	}

	@Override
	public RecyclerViewAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
	}

	@Override
	public void onBindViewHolder(final RecyclerViewAdapter.EventViewHolder holder, int position) {

		holder.eventTitle.setText(eventTitles[holder.getAdapterPosition()]);
		holder.eventTiming.setText(eventTimings[holder.getAdapterPosition()]);
		holder.eventDesc.setText(eventDescs[holder.getAdapterPosition()]);
		holder.eventContact1.setText(eventContact1s[holder.getAdapterPosition()]);
		holder.eventPhone1.setText(eventPhone1s[holder.getAdapterPosition()]);
		holder.eventContact2.setText(eventContact2s[holder.getAdapterPosition()]);
		holder.eventPhone2.setText(eventPhone2s[holder.getAdapterPosition()]);

	}

	@Override
	public int getItemCount() {
		return eventTitles.length;
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}

	public class EventViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

		TextView eventTitle;
		TextView eventTiming;
		TextView eventDesc;
		LinearLayout eventOrganizer;
		TextView eventContact1;
		TextView eventContact2;
		TextView eventPhone1;
		TextView eventPhone2;
		CardView eventCard;
		Context scrollingActivity;

		public EventViewHolder(final View itemView) {
			super(itemView);
			this.eventTitle = ( TextView ) itemView.findViewById(R.id.title);
			this.eventTiming = ( TextView ) itemView.findViewById(R.id.timings);
			this.eventDesc = ( TextView ) itemView.findViewById(R.id.desc);
			this.eventOrganizer = ( LinearLayout ) itemView.findViewById(R.id.organizers);
			this.eventContact1 = ( TextView ) itemView.findViewById(R.id.contact1);
			this.eventContact2 = ( TextView ) itemView.findViewById(R.id.contact2);
			this.eventPhone1 = ( TextView ) itemView.findViewById(R.id.phone1);
			this.eventPhone2 = ( TextView ) itemView.findViewById(R.id.phone2);
			this.eventCard = ( CardView ) itemView.findViewById(R.id.card);
			this.scrollingActivity = new ScrollingActivity();
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if ( mItemClickListener != null ) {
				mItemClickListener.onItemClick(v, getAdapterPosition());
			}
		}

	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}

}
