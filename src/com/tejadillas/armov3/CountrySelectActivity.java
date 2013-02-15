package com.tejadillas.armov3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CountrySelectActivity extends Activity {

	public final static String EXTRA_COUNTRY = "com.tejadillas.armov3.COUNTRY";
	private Intent intent;
	private String country;
	private ArrayList<String[]> landformsDB;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_select);

		country = "Norway";
		intent = new Intent(this, DisplayLandformActivity.class);

		final Button boton = (Button) findViewById(R.id.button_1);
		boton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String providers = Settings.Secure.getString(
						getContentResolver(),
						Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
				if (providers.contains("gps")) {
					// GPS Enabled
					intent.putExtra(EXTRA_COUNTRY, country);
					startActivity(intent);
				} else {
					CheckEnableGPS();
				}
			}
		});

		final Spinner spinner = (Spinner) findViewById(R.id.spinner_1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				country = arg0.getItemAtPosition(arg2).toString();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		landformsDB = new ArrayList<String[]>();
		
		//Preprocessing
		filterFile();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_country_select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_about:
			Toast.makeText(this, "Created by: José Carlos Tejada Saracho",
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void CheckEnableGPS() {

		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.prompts, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
	
	public void filterFile() {

		BufferedReader reader = null;
		
//		To get access to the folder /assets we need the context of the application
		getApplicationContext().getAssets();
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(CountrySelectActivity.EXTRA_COUNTRY);
		String fileName = null;
		if(message.equals("Norway")){
			fileName = "NO.txt";
		}else if(message.equals("Spain")){
			fileName = "ES.txt";
		}else if(message.equals("Poland")){
			fileName = "PL.txt";
		}else if(message.equals("France")){
			fileName = "FR.txt";
		}else if(message.equals("Italy")){
			fileName = "IT.txt";
		}else if(message.equals("Germany")){
			fileName = "DE.txt";
		}else if(message.equals("England")){
			fileName = "GB.txt";
		}
		
		InputStream is = null;
		try {
			is = getResources().getAssets().open(fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String line = "";
		while (line != null) {
			String[] landform = new String[4];
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (line == null)
				break;
			String[] fields = line.split("\t");

			if (fields[6].equals("H") || fields[6].equals("L") || 
					fields[6].equals("T") || fields[6].equals("V")) {
				landform[0] = fields[1];
				landform[1] = fields[4];
				landform[2] = fields[5];
				landform[3] = fields[16];
				landformsDB.add(landform);
			}
			
		}
		
		landformsDB.toString();

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
