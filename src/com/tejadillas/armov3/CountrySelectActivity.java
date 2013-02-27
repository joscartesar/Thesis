package com.tejadillas.armov3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

	private Intent intent;
	private String country;
	private DBManager dbman;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_select);

		dbman = new DBManager(this);
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
				filterFile(dbman, country);

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});


		// Preprocessing
		filterFile(dbman, country);
		
		dbman.open();
		Cursor c = dbman.getTitle(2);
		if (c.moveToFirst())
			DisplayLandform(c);
		else
			Toast.makeText(this, "No landform found", Toast.LENGTH_LONG).show();
		dbman.close();

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

	public void filterFile(DBManager database, String countrySelected) {

		BufferedReader reader = null;

		// To get access to the folder /assets we need the context of the
		// application
		getApplicationContext().getAssets();

		String fileName = null;
		if (countrySelected.equals("Norway")) {
			fileName = "NO.txt";
		} else if (countrySelected.equals("Spain")) {
			fileName = "ES.txt";
		} else if (countrySelected.equals("Poland")) {
			fileName = "PL.txt";
		} else if (countrySelected.equals("France")) {
			fileName = "FR.txt";
		} else if (countrySelected.equals("Italy")) {
			fileName = "IT.txt";
		} else if (countrySelected.equals("Germany")) {
			fileName = "DE.txt";
		} else if (countrySelected.equals("England")) {
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

		database.open();
		String line = "";
		while (line != null) {
//			String[] landform = new String[4];
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (line == null)
				break;
			String[] fields = line.split("\t");

			if (fields[6].equals("H") || fields[6].equals("L")
					|| fields[6].equals("T") || fields[6].equals("V")) {
//				 landform[0] = fields[1];
//				 landform[1] = fields[4];
//				 landform[2] = fields[5];
//				 landform[3] = fields[16];
				database.insertLandform(fields[1], fields[4], fields[5],
						fields[16]);
			}

		}


		try {
			reader.close();
			database.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void DisplayLandform(Cursor c) {
		Toast.makeText(
				this,
				"id: " + c.getString(0) + "\n" + "Name: " + c.getString(1)
						+ "\n" + "Lat: " + c.getString(2) + "\n" + "Long:  "
						+ c.getString(3) + "\n" + "Alt:  " + c.getString(4),
				Toast.LENGTH_LONG).show();
	}
	
}
