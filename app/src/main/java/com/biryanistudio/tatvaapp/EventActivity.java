package com.biryanistudio.tatvaapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class EventActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = ( FloatingActionButton ) findViewById(R.id.fab);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
